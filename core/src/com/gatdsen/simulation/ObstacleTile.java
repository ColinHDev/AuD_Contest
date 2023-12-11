package com.gatdsen.simulation;

public class ObstacleTile extends Tile{
    /**
     * Erstellt ein Hindernis (Obstacle) an der angegebenen Position.
     * @param x x-Koordinate
     * @param y y-Koordinate
     */
    ObstacleTile(int x, int y) {
        super(x, y);
    }

    ObstacleTile(ObstacleTile original) {
        super(original.getPosition().x, original.getPosition().y);
    }

 
    @Override
    protected Tile copy() {
        return new ObstacleTile(this);
    }
}
