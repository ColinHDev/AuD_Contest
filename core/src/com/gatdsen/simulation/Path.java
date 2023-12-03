package com.gatdsen.simulation;

import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;

/**
 * Speichert den Pfad, den ein Objekt zurücklegt, innerhalb des Intervalls zwischen 0 und {@link #getDuration()}
 */
public interface Path extends Serializable {

    /**
     * Gibt die Position für die angegebene Zeit zurück.
     * Liefert nur gültige Ergebnisse zwischen 0 und {@link #getDuration()} (einschließlich).
     *
     * @param t Zeit in Sekunden
     * @return die Position zur Zeit t
     */
    Vector2 getPos(float t);

    /**
     * Gibt eine Tangente an dem Pfad zur angegebenen Zeit zurück.
     * Liefert nur gültige Ergebnisse zwischen 0 und {@link #getDuration()} (einschließlich).
     *
     * @param t Zeit in Sekunden
     * @return die Bewegungsrichtung zur Zeit t
     */
    Vector2 getDir(float t);


    /**
     * @return die Dauer des Pfades in Sekunden
     */
    float getDuration();


    /**
     * Setzt die Dauer des Pfades in Sekunden.
     *
     * @param duration die Dauer des Pfades in Sekunden
     */
    void setDuration(float duration);

    /**
     * Setzt die Dauer des Pfades anhand der Start- und Endposition.
     *
     * @param endPosition die Endposition
     */
    void setDuration(Vector2 endPosition);

    /**
     * @return den Pfad als String
     */
    @Override
    String toString();
}
