package com.example.manager;

import com.example.manager.concurrent.ThreadExecutor;
import com.example.manager.player.Bot;
import com.example.manager.player.Player;
import com.example.simulation.GameState;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 */
public final class PlayerThread {

    private static final int AI_EXECUTION_TIMEOUT = 500;
    private static final int AI_EXECUTION_GRACE_PERIODE = 100;
    private static final int AI_INIT_TIMEOUT = 1000;
    private static final int AI_CONTROLLER_USES = 200;

    private static final int HUMAN_EXECUTION_TIMEOUT = 30000;
    private static final int HUMAN_EXECUTION_GRACE_PERIODE = 5000;
    private static final int HUMAN_INIT_TIMEOUT = 30000;
    private static final int HUMAN_CONTROLLER_USES = 100000;

    private final ThreadExecutor executor = new ThreadExecutor();
    private final boolean isDebug;

    private final Player player;

    public PlayerThread(Class<? extends Player> playerClass, boolean isDebug) {
        try {
            this.player = (Player) playerClass.getDeclaredConstructors()[0].newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        this.isDebug = isDebug;
    }

    public void init(GameState state) {
        Future<?> future = executor.execute(() -> {
            Thread.currentThread().setName("Init_Thread_Player_" + player.getName());
            if (player instanceof Bot) {
                ((Bot) player).setRnd(Manager.getSeed());
            }
            player.init(state);
        });
        try {
            if (isDebug) {
                future.get();
            } else {
                future.get(getPlayerInitializationTimeout(player), TimeUnit.MILLISECONDS);
            }
        } catch (InterruptedException e) {
            System.out.println("bot was interrupted");
        } catch (ExecutionException e) {
            System.out.println("bot failed initialization with exception: " + e.getCause());
        } catch (TimeoutException e) {
            future.cancel(true);
            executor.forceStop();

            System.out.println(player.getName() + " initialization surpassed timeout");
        }
    }

    public void executeTurn(GameState state) {
        Controller controller = new Controller(getPlayerControllerUses(player));
        Future<?> future = executor.execute(() -> {
            Thread.currentThread().setName("Run_Thread_Player_" + player.getName());
            // simulation.setTurnTimer(new Timer(1000 * AI_EXECUTION_TIMEOUT));
            player.executeTurn(state, controller);
        });

        controller.deactivate();
    }

    private int getPlayerInitializationTimeout(Player player) {
        return switch (player.getType()) {
            case Human -> HUMAN_INIT_TIMEOUT;
            case AI -> AI_INIT_TIMEOUT;
        };
    }

    private int getPlayerExecutionTimeout(Player player) {
        return switch (player.getType()) {
            case Human -> HUMAN_EXECUTION_TIMEOUT + HUMAN_EXECUTION_GRACE_PERIODE;
            case AI -> AI_EXECUTION_TIMEOUT + AI_EXECUTION_GRACE_PERIODE;
        };
    }

    private int getPlayerControllerUses(Player player) {
        return switch (player.getType()) {
            case Human -> HUMAN_CONTROLLER_USES;
            case AI -> AI_CONTROLLER_USES;
        };
    }
}
