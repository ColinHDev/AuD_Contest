package com.gatdsen.simulation;

import com.gatdsen.simulation.action.*;

import java.io.Serializable;

/**
 * Die Klasse Enemy repräsentiert einen Gegner im Spiel.
 */
public class Enemy implements Serializable {
    private final PlayerState playerState;
    private int health;
    private int level;
    private int damage;
    private PathTile posTile;

    /**
     * Erstellt einen neuen Gegner.
     *
     * @param health  Die Lebenspunkte des Gegners.
     * @param level   Die Stufe des Gegners.
     * @param posTile Die Position des Gegners.
     */
    public Enemy(PlayerState playerState, int health, int level, PathTile posTile) {
        this.health = health;
        this.level = level;
        this.posTile = posTile;
        this.damage = 10 * level;
        this.playerState = playerState;
    }

    /**
     * Erstellt eine Kopie des Gegners.
     *
     * @param posTile Die Position des Gegners.
     * @return Die Kopie des Gegners.
     */
    Enemy copy(PathTile posTile) {
        return new Enemy(this.playerState, this.health, this.level, posTile);
    }

    /**
     * Fügt dem Gegner Schaden zu.
     *
     * @param damage Der Schaden, der dem Gegner zugefügt wird.
     * @param head   Die vorrausgehende Action.
     * @return Die letzte Action.
     */
    Action updateHealth(int damage, Action head) {
        if (health - damage <= 0) {
            health = 0;
            posTile.getEnemies().remove(this);

            /*
            chaining:
            | -> head -> update health -> enemy defeat -> |
             */
            Action updateHealthAction = new EnemyUpdateHealthAction(0, posTile.getPosition(), 0, level, playerState.getIndex());
            head.addChild(updateHealthAction);
            updateHealthAction.addChild(new EnemyDefeatAction(0, posTile.getPosition(), level, playerState.getIndex()));
            head = playerState.updateMoney(30 * level, updateHealthAction);
        } else {
            health -= damage;
            head.addChild(new EnemyUpdateHealthAction(0, posTile.getPosition(), health, level, playerState.getIndex()));
        }
        return head;
    }

    /**
     * Bewegt den Gegner auf dem Spielfeld. Falls der Gegner das Ende des Pfades erreicht,
     * verliert der Spieler Lebenspunkte.
     *
     * @param head Die vorrausgehende Action
     * @return Die letzte Action
     */
    Action move(Action head) {
        if (posTile.getNext() != null) {
            posTile.getEnemies().remove(this);
            posTile = posTile.getNext();
            posTile.getEnemies().add(this);
            Action action = new EnemyMoveAction(0, posTile.getPrev().getPosition(), posTile.getPosition(), level, playerState.getIndex());
            head.addChild(action);
            head = action;
        } else {
            head = playerState.setHealth(damage, head);

            //TODO Player gets damage
            // -> Simulation or link Enemy with GameState
        }
        return head;
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
