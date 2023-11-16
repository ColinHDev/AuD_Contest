package com.example.simulation;

import com.example.manager.Timer;

import java.io.Serializable;
import java.util.*;


/**
 * Repräsentiert ein laufendes Spiel mit allen dazugehörigen Daten
 */
public class StaticGameState implements Serializable {

    // Spielbrett
    // x - Spalten
    // y - Zeile


    private StaticPlayerState[] staticPlayerStates;

    private int turn;

    protected enum MapTileType {
        LAND,
        OBSTACLE,
        PATH_RIGHT,
        PATH_DOWN,
        PATH_LEFT,
        PATH_UP
    }

    protected MapTileType[][] map;

    public int getTurn() {
        return turn;
    }

    private void nextTurn(){
        ++turn;
    }

    public float[] getScores() {
        float[] healths = new float[staticPlayerStates.length];
        for (int i = 0; i < staticPlayerStates.length; i++) {
            healths[i] = staticPlayerStates[i].getHealth();
        }
        return healths;
    }

    public StaticGameState copy() {
        return new StaticGameState(this);
    }


    private StaticGameState(StaticGameState original) {
        //ToDo this needs to deep copy all read only attributes
        gameMode = original.gameMode;
        turnTimer = original.turnTimer;
        turn = original.turn;

        map = Arrays.copyOf(original.map, original.map.length);

        playerCount = original.playerCount;
        active = original.active;
        sim = null;
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


    private final int playerCount;
    private boolean active;
    private final transient Simulation sim;


    /**
     * Creates a new StaticGameState for the specified attributes.
     *
     * @param gameMode    selected game mode
     * @param mapName     name of the selected map as String
     * @param playerCount number of players
     * @param sim         the respective simulation instance
     */
    StaticGameState(GameMode gameMode, String mapName, int playerCount, Simulation sim) {
        this.gameMode = gameMode;
        this.mapName = mapName;
        this.map = MapLoader.getInstance().loadMap(
                gameMode == GameMode.Campaign ? "campaign/" + mapName : mapName
        );

        this.playerCount = playerCount;
        this.active = true;
        this.sim = sim;
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



    //ToDo migrate to Simulation
    /**
     * Return whether the Game is still active.
     *
     * @return True, if the game is still in progress.
     */
    public boolean isActive() {
        return active;
    }

    //ToDo migrate to Simulation
    protected void deactivate() {
        this.active = false;
    }

    /**
     * @return the respective simulation instance
     */
    protected Simulation getSim() {
        return sim;
    }


    /**
     * @return Anzahl der Spieler
     */
    public int getPlayerCount() {
        return playerCount;
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
        return MapLoader.getInstance().width;
    }

    /**
     * @return Vertikale Größe des Spielfeldes in #Boxen
     */
    public int getBoardSizeY() {
        return MapLoader.getInstance().height;
    }

    public Timer getTurnTimer() {
        return turnTimer;
    }

    protected void setTurnTimer(Timer turnTimer) {
        this.turnTimer = turnTimer;
    }
}
