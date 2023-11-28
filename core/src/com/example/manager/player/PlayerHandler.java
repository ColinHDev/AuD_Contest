package com.example.manager.player;

import com.example.manager.command.CommandHandler;
import com.example.simulation.GameState;

import java.util.concurrent.Future;

public interface PlayerHandler {

    boolean isHumanPlayer();

    boolean isBotPlayer();

    Future<?> init(GameState gameState, boolean isDebug, CommandHandler commandHandler);

    Future<?> executeTurn(GameState gameState, CommandHandler commandHandler);

    void dispose();
}
