package com.gatdsen.simulation.action;

/**
 * Spezialisierte Klasse von {@link Action} die anzeigt, dass das Spiel beendet ist
 */
public class GameOverAction extends Action {
    private final int team;

    /**
     * Speichert das Ereignis, dass das Spiel beendet ist
     *
     * @param team index des gewinnenden Teams
     */
    public GameOverAction(int team) {
        super(0f);
        this.team = team;
    }

    /**
     * @return index des gewinnenden Teams
     */
    public int getTeam() {
        return team;
    }

    @Override
    public String toString() {
        return "GameOverAction{" +
                "team=" + team +
                "} " + super.toString();
    }
}
