package com.gatdsen.simulation;

import com.gatdsen.simulation.action.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Speichert den Zustand eines Spielers.
 */
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
    private boolean disqualified;
    private boolean deactivated;

    /**
     * Erstellt einen neuen PlayerState.
     *
     * @param gameState das Spiel, zu dem der Zustand gehört
     * @param index     der Index des Spielers
     * @param health    die Lebenspunkte des Spielers
     * @param money     das Geld des Spielers
     */
    PlayerState(GameState gameState, int index, int health, int money) {
        this.gameState = gameState;
        this.index = index;
        int width = gameState.getBoardSizeX();
        int height = gameState.getBoardSizeY();
        board = new Tile[width][height];
        this.health = health;
        this.money = money;


        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (gameState.getMap()[i][j].ordinal() >= GameState.MapTileType.PATH_RIGHT.ordinal()) {
                    board[i][j] = new PathTile(i, j);
                }
            }
        }

        IntRectangle mapOutline = new IntRectangle(0, 0, width, height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (gameState.getMap()[i][j].ordinal() >= GameState.MapTileType.PATH_RIGHT.ordinal()) {
                    IntVector2 destination = new IntVector2(i, j);
                    switch (gameState.getMap()[i][j]) {
                        case PATH_RIGHT -> destination.add(1, 0);
                        case PATH_DOWN -> destination.add(0, -1);
                        case PATH_LEFT -> destination.add(-1, 0);
                        case PATH_UP -> destination.add(0, 1);
                    }
                    PathTile current = (PathTile) board[i][j];
                    PathTile next = null;
                    if (mapOutline.contains(destination.toFloat())) {
                        next = (PathTile) board[destination.x][destination.y];
                    }
                    if (next == null) {
                        endTile = current;
                    } else {
                        current.setNext(next);
                    }
                }
            }
        }

        if (endTile == null) {
            throw new RuntimeException("There is no path");
        }
        spawnTile = endTile.getFirstPathTile();
        spawnTile.indexPathTiles();
        initEnemiesToBeSpawned();
    }

    /**
     * Kopierkonstruktor
     *
     * @param original  der zu kopierende PlayerState
     * @param gameState der neue GameState
     */
    private PlayerState(PlayerState original, GameState gameState) {
        this.gameState = gameState;
        this.index = original.index;
        int boardX = gameState.getBoardSizeX();
        int boardY = gameState.getBoardSizeY();

        board = new Tile[boardX][boardY];
        for (int i = 0; i < boardX; i++) {
            for (int j = 0; j < boardY; j++) {
                if (original.board[i][j] != null) {
                    board[i][j] = original.board[i][j].copy();
                }
            }
        }

        for (int i = 0; i < boardX; i++) {
            for (int j = 0; j < boardY; j++) {
                if (board[i][j] instanceof PathTile actual) {
                    PathTile originalPT = (PathTile) original.board[i][j];
                    PathTile next = null;
                    if (originalPT.getNext() != null) {
                        IntVector2 nextPos = originalPT.getNext().getPosition();
                        next = (PathTile) board[nextPos.x][nextPos.y];
                    }
                    actual.setNext(next);
                }
            }
        }
        health = original.health;
        money = original.money;
        deactivated = original.deactivated;
        disqualified = original.disqualified;
    }

    /**
     * Erstellt eine Kopie des PlayerStates.
     *
     * @param newGameState GameState
     * @return eine Kopie des PlayerStates
     */
    PlayerState copy(GameState newGameState) {
        return new PlayerState(this, newGameState);
    }

    /**
     * Deaktiviert den PlayerState
     *
     * @param head Kopf der Action-Liste
     * @return neuer Kopf der Action-Liste
     */
    Action deactivate(Action head) {
        deactivated = true;
        Action action = new PlayerDeactivateAction(0, index, disqualified);
        head.addChild(action);
        return action;
    }

    /**
     * Gibt zurück, ob der Spieler deaktiviert ist
     *
     * @return true, wenn der Spieler deaktiviert ist
     */
    public boolean isDeactivated() {
        return deactivated;
    }

    /**
     * Gibt zurück, ob der Spieler disqualifiziert ist
     *
     * @return true, wenn der Spieler disqualifiziert ist
     */
    public boolean isDisqualified() {
        return disqualified;
    }

    /**
     * Disqualifiziert den Spieler und deaktiviert seinen PlayerState
     */
    void disqualify() {
        disqualified = true;
        deactivated = true;
    }

    /**
     * Gibt das Spielfeld des Spielers zurück
     *
     * @return Spielfeld
     */
    public Tile[][] getBoard() {
        return board;
    }

    /**
     * Gibt den Index des Spielers zurück
     *
     * @return Spielerindex
     */
    public int getIndex() {
        return index;
    }

    /**
     * Gibt die aktuelle Lebenspunkte des Spielers zurück
     *
     * @return Lebenspunkte
     */
    public int getHealth() {
        return health;
    }

    /**
     * Gibt das aktuelle Geld des Spielers zurück
     *
     * @return Geld
     */
    public int getMoney() {
        return money;
    }

    /**
     * Platziert einen Tower auf dem Spielfeld
     *
     * @param x    x-Koordinate des Towers
     * @param y    y-Koordinate des Towers
     * @param type Typ des Towers
     * @param head Kopf der Action-Liste
     * @return neuer Kopf der Action-Liste
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

        board[x][y] = new Tower(this, type, x, y, board);
        IntVector2 pos = new IntVector2(x, y);
        Action action = new TowerPlaceAction(0, pos, type.ordinal(), index);
        head.addChild(action);
        return head;
    }

    /**
     * Upgraded einen Tower auf dem Spielfeld
     *
     * @param x    x-Koordinate des Towers
     * @param y    y-Koordinate des Towers
     * @param head Kopf der Action-Liste
     * @return neuer Kopf der Action-Liste
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

    /**
     * Initialisiert die Gegner, die gespawnt werden sollen
     */
    void initEnemiesToBeSpawned() {
        for (int i = 0; i < enemiesToBeSpawned.length; i++) {
            for (int j = 0; j < enemyTypeCount; j++) {
                enemiesToBeSpawned[i][j] = new Enemy(this, 100 * ((i / 20) + 1), (i / 20) + 1, spawnTile);
            }
        }
    }

    /**
     * Spawnt die Gegner
     *
     * @param head Die vorherige Action
     * @param wave Die aktuelle Welle
     * @return der Action Head
     */
    Action spawnEnemies(Action head, int wave) {
        for (int i = 0; i < enemyTypeCount; i++) {
            Enemy actual = enemiesToBeSpawned[wave][i];
            spawnTile.getEnemies().add(actual);
            head.addChild(new EnemySpawnAction(0, spawnTile.getPosition(), actual.getLevel(), index));
        }
        return head;
    }

    /**
     * Bewegt die Gegner
     *
     * @param head Die vorherige Action
     * @return der neue Action Head
     */
    Action moveEnemies(Action head) {
        /*System.out.println("MoveEnemies");
        PathTile actual = endTile;
        while (actual.getPrev() != null) {
            if (!actual.getEnemies().isEmpty()) {
                for (Enemy enemy : actual.getEnemies()) {
                    head = enemy.move(head);
                }
            }
            actual = actual.getPrev();
        }

        if (!actual.getEnemies().isEmpty()) for(Enemy enemy : actual.getEnemies()) head = enemy.move(head);
        return head;*/
        PathTile actual = endTile;
        while (actual.getPrev() != null) {
            List<Enemy> enemiesCopy = new ArrayList<>(actual.getEnemies());
            for (Enemy enemy : enemiesCopy) {
                head = enemy.move(head);
            }
            actual = actual.getPrev();
        }

        List<Enemy> lastEnemiesCopy = new ArrayList<>(actual.getEnemies());
        for (Enemy enemy : lastEnemiesCopy) {
            head = enemy.move(head);
        }
        return head;
    }



    /**
     * Setzt die Lebenspunkte des Spielers
     *
     * @param damage Schaden, der dem Spieler zugefügt wird (negativ für Heilung)
     * @param head   Kopf der Action-Liste
     * @return neuer Kopf der Action-Liste
     */
    Action setHealth(int damage, Action head) {
        // do heal action if damage is negative
        health -= damage;
        Action updateHealthAction = new UpdateHealthAction(0, health, index);
        head.addChild(updateHealthAction);
        head = updateHealthAction;
        if (health <= 0) head = deactivate(head);
        return head;
    }

    /**
     * Setzt das Geld des Spielers
     *
     * @param money Geld, das dem Spieler zugefügt wird (negativ für Abzug)
     * @param head  Kopf der Action-Liste
     * @return neuer Kopf der Action-Liste
     */
    Action updateMoney(int money, Action head) {
        this.money += money;
        Action updateMoneyAction = new UpdateCurrencyAction(0, this.money, index);
        head.addChild(updateMoneyAction);
        head = updateMoneyAction;
        return head;
    }

    /**
     * Führt alle Tower-Aktionen aus
     *
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
