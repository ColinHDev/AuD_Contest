package com.gatdsen.manager.command;

import com.gatdsen.manager.player.PlayerHandler;
import com.gatdsen.simulation.PlayerController;
import com.gatdsen.simulation.action.ActionLog;

import java.io.Serializable;

/**
 * Die Basisklasse für alle Commands.
 * Ein Command repräsentiert eine Aktion, die von einem Spieler ausgeführt wird.
 */
public abstract class Command implements Serializable {

    protected abstract ActionLog onExecute(PlayerHandler playerHandler);

    public final ActionLog run(PlayerHandler playerHandler) {
        return onExecute(playerHandler);
    }

    public boolean endsTurn() {
        return false;
    }
}
