package com.gatdsen.manager.player;

import com.gatdsen.manager.command.CommandHandler;
import com.gatdsen.simulation.GameState;
import com.gatdsen.simulation.PlayerController;

import java.util.concurrent.Future;

public abstract class PlayerHandler {

    protected final Class<? extends Player> playerClass;
    protected PlayerController controller;

    public PlayerHandler(Class<? extends Player> playerClass) {
        this.playerClass = playerClass;
    }

    public final boolean isHumanPlayer() {
        return HumanPlayer.class.isAssignableFrom(playerClass);
    }

    public final boolean isBotPlayer() {
        return Bot.class.isAssignableFrom(playerClass);
    }

    public final void setPlayerController(PlayerController controller) {
        this.controller = controller;
    }

    public abstract Future<?> init(GameState gameState, boolean isDebug, long seed, CommandHandler commandHandler);

    public abstract Future<?> executeTurn(GameState gameState, CommandHandler commandHandler);

    public abstract void dispose();
}
