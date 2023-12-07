package com.gatdsen.manager;

import com.gatdsen.manager.player.Player;
import com.gatdsen.manager.run.config.RunConfiguration;
import com.gatdsen.simulation.GameState.GameMode;
import com.gatdsen.ui.hud.UiMessenger;

import java.io.Serializable;
import java.util.*;

public final class GameConfig implements Serializable {

    public final GameMode gameMode;
    public final boolean gui;
    public final transient AnimationLogProcessor animationLogProcessor;
    public final transient InputProcessor inputProcessor;
    public final transient UiMessenger uiMessenger;
    public final String mapName;
    public final boolean replay;
    public final Class<? extends Player>[] players;
    public final int playerCount;

    @SuppressWarnings("unchecked")
    public GameConfig(RunConfiguration runConfig) {
        this.gameMode = runConfig.gameMode;
        this.gui = runConfig.gui;
        this.animationLogProcessor = runConfig.animationLogProcessor;
        this.inputProcessor = runConfig.inputProcessor;
        this.uiMessenger = runConfig.uiMessenger;
        this.mapName = runConfig.mapName;
        this.replay = runConfig.replay;
        this.players = runConfig.players.toArray(new Class[0]);
        this.playerCount = runConfig.players.size();
    }

    private GameConfig(GameConfig original) {
        gameMode = original.gameMode;
        gui = original.gui;
        animationLogProcessor = original.animationLogProcessor;
        inputProcessor = original.inputProcessor;
        uiMessenger = original.uiMessenger;
        mapName = original.mapName;
        replay = original.replay;
        players = original.players;
        playerCount = original.playerCount;
    }

    public GameConfig copy() {
        return new GameConfig(this);
    }
}
