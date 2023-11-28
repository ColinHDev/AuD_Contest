package com.gatdsen.manager;

import com.gatdsen.simulation.GameState;
import com.gatdsen.simulation.action.ActionLog;

import java.io.Serializable;
import java.util.ArrayList;

public class GameResults implements Serializable {
    private final ArrayList<ActionLog> actionLogs = new ArrayList<>();
    private transient final GameConfig config;
    private GameState initialState;

    private String[] playerNames;

    private String[][] skins;

    private Game.Status status;
    private float[] scores;

    protected GameResults(GameConfig config) {
        this.config = config;

    }

    protected void setInitialState(GameState initialState) {
        this.initialState = initialState.copy();
    }

    public void addActionLog(ActionLog log){
        actionLogs.add(log);
    }

    public ArrayList<ActionLog> getActionLogs() {
        return actionLogs;
    }

    public GameConfig getConfig() {
        return config;
    }

    public GameState getInitialState() {
        return initialState;
    }

    public float[] getScores() {
        return scores;
    }

    public Game.Status getStatus() {
        return status;
    }

    protected void setStatus(Game.Status status) {
        this.status = status;
    }

    public String[] getPlayerNames() {
        return playerNames;
    }

    protected void setPlayerNames(String[] playerNames) {
        this.playerNames = playerNames;
    }

    public String[][] getSkins() {
        return skins;
    }

    public void setSkins(String[][] skins) {
        this.skins = skins;
    }

    public void setScores(float[] scores) {
        this.scores = scores;
    }
}
