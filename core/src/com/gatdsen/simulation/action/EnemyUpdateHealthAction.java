package com.gatdsen.simulation.action;

import com.gatdsen.simulation.IntVector2;

public class EnemyUpdateHealthAction extends EnemyAction{

    final int newHealth;
    public EnemyUpdateHealthAction(float delay, IntVector2 pos, int newHealth, int level,  int team) {
        super(delay, pos, level, team);
        this.newHealth = newHealth;
    }

    public int getNewHealth() {
        return newHealth;
    }

    @Override
    public String toString() {
        return "EnemyUpdateHealthAction{" +
                "team=" + team +
                '}';
    }
}
