package com.gatdsen.manager.player;

import com.gatdsen.manager.command.CommandHandler;
import com.gatdsen.manager.player.data.PlayerInformation;
import com.gatdsen.simulation.GameState;
import com.gatdsen.simulation.PlayerController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public abstract class PlayerHandler {

    protected final Class<? extends Player> playerClass;
    protected final int playerIndex;
    protected PlayerController controller;
    protected PlayerInformation information;
    protected long seedModifier;

    protected int turnsToMiss = 0;

    public PlayerHandler(Class<? extends Player> playerClass, int playerIndex) {
        this.playerClass = playerClass;
        this.playerIndex = playerIndex;
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

    public final PlayerController getPlayerController() {
        return controller;
    }

    public final PlayerInformation getPlayerInformation() {
        return information;
    }

    public final void setPlayerInformation(PlayerInformation information) {
        this.information = information;
    }

    public final void setSeedModifier(long seedModifier) {
        this.seedModifier = seedModifier;
    }

    public final long getSeedModifier() {
        return seedModifier;
    }

    public final void missNextTurn() {
        turnsToMiss++;
    }

    public abstract Future<?> create(CommandHandler commandHandler);

    public abstract Future<?> init(GameState gameState, boolean isDebug, long seed, CommandHandler commandHandler);

    public final Future<?> executeTurn(GameState gameState, CommandHandler commandHandler) {
        if (turnsToMiss > 0) {
            turnsToMiss--;
            return CompletableFuture.completedFuture(null);
        }
        return onExecuteTurn(gameState, commandHandler);
    }

    protected abstract Future<?> onExecuteTurn(GameState gameState, CommandHandler commandHandler);

    public abstract void dispose();
}
