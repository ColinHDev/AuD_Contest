package com.gatdsen.simulation;

import java.util.ArrayList;
import java.util.List;

public abstract class Tile {

    protected IntVector2 pos;

    Tile(int x, int y) {
        this.pos = new IntVector2(x, y);
    }

    public IntVector2 getPosition() {
        return pos;
    }

    protected abstract Tile copy();


    /**
     * Gibt die umliegenden Tiles in einer bestimmten Reichweite zurück
     * @param range Reichweite um das Tile herum
     * @param board Map auf der nachgeschaut wird
     * @return
     */
    List<Tile> getNeighbours(int range, Tile[][] board) {
        int diameter = (range * 2) + 1;
        List<Tile> neighbours = new ArrayList<>(diameter * diameter - 1);
        for (int i = 0; i < diameter; i++) {
            for (int j = 0; j < diameter; j++) {
                if (pos.x + range >=board.length || pos.x - range < 0 || pos.y -range < 0 || pos.y + range >= board[0].length ){
                    continue;
                }
                neighbours.add(board[pos.x -range + i][pos.y -range + j]);
            }
        }
        return neighbours;
    }

    @Override
    public String toString() {
        return "Tile{" +
                ", pos=" + pos +
                '}';
    }
}
