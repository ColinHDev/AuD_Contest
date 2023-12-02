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
     * Erstellt einen linearen Pfad von start zu end, der mit der angegebenen Geschwindigkeit zurückgelegt wird.
     * Die Geschwindigkeit muss größer als Null sein.
     *
     * @param start Start-Vektor
     * @param end   End-Vector
     * @param v     Geschwindigkeit
     */
    public LinearPath(Vector2 start, Vector2 end, float v) {
        this.start = start;
        this.dir = end.cpy().sub(start);
        this.duration = dir.len() / v;
        this.end = end;
        this.v = v;
    }

    /**
     * Erstellt einen linearen Pfad von start in Richtung dir, der mit der angegebenen Geschwindigkeit zurückgelegt wird.
     * Die Geschwindigkeit muss größer als Null sein.
     *
     * @param start    Start-Vektor
     * @param dir      Richtungs-Vektor
     * @param duration Dauer
     * @param v        Geschwindigkeit
     */
    public LinearPath(Vector2 start, Vector2 dir, float duration, float v) {
        this.start = start;
        this.end = start.cpy().add(dir.cpy().scl(duration * v));
        this.dir = end.cpy().sub(start);
        this.duration = dir.len();
        this.v = v;
    }

    /**
     * Gibt die Position für die angegebene Zeit zurück, indem eine lineare Interpolation zwischen Start und Ende verwendet wird.
     * Liefert nur gültige Ergebnisse zwischen 0 und {@link #getDuration()} (einschließlich).
     *
     * @param t Zeit in Sekunden
     * @return die Position zur Zeit t
     */
    @Override
    public Vector2 getPos(float t) {
        if (duration == 0) return start.cpy();
        double step = t / duration;
        Vector2 addV = new Vector2((float) (dir.x * step), (float) (dir.y * step));
        return start.cpy().add(addV);
    }

    /**
     * @return die Dauer des Pfades in Sekunden
     */
    public float getDuration() {
        return duration;
    }

    /**
     * Gibt die Richtung dieses Pfades zurück.
     * Da dies ein linearer Pfad ist, wird er für alle Zeiten t konstant sein.
     *
     * @param t Zeit in Sekunden
     * @return die Bewegungsrichtung
     */
    @Override
    public Vector2 getDir(float t) {
        return dir;
    }

    /**
     * @return die Startposition des Pfades
     */
    protected Vector2 getStart() {
        return start;
    }

    /**
     * @return die Endposition des Pfades
     */
    protected Vector2 getEnd() {
        return getPos(duration);
    }

    /**
     * Setzt die Dauer des Pfades.
     *
     * @param duration die neue Dauer in Sekunden
     */
    public void setDuration(float duration) {
        this.duration = duration;
    }

    /**
     * Setzt die Dauer des Pfades anhand der Endposition.
     *
     * @param endPosition die neue Endposition
     */
    @Override
    public void setDuration(Vector2 endPosition) {
        this.duration = endPosition.cpy().sub(start).len() / v;
    }

    /**
     * @return der Pfad als String
     */
    @Override
    public String toString() {
        return "from: " + getPos(0) + "to: " + getPos(getDuration());
    }
}
