package com.gatdsen.simulation.action;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class DebugPointAction extends Action{

    private final Vector2 pos;
    private final float[] color;
    private final float duration;
    private final boolean isCross;

    /**
     * Creates an Action that will place a point on screen for debugging-purposes.
     * Will be replaced by ParticleAction in the future.
     * The crossOption can be used to render a small 3px cross instead for better visibility.
     *
     * @param delay     non-negative time-based offset to its parent in seconds
     * @param pos       position the point should appear at
     * @param color     the color of the point
     * @param duration  how long the point lasts in seconds
     * @param isCross   true if the indicator should be a small cross instead
     */
    public DebugPointAction(float delay, Vector2 pos, Color color, float duration, boolean isCross) {
        super(delay);
        this.pos = pos;
        this.color = new float[]{color.r, color.g, color.b, color.a};
        this.duration = duration;
        this.isCross = isCross;
    }

    /**
     * @return position the point should appear at
     */
    public Vector2 getPos() {
        return pos;
    }
    /**
     * @return the color of the point
     */
    public Color getColor() {
        return new Color(color[0], color[1], color[2], color[3]);
    }

    /**
     * @return how long the point lasts in seconds
     */
    public float getDuration() {
        return duration;
    }

    /**
     * @return true if the indicator should be a small cross instead
     */
    public boolean isCross() {
        return isCross;
    }
}
