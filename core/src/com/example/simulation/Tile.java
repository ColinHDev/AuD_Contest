package com.example.simulation;

import com.badlogic.gdx.math.Vector2;

public class Tile {

    enum tileTypes {
        FREE,
        PATH,
        TOWER,
        OBSTACLE,
        SPAWN,
        GOAL
    }

    private tileTypes tileType;

    private IntVector2 pos;
    
    public IntVector2 getPos() {
        return pos;
    }

    public tileTypes getTileType() {
        return tileType;
    }
}
