package com.gatdsen.simulation.action;
import com.gatdsen.simulation.IntVector2;

public class EnemyMoveAction extends EnemyAction{

    public EnemyMoveAction(float delay, IntVector2 pos, IntVector2 des, int level) {
        super(delay, pos, level);
    }

    @Override
    public String toString() {
        return "Enemy moved to: " + getPosition().x + ",  " + getPosition().y;
    }
}
