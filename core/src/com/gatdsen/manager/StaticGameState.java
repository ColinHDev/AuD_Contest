package com.gatdsen.manager;

import com.gatdsen.simulation.GameState;

public final class StaticGameState {

    private final GameState state;
    private final int playerIndex;
    private final StaticPlayerState[] playerStates;

    StaticGameState(GameState state, int playerIndex) {
        this.state = state;
        this.playerIndex = playerIndex;
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
