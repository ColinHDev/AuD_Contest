package com.example.simulation;

public class Enemy {

    private int health;
    private int level;
    private IntVector2 position;

    public Enemy(int health, int level, IntVector2 position){
        this.health = health;
        this.level = level;
        this.position = position;
    }

    private void updateHealth(int damage){
        if (health-damage <= 0){
        } else health -= damage;
    }

    private void move(){
        //TODO: Implement Move
    }

    public int getHealth() {
        return health;
    }

    public int getLevel() {
        return level;
    }

    public IntVector2 getPosition() {
        return position;
    }


}
