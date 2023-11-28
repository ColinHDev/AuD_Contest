package com.gatdsen.simulation.action;

public class TeamAction extends Action{
    /**
     * Constructs a new Action: every Action carries a time-based offset relative to its parent
     *
     * @param delay non-negative time-based offset to its parent in seconds
     */
    int team;
    public TeamAction(float delay, int team) {
        super(delay);
        this.team = team;
    }
    public int getTeam() {
        return team;
    }

    @Override
    public String toString() {
        return "TeamAction{" +
                "team=" + team +
                '}';
    }
}
