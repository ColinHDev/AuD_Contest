package com.gatdsen.simulation.action;
import com.gatdsen.simulation.IntVector2;

public class EnemyAction extends Action{
    private final IntVector2 pos;
    private final int level;

    public EnemyAction(float delay, IntVector2 pos, int level) {
        super(delay);
        this.pos = pos;
        this.level = level;
    }

    public IntVector2 getPosition(){
        return pos;
    }
    public int getLevel(){
        return level;
    }

    @Override
    public String toString() {
        return "EnemyAction{" +
                "pos=" + pos +
                ", level=" + level +
                '}';
    }
}
