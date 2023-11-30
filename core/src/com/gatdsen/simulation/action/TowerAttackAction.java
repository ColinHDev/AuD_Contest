package com.gatdsen.simulation.action;

import com.gatdsen.simulation.IntVector2;

/**
 * Spezialisierte Klasse von {@link TowerAction} die anzeigt, dass ein Turm einen Angriff ausführt
 */
public class TowerAttackAction extends TowerAction {
    private final IntVector2 dir;

    /**
     * Speichert das Ereignis, dass ein Turm einen Angriff ausführt
     *
     * @param delay nicht-negativer zeitbasierter Offset zu seinem Elternteil in Sekunden
     * @param pos   Position des Turms
     * @param dir   Richtung des Angriffs
     * @param type  Typ des Turms
     * @param team  index des Teams
     */
    public TowerAttackAction(float delay, IntVector2 pos, IntVector2 dir, int type, int team) {
        super(delay, pos, type, team);
        this.dir = dir;
    }

    /**
     * @return Richtung des Angriffs
     */
    public IntVector2 getDir() {
        return dir;
    }

    @Override
    public String toString() {
        return "TowerAttackAction{" +
                "dir=" + dir +
                '}' + super.toString();
    }
}
