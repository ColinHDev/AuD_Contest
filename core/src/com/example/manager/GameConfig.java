package com.example.manager;

import com.example.manager.player.Player;
import com.example.simulation.GameState;
import com.example.ui.hud.UiMessenger;

import java.io.Serializable;
import java.util.*;

class GameConfig implements Serializable {

    public GameConfig() {
    }

    public GameConfig(RunConfiguration runConfiguration) {
        gui = runConfiguration.gui;
        animationLogProcessor = runConfiguration.animationLogProcessor;
        inputProcessor = runConfiguration.inputProcessor;
        uiMessenger = runConfiguration.uiMessenger;
        gameMode = runConfiguration.gameMode;
        players = runConfiguration.players;
        mapName = runConfiguration.mapName;
        teamCount = runConfiguration.teamCount;
        replay = runConfiguration.replay;
    }

    //Todo add default values
    public GameState.GameMode gameMode = null;

    public boolean gui = false;

    public transient AnimationLogProcessor animationLogProcessor = null;
    public transient InputProcessor inputProcessor = null;
    public transient UiMessenger uiMessenger = null;

    public String mapName;

    public int teamCount;

    public boolean replay = false;

    public List<Class<? extends Player>> players;

    public GameConfig copy() {
        GameConfig copy = new GameConfig();
        copy.gui = gui;
        copy.animationLogProcessor = animationLogProcessor;
        copy.inputProcessor = inputProcessor;
        copy.uiMessenger = uiMessenger;
        copy.gameMode = gameMode;
        copy.players = new ArrayList<>(players);
        copy.mapName = mapName;
        copy.teamCount = teamCount;
        copy.replay = replay;
        return copy;
    }

}
