package com.gatdsen.simulation.action;

/**
 * Spezialisierte Klasse von {@link Action} die als Oberklasse für alle Aktionen dient,
 * welche ein Team benötigen.
 */
public class TeamAction extends Action {
    final int team;

    /**
     * Speichert das Ereignis, dass ein Team eine Aktion ausführt
     *
     * @param delay nicht-negativer zeitbasierter Offset zu seinem Elternteil in Sekunden
     * @param team  index des Teams
     */
    public TeamAction(float delay, int team) {
        super(delay);
        this.team = team;
    }

    /**
     * @return index des Teams
     */
    public int getTeam() {
        return team;
    }

    @Override
    public String toString() {
        return "TeamAction{" +
                "team=" + team +
                '}' + super.toString();
    }
}
