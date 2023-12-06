package com.gatdsen.animation;

import com.badlogic.gdx.math.Vector2;
import com.gatdsen.simulation.Path;

public class AnimatorPath implements Path {
    private Path simPath;
    private Vector2 boardPos;
    private int tileSize;


    public AnimatorPath(Path simPath, Vector2 boardPos, int tileSize) {
        this.simPath = simPath;
        this.boardPos = boardPos;
        this.tileSize = tileSize;
    }

    @Override
    public Vector2 getPos(float t) {
        return simPath.getPos(t).scl(tileSize).add(boardPos);
    }

    @Override
    public Vector2 getDir(float t) {
        return simPath.getDir(t).scl(tileSize).add(boardPos);
    }

    @Override
    public float getDuration() {
        return simPath.getDuration();
    }

    @Override
    public void setDuration(float duration) {
        simPath.setDuration(duration);
    }

    @Override
    public void setDuration(Vector2 endPosition) {
        simPath.setDuration(endPosition);
    }

    @Override
    public String toString() {
        return simPath.toString();
    }
}
