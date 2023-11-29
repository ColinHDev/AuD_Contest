package com.gatdsen.simulation.action;

import com.gatdsen.simulation.IntVector2;

public class TowerAttackAction extends TeamAction {

    private final int type;
    private final IntVector2 pos;
    private final IntVector2 direction;

    public TowerAttackAction(float delay, IntVector2 pos, IntVector2 direction, int type, int team) {
        super(delay, team);
        this.type = type;
        this.pos = pos;
        this.direction = direction;
    }

    public IntVector2 getPos() {
        return pos;
    }

    public IntVector2 getDirection() {
        return direction;
    }

    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return "TowerAttackAction{" +
                "type=" + type +
                ", pos=" + pos +
                ", direction=" + direction +
                '}';
    }
}
