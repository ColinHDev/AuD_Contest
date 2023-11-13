package com.example.manager.player;

import com.example.manager.PlayerThread;
import com.example.simulation.GameState;

public final class LocalPlayerHandler implements PlayerHandler {

    private final PlayerThread playerThread;

    public LocalPlayerHandler(Class<? extends Player> playerClass) {
        playerThread = new PlayerThread(playerClass);
    }

    @Override
    public void create(GameState gameState) {
    }

    @Override
    public void update(GameState gameState) {

    }

    @Override
    public void dispose() {

    }
}
