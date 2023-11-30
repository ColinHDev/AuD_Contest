package com.gatdsen.simulation.action;

import com.gatdsen.simulation.IntVector2;

/**
 * Die Klasse EnemyMoveAction ist eine Unterklasse von {@link EnemyAction EnemyAction}
 * und repräsentiert eine Aktion, bei der ein Gegner sich bewegt.
 */
public class EnemyMoveAction extends EnemyAction {
    private final IntVector2 des;

    /**
     * Konstruktor der Klasse EnemyMoveAction.
     *
     * @param delay Verzögerung bis zum Ausführen der Aktion
     * @param pos   Position des Gegners
     * @param des   Zielposition des Gegners
     * @param level Level des Gegners
     */
    public EnemyMoveAction(float delay, IntVector2 pos, IntVector2 des, int level) {
        super(delay, pos, level, 0);
        this.des = des;
        //Handling which team?
    }

    /**
     * Gibt die Zielposition des Gegners zurück.
     *
     * @return Zielposition des Gegners
     */
    public IntVector2 getDes() {
        return des;
    }

    @Override
    public String toString() {
        return "EnemyMoveAction{" +
                "des=" + des +
                "} " + super.toString();
    }
}
