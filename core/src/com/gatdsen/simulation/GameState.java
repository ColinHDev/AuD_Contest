package com.gatdsen.simulation;

import java.io.Serializable;
import java.util.*;

/**
 * Repräsentiert ein laufendes Spiel mit allen dazugehörigen Daten
 */
public class GameState implements Serializable {

    /**
     * Enum für die verschiedenen Spielmodi
     */
    public enum GameMode {
        Normal,
        Campaign,
        Exam_Admission,
        Tournament_Phase_1,
        Tournament_Phase_2,
        Replay
    }

    /**
     * Enum für die verschiedenen Feldtypen
     */
    public enum MapTileType {
        LAND,
        OBSTACLE,
        PATH_RIGHT,
        PATH_DOWN,
        PATH_LEFT,
        PATH_UP
    }

    private final PlayerState[] playerStates;
    private final MapTileType[][] map;
    private final GameMode gameMode;
    private final int playerCount;
    private final transient Simulation sim;
    private int turn;
    private boolean active;

    /**
     * Erstellt ein neues GameState-Objekt mit den angegebenen Attributen.
     *
     * @param gameMode    Spielmodus
     * @param mapName     Name der Map als String
     * @param playerCount Anzahl der Spieler
     * @param sim         Simulation Instanz
     */
    GameState(GameMode gameMode, String mapName, int playerCount, Simulation sim) {
        this.gameMode = gameMode;
        this.map = MapLoader.getInstance().loadMap(
                gameMode == GameMode.Campaign ? "campaign/" + mapName : mapName
        );

        this.playerCount = playerCount;
        this.active = true;
        this.sim = sim;
        playerStates = new PlayerState[playerCount];
        Arrays.setAll(playerStates, index -> new PlayerState(this, index, 300, 100));
    }

    /**
     * Copy constructor erstellt eine Kopie des übergebenen GameState-Objekts.
     *
     * @param original Das zu kopierende GameState-Objekt
     */
    private GameState(GameState original) {
        gameMode = original.gameMode;
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

    /**
     * Erstellt eine Kopie des GameState-Objekts.
     *
     * @return Kopie des GameState-Objekts
     */
    public GameState copy() {
        return new GameState(this);
    }

    /**
     * Gibt die PlayerStates beider Spieler zurück
     *
     * @return PlayerStates
     */
    public PlayerState[] getPlayerStates() {
        return playerStates;
    }

    /**
     * Erhöht den Turn-Zähler um 1
     */
    void nextTurn() {
        ++turn;
    }

    /**
     * Gibt die PlayerState des angegebenen Spielers zurück
     *
     * @param player Index des Spielers
     * @return PlayerState des Spielers
     */
    public Tile[][] getPlayerBoard(int player) {
        return playerStates[player].getBoard();
    }

    /**
     * Gibt die Map zurück
     *
     * @return Map
     */
    public MapTileType[][] getMap() {
        return map;
    }

    /**
     * @return Aktueller Turn
     */
    public int getTurn() {
        return turn;
    }

    /**
     * @return Lebenspunkte beider Spieler
     */
    public float[] getHealth() {
        float[] healths = new float[playerStates.length];
        for (int i = 0; i < playerStates.length; i++) {
            healths[i] = playerStates[i].getHealth();
        }
        return healths;
    }


    /**
     * Gibt den Spiel-Modus des laufenden Spiels zurück.
     *
     * @return Spiel-Modus als int
     */
    public GameMode getGameMode() {
        return gameMode;
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

    //ToDo migrate to Simulation

    /**
     * Deactivates the game.
     */
    protected void deactivate() {
        this.active = false;
    }

    /**
     * @return the respective simulation instance
     */

    /**
     * @return die Simulation Instanz
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
}
