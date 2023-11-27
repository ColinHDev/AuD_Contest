package com.gatdsen.simulation.action;

import com.badlogic.gdx.math.Vector2;

public class TowerPlaceAction extends Action{

    private final Vector2 pos;
    private final int type;

    public TowerPlaceAction(float delay, Vector2 pos, int type) {
        super(delay);
        this.pos = pos;
        this.type = type;
    }

    public Vector2 getPos() {return pos;}
    public int getType(){return type;}

    public String toString(){
        return "TowerPlaceAction{" +
                    "type=" + type +
                    ", position=" + pos.toString() +
                "}";
    }
}
