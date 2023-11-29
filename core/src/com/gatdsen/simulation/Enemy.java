package com.gatdsen.simulation;

import com.gatdsen.simulation.action.*;


public class Enemy {

    private int health;
    private int level;
    private PathTile posTile;

    public Enemy(int health, int level, PathTile posTile){
        this.health = health;
        this.level = level;
        this.posTile = posTile;
    }
    Enemy copy(PathTile posTile){
        return new Enemy(this.health, this.level, posTile);
    }

    void updateHealth(int damage, Action head){
        if (health-damage <= 0){
            health = 0;
            posTile.getEnemies().remove(this);

            // TODO: define Team instead of 0!!!
            head.addChild(new EnemyUpdateHealthAction(0, posTile.getPosition(), 0, level, 0));
            head.addChild(new EnemyDefeatAction(0, posTile.getPosition(), level, 0));
            //TODO Player gets money

        } else {
            health -= damage;
            head.addChild(new EnemyUpdateHealthAction(0, posTile.getPosition(), health, level, 0));
        }
    }

    void move(Action head){
        if (posTile.getNext() != null) {
            posTile.getEnemies().remove(this);
            posTile = posTile.getNext();
            posTile.getEnemies().add(this);
            head.addChild(new EnemyMoveAction(0, posTile.getPrev().getPosition(), posTile.getPosition(), level));
        }
        else{


            // TODO: define Team instead of 0!!!
            //head.addChild(new UpdateHealthAction(0, , 0));
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
