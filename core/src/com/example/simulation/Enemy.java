package com.example.simulation;

import com.example.simulation.action.Action;
import com.example.simulation.action.EnemyAction;
import com.example.simulation.action.EnemyMoveAction;

public class Enemy {

    private int health;
    private int level;
    private PathTile posTile;

    public Enemy(int health, int level, PathTile posTile){
        this.health = health;
        this.level = level;
        this.posTile = posTile;
    }

    private void updateHealth(int damage){
        if (health-damage <= 0){
        } else health -= damage;
    }

    private void move(Action head){
        if (posTile.next != null) {
            posTile.enemies.remove(this);
            posTile = posTile.next;
            posTile.enemies.add(this);
            head.addChild(new EnemyMoveAction(0, posTile.prev.getPosition(), posTile.getPosition(), level));

        }
        else{

            //TODO Player gets damage
        }
    }


    public int getHealth() {
        return health;
    }

    public int getLevel() {
        return level;
    }

    public IntVector2 getPosition() {
        return new IntVector2(posTile.getPosition());
    }


}
