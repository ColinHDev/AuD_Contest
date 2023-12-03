package com.gatdsen.manager.command;

import com.gatdsen.manager.player.PlayerHandler;
import com.gatdsen.simulation.PlayerController;
import com.gatdsen.simulation.action.ActionLog;

/**
 * Dieser Befehl markiert das Ende eines Zuges und bricht die Befehlsausführung für den aktuellen Spieler im aktuellen
 * Zug ab.
 * Sollte NICHT direkt über den {@link com.gatdsen.manager.Controller} verfügbar sein.
 */
public class EndTurnCommand extends Command {

    @Override
    protected ActionLog onExecute(PlayerHandler playerHandler) {
        return null;
    }

    @Override
    public boolean endsTurn() {
        return true;
    }
}
