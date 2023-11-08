package com.example.simulation;

import com.badlogic.gdx.math.Vector2;

public class Tile {

    private enum tileTypes {
        FREE ,
        PATH,
        TOWER,
        OBSTACLE,
        SPAWN,
        GOAL
    }

    private tileTypes tileType;

    private IntVector2 pos;

    Tile(int x, int y, int type){
        this.pos = new IntVector2(x, y);
        this.tileType =  tileTypes.valueOf(String.valueOf(type));
    }

    public IntVector2 getPosition() {
        return pos;
    }

    public tileTypes getType() {
        return tileType;
    }


    @Override
    public String toString() {
        return "Tile{" +
                "tileType=" + tileType +
                ", pos=" + pos +
                '}';
    }
}
