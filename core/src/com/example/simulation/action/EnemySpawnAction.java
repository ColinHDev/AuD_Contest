package com.example.simulation.action;
import com.example.simulation.IntVector2;

public class EnemySpawnAction extends EnemyAction {

    public EnemySpawnAction(float delay, IntVector2 pos, int level) {
        super(delay, pos, level);
    }

    @Override
    public String toString() {
        return "Enemy spawned at: " + getPosition().x + ", " + getPosition().y ;
    }
}
