package com.gatdsen.simulation;

import com.gatdsen.manager.Timer;

import java.io.Serializable;
import java.util.*;


/**
 * Repräsentiert ein laufendes Spiel mit allen dazugehörigen Daten
 */
public class GameState implements Serializable {
    public enum GameMode {
        Normal,
        Campaign,
        Exam_Admission,
        Tournament_Phase_1,
        Tournament_Phase_2,
        Replay
        }
    protected enum MapTileType {
        LAND,
        OBSTACLE,
        PATH_RIGHT,
        PATH_DOWN,
        PATH_LEFT,
        PATH_UP

    }

    PlayerState[] playerStates;
    protected MapTileType[][] map;
    private int turn;
    private boolean active;
    private final GameMode gameMode;
    private final int playerCount;
    private final transient Simulation sim;
    private transient Timer turnTimer;



    /**
     * Creates a new GameState for the specified attributes.
     *
     * @param gameMode    selected game mode
     * @param mapName     name of the selected map as String
     * @param playerCount number of players
     * @param sim         the respective simulation instance
     */
    GameState(GameMode gameMode, String mapName, int playerCount, Simulation sim) {
        this.gameMode = gameMode;
        this.map = MapLoader.getInstance().loadMap(
                gameMode == GameMode.Campaign ? "campaign/" + mapName : mapName
        );

        this.playerCount = playerCount;
        this.active = true;
        this.sim = sim;
    }

    private GameState(GameState original) {
        //ToDo this needs to deep copy all read only attributes
        gameMode = original.gameMode;
        turnTimer = original.turnTimer;
        turn = original.turn;
        map = Arrays.copyOf(original.map, original.map.length);
        playerStates = new PlayerState[original.playerStates.length];
        for (int i = 0; i < playerStates.length; i++) {
            playerStates[i] = original.playerStates[i].copy(this);
        }
        playerCount = original.playerCount;
        active = original.active;
        sim = null;
    }

    private void nextTurn(){
        ++turn;
    }

    public int getTurn() {
        return turn;
    }

    public float[] getScores() {
        float[] healths = new float[playerStates.length];
        for (int i = 0; i < playerStates.length; i++) {
            healths[i] = playerStates[i].getHealth();
        }
        return healths;
    }

    public GameState copy() {
        return new GameState(this);
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
        return map.length;
    }

    /**
     * @return Vertikale Größe des Spielfeldes in #Boxen
     */
    public int getBoardSizeY() {
        return map[0].length;
    }

    public Timer getTurnTimer() {
        return turnTimer;
    }

    protected void setTurnTimer(Timer turnTimer) {
        this.turnTimer = turnTimer;
    }
}
