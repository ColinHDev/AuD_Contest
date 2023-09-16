package com.example.simulation.action;

/**
 * Special type of {@link Action} that indicates the start of a new turn. May replace an {@link InitAction} as root of an {@link ActionLog}.
 */
public class TurnStartAction extends Action {

    private final int team;


    /**
     * Stores the event of a new turn beginning. Declares which Character may execute commands during the new turn.
     *
     * @param delay     non-negative time-based offset to its parent in seconds
     * @param team      team index of the Character
     */
    public TurnStartAction(long delay, int team) {
        super(delay);
        this.team = team;
    }


    public int getTeam() {
        return team;
    }

    @Override
    public String toString() {
        return "TurnStart: " + getTeam();
    }


}
