package com.gatdsen.simulation;

import com.gatdsen.manager.Timer;
import com.gatdsen.simulation.GameState.GameMode;
import com.gatdsen.simulation.action.ActionLog;
import com.gatdsen.simulation.action.GameOverAction;
import com.gatdsen.simulation.action.InitAction;
import com.gatdsen.simulation.action.TurnStartAction;

/**
 * Enthält die Logik, welche die Spielmechaniken bestimmt.
 * Während die Simulation läuft werden alle Ereignisse in ActionLogs festgehalten, die anschließend durch das animation package dargestellt werden können.
 */
public class Simulation {

    private final GameState gameState;
    private final PlayerState[] playerStates;
    private ActionLog actionLog;

    int turnsWithoutAction = 0;

    /**
     * erstellt eine neue Simulation
     *
     * @param gameMode    Modus in dem gespielt wird
     * @param mapName     Map auf der gespielt wird
     * @param playerCount Anzahl Spieler
     */
    public Simulation(GameMode gameMode, String mapName, int playerCount) {
        gameState = new GameState(gameMode, mapName, playerCount, this);
        playerStates = gameState.getPlayerStates();
        actionLog = new ActionLog(new TurnStartAction(0));
    }

    /**
     * gibt den aktuellen GameState zurück
     *
     * @return aktueller GameState
     */
    public GameState getState() {
        return gameState;
    }

    ActionLog getActionLog() {
        return actionLog;
    }

    public PlayerController getController(int playerIndex) {
        return new PlayerController(playerIndex, gameState);
    }

    public ActionLog[] endTurn() {
        ActionLog[] actionLogs = new ActionLog[playerStates.length];
        for (int i = 0; i < playerStates.length; i++) {

        }

        // Integer team = gameState.getTurn().peek();
        ActionLog lastTurn = this.actionLog;
        //     assert team != null;
        this.actionLog = new ActionLog(new TurnStartAction(0));


        return null;
    }

    public ActionLog clearAndReturnActionLog() {
        ActionLog tmp = this.actionLog;
        this.actionLog = new ActionLog(new InitAction());
        return tmp;
    }

    public void penalizeCurrentPlayer() {
        //gameState.addScore(actionLog.getRootAction(), getActiveTeam(), SCORE_ERROR_PENALTY);
    }
}
