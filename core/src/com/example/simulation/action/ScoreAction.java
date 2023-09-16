package com.example.simulation.action;

public class ScoreAction extends Action{

    private final int team;
    private final float newScore;

    public ScoreAction(float delay, int team, float newScore) {
        super(delay);
        this.team = team;
        this.newScore = newScore;
    }

    public int getTeam() {
        return team;
    }

    public float getNewScore() {
        return newScore;
    }
}
