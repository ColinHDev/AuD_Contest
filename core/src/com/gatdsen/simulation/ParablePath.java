package com.gatdsen.simulation;

import com.badlogic.gdx.math.Vector2;

/**
 * Speichert einen parabolischen Pfad von einem Punkt zu einem anderen.
 */
public class ParablePath implements Path {
    private float duration;
    private final Vector2 startPosition;
    private final static float g = 9.81f * 16; //9.81 meter/s^2 * 16 pixel pro meter
    private final Vector2 startVelocity;

    /**
     * Erstellt einen parabolischen Pfad von start zu end, der mit der angegebenen Geschwindigkeit zurückgelegt wird.
     *
     * @param startPosition Start-Vektor
     * @param endPosition   End-Vektor
     * @param startVelocity Geschwindigkeit
     */
    public ParablePath(Vector2 startPosition, Vector2 endPosition, Vector2 startVelocity) {
        this.startPosition = startPosition;
        this.startVelocity = startVelocity;
        if (startVelocity.cpy().nor().epsilonEquals(0, 1, 0.001f)) {
            float t = startVelocity.y / g * 2;
            this.duration = startPosition.epsilonEquals(endPosition, 0.001f) ? t * 2 : t;
            if (endPosition.y < startPosition.y)
                this.duration += ((startPosition.y - (endPosition.y + 9)) / startVelocity.y);
        } else {
            this.duration = -((startPosition.x - endPosition.x) / startVelocity.x);
        }
    }

    /**
     * Erstellt einen parabolischen Pfad von start zu end, der mit der angegebenen Geschwindigkeit und
     * Dauer zurückgelegt wird.
     *
     * @param startPosition Start-Vektor
     * @param endPosition   End-Vektor
     * @param startVelocity Geschwindigkeit
     * @param dur           Dauer
     */
    public ParablePath(Vector2 startPosition, Vector2 endPosition, Vector2 startVelocity, float dur) {
        this.startPosition = startPosition;
        this.startVelocity = startVelocity;
        this.duration = dur;
    }

    /**
     * Erstellt einen parabolischen Pfad von start zu end, der mit der angegebenen Geschwindigkeit und
     * Dauer zurückgelegt wird.
     *
     * @param startPosition Start-Vektor
     * @param startVelocity Geschwindigkeit
     * @param duration      Dauer
     */
    public ParablePath(Vector2 startPosition, float duration, Vector2 startVelocity) {
        this.startPosition = startPosition;
        this.startVelocity = startVelocity;
        this.duration = duration;
    }

    /**
     * Gibt die Position für die angegebene Zeit zurück.
     * Liefert nur gültige Ergebnisse zwischen 0 und {@link #getDuration()} (einschließlich).
     *
     * @param t Zeit in Sekunden
     * @return die Position zur Zeit t
     */
    @Override
    public Vector2 getPos(float t) {
        float x = (startVelocity.x * t) + startPosition.x;
        float y = (((startVelocity.y * t) - ((g / 2) * t * t)) + startPosition.y);
        return new Vector2(x, y);
    }

    /**
     * Gibt eine Tangente an dem Pfad zur angegebenen Zeit zurück.
     * Liefert nur gültige Ergebnisse zwischen 0 und {@link #getDuration()} (einschließlich).
     *
     * @param t Zeit in Sekunden
     * @return die Bewegungsrichtung zur Zeit t
     */
    public Vector2 getDir(float t) {
        return new Vector2(startVelocity.x, startVelocity.y - (g * t * t));
    }

    /**
     * @return die Dauer des Pfades in Sekunden
     */
    @Override
    public float getDuration() {
        return this.duration;
    }

    /**
     * Setzt die Dauer des Pfades in Sekunden.
     *
     * @param duration die Dauer des Pfades in Sekunden
     */
    @Override
    public void setDuration(float duration) {
        this.duration = duration;
    }

    /**
     * @return die Initialgeschwindigkeit als 2D-Vektor
     */
    public Vector2 getStartVelocity() {
        return startVelocity;
    }

    /**
     * Setzt die Dauer des Pfades anhand der Start- und Endposition.
     *
     * @param endPosition die Endposition
     */
    @Override
    public void setDuration(Vector2 endPosition) {
        this.duration = -((startPosition.x - endPosition.x) / startVelocity.x);
    }

    /**
     * @return der Pfad als String
     */
    @Override
    public String toString() {
        return "from: " + getPos(0) + "to: " + getPos(duration);
    }
}
