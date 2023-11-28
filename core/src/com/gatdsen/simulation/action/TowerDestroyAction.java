package com.gatdsen.simulation.action;

import com.badlogic.gdx.math.Vector2;

public class TowerDestroyAction extends TeamAction{

    private final Vector2 pos;
    private final int type;

    public TowerDestroyAction(float delay, Vector2 pos, int type, int team){
        super(delay, team);
        this.pos = pos;
        this.type = type;
    }

    public Vector2 getPos() {
        return pos;
    }

    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return "TowerDestroyAction{" +
                "pos=" + pos +
                ", type=" + type +
                '}';
    }
}
