package com.example.manager.player;

import com.example.manager.concurrent.BotThread;
import com.example.manager.Manager;
import com.example.simulation.GameState;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Future;

public final class LocalPlayerHandler implements PlayerHandler {

    private final Class<? extends Player> playerClass;
    private final BotThread executor = new BotThread();

    private Player player;

    public LocalPlayerHandler(Class<? extends Player> playerClass) {
        this.playerClass = playerClass;
    }

    @Override
    public void create(GameState gameState) {
        try {
            player = (Player) playerClass.getDeclaredConstructors()[0].newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        Future<?> future = executor.execute(() -> {
            Thread.currentThread().setName("Init_Thread_Player_" + player.getName());
            if (player instanceof Bot) {
                ((Bot) player).setRnd(Manager.getSeed());
            }
            player.init(gameState);
        });
        /*try {
            if (isDebug) future.get();
            else
                future.get(AI_INIT_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            System.out.println("bot was interrupted");
        } catch (ExecutionException e) {
            System.out.println("bot failed initialization with exception: " + e.getCause());
        } catch (TimeoutException e) {
            future.cancel(true);
            executor.forceStop();

            System.out.println("bot" + i + "(" + curPlayer.getName() + ") initialization surpassed timeout");
        }*/
    }

    @Override
    public void dispose() {

    }
}
