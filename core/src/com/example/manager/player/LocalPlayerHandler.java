package com.example.manager.player;

import com.example.manager.PlayerThread;
import com.example.simulation.GameState;

public final class LocalPlayerHandler implements PlayerHandler {

    private final Class<? extends Player> playerClass;
    private PlayerThread playerThread;

    public LocalPlayerHandler(Class<? extends Player> playerClass) {
        this.playerClass = playerClass;
    }

    @Override
    public void init(GameState gameState, boolean isDebug) {
        playerThread = new PlayerThread(playerClass, isDebug);
    }

    @Override
    public void executeTurn(GameState gameState) {

    }

    @Override
    public void dispose() {

    }
}
