package com.example.simulation;

import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;

/**
 * Stores the path an object travels, within the interval between 0 and {@link #getDuration()}
 */
public interface Path extends Serializable {

    /**
     * Returns the position for the specified time.
     * Will only give valid results between 0 and {@link #getDuration()} (inclusive).
     * @param t time in seconds
     * @return the position at time t
     */
    Vector2 getPos(float t);

    /**
     * Returns a tangent on the path at the specified time.
     * Will only give valid results between 0 and {@link #getDuration()} (inclusive).
     * @param t time in seconds
     * @return the movement direction at time t
     */
    Vector2 getDir(float t);


    /**
     * @return the maximum valid input-time for this path in seconds
     */
    float getDuration();


    /**
     * sets the maximum valid input-time for this path
     * @param duration duration in seconds
     */
    void setDuration(float duration);

    void setDuration(Vector2 endPosition);

    String toString();
}
