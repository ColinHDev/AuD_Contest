package com.gatdsen.manager.run.config;

import com.gatdsen.manager.AnimationLogProcessor;
import com.gatdsen.manager.GameConfig;
import com.gatdsen.manager.InputProcessor;
import com.gatdsen.manager.player.IdleBot;
import com.gatdsen.manager.player.Player;
import com.gatdsen.simulation.GameState.GameMode;
import com.gatdsen.ui.hud.UiMessenger;

import java.util.ArrayList;
import java.util.List;

public final class RunConfiguration {

    public RunConfiguration() {
    }

    /**
     * Privater Konstruktor, der eine tiefe Kopie einer vorhandenen RunConfiguration erstellt.
     * @param original Die ursprüngliche RunConfiguration, von der eine Kopie erstellt wird.
     */
    private RunConfiguration(RunConfiguration original) {
        gameMode = original.gameMode;
        gui = original.gui;
        animationLogProcessor = original.animationLogProcessor;
        uiMessenger = original.uiMessenger;
        inputProcessor = original.inputProcessor;
        mapName = original.mapName;
        replay = original.replay;
        players = new ArrayList<>(original.players);
    }

    public GameMode gameMode = GameMode.Normal;
    public boolean gui = true;
    public AnimationLogProcessor animationLogProcessor = null;
    public UiMessenger uiMessenger = null;
    public InputProcessor inputProcessor = null;
    public String mapName = null;
    public boolean replay = false;
    public List<Class<? extends Player>> players = new ArrayList<>();

    public boolean validate() {
        boolean isValid = true;
        switch (gameMode) {
            case Normal:
                if (mapName == null) {
                    System.err.println("RunConfiguration: No map name was provided.");
                    isValid = false;
                }
                if (players.size() != 2) {
                    System.err.println("RunConfiguration: Only two players are allowed in normal game mode.");
                    isValid = false;
                }
                break;
            case Christmas_Task:
                if (mapName != null) {
                    System.err.println("RunConfiguration: A map can't be provided for the christmas task.");
                    isValid = false;
                }
                if (players.size() != 1) {
                    System.err.println("RunConfiguration: Only one player is allowed for the christmas task.");
                    isValid = false;
                }
                break;
            default:
                throw new RuntimeException("RunConfiguration: Gamemode " + gameMode + " is not unlocked yet.");
        }
        return isValid;
    }

    public GameConfig asGameConfig() {
        RunConfiguration config = copy();
        switch (gameMode) {
            case Normal:
                config.players = players;
                break;
            case Christmas_Task:
                config.mapName = "map2";
                config.players.add(IdleBot.class);
                break;
            default:
                throw new RuntimeException("RunConfiguration: Gamemode " + gameMode + " is not unlocked yet.");
        }
        return new GameConfig(config);
    }

    /**
     * Erstellt und gibt eine Kopie des aktuellen RunConfiguration-Objekts zurück.
     * @return Eine Kopie des aktuellen RunConfiguration-Objekts.
     */
    public RunConfiguration copy(){
        return new RunConfiguration(this);
    }

    public String toString() {
        return "RunConfiguration{" +
                "gameMode=" + gameMode +
                ", gui=" + gui +
                ", animationLogProcessor=" + animationLogProcessor +
                ", uiMessenger=" + uiMessenger +
                ", inputProcessor=" + inputProcessor +
                ", mapName=\"" + mapName + "\"" +
                ", replay=" + replay +
                ", players=" + players +
                "}";
    }
}
