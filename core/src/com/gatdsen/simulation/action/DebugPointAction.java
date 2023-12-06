package com.gatdsen.simulation.action;

import com.badlogic.gdx.graphics.Color;
import com.gatdsen.simulation.IntVector2;

/**
 * Die DebugPointAction wird verwendet, um einen Punkt auf dem Bildschirm zu rendern.
 * Wird in Zukunft durch ParticleAction ersetzt.
 */
public class DebugPointAction extends Action {
    private final IntVector2 pos;
    private final float[] color;
    private final float duration;
    private final boolean isCross;

    /**
     * Erstellt eine Aktion, die einen Punkt zum Debuggen auf dem Bildschirm platziert.
     * Wird in Zukunft durch ParticleAction ersetzt.
     * Zur Verbesserung der Sichtbarkeit, kann die Cross-Option verwendet werden,
     * um stattdessen ein kleines 3px-Kreuz zu rendern.
     *
     * @param delay    nicht-negativer zeitbasierter Offset zu seinem Elternteil in Sekunden
     * @param pos      Postion an der der Punkt erscheinen soll
     * @param color    Farbe des Punktes
     * @param duration wie lange der Punkt in Sekunden existiert
     * @param isCross  true, wenn der Indikator ein kleines Kreuz sein soll
     */
    public DebugPointAction(float delay, IntVector2 pos, Color color, float duration, boolean isCross) {
        super(delay);
        this.pos = pos;
        this.color = new float[]{color.r, color.g, color.b, color.a};
        this.duration = duration;
        this.isCross = isCross;
    }

    /**
     * @return Position, an der der Punkt erscheinen soll
     */
    public IntVector2 getPos() {
        return pos;
    }

    /**
     * @return Farbe des Punktes
     */
    public Color getColor() {
        return new Color(color[0], color[1], color[2], color[3]);
    }

    /**
     * @return wie lange der Punkt in Sekunden existiert
     */
    public float getDuration() {
        return duration;
    }

    /**
     * @return true, wenn der Indikator ein kleines Kreuz sein soll
     */
    public boolean isCross() {
        return isCross;
    }

    @Override
    public String toString() {
        return "DebugPointAction{" +
                "pos=" + pos +
                ", color=" + getColor() +
                ", duration=" + duration +
                ", isCross=" + isCross +
                '}' + super.toString();
    }
}
