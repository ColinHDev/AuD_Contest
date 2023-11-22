package com.example.simulation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PathTile extends Tile {

    PathTile prev;
    PathTile next;
    List<Enemy> enemies;

    protected PathTile copy() {
        return new PathTile(getPosition().x, getPosition().y, copyList(enemies));
    }

    PathTile(int x, int y) {
        super(x, y);
    }

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
            newEnemyList.add(enemy);
        }
        return newEnemyList;
    }


}
