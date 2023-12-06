package com.gatdsen.simulation.action;

/**
 * Spezialisierte Klasse von {@link Action} die anzeigt, dass ein Team einen neuen Punktestand hat.
 * Wird in Zukunft entfernt.
 */
public class ScoreAction extends Action {
    private final int team;
    private final float newScore;

    /**
     * Speichert das Ereignis, dass ein Team einen neuen Punktestand hat
     *
     * @param delay    nicht-negativer zeitbasierter Offset zu seinem Elternteil in Sekunden
     * @param team     index des Teams
     * @param newScore neuer Punktestand
     */
    public ScoreAction(float delay, int team, float newScore) {
        super(delay);
        this.team = team;
        this.newScore = newScore;
    }

    /**
     * @return index des Teams
     */
    public int getTeam() {
        return team;
    }

    /**
     * @return neuer Punktestand
     */
    public float getNewScore() {
        return newScore;
    }

    @Override
    public String toString() {
        return "ScoreAction{" +
                "team=" + team +
                ", newScore=" + newScore +
                "} " + super.toString();
    }
}
