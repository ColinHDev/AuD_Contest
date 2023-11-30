package com.gatdsen.simulation.action;

/**
 * Spezialisierte Klasse von {@link TeamAction} die anzeigt, dass die Leben eines Teams aktualisiert wurde
 */
public class UpdateHealthAction extends TeamAction {
    private final int newHealth;

    /**
     * Speichert das Ereignis, dass die Leben eines Teams aktualisiert wurde
     *
     * @param delay     nicht-negativer zeitbasierter Offset zu seinem Elternteil in Sekunden
     * @param newHealth neuer Lebenstand
     * @param team      index des Teams
     */
    public UpdateHealthAction(float delay, int newHealth, int team) {
        super(delay, team);
        this.newHealth = newHealth;
    }

    /**
     * @return neuer Lebenstand
     */
    public int getNewHealth() {
        return newHealth;
    }

    @Override
    public String toString() {
        return "UpdateHealthAction{" +
                "newHealth=" + newHealth +
                "} " + super.toString();
    }
}
