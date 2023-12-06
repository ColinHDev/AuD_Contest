package com.gatdsen.simulation;

import com.gatdsen.simulation.GameState.GameMode;
import com.gatdsen.simulation.action.*;

import java.util.Arrays;

/**
 * Enthält die Logik, welche die Spielmechaniken bestimmt.
 * Während die Simulation läuft werden alle Ereignisse in ActionLogs festgehalten, die anschließend durch das animation package dargestellt werden können.
 */
public class Simulation {
    private final GameState gameState;
    private final PlayerState[] playerStates;
    private ActionLog actionLog;

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
        actionLog = new ActionLog(new InitAction());
    }

    /**
     * gibt den aktuellen GameState zurück
     *
     * @return aktueller GameState
     */
    public GameState getState() {
        return gameState;
    }

    /**
     * gibt den aktuellen ActionLog zurück
     *
     * @return aktueller ActionLog
     */
    ActionLog getActionLog() {
        return actionLog;
    }

    /**
     * gibt den PlayerController für einen Spieler zurück
     *
     * @param playerIndex Index des Spielers
     * @return PlayerController für den Spieler
     */
    public PlayerController getController(int playerIndex) {
        return new PlayerController(playerIndex, gameState);
    }

    /**
     * Beendet den aktuellen Zug und führt die funktionen aus, die am Ende eines Zuges ausgeführt werden müssen
     * (Turmactions, Enemyactions, Spawnactions...)
     *
     * @return der ActionLog, der durch das Ausführen der Befehle entstanden ist
     */
    public ActionLog endTurn() {
        Action head = actionLog.getRootAction();

        for (PlayerState playerState : playerStates) {
            head = playerState.tickTowers(head);
        }

        head = new InitAction();
        actionLog.addRootAction(head);

        for (PlayerState playerState : playerStates) {
            head = playerState.moveEnemies(head);
        }

        head = new InitAction();
        actionLog.addRootAction(head);

        for (PlayerState playerState : playerStates) {
            head = playerState.spawnEnemies(head, gameState.getTurn());
        }

        int winner = -1;
        int livingPlayers = playerStates.length;
        for (int i = 0; i < playerStates.length; i++) {
            if (playerStates[i].isDeactivated()) --livingPlayers;
            else winner = i;
        }

        if (livingPlayers <= 1) {
            head = new InitAction();
            actionLog.addRootAction(head);
            head.addChild(new GameOverAction(winner));
            gameState.deactivate();
        }

        gameState.nextTurn();
        ActionLog temp = actionLog;
        actionLog = new ActionLog(new InitAction());

        return temp;
    }

    /**
     * Setzt den ActionLog zurück und gibt den alten zurück
     *
     * @return der alte ActionLog
     */
    public ActionLog clearAndReturnActionLog() {
        ActionLog tmp = this.actionLog;
        this.actionLog = new ActionLog(new InitAction());
        return tmp;
    }
}