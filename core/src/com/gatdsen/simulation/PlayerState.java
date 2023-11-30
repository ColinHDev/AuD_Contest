package com.gatdsen.simulation;

import com.gatdsen.simulation.action.*;

import java.io.Serializable;

public class PlayerState implements Serializable {

    private final GameState gameState;
    private final Tile[][] board;

    private int health;
    private int money;
    private final int index;
    private PathTile spawnTile;
    private PathTile endTile;
    private final int enemyTypeCount = 1;

    private final Enemy[][] enemiesToBeSpawned = new Enemy[100][enemyTypeCount];


    PlayerState copy(GameState newGameState){
        return new PlayerState(this, newGameState);
    }

    /**
     * Creates a Deep-Copy of the player state
     */
    private PlayerState(PlayerState original, GameState gameState) {
        this.gameState = gameState;
        this.index = original.index;
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

    public PlayerState(GameState gameState, int index, int health, int money){
        this.gameState = gameState;
        this.index = index;
        int width = gameState.getBoardSizeX();
        int height = gameState.getBoardSizeY();
        board = new Tile[width][height];
        this.health = health;
        this.money = money;
        initEnemiesToBeSpawned();

        for (int i = 0; i <width; i++) {
            for (int j = 0; j < height; j++) {
                if (gameState.getMap()[i][j].ordinal() >= GameState.MapTileType.PATH_RIGHT.ordinal()){
                    board[i][j] = new PathTile(i, j);
                }
            }
        }

        IntRectangle mapOutline = new IntRectangle(0,0,width, height);
        for (int i = 0; i <width; i++) {
            for (int j = 0; j < height; j++) {
                if (gameState.getMap()[i][j].ordinal() >= GameState.MapTileType.PATH_RIGHT.ordinal()){
                    IntVector2 destination = new IntVector2(i, j);
                    switch (gameState.getMap()[i][j]){
                        case PATH_RIGHT -> destination.add(1,0);
                        case PATH_DOWN -> destination.add(0,-1);
                        case PATH_LEFT -> destination.add(-1,0);
                        case PATH_UP -> destination.add(0,1);
                    }
                    if (mapOutline.contains(destination.toFloat())){
                        ((PathTile) board[i][j]).setNext((PathTile) board[destination.x][destination.y]);
                        if (((PathTile) board[i][j]).getNext() == null){
                            endTile = (PathTile) board[i][j];
                        }
                    }
                }
            }
        }

        if (endTile == null) {
            throw new RuntimeException("There is no path");
        }
        spawnTile = endTile.getFirstPathTile();
        spawnTile.indexPathTiles();
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
     * Gibt den Index des Spielers zurück
     * @return Spielerindex
     */
    public int getIndex() {
        return index;
    }

    /**
     * Gibt das Spielfeld des Spielers zurück
     * @return Spielfeld
     */
    public Tile[][] getBoard() {
        return board;
    }



    /**
     * Platziert einen Tower auf dem Spielfeld
     * @param x x-Koordinate des Towers
     * @param y y-Koordinate des Towers
     * @param type Typ des Towers
     * @param head Kopf der Action-Liste
     */
    void placeTower(int x, int y, Tower.TowerType type, Action head) {
        if (board[x][y] != null) {
            // ToDo: append error action
            return;
        }

        if (money < Tower.getPrice(type)) {
            // ToDo: append error action
            return;
        }

        money -= Tower.getPrice(type);

        board[x][y] = new Tower(type, x, y, board);
        IntVector2 pos = new IntVector2(x, y);
        Action action = new TowerPlaceAction(0, pos, type.ordinal(), index);
        head.addChild(action);
    }

    /**
     * Upgraded einen Tower auf dem Spielfeld
     * @param x x-Koordinate des Towers
     * @param y y-Koordinate des Towers
     * @param head Kopf der Action-Liste
     */
    void upgradeTower(int x, int y, Action head) {
        if (board[x][y] == null) {
            // ToDo: append error action
            return;
        }
        if (board[x][y] instanceof Tower tower && tower.getLevel() < Tower.getMaxLevel() && money > tower.getUpgradePrice()) {
            money -= tower.getUpgradePrice();
            tower.upgrade();
            head.addChild(new TowerPlaceAction(0, tower.getPosition(), tower.getType().ordinal(), index));
        } else {
            // ToDo: append error action
            return;
        }
    }

    void initEnemiesToBeSpawned(){
        for (int i = 0; i < enemiesToBeSpawned.length; i++) {
            for (int j = 0; j < enemyTypeCount; j++) {
                enemiesToBeSpawned[i][j] = new Enemy(this, 100 * ((i/20) + 1), (i/20) + 1, spawnTile);
            }
        }
    }

    Action spawnEnemies(Action head, int wave){
        for (int i = 0; i < enemyTypeCount; i++) {
            Enemy actual = enemiesToBeSpawned[wave][i];
            spawnTile.getEnemies().add(actual);
            head.addChild(new EnemySpawnAction(0, spawnTile.getPosition(),actual.getLevel(), index));
        }
        return head;
    }

    Action moveEnemies(Action head){
        PathTile actual = endTile;
        while (actual.getPrev() != null){
            for (Enemy enemy : actual.getEnemies()) {
                head = enemy.move(head);
            }
            actual = actual.getPrev();
        }
        return head;
    }

    /**
     * Setzt die Lebenspunkte des Spielers
     * @param damage Schaden, der dem Spieler zugefügt wird (negativ für Heilung)
     * @param head Kopf der Action-Liste
     * @return neuer Kopf der Action-Liste
     */
    Action setHealth(int damage, Action head){
        // do heal action if damage is negative
        health -= damage;
        Action updateHealthAction = new UpdateHealthAction(0, health, index);
        head.addChild(updateHealthAction);
        head = updateHealthAction;
        return head;
    }

    Action updateMoney(int money, Action head){
        this.money += money;
        Action updateMoneyAction = new UpdateCurrencyAction(0, this.money, index);
        head.addChild(updateMoneyAction);
        head = updateMoneyAction;
        return head;
    }

    /**
     * Führt alle Tower-Aktionen aus
     * @param head Kopf der Action-Liste
     * @return neuer Kopf der Action-Liste
     */
    Action tickTowers(Action head) {
        for (Tile[] tiles : board) {
            for (Tile tile : tiles) {
                if (tile instanceof Tower tower) {
                    head = tower.tick(head);
                }
            }
        }
        return head;
    }
}
