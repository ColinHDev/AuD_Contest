package com.gatdsen.simulation.action;

/**
 * Special type of {@link Action} that indicates the start of a new turn. May replace an {@link InitAction} as root of an {@link ActionLog}.
 */
public class TurnStartAction extends Action {

    /**
     * Stores the event of a new turn beginning. Declares which Character may execute commands during the new turn.
     *
     * @param delay     non-negative time-based offset to its parent in seconds
     */
    public TurnStartAction(long delay) {
        super(delay);
    }

    @Override
    public String toString() {
        return "TurnStart";
    }
}
