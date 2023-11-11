package com.example.manager.player;

import com.example.simulation.GameState;

public interface PlayerHandler {

    void create(GameState gameState);

    void update(GameState gameState);

    void dispose();
}
