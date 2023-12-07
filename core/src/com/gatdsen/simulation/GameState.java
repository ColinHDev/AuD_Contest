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
        Replay,
        Christmas_Task
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
        if (gameMode == GameMode.Christmas_Task) {
            playerStates[0] = new PlayerState(this, 0, 300000, 1337);
        }
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
     * Gibt den Spiel-Modus des laufenden Spiels zurück.
     *
     * @return Spiel-Modus als int
     */
    public GameMode getGameMode() {
        return gameMode;
    }

    /**
     * @return die Simulation Instanz
     */
    protected Simulation getSim() {
        return sim;
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

    /**
     * @return Anzahl der Spieler
     */
    public int getPlayerCount() {
        return playerCount;
    }

    /**
     * @return Lebenspunkte aller Spieler
     */
    public float[] getHealth() {
        float[] healths = new float[playerStates.length];
        for (int i = 0; i < playerStates.length; i++) {
            healths[i] = playerStates[i].getHealth();
        }
        return healths;
    }

    /**
     * @return Aktueller Turn
     */
    public int getTurn() {
        return turn;
    }

    /**
     * Erhöht den Turn-Zähler um 1
     */
    void nextTurn() {
        ++turn;
    }

    /**
     * @return True, wenn das Spiel aktiv ist
     */
    public boolean isActive() {
        //ToDo migrate to Simulation
        return active;
    }

    /**
     * Deaktiviert das Spiel.
     */
    protected void deactivate() {
        //ToDo migrate to Simulation
        this.active = false;
    }
}
