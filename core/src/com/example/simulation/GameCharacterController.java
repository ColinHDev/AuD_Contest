package com.example.simulation;

import com.badlogic.gdx.math.Vector2;
import com.example.simulation.action.Action;
import com.example.simulation.action.ActionLog;

/**
 * Provides an access-controlled interface to send commands to players.
 * Allows Control over the corresponding Character
 * Is only active for a single turn and will be permanently deactivated afterward.
 */
public class GameCharacterController {

    private int team;
    private final GameState state;
    boolean active = true;

    /**
     * Returns the root of the ActionLog that is currently recording in the simulation instance.
     */
    private Action getRoot(){
        return state.getSim().getActionLog().getRootAction();
    }

    /**
     * Signals the current command as completed and will reset the ActionLog for the next command
     * @return The ActionLog produced by the previously executed command
     */
    private ActionLog endCommand(){
        return state.getSim().clearAndReturnActionLog();
    }

    /**
     *
     * @param team          the team this Controller will grant access to
     * @param state         the current state of the game
     */
    protected GameCharacterController(int team, GameState state) {
        this.team = team;
        this.state = state;
    }

    public int getTeam() {
        return team;
    }

    /**
     * Example command, replace with correct logic and add more commands
     *
     * @param i   example parameter
     * @return the ActionLog produced when executing this command
     */
    public ActionLog foo(int i) {
        //ToDo execute command
        return endCommand();
    }


    public void deactivate() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }
}
