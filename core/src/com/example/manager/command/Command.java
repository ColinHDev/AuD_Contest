package com.example.manager.command;

import com.example.simulation.GameCharacterController;
import com.example.simulation.action.ActionLog;

/**
 * Base Class
 * Every Command has to inherit from this
 */
public abstract class Command {

    protected boolean isEndTurn = false;
    protected GameCharacterController controller;

    public Command(GameCharacterController controller) {
        this.controller = controller;
    }

    public abstract ActionLog onExecute();

    public ActionLog run(){
        if (controller.isActive()) return onExecute();
        return null;
    }

    public boolean isEndTurn() {
        return controller.isActive() && isEndTurn;
    }
}
