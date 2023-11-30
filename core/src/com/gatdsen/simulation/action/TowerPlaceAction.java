package com.gatdsen.simulation.action;

import com.gatdsen.simulation.IntVector2;

/**
 * Spezialisierte Klasse von {@link TowerAction} die anzeigt, dass ein Turm platziert wurde
 */
public class TowerPlaceAction extends TowerAction {

    /**
     * Speichert das Ereignis, dass ein Turm platziert wurde
     *
     * @param delay nicht-negativer zeitbasierter Offset zu seinem Elternteil in Sekunden
     * @param pos   Position des Turms
     * @param type  Typ des Turms
     * @param team  index des Teams
     */
    public TowerPlaceAction(float delay, IntVector2 pos, int type, int team) {
        super(delay, pos, type, team);
    }

    @Override
    public String toString() {
        return "TowerPlaceAction{} " + super.toString();
    }
}
