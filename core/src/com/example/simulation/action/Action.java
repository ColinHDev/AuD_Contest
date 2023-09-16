package com.example.simulation.action;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents a single UI-relevant Event
 * Base-Component of the asynchronous interface between the simulation and ui/animation package
 */
public abstract class Action implements Iterable<Action>, Serializable {
    private final float delay;
    private final ArrayList<Action> arrayList = new ArrayList<>();

    /**
     * Constructs a new Action: every Action carries a time-based offset relative to its parent
     * @param delay non-negative time-based offset to its parent in seconds
     */
    public Action(float delay) {
        if (delay < 0) throw new InvalidParameterException("An Action's delay to its parent may only be non-negative");
        this.delay = delay;
    }

    /**
     * @return non-negative time-based offset to its parent in seconds
     */
    public float getDelay() {
        return this.delay;
    }

    /**
     * Every Action may have an arbitrary number of children, making it their parent.
     * A child is supposed to be executed, after its parent has completed execution.
     * The exact offset between these two events is determined by the child's delay.
     * @return A list of this Action's children
     */
    public ArrayList<Action> getChildren() {
        return this.arrayList;
    }

    /**
     * Adds another Action as a child of this action.
     * @param a Action to link
     */
    public void addChild(Action a) {
        this.arrayList.add(a);
    }

    /**
     * @return An iterator over the list of children
     */
    public Iterator<Action> iterator() {
        return this.arrayList.iterator();
    }

    public String toString(){
        return "Action: ";
    }

}
