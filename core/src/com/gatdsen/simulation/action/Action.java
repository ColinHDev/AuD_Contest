package com.gatdsen.simulation.action;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Representiert ein einzelnes UI-relevantes Event
 * Basiskomponente der asynchronen Schnittstelle zwischen Simulation und UI/Animation
 */
public abstract class Action implements Iterable<Action>, Serializable {
    private final float delay;
    private final ArrayList<Action> arrayList = new ArrayList<>();

    /**
     * Konstruiert eine neue Action: jede Action trägt einen zeitbasierten Offset relativ zu ihrem Elternteil
     *
     * @param delay nicht-negativer zeitbasierter Offset zu seinem Elternteil in Sekunden
     */
    public Action(float delay) {
        if (delay < 0) throw new InvalidParameterException("An Action's delay to its parent may only be non-negative");
        this.delay = delay;
    }

    /**
     * @return nicht-negativer zeitbasierter Offset zu seinem Elternteil in Sekunden
     */
    public float getDelay() {
        return this.delay;
    }

    /**
     * Jede Action kann eine beliebige Anzahl von Kindern haben, die sie als Elternteil hat.
     * Ein Kind soll ausgeführt werden, nachdem sein Elternteil die Ausführung abgeschlossen hat.
     * Der genaue Offset zwischen diesen beiden Ereignissen wird durch die Verzögerung des Kindes bestimmt.
     *
     * @return Eine Liste der Kinder dieser Aktion
     */
    public ArrayList<Action> getChildren() {
        return this.arrayList;
    }

    /**
     * Fügt eine weitere Aktion als Kind dieser Aktion hinzu.
     *
     * @param a Aktion zum Verknüpfen
     */
    public void addChild(Action a) {
        this.arrayList.add(a);
    }

    /**
     * @return Ein Iterator über die Liste der Kinder
     */
    public Iterator<Action> iterator() {
        return this.arrayList.iterator();
    }

    /**
     * @return Eine String-Repräsentation dieser Aktion
     */
    @Override
    public String toString() {
        return "Action: ";
    }
}
