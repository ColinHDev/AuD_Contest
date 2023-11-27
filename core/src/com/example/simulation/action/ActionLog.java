package com.example.simulation.action;

import java.io.Serializable;

/**
 * Represents a collection of all UI-relevant {@link Action Actions},
 * that accumulated during a certain part of the simulation. Usually this is the execution of a single command.
 */
public class ActionLog implements Serializable {

    private final Action rootAction;

    /**
     * Creates a new ActionLog. Due to the structure of {@link Action Actions}
     * this will result in a tree structure with the rootAction at its root.
     * When replaying the log the tree will be traversed in a fashion, where on
     * every node, each branch will be executed in parallel, able to further spilt at following nodes.
     * Additionally, even parallel branches may have different execution times.
     * @param rootAction the first Action of the log
     */
    public ActionLog(Action rootAction) {
        this.rootAction = rootAction;
    }

    /**
     * Will return the root of this tree-like structured log.
     * @return The rootAction
     */
    public Action getRootAction() {
        return rootAction;
    }

}
