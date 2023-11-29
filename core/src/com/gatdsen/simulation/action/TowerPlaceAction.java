package com.gatdsen.simulation.action;

import com.gatdsen.simulation.IntVector2;

public class TowerPlaceAction extends TeamAction{

    private final IntVector2 pos;
    private final int type;

    public TowerPlaceAction(float delay, IntVector2 pos, int type, int team) {
        super(delay, team);
        this.pos = pos;
        this.type = type;
    }

    public IntVector2 getPos() {return pos;}
    public int getType(){return type;}

    public String toString(){
        return "TowerPlaceAction{" +
                    "type=" + type +
                    ", position=" + pos.toString() +
                "}";
    }
}
