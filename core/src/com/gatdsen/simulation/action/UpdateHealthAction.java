package com.gatdsen.simulation.action;

public class UpdateHealthAction extends TeamAction{

    int newHealth;

    public UpdateHealthAction(float delay, int newHealth, int team) {
        super(delay, team);
        this.newHealth = newHealth;
    }

    public int getNewHealth() {
        return newHealth;
    }

    @Override
    public String toString() {
        return "UpdateHealthAction{" +
                "newHealth=" + newHealth +
                '}';
    }
}
