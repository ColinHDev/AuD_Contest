package com.example.simulation;

public class PathTile extends Tile{

    PathTile prev;
    PathTile next;
    //TODO: Add Copy Overwrite

    PathTile(int x, int y) {
        super(x, y);
    }
    public void setNext(PathTile next){
        this.next = next;
        next.prev = this;
    }

}
