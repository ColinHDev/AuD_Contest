package com.gatdsen.simulation;

import com.gatdsen.simulation.action.*;

/**
 * Die Klasse Enemy repräsentiert einen Gegner im Spiel.
 */
public class Enemy {
    private int health;
    private int level;
    private PathTile posTile;

    /**
     * Erstellt einen neuen Gegner.
     *
     * @param health  Die Lebenspunkte des Gegners.
     * @param level   Die Stufe des Gegners.
     * @param posTile Die Position des Gegners.
     */
    public Enemy(int health, int level, PathTile posTile) {
        this.health = health;
        this.level = level;
        this.posTile = posTile;
    }

    /**
     * Erstellt eine Kopie des Gegners.
     *
     * @param posTile Die Position des Gegners.
     * @return Die Kopie des Gegners.
     */
    Enemy copy(PathTile posTile) {
        return new Enemy(this.health, this.level, posTile);
    }

    /**
     * Fügt dem Gegner Schaden zu.
     *
     * @param damage Der Schaden, der dem Gegner zugefügt wird.
     * @param head   Die vorrausgehende Action.
     */
    void updateHealth(int damage, Action head) {

        if (health - damage <= 0) {
            health = 0;
            posTile.getEnemies().remove(this);

            // TODO: define Team instead of 0!!!

            /*
            chaining:
            | -> head -> update health -> enemy defeat -> |
             */
            Action updateHealthAction = new EnemyUpdateHealthAction(0, posTile.getPosition(), 0, level, 0);
            head.addChild(updateHealthAction);
            updateHealthAction.addChild(new EnemyDefeatAction(0, posTile.getPosition(), level, 0));

            //TODO Player gets money
            // -> Simulation or link Enemy with GameState

        } else {
            health -= damage;
            head.addChild(new EnemyUpdateHealthAction(0, posTile.getPosition(), health, level, 0));
        }
    }

    void move(Action head) {

        if (posTile.getNext() != null) {
            posTile.getEnemies().remove(this);
            posTile = posTile.getNext();
            posTile.getEnemies().add(this);
            head.addChild(new EnemyMoveAction(0, posTile.getPrev().getPosition(), posTile.getPosition(), level));
        } else {


            // TODO: define Team instead of 0!!!
            //head.addChild(new UpdateHealthAction(0, , 0));
            //TODO Player gets damage
            // -> Simulation or link Enemy with GameState
        }
    }


    /**
     * @return Die Lebenspunkte des Gegners.
     */
    public int getHealth() {
        return health;
    }

    /**
     * @return Die Stufe des Gegners.
     */
    public int getLevel() {
        return level;
    }

    /**
     * @return Die Position des Gegners als IntVector2.
     */
    public IntVector2 getPosition() {
        return new IntVector2(posTile.getPosition());
    }
}
