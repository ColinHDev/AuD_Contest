package com.gatdsen.manager;

import com.gatdsen.manager.command.Command;
import com.gatdsen.manager.command.PlayerInformationCommand;
import com.gatdsen.manager.concurrent.ThreadExecutor;
import com.gatdsen.manager.player.analyzer.PlayerClassAnalyzer;
import com.gatdsen.manager.player.Bot;
import com.gatdsen.manager.player.HumanPlayer;
import com.gatdsen.manager.player.Player;
import com.gatdsen.simulation.GameState;
import org.lwjgl.Sys;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.*;

/**
 *
 */
public final class PlayerThread {

    private static final int AI_EXECUTE_GRACE_PERIODE = 100;
    public static final int AI_EXECUTE_INIT_TIMEOUT = 1000;
    public static final int AI_EXECUTE_TURN_TIMEOUT = 500 + AI_EXECUTE_GRACE_PERIODE;
    private static final int AI_CONTROLLER_USES = 200;

    public static final int HUMAN_EXECUTE_INIT_TIMEOUT = 30000;
    public static final int HUMAN_EXECUTE_TURN_TIMEOUT = 60000;
    private static final int HUMAN_CONTROLLER_USES = 100000;

    private final ThreadExecutor executor = new ThreadExecutor();

    private Player player;
    private InputProcessor inputGenerator;

    private boolean isDebug;

    private boolean isCreated = false;
    private boolean isInitialized = false;

    public boolean isCreated() {
        return isCreated;
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public BlockingQueue<Command> create(Class<? extends Player> playerClass, InputProcessor inputGenerator) {
        isCreated = true;
        isInitialized = false;
        this.inputGenerator = inputGenerator;
        try {
            player = (Player) playerClass.getDeclaredConstructors()[0].newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        PlayerClassAnalyzer analyzer = new PlayerClassAnalyzer(playerClass);
        Controller controller = createController();
        controller.commands.add(new PlayerInformationCommand(player.getPlayerInformation(), analyzer.getSeedModifier()));
        if (player.getType().equals(Player.PlayerType.AI)) {
            String[] illegalImports = analyzer.getIllegalImports();
            if (illegalImports.length > 0) {
                controller.disqualify();
            }
        }
        controller.endTurn();
        return controller.commands;
    }

    public BlockingQueue<Command> init(GameState state, boolean isDebug, long seed) {
        isInitialized = true;
        this.isDebug = isDebug;
        Controller controller = createController();
        switch (player.getType()) {
            case Human ->{
                Future<?> future = executor.execute(() -> {
                    Thread.currentThread().setName("Init_Thread_Player_" + player.getName());
                    player.init(new StaticGameState(state));
                });
                awaitHumanPlayerFuture(future, controller, HUMAN_EXECUTE_INIT_TIMEOUT);
            }
            case AI -> {
                Future<?> future = executor.execute(() -> {
                    Thread.currentThread().setName("Init_Thread_Player_" + player.getName());
                    ((Bot) player).setRnd(seed);
                    player.init(new StaticGameState(state));
                });
                awaitBotFuture(future, controller, AI_EXECUTE_INIT_TIMEOUT);
            }
        }
        return controller.commands;
    }

    public BlockingQueue<Command> executeTurn(GameState state) {
        Controller controller = createController();
        Future<?> future = executor.execute(() -> {
            Thread.currentThread().setName("Run_Thread_Player_" + player.getName());
            System.out.println("Executing turn for player " + player.getName());
            player.executeTurn(new StaticGameState(state), controller);
            System.out.println("Executed turn for player " + player.getName());
        });
        Thread futureExecutor = switch (player.getType()) {
            case Human -> new Thread(() -> {
                Thread.currentThread().setName("Future_Executor_Player_" + player.getName());
                inputGenerator.activateTurn((HumanPlayer) player);
                awaitHumanPlayerFuture(future, controller, HUMAN_EXECUTE_TURN_TIMEOUT);
                inputGenerator.endTurn();
            });
            case AI -> new Thread(() -> {
                Thread.currentThread().setName("Future_Executor_Player_" + player.getName());
                awaitBotFuture(future, controller, AI_EXECUTE_TURN_TIMEOUT);
            });
        };
        futureExecutor.start();
        return controller.commands;
    }

    private void awaitHumanPlayerFuture(Future<?> future, Controller controller, long timeout) {
        try {
            if (isDebug) {
                future.get();
            } else {
                future.get(timeout, TimeUnit.MILLISECONDS);
            }
        } catch (InterruptedException e) {
            future.cancel(true);
            System.out.println("HumanPlayer was interrupted");
            e.printStackTrace(System.err);
        } catch (ExecutionException e) {
            System.out.println("HumanPlayer failed with exception: " + e.getCause());
            e.printStackTrace(System.err);
        } catch (TimeoutException e) {
            future.cancel(true);
            executor.interrupt();
            System.out.println("HumanPlayer computation surpassed timeout");
        }
        controller.endTurn();
    }

    private void awaitBotFuture(Future<?> future, Controller controller, long timeout) {
        long startTime = System.currentTimeMillis();
        try {
            if (isDebug) {
                future.get();
            } else {
                future.get(2 * timeout, TimeUnit.MILLISECONDS);
            }
        } catch (InterruptedException e) {
            future.cancel(true);
            System.out.println("Bot \"" + player.getName() + "\" was interrupted");
            e.printStackTrace(System.err);
        } catch (ExecutionException e) {
            System.out.println("Bot \"" + player.getName() + "\" failed with exception: " + e.getCause());
            e.printStackTrace(System.err);
            System.err.println("Bot \"" + player.getName() + "\" has to miss the next turn!");
            controller.missNextTurn();
            return;
        } catch (TimeoutException e) {
            future.cancel(true);
            executor.interrupt();
            System.out.println("Bot \"" + player.getName() + "\" surpassed computation timeout by taking more than " + 2 * timeout + "ms");
            System.err.println("Bot \"" + player.getName() + "\" has been disqualified!");
            controller.disqualify();
            return;
        }
        long endTime = System.currentTimeMillis();
        if (!isDebug && endTime - startTime > timeout) {
            System.out.println("Bot " + player.getName() + " surpassed computation timeout by taking " + (endTime - startTime - timeout) + "ms longer than allowed");
            System.err.println("Bot \"" + player.getName() + "\" has to miss the next turn!");
            controller.missNextTurn();
            return;
        }
        controller.endTurn();
    }

    private Controller createController() {
        return new Controller(
                player.getType() == Player.PlayerType.Human ? HUMAN_CONTROLLER_USES : AI_CONTROLLER_USES
        );
    }

    public void dispose() {
        executor.interrupt();
    }
}
