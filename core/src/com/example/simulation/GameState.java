package com.example.simulation;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.example.manager.Timer;
import com.example.simulation.action.Action;
import com.example.simulation.action.ScoreAction;
import com.example.simulation.campaign.CampaignResources;

import java.io.Serializable;
import java.nio.file.Paths;
import java.util.*;


/**
 * Repräsentiert ein laufendes Spiel mit allen dazugehörigen Daten
 */
public class GameState implements Serializable {

    // Spielbrett
    // x - Spalten
    // y - Zeile

    private final boolean winnerTakesAll;

    private final float[] scores;

    private int width = 0;

    private int height = 0;


    public float[] getScores() {
        return Arrays.copyOf(scores, scores.length);
    }

    public GameState copy() {
        return new GameState(this);
    }


    private GameState(GameState original) {
        //ToDo this needs to deep copy all read only attributes
        gameMode = original.gameMode;
        turnTimer = original.turnTimer;

        winnerTakesAll = original.winnerTakesAll;
        teamCount = original.teamCount;
        turn = null;
        active = original.active;
        sim = null;
        scores = Arrays.copyOf(original.scores, original.scores.length);
    }

    public enum GameMode {
        Normal,
        Campaign,

        Exam_Admission,
        Tournament_Phase_1,
        Tournament_Phase_2,
        Replay
    }

    private final GameMode gameMode;
    private String mapName;


    private transient Timer turnTimer;


    private final int teamCount;
    private final ArrayDeque<Integer> turn;
    private boolean active;
    private final transient Simulation sim;


    /**
     * Creates a new GameState for the specified attributes.
     *
     * @param gameMode          selected game mode
     * @param mapName           name of the selected map as String
     * @param teamCount         number of teams/players
     * @param sim               the respective simulation instance
     */
    GameState(GameMode gameMode, String mapName, int teamCount, Simulation sim) {
        this.gameMode = gameMode;
        this.mapName = mapName;
        List<List<IntVector2>> spawnpoints = loadMap(gameMode == GameMode.Campaign ? "campaign/" + mapName : mapName);
        this.teamCount = teamCount;

        this.active = true;
        this.sim = sim;

        this.turn = new ArrayDeque<>();

        this.scores = new float[teamCount];
        this.winnerTakesAll = gameMode == GameMode.Campaign || gameMode == GameMode.Tournament_Phase_2;
        this.initTeam(spawnpoints);
    }

    /**
     * Gibt den Spiel-Modus des laufenden Spiels zurück.
     *
     * @return Spiel-Modus als int
     */
    public GameMode getGameMode() {
        return gameMode;
    }

    /**
     * Spawns players randomly distributed over the possible spawn-location, specified by the map.
     */
    void initTeam(List<List<IntVector2>> spawnpoints) {

        int typeCount = spawnpoints.size();
        if (typeCount < teamCount)
            throw new RuntimeException(String.format(
                    "Requested %d Teams, but the selected map only supports %d different teams",
                    teamCount, typeCount));
        Random rnd = new Random();
        ArrayList<int[]> weapons;
        ArrayList<int[]> health;
        if (gameMode == GameMode.Campaign) {
            //ToDo retrieve campaign resources
        } else {

        }

        for (int i = 0; i < teamCount; i++) {
            turn.add(i);
        }
    }


    //ToDo migrate to Simulation

    /**
     * Return whether the Game is still active.
     *
     * @return True, if the game is still in progress.
     */
    public boolean isActive() {
        return active;
    }

    protected Action addScore(Action head, int team, float score) {
        if (!winnerTakesAll) {

            scores[team] += score;
        } else if (score == Simulation.SCORE_WIN[0]) {
            scores[team] = 1;
        } else
            return head;
        ScoreAction scoreAction = new ScoreAction(0, team, scores[team]);
        head.addChild(scoreAction);
        return scoreAction;
    }

    //ToDo migrate to Simulation
    protected void deactivate() {
        this.active = false;
    }

    /**
     * @return the Queue that saves the order Characters may act in
     */
    protected ArrayDeque<Integer> getTurn() {
        return turn;
    }

    /**
     * @return the respective simulation instance
     */
    protected Simulation getSim() {
        return sim;
    }

    /**
     * ToDo: move to separate class
     * Loads a Map from the asset-directory
     * Assumes that all Tiles on the map are directly or indirectly anchored.
     * The Map file has t be encoded in JSON.
     *
     * @param mapName Name of the map without type as String
     */
    private List<List<IntVector2>> loadMap(String mapName) {
        JsonReader reader = new JsonReader();
        JsonValue map;
        try {
            //attempt to load map from jar
            map = reader.parse(getClass().getClassLoader().getResourceAsStream("maps/" + mapName + ".json"));
        } catch (Exception e) {
            map = null;
        }
        if (map == null) {
            try {
                //attempt to load map from external maps dir
                map = reader.parse(new FileHandle(Paths.get("./maps/" + mapName + ".json").toFile()));
            } catch (Exception e) {
                throw new RuntimeException("Could not find or load map:" + mapName);
            }
        }

        width = map.get("width").asInt();
        height = map.get("height").asInt();
        //board = new Tile[width][height];

        JsonValue tileData = map.get("layers").get(0).get("data");

        // List<List<IntVector2>> spawnpoints = new LinkedList<>();
        Map<Integer, List<IntVector2>> teams = new TreeMap<>();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int type = tileData.get(i + (height - j - 1) * width).asInt();
                if (type > 100) {
                    //int team = type - 101; //teams starting at 0
                    if (teams.containsKey(type)) {
                        teams.get(type).add(new IntVector2(i, j));
                    } else {
                        teams.put(type, new LinkedList<>());
                        teams.get(type).add(new IntVector2(i, j));
                    }
                    //while (spawnpoints.size() <= team)
                    //    spawnpoints.add(new LinkedList<>()); //Increase list of spawnpoints as necessary
                    //spawnpoints.get(team).add(new IntVector2(i, j)); // Add current tile
                } else
                    switch (type) {
                        case 0:
                            break;
                        case 1:
                            //board[i][j] = new Tile(i, j, this, true);
                            break;
                        default:
                            //board[i][j] = new Tile(i, j, this, false);
                    }
            }
        }

        List<List<IntVector2>> spawns = new LinkedList<>(teams.values());

        return spawns;

    }

    /**
     * @return Anzahl der Teams/Spieler
     */
    public int getTeamCount() {
        return teamCount;
    }



    /**
     * @return The 2D array that saves all Tiles
     */
//    Tile[][] getBoard() {
//        return board;
//    }


    /**
     * @return Horizontale Größe des Spielfeldes in #Boxen
     */
    public int getBoardSizeX() {
        return width;
    }

    /**
     * @return Vertikale Größe des Spielfeldes in #Boxen
     */
    public int getBoardSizeY() {
        return height;
    }

    public Timer getTurnTimer() {
        return turnTimer;
    }

    protected void setTurnTimer(Timer turnTimer) {
        this.turnTimer = turnTimer;
    }


}
