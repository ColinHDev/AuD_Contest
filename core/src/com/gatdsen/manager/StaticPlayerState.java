package com.gatdsen.manager;

import com.gatdsen.simulation.PlayerState;
import com.gatdsen.simulation.Tile;

public final class StaticPlayerState {

    private final PlayerState state;

    StaticPlayerState(PlayerState state) {
        this.state = state;
    }

    PlayerState getPlayerState() {
        return state;
    }

    public int getHealth() {
        return state.getHealth();
    }

    public int getMoney() {
        // TODO
        // return state.getMoney();
        return 0;
    }

    public Tile[][] getMap() {
        // TODO
        //return state.getMap();
        return null;
    }
}
