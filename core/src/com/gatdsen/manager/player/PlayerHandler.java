package com.gatdsen.manager.player;

import com.gatdsen.manager.command.CommandHandler;
import com.gatdsen.simulation.GameState;

import java.util.concurrent.Future;

public interface PlayerHandler {

    boolean isHumanPlayer();

    boolean isBotPlayer();

    Future<?> init(GameState gameState, boolean isDebug, long seed, CommandHandler commandHandler);

    Future<?> executeTurn(GameState gameState, CommandHandler commandHandler);

    void dispose();
}
