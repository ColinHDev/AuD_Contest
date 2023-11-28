package com.gatdsen.simulation;

import com.badlogic.gdx.math.Vector2;

/**
 * Stores a linear path from a start to an end point
 */
public class LinearPath implements Path {

    private final Vector2 start;
    private float duration;
    private final float v;
    private final Vector2 end;
    private final Vector2 dir;

    /**
     * Creates a linear path from start to end that will be travelled with the specified velocity.
     * The velocity has to be larger than zero.
     *
     * @param start Start-Vektor
     * @param end End-Vector
     * @param v velocity
     */
    public LinearPath(Vector2 start, Vector2 end, float v) {
        this.start = start;
        this.dir = end.cpy().sub(start);
        this.duration = dir.len() / v;
        this.end = end;
        this.v = v;
    }

    /**
     * Creates a Linear path from start to a duration given end that will be travelled with the specified velocity
     * The velocity has to be larger
     * @param start
     * @param dir
     * @param duration
     * @param v
     */
    public LinearPath(Vector2 start, Vector2 dir, float duration, float v) {
        this.start = start;
        this.end = start.cpy().add(dir.cpy().scl(duration * v));
        this.dir = end.cpy().sub(start);
        this.duration = dir.len();
        this.v = v;
    }

    /**
     * Returns the position for the specified time, using linear interpolation between start and end
     * Will only give valid results between 0 and {@link #getDuration()} (inclusive).
     * @param t time in seconds
     * @return the position at time t
     */
    @Override
    public Vector2 getPos(float t) {
        if (duration == 0) return start.cpy();
        double step = t / duration;
        Vector2 addV = new Vector2((float) (dir.x * step), (float) (dir.y * step));
        return start.cpy().add(addV);
    }

    /**
     * @return the duration
     */
    public float getDuration() {
        return duration;
    }

    /**
     * Returns the direction of this path.
     * Since this is a linear path it will be constant for all times t
     *
     * @param t time in seconds
     * @return the movement direction
     */
    @Override
    public Vector2 getDir(float t) {
        return dir;
    }

    /**
     * @return the start position of the path
     */
    protected Vector2 getStart() {
        return start;
    }

    /**
     * @return the end position of the path
     */
    protected Vector2 getEnd() {
        return getPos(duration);
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    @Override
    public void setDuration(Vector2 endPosition) {
        this.duration = endPosition.cpy().sub(start).len() / v;
    }

    @Override
    public String toString() {
        return "from: " + getPos(0) + "to: " +getPos(getDuration());
    }
}
