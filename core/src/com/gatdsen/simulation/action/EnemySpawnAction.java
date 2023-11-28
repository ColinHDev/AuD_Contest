package com.gatdsen.simulation.action;
import com.gatdsen.simulation.IntVector2;

public class EnemySpawnAction extends EnemyAction {

    public EnemySpawnAction(float delay, IntVector2 pos, int level, int team) {
        super(delay, pos, level, team);
    }

    @Override
    public String toString() {
        return "Enemy spawned at: " + getPosition().x + ", " + getPosition().y ;
    }
}
