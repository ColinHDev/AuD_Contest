package com.gatdsen.simulation.action;

import com.gatdsen.simulation.IntVector2;

/**
 * Die EnemyAction ist eine Oberklasse für alle {@link Action Actions}, die ein Gegner ausführen kann.
 */
public class EnemyAction extends TeamAction {
    private final IntVector2 pos;
    private final int level;

    /**
     * Erstellt eine neue EnemyAction.
     *
     * @param delay Die Verzögerung, die die Action haben soll.
     * @param pos   Die Position, an der die Action ausgeführt werden soll.
     * @param level Die Stufe des Gegners, der die Action ausführt.
     * @param team  Das Team, dem der Gegner angehört.
     */
    public EnemyAction(float delay, IntVector2 pos, int level, int team) {
        super(delay, team);
        this.pos = pos;
        this.level = level;
    }

    /**
     * Gibt die Position zurück, an der die Action ausgeführt werden soll.
     *
     * @return Die Position, an der die Action ausgeführt werden soll.
     */
    public IntVector2 getPosition() {
        return pos;
    }

    /**
     * Gibt die Stufe des Gegners zurück, der die Action ausführt.
     *
     * @return Die Stufe des Gegners, der die Action ausführt.
     */
    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "EnemyAction{" +
                "pos=" + pos +
                ", level=" + level +
                '}' + super.toString();
    }
}
