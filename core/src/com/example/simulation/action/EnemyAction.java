package com.example.simulation.action;

import com.badlogic.gdx.math.Vector2;

public class EnemyAction extends Action{
    private final Vector2 pos;

    public EnemyAction(float delay, Vector2 pos) {
        super(delay);
        this.pos = pos;
    }

    public Vector2 getPosition(){
        return pos;
    }

    @Override
    public String toString() {
        return "EnemyAction{" +
                "pos=" + pos +
                '}';
    }
}
