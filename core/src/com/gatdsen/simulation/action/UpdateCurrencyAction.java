package com.gatdsen.simulation.action;

/**
 * Spezialisierte Klasse von {@link TeamAction} die anzeigt, dass das Geld eines Teams aktualisiert wurde
 */
public class UpdateCurrencyAction extends TeamAction {
    private final int newCurrency;

    /**
     * Speichert das Ereignis, dass das Geld eines Teams aktualisiert wurde
     *
     * @param delay       nicht-negativer zeitbasierter Offset zu seinem Elternteil in Sekunden
     * @param newCurrency neuer Geldstand
     * @param team        index des Teams
     */
    public UpdateCurrencyAction(float delay, int newCurrency, int team) {
        super(delay, team);
        this.newCurrency = newCurrency;
    }

    /**
     * @return neuer Geldstand
     */
    public int getNewCurrency() {
        return newCurrency;
    }

    @Override
    public String toString() {
        return "UpdateCurrencyAction{" +
                "newCurrency=" + newCurrency +
                "} " + super.toString();
    }
}
