package com.gatdsen.simulation;

import java.util.ArrayList;
import java.util.List;

public class PathTile extends Tile {

    private PathTile prev;
    private PathTile next;
    private int index;
    private List<Enemy> enemies;

    PathTile(int x, int y) {
        super(x, y);
    }

    protected PathTile copy() {
        return new PathTile(getPosition().x, getPosition().y, copyList(enemies));
    }

    /**
     * Creates a Deep-Copy of the PathTile
     */
    PathTile(int x, int y, List<Enemy> enemies){
        super(x,y);
        this.enemies = enemies;
    }

    protected void setNext(PathTile next) {
        this.next = next;
        if (next != null)
            next.prev = this;
    }
    protected void setPrev(PathTile prev){
        this.prev = prev;
        if (prev != null){
            prev.next = this;
        }
    }
    
    private List<Enemy> copyList(List<Enemy> enemies){
        List<Enemy> newEnemyList = new ArrayList<>();
        for (Enemy enemy: enemies) {
            newEnemyList.add(enemy.copy(this));
        }
        return newEnemyList;
    }

    PathTile getFirstPathTile(){
        PathTile current = this;
        while (current.prev != null){
            current = current.prev;
        }
        return current;
    }

    void indexPathTiles(){
        PathTile current = this;
        int index = 0;
        while (current != null){
            current.index = index;
            current = current.next;
            index++;
        }
    }

    public List<Enemy> getEnemies(){
        return enemies;
    }

    // ToDo: getter for prev
    public PathTile getPrev() {
        return prev;
    }

    // ToDo: getter for next
    public PathTile getNext() {
        return next;
    }

    public int getIndex(){
        return index;
    }
}
