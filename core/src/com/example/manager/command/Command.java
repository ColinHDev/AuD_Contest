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

    public abstract ActionLog onExecute(GameCharacterController controller);

    public ActionLog run(GameCharacterController controller) {
        if (controller.isActive()) {
            return onExecute(controller);
        }
        return null;
    }

    public boolean endsTurn() {
        return isEndTurn;
    }
}
