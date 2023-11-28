package com.example.simulation.action;

public class UpdateCurrencyAction extends TeamAction{

    private final int newCurrency;

    public UpdateCurrencyAction(float delay, int newCurrency, int team) {
        super(delay, team);
        this.newCurrency = newCurrency;
    }

    public int getNewCurrency() {
        return newCurrency;
    }

    @Override
    public String toString() {
        return "UpdateCurrencyAction{" +
                "newCurrency=" + newCurrency +
                '}';
    }
}
