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

    //public static final float SCORE_KILL = 50;
    //public static final float SCORE_ELIMINATION = 50;
    protected static final float[] SCORE_WIN = new float[]{200, 100, 50};

    public static final float SCORE_ERROR_PENALTY = -50;

    public static float getWinScore(int placement) {
        if (placement >= SCORE_WIN.length) return 0;
        return SCORE_WIN[placement];
    }

    public static final float SCORE_ASSIST = 25;
    private final GameState gameState;
    private ActionLog actionLog;

    private int remainingTeams;
    int turnsWithoutAction = 0;

    /**
     * erstellt eine neue Simulation
     *
     * @param gameMode Modus in dem gespielt wird
     * @param mapName  Map auf der gespielt wird
     * @param teamAm   Anzahl Teams
     */
    public Simulation(GameMode gameMode, String mapName, int teamAm) {
        gameState = new GameState(gameMode, mapName, teamAm, this);
        //Integer team = gameState.getTurn().peek();
        //assert team != null;
        actionLog = new ActionLog(new TurnStartAction(0));
        remainingTeams = teamAm;
    }

    public static IntVector2 convertToTileCoords(IntVector2 worldCoords) {
        return new IntVector2(convertToTileCoordsX(worldCoords.x), convertToTileCoordsY(worldCoords.y));
    }


    public static int convertToTileCoordsX(int x) {
        return x / 16;
    }

    public static int convertToTileCoordsY(int y) {
        return y / 16;
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

    public void setTurnTimer(Timer timer) {
        gameState.setTurnTimer(timer);
    }

    public GameCharacterController getController() {
        //Integer team = gameState.getTurn().peek();
        //assert team != null;
        //return new GameCharacterController(team, gameState);
        return null;
    }

    public ActionLog endTurn() {
        turnsWithoutAction++;

        //   int activeTeam = getActiveTeam();

        //  ArrayDeque<Integer> turn = gameState.getTurn();
        int teamCount = gameState.getPlayerCount();
        int[] remainingCharacters = new int[teamCount];
        boolean[] lostChar = new boolean[teamCount];

        //    turn.add(turn.pop());

        //ToDo: calculate scores and end conditions


        if (remainingTeams <= 1) {

            if (remainingTeams == 1) {
                //Reward score to surviving winner
                for (int i = 0; i < teamCount; i++) {
                    if (remainingCharacters[i] > 0) {
                        //gameState.addScore(actionLog.getRootAction(), i, SCORE_WIN[0]);
                        actionLog.getRootAction().addChild(new GameOverAction(i));
                        break;
                    }
                }
            } else {
                actionLog.getRootAction().addChild(new GameOverAction(-1));
            }
            //End game
            gameState.deactivate();
            return this.actionLog;
        }

        // Integer team = gameState.getTurn().peek();
        ActionLog lastTurn = this.actionLog;
        //     assert team != null;
        this.actionLog = new ActionLog(new TurnStartAction(0));
        return lastTurn;

    }

    public ActionLog clearAndReturnActionLog() {
        ActionLog tmp = this.actionLog;
        this.actionLog = new ActionLog(new InitAction());
        return tmp;
    }

    // public int getActiveTeam() {
    //     return Objects.requireNonNull(gameState.getTurn().peek());
    // }


    public void penalizeCurrentPlayer() {
        //gameState.addScore(actionLog.getRootAction(), getActiveTeam(), SCORE_ERROR_PENALTY);
    }
}
