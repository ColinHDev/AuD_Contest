package com.gatdsen.simulation;

import java.util.List;
import java.util.ArrayList;

/**
 * Speichert ein PathTile, das Teil eines Pfades auf der Map ist.
 */
public class PathTile extends Tile {
    private PathTile prev;
    private PathTile next;
    private int index;
    private List<Enemy> enemies;

    /**
     * Erstellt einen PathTile an der angegebenen Position.
     *
     * @param x x-Koordinate
     * @param y y-Koordinate
     */
    PathTile(int x, int y) {
        super(x, y);
    }

    /**
     * Erstellt eine Kopie eines PathTiles.
     *
     * @param original das zu kopierende PathTile
     */
    PathTile(PathTile original) {
        super(original.getPosition().x, original.getPosition().y);
        this.enemies = copyList(original.enemies);
        this.index = original.index;
        this.prev = null;
        this.next = null;
    }

    /**
     * Erstellt eine Kopie des PathTiles.
     *
     * @return eine Kopie des PathTiles
     */
    @Override
    protected PathTile copy() {
        return new PathTile(this);
    }

    /**
     * Setzt das next-Attribut des PathTiles.
     *
     * @param next das nächste PathTile
     */
    protected void setNext(PathTile next) {
        this.next = next;
        if (next != null)
            next.prev = this;
    }

    /**
     * Setzt das prev-Attribut des PathTiles.
     *
     * @param prev das vorherige PathTile
     */
    protected void setPrev(PathTile prev) {
        this.prev = prev;
        if (prev != null) {
            prev.next = this;
        }
    }

    /**
     * Kopiert eine Liste von Enemies.
     *
     * @param enemies die zu kopierende Liste
     * @return eine Kopie der Liste
     */
    private List<Enemy> copyList(List<Enemy> enemies) {
        List<Enemy> newEnemyList = new ArrayList<>();
        for (Enemy enemy : enemies) {
            newEnemyList.add(enemy.copy(this));
        }
        return newEnemyList;
    }

    /**
     * @return das erste PathTile in der Kette
     */
    PathTile getFirstPathTile() {
        PathTile current = this;
        while (current.prev != null) {
            current = current.prev;
        }
        return current;
    }

    /**
     * indexiert die PathTiles in der Kette aufsteigend
     */
    void indexPathTiles() {
        PathTile current = this;
        int index = 0;
        while (current != null) {
            current.index = index;
            current = current.next;
            index++;
        }
    }

    /**
     * @return die Liste der Enemies auf dem PathTile
     */
    public List<Enemy> getEnemies() {
        return enemies;
    }

    /**
     * @return das vorherige PathTile
     */
    public PathTile getPrev() {
        return prev;
    }

    /**
     * @return das nächste PathTile
     */
    public PathTile getNext() {
        return next;
    }

    /**
     * @return den Index des PathTiles
     */
    public int getIndex() {
        return index;
    }
}
