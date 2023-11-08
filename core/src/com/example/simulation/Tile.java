package com.example.simulation;

public class Tile {

    private IntVector2 pos;

    Tile(int x, int y){
        this.pos = new IntVector2(x, y);
    }

    public IntVector2 getPosition() {
        return pos;
    }



    public Tile copy(){
        return new Tile(pos.x, pos.y);
    }


    @Override
    public String toString() {
        return "Tile{" +
                ", pos=" + pos +
                '}';
    }
}
