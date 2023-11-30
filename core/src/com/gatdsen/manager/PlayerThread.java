package com.gatdsen.manager;

import com.gatdsen.manager.command.Command;
import com.gatdsen.manager.command.PlayerInformationCommand;
import com.gatdsen.manager.concurrent.ThreadExecutor;
import com.gatdsen.manager.player.Bot;
import com.gatdsen.manager.player.HumanPlayer;
import com.gatdsen.manager.player.Player;
import com.gatdsen.manager.player.data.BotInformation;
import com.gatdsen.manager.player.data.PlayerInformation;
import com.gatdsen.simulation.GameState;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.*;

/**
 *
 */
public final class PlayerThread {

    private static final int AI_EXECUTE_GRACE_PERIODE = 100;
    private static final int AI_EXECUTE_INIT_TIMEOUT = 1000;
    private static final int AI_EXECUTE_TURN_TIMEOUT = 500 + AI_EXECUTE_GRACE_PERIODE;
    private static final int AI_CONTROLLER_USES = 200;

    private static final int HUMAN_EXECUTE_GRACE_PERIODE = 5000;
    private static final int HUMAN_EXECUTE_INIT_TIMEOUT = 30000;
    private static final int HUMAN_EXECUTE_TURN_TIMEOUT = 30000 + HUMAN_EXECUTE_GRACE_PERIODE;
    private static final int HUMAN_CONTROLLER_USES = 100000;

    private final ThreadExecutor executor = new ThreadExecutor();
    private final boolean isDebug;

    private final Player player;
    private final InputProcessor inputGenerator;

    public PlayerThread(Class<? extends Player> playerClass, boolean isDebug) {
        try {
            this.player = (Player) playerClass.getDeclaredConstructors()[0].newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        this.isDebug = isDebug;
        inputGenerator = null;
    }

    public BlockingQueue<Command> init(GameState state, long seed) {
        Controller controller = createController();
        // TODO: In Player und Bot Klasse auslagern
        PlayerInformation playerInformation = switch (player.getType()) {
            case Human -> new PlayerInformation(player.getType(), player.getName());
            case AI -> new BotInformation(
                    player.getType(),
                    player.getName(),
                    ((Bot) player).getStudentName(),
                    ((Bot) player).getMatrikel()
            );
        };
        controller.commands.add(new PlayerInformationCommand(playerInformation));
        Future<?> future = executor.execute(() -> {
            Thread.currentThread().setName("Init_Thread_Player_" + player.getName());
            if (player instanceof Bot) {
                // TODO: Manager.getSeed() nicht über Prozesse hinweg
                ((Bot) player).setRnd(seed);
            }
            player.init(new StaticGameState(state));
        });
        try {
            if (isDebug) {
                future.get();
            } else {
                future.get(
                        player.getType() == Player.PlayerType.Human ? HUMAN_EXECUTE_INIT_TIMEOUT : AI_EXECUTE_INIT_TIMEOUT,
                        TimeUnit.MILLISECONDS
                );
            }
            controller.endTurn();
        } catch (InterruptedException e) {
            System.out.println("bot was interrupted");
            controller.endTurn();
        } catch (ExecutionException e) {
            System.out.println("bot failed initialization with exception: " + e.getCause());
            controller.missNextTurn();
        } catch (TimeoutException e) {
            future.cancel(true);
            executor.forceStop();
            System.out.println(player.getName() + " initialization surpassed timeout");
            controller.missNextTurn();
        }
        return controller.commands;
    }

    public BlockingQueue<Command> executeTurn(GameState state) {
        Controller controller = createController();
        Future<?> future = executor.execute(() -> {
            Thread.currentThread().setName("Run_Thread_Player_" + player.getName());
            player.executeTurn(new StaticGameState(state), controller);
        });
        Thread futureExecutor = switch (player.getType()) {
            case Human -> new Thread(() -> {
                Thread.currentThread().setName("Future_Executor_Player_" + player.getName());
                inputGenerator.activateTurn((HumanPlayer) player);
                try {
                    if (isDebug) {
                        future.get();
                    } else {
                        future.get(HUMAN_EXECUTE_TURN_TIMEOUT, TimeUnit.MILLISECONDS);
                    }
                } catch (InterruptedException e) {
                    // Executor was interrupted: Interrupt Player
                    future.cancel(true);
                    System.err.println("HumanPlayer was interrupted");
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    System.err.println("HumanPlayer failed with exception: " + e.getCause());
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    future.cancel(true);
                    executor.forceStop();
                    System.err.println("HumanPlayer computation surpassed timeout");
                }
                inputGenerator.endTurn();
                controller.endTurn();
            });
            case AI -> new Thread(() -> {
                Thread.currentThread().setName("Future_Executor_Player_" + player.getName());
                long startTime = System.currentTimeMillis();
                try {
                    if (isDebug) {
                        future.get();
                    } else {
                        // TODO: Kommentar
                        future.get(2 * AI_EXECUTE_TURN_TIMEOUT, TimeUnit.MILLISECONDS);
                    }
                } catch (InterruptedException e) {
                    // Executor was interrupted: Interrupt Bot
                    future.cancel(true);
                    System.err.println("Bot " + player.getName() + " was interrupted");
                    e.printStackTrace(System.err);
                } catch (ExecutionException e) {
                    System.out.println("Bot " + player.getName() + " failed with exception: " + e.getCause());
                    e.printStackTrace();
                    System.err.println("The failed player has to miss the next turn!");
                    controller.missNextTurn();
                    return;
                } catch (TimeoutException e) {
                    future.cancel(true);
                    executor.forceStop();

                    System.out.println("Bot " + player.getName() + " surpassed computation timeout by taking more than " + 2 * AI_EXECUTE_TURN_TIMEOUT + "ms");
                    System.err.println("The failed bot has been disqualified!");
                    controller.disqualify();
                    return;
                }
                long endTime = System.currentTimeMillis();
                if (!isDebug && endTime - startTime > AI_EXECUTE_TURN_TIMEOUT) {
                    System.out.println("Bot " + player.getName() + " surpassed computation timeout by taking " + (endTime - startTime - AI_EXECUTE_TURN_TIMEOUT) + "ms longer than allowed");
                    System.err.println("The failed bot has to miss the next turn!");
                    controller.missNextTurn();
                    return;
                }
                controller.endTurn();
            });
        };
        futureExecutor.start();
        return controller.commands;
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