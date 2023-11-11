package com.example.manager.command;

import com.example.simulation.GameCharacterController;
import com.example.simulation.action.ActionLog;

import java.io.Serializable;

/**
 * Die Basisklasse für alle Commands.
 * Ein Command repräsentiert eine Aktion, die von einem Spieler ausgeführt wird.
 */
public abstract class Command implements Serializable {

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

    public boolean endsTurn() {
        return controller.isActive() && isEndTurn;
    }
}
