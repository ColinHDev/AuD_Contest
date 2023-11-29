package com.gatdsen.simulation.action;
import com.gatdsen.simulation.IntVector2;

public class EnemyMoveAction extends EnemyAction{


    final IntVector2 des;
    public EnemyMoveAction(float delay, IntVector2 pos, IntVector2 des, int level) {
        super(delay, pos, level, 0);
        this.des = des;
        //Handling which team?
    }

    public IntVector2 getDes() {
        return des;
    }

    @Override
    public String toString() {
        return "Enemy moved to: " + getPosition().x + ",  " + getPosition().y;
    }
}
