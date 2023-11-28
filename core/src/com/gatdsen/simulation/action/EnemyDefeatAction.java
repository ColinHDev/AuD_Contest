package com.gatdsen.simulation.action;

import com.gatdsen.simulation.IntVector2;

public class EnemyDefeatAction extends EnemyAction {

    public EnemyDefeatAction(float delay, IntVector2 pos, int level, int team) {
        super(delay, pos, level, team);
    }

    @Override
    public String toString() {
        return "Enemy defeated at position: " + getPosition().x + ", " + getPosition().y;
    }
}