package com.example.simulation;

import com.example.manager.Timer;
import com.example.simulation.StaticGameState.GameMode;
import com.example.simulation.action.ActionLog;
import com.example.simulation.action.GameOverAction;
import com.example.simulation.action.InitAction;
import com.example.simulation.action.TurnStartAction;

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
    private final StaticGameState staticGameState;
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
        staticGameState = new StaticGameState(gameMode, mapName, teamAm, this);
        //Integer team = staticGameState.getTurn().peek();
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
     * gibt den aktuellen StaticGameState zurück
     *
     * @return aktueller StaticGameState
     */
    public StaticGameState getState() {
        return staticGameState;
    }

    ActionLog getActionLog() {
        return actionLog;
    }

    public void setTurnTimer(Timer timer) {
        staticGameState.setTurnTimer(timer);
    }

    public GameCharacterController getController() {
        //Integer team = staticGameState.getTurn().peek();
        //assert team != null;
        //return new GameCharacterController(team, staticGameState);
        return null;
    }

    public ActionLog endTurn() {
        turnsWithoutAction++;

        //   int activeTeam = getActiveTeam();

        //  ArrayDeque<Integer> turn = staticGameState.getTurn();
        int teamCount = staticGameState.getPlayerCount();
        int[] remainingCharacters = new int[teamCount];
        boolean[] lostChar = new boolean[teamCount];

        //    turn.add(turn.pop());

        //ToDo: calculate scores and end conditions


        if (remainingTeams <= 1) {

            if (remainingTeams == 1) {
                //Reward score to surviving winner
                for (int i = 0; i < teamCount; i++) {
                    if (remainingCharacters[i] > 0) {
                        //staticGameState.addScore(actionLog.getRootAction(), i, SCORE_WIN[0]);
                        actionLog.getRootAction().addChild(new GameOverAction(i));
                        break;
                    }
                }
            } else {
                actionLog.getRootAction().addChild(new GameOverAction(-1));
            }
            //End game
            staticGameState.deactivate();
            return this.actionLog;
        }

        // Integer team = staticGameState.getTurn().peek();
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
    //     return Objects.requireNonNull(staticGameState.getTurn().peek());
    // }


    public void penalizeCurrentPlayer() {
        //staticGameState.addScore(actionLog.getRootAction(), getActiveTeam(), SCORE_ERROR_PENALTY);
    }
}
