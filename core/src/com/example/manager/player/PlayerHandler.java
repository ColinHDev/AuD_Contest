package com.example.manager.player;

import com.example.simulation.GameState;

public interface PlayerHandler {

    void init(GameState gameState, boolean isDebug);

    void executeTurn(GameState gameState);

    void dispose();
}
