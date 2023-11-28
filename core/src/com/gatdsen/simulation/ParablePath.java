package com.gatdsen.simulation;

import com.badlogic.gdx.math.Vector2;

/**
 * Stores a parabolic path.
 */
public class ParablePath implements Path {

    private float duration;
    private final Vector2 startPosition;
    private final static float g = 9.81f * 16; //9.81 meter/s^2 * 16 pixel pro meter
    private final Vector2 startVelocity;

    /**
     * Creates a parabolic path, starting at the specified position with the specified velocity and is followed for the specified duration.
     *
     * @param startPosition
     * @param startVelocity
     * @param endPosition
     */
    public ParablePath(Vector2 startPosition, Vector2 endPosition, Vector2 startVelocity) {
        this.startPosition = startPosition;
        this.startVelocity = startVelocity;
        if (startVelocity.cpy().nor().epsilonEquals(0,1, 0.001f)) {
            float t = startVelocity.y / g * 2;
            this.duration = startPosition.epsilonEquals(endPosition, 0.001f) ? t * 2 : t;
            if (endPosition.y < startPosition.y) this.duration += ((startPosition.y - (endPosition.y + 9)) / startVelocity.y);
        } else {
            this.duration = -((startPosition.x - endPosition.x) / startVelocity.x);
        }
    }

    public ParablePath(Vector2 startPosition, Vector2 endPosition, Vector2 startVelocity, float dur) {
        this.startPosition = startPosition;
        this.startVelocity = startVelocity;
        this.duration = dur;
    }


    /**
     * Creates a parabolic path, starting at the specified position with the specified velocity and is followed for the specified duration.
     *
     * @param startPosition
     * @param startVelocity
     * @param duration
     */
    public ParablePath(Vector2 startPosition, float duration, Vector2 startVelocity) {
        this.startPosition = startPosition;
        this.startVelocity = startVelocity;
        this.duration = duration;
    }

    /**
     * Returns the position for the specified time.
     * Will only give valid results between 0 and {@link #getDuration()} (inclusive).
     * @param t time in seconds
     * @return the position at time t
     */
    @Override
    public Vector2 getPos(float t) {
        float x = (startVelocity.x * t) + startPosition.x;
        float y = (((startVelocity.y * t) - ((g / 2) * t * t)) + startPosition.y);
        return new Vector2(x, y);
    }

    /**
     * Returns a tangent on the path at the specified time.
     * Will only give valid results between 0 and {@link #getDuration()} (inclusive).
     * @param t time in seconds
     * @return the movement direction at time t
     */
    public Vector2 getDir(float t) {
        return new Vector2(startVelocity.x, startVelocity.y - (g * t * t));
    }

    /**
     * @return the maximum valid input-time for this path in seconds
     */
    @Override
    public float getDuration() {
        return this.duration;
    }

    @Override
    public void setDuration(float duration) {
        this.duration = duration;
    }

    /**
     * @return the initial velocity as 2D vector
     */
    public Vector2 getStartVelocity() {
        return startVelocity;
    }

    @Override
    public void setDuration(Vector2 endPosition) {
        this.duration = -((startPosition.x - endPosition.x) / startVelocity.x);
    }


    @Override
    public String toString() {
        String output = "from: " + getPos(0) + "to: " +getPos(duration);
        return output;
    }
}
