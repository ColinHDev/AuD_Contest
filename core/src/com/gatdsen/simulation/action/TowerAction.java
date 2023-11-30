package com.gatdsen.simulation.action;

import com.gatdsen.simulation.IntVector2;

/**
 * Spezialisierte Klasse von {@link TeamAction} die Oberklasse für alle Turmaktionen ist
 */
public class TowerAction extends TeamAction{
    private final int type;
    private final IntVector2 pos;

    /**
     * Speichert das Ereignis, dass ein Turm eine Aktion ausführt
     *
     * @param delay nicht-negativer zeitbasierter Offset zu seinem Elternteil in Sekunden
     * @param pos   Position des Turms
     * @param type  Typ des Turms
     * @param team  index des Teams
     */
    public TowerAction(float delay, IntVector2 pos, int type, int team) {
        super(delay, team);
        this.type = type;
        this.pos = pos;
    }

    /**
     * @return Position des Turms
     */
    public IntVector2 getPos() {
        return pos;
    }

    /**
     * @return Typ des Turms
     */
    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return "TowerAction{" +
                "type=" + type +
                ", pos=" + pos +
                '}' + super.toString();
    }
}
