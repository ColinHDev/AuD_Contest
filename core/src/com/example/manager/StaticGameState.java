package com.example.manager;

import com.example.simulation.GameState;

public final class StaticGameState {

    private final GameState state;
    private final StaticPlayerState[] playerStates;

    StaticGameState(GameState state) {
        this.state = state;
        this.playerStates = new StaticPlayerState[state.getPlayerCount()];
    }

    public StaticPlayerState getPlayerState() {
        // TODO
        // return ;
        return null;
    }

    public StaticPlayerState getEnemyPlayerState() {
        // TODO
        // return ;
        return null;
    }

    public int getTurn() {
        return state.getTurn();
    }

    public int getPlayerCount() {
        return state.getPlayerCount();
    }

    public int getBoardSizeX() {
        return state.getBoardSizeX();
    }

    public int getBoardSizeY() {
        return state.getBoardSizeY();
    }
}
