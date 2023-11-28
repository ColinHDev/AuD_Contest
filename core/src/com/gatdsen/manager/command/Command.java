package com.gatdsen.manager.command;

import com.gatdsen.simulation.GameCharacterController;
import com.gatdsen.simulation.action.ActionLog;

import java.io.Serializable;

/**
 * Die Basisklasse für alle Commands.
 * Ein Command repräsentiert eine Aktion, die von einem Spieler ausgeführt wird.
 */
public abstract class Command implements Serializable {

    public abstract ActionLog onExecute(GameCharacterController controller);

    public final ActionLog run(GameCharacterController controller) {
        if (controller.isActive()) {
            return onExecute(controller);
        }
        return null;
    }

    public boolean endsTurn() {
        return false;
    }
}
