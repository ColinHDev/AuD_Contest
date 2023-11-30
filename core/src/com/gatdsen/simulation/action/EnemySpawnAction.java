package com.gatdsen.simulation.action;

import com.gatdsen.simulation.IntVector2;

/**
 * Die Klasse EnemySpawnAction ist eine Unterklasse von {@link EnemyAction EnemyAction}
 * und repräsentiert eine Aktion, bei der ein Gegner gespawnt wird.
 */
public class EnemySpawnAction extends EnemyAction {

    /**
     * Konstruktor der Klasse EnemySpawnAction.
     *
     * @param delay Verzögerung bis zum Ausführen der Aktion
     * @param pos   Position des Gegners
     * @param level Level des Gegners
     * @param team  Team des Gegners
     */
    public EnemySpawnAction(float delay, IntVector2 pos, int level, int team) {
        super(delay, pos, level, team);
    }

    @Override
    public String toString() {
        return "EnemySpawnAction{} " + super.toString();
    }
}
