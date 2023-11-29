package com.gatdsen.simulation;

import com.gatdsen.simulation.action.Action;
import com.gatdsen.simulation.action.ActionLog;

/**
 * Provides an access-controlled interface to send commands to players.
 * Allows Control over the corresponding Character
 * Is only active for a single turn and will be permanently deactivated afterward.
 */
public class PlayerController {

    private final int playerIndex;
    private final GameState state;
    private final PlayerState playerState;
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
     * @param playerIndex          the team this Controller will grant access to
     * @param state         the current state of the game
     */
    protected PlayerController(int playerIndex, GameState state) {
        this.playerIndex = playerIndex;
        this.state = state;
        this.playerState = state.getPlayerStates()[playerIndex];
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    //ToDo: add more calls to complete your API
    //ToDo: all added calls should have respective Counterparts in manager.Controller and manager.command.[...]

    /**
     * Platziert einen Turm auf dem Spielfeld
     * @param x x-Koordinate
     * @param y y-Koordinate
     * @param type Turm-Typ als Enum
     * @return Der ActionLog der durch das Ausführen des Befehls entstanden ist
     */
    public ActionLog placeTower(int x, int y, Tower.TowerType type) {
        playerState.placeTower(x, y, type, getRoot());
        return endCommand();
    }

    /**
     * Upgraded einen Turm auf dem Spielfeld
     * @param x x-Koordinate
     * @param y y-Koordinate
     * @return Der ActionLog der durch das Ausführen des Befehls entstanden ist
     */
    public ActionLog upgradeTower(int x, int y) {
        playerState.upgradeTower(x, y, getRoot());
        return endCommand();
    }

    /**
     * Verkauft einen Turm auf dem Spielfeld
     * @param x x-Koordinate
     * @param y y-Koordinate
     * @return Der ActionLog der durch das Ausführen des Befehls entstanden ist
     */
    private ActionLog sellTower(int x, int y) {
        // ToDo: implement sellTower after christmas task and make it public
        return endCommand();
    }


    public void deactivate() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }
}
