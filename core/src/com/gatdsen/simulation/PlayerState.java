package com.gatdsen.simulation;

import com.gatdsen.simulation.action.Action;
import com.gatdsen.simulation.action.TowerPlaceAction;

import java.io.Serializable;

public class PlayerState implements Serializable {

    private final GameState gameState;
    private Tile[][] board;

    private int health;
    private int money;
    private int enemyIndex;
    private int index;

    PlayerState copy(GameState newGameState){
        return new PlayerState(this, newGameState);
    }

    /**
     * Creates a Deep-Copy of the player state
     */
    private PlayerState(PlayerState original, GameState gameState){
        this.gameState = gameState;
        int boardX = gameState.getBoardSizeX();
        int boardY = gameState.getBoardSizeY();

        board = new Tile[boardX][boardY];
        for (int i = 0; i < boardX; i++) {
            for (int j = 0; j < boardY; j++) {
                if(original.board[i][j] != null){
                    board[i][j] = original.board[i][j].copy();
                }
            }
        }

        for (int i = 0; i < boardX; i++) {
            for (int j = 0; j < boardX; j++) {
                if (board[i][j] instanceof PathTile actual){
                    PathTile originalPT = (PathTile) original.board[i][j];
                    PathTile next = null;
                    if (originalPT.getNext() != null){
                        IntVector2 nextPos = originalPT.getNext().getPosition();
                        next = (PathTile) board[nextPos.x][nextPos.y];
                    }
                    actual.setNext(next);

                }
            }
        }
        health = original.health;
        money = original.money;
    }

    public PlayerState(GameState gameState, int health, int money){
        this.index = index;
        this.enemyIndex = index == 0 ? 1 : 0;
        this.gameState = gameState;
        int width = gameState.getBoardSizeX();
        int height = gameState.getBoardSizeY();
        board = new Tile[width][height];
        this.health = health;
        this.money = money;

        for (int i = 0; i <width; i++) {
            for (int j = 0; j < height; j++) {
                if (gameState.map[i][j].ordinal() >= GameState.MapTileType.PATH_RIGHT.ordinal()){
                    board[i][j] = new PathTile(i, j);
                }
            }
        }

        IntVector2 end = null;
        IntRectangle mapOutline = new IntRectangle(0,0,width, height);
        for (int i = 0; i <width; i++) {
            for (int j = 0; j < height; j++) {
                if (gameState.map[i][j].ordinal() >= GameState.MapTileType.PATH_RIGHT.ordinal()){
                    IntVector2 destination = new IntVector2(i, j);
                    switch (gameState.map[i][j]){
                        case PATH_RIGHT -> destination.add(1,0);
                        case PATH_DOWN -> destination.add(0,-1);
                        case PATH_LEFT -> destination.add(-1,0);
                        case PATH_UP -> destination.add(0,1);
                    }
                    if (mapOutline.contains(destination.toFloat())){
                        ((PathTile) board[i][j]).setNext((PathTile) board[destination.x][destination.y]);
                        if (((PathTile) board[i][j]).getNext() == null){
                            end = new IntVector2(i, j);
                        }
                    }
                }
            }
        }

        if (end == null || !(board[end.x][end.y] instanceof PathTile)) {
            throw new RuntimeException("There is no path");
        }

        ((PathTile) board[end.x][end.y]).indexPathTiles();
    }


    /**
     * Gibt die aktuelle Lebenspunkte des Spielers zurück
     * @return Lebenspunkte
     */
    public int getHealth() {
        return health;
    }

    /**
     * Gibt das aktuelle Geld des Spielers zurück
     * @return Geld
     */
    public int getMoney() {
        return money;
    }

    /**
     * Gibt den Index des Gegners zurück
     * @return Gegnerindex
     */
    public int getEnemyIndex() {
        return enemyIndex;
    }

    /**
     * Gibt den Index des Spielers zurück
     * @return Spielerindex
     */
    public int getMyIndex() {
        return index;
    }

    /**
     * Gibt das Spielfeld des Spielers zurück
     * @return Spielfeld
     */
    public Tile[][] getBoard() {
        return board;
    }

    public Tile[][] getMap(){
        return board;
    }

    public Tile[][] getEnemyMap(){
        return gameState.playerStates[enemyIndex].getMap();
    }


    /**
     * Platziert einen Tower auf dem Spielfeld
     * @param x x-Koordinate des Towers
     * @param y y-Koordinate des Towers
     * @param type Typ des Towers
     * @param head Kopf der Action-Liste
     * @return Kopf der Action-Liste
     */
    Action placeTower(int x, int y, Tower.TowerType type, Action head) {
        if (board[x][y] != null) {
            // ToDo: append error action
            return head;
        }

        if (money < Tower.getPrice(type)) {
            // ToDo: append error action
            return head;
        }

        money -= Tower.getPrice(type);

        board[x][y] = new Tower(type, x, y, board);
        IntVector2 pos = new IntVector2(x, y);
        Action action = new TowerPlaceAction(0, pos, type.ordinal(), index);
        head.addChild(action);
        return head;
    }

    /**
     * Upgraded einen Tower auf dem Spielfeld
     * @param x x-Koordinate des Towers
     * @param y y-Koordinate des Towers
     * @param head Kopf der Action-Liste
     * @return Kopf der Action-Liste
     */
    Action upgradeTower(int x, int y, Action head) {
        if (board[x][y] == null) {
            // ToDo: append error action
            return head;
        }
        if (board[x][y] instanceof Tower tower && tower.getLevel() < Tower.getMaxLevel() && money > tower.getUpgradePrice()) {
            money -= tower.getUpgradePrice();
            tower.upgrade();
            head.addChild(new TowerPlaceAction(0, tower.getPosition(), tower.getType().ordinal(), index));
        } else {
            // ToDo: append error action
            return head;
        }

        return head;
    }


    // ToDo: --------------
    void tickTowers(Action head) {
        for (Tile[] tiles : board) {
            for (Tile tile : tiles) {
                if (tile instanceof Tower tower) {
                    tower.tick();
                }
            }
        }
    }
}
