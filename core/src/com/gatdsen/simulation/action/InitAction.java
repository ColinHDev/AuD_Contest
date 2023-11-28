package com.gatdsen.simulation.action;


/**
 * Special type of {@link Action} that indicates no event, but is used as the root of an {@link ActionLog}
 */
public final class InitAction extends Action{
    /**
     * Functions as a skippable Action, for being able to have multiple Actions,
     * that are directly executed at the root of an {@link ActionLog}
     */
    public InitAction() {
        super(0);
    }

    @Override
   public String toString() {

       return "Init";

    }



}
