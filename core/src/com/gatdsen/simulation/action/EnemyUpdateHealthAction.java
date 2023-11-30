package com.gatdsen.simulation.action;

import com.gatdsen.simulation.IntVector2;

/**
 * Die Klasse EnemyUpdateHealthAction ist eine Unterklasse von {@link EnemyAction EnemyAction}
 * und repräsentiert eine Aktion, bei der die Lebenspunkte eines Gegners aktualisiert werden.
 */
public class EnemyUpdateHealthAction extends EnemyAction {
    private final int newHealth;

    /**
     * Konstruktor der Klasse EnemyUpdateHealthAction.
     *
     * @param delay     Verzögerung bis zum Ausführen der Aktion
     * @param pos       Position des Gegners
     * @param newHealth neue Lebenspunkte des Gegners
     * @param level     Level des Gegners
     * @param team      Team des Gegners
     */
    public EnemyUpdateHealthAction(float delay, IntVector2 pos, int newHealth, int level, int team) {
        super(delay, pos, level, team);
        this.newHealth = newHealth;
    }

    /**
     * @return neue Lebenspunkte des Gegners
     */
    public int getNewHealth() {
        return newHealth;
    }

    @Override
    public String toString() {
        return "EnemyUpdateHealthAction{" +
                "newHealth=" + newHealth +
                "} " + super.toString();
    }
}
