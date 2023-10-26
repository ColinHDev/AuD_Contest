package com.example.simulation.action;

import com.badlogic.gdx.math.Vector2;

public class EnemySpawnAction extends EnemyAction {

    private final int level;

    public EnemySpawnAction(float delay, int level, Vector2 pos) {
        super(delay, pos);
        this.level = level;
    }

    public String toString(){
        return "EnemySpawnAction{" +
                "level=" + level +
                "}";
    }



}
