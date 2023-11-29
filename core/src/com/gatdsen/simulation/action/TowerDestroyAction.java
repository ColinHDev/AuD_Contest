package com.gatdsen.simulation.action;
import com.gatdsen.simulation.IntVector2;

public class TowerDestroyAction extends TeamAction{

    private final IntVector2 pos;
    private final int type;

    public TowerDestroyAction(float delay, IntVector2 pos, int type, int team){
        super(delay, team);
        this.pos = pos;
        this.type = type;
    }

    public IntVector2 getPos() {
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
