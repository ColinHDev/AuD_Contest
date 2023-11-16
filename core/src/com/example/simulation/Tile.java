package com.example.simulation;

public class Tile {

    private IntVector2 pos;

    Tile(int x, int y) {
        this.pos = new IntVector2(x, y);
    }

    public IntVector2 getPosition() {
        return pos;
    }

    protected Tile copy() {
        return new Tile(pos.x, pos.y);
    }


    /**
     * Gibt die umliegenden Tiles in einer bestimmten Reichweite zurück
     * @param range Reichweite um das Tile herum
     * @param board Map auf der nachgeschaut wird
     * @return
     */
    Tile[][] getNeighbours(int range, Tile[][] board) {
        int diameter = (range * 2) + 1;
        Tile[][] neighbours = new Tile[diameter][diameter];
        for (int i = 0; i < diameter; i++) {
            for (int j = 0; j < diameter; j++) {
                if (pos.x + range >=board.length || pos.x - range < 0 || pos.y -range < 0 || pos.y + range >= board[0].length ){
                    continue;
                }
                neighbours[i][j] = board[pos.x -range + i][pos.y -range + j];
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
