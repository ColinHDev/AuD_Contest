package com.example.simulation.action;

import com.example.simulation.IntVector2;

public class EnemyDefeatAction extends EnemyAction {

    public EnemyDefeatAction(float delay, IntVector2 pos, int level) {
        super(delay, pos, level);
    }

    @Override
    public String toString() {
        return "Enemy defeated at position: " + getPosition().x + ", " + getPosition().y;
    }
}