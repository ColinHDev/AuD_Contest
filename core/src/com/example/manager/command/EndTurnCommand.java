package com.example.manager.command;

import com.example.simulation.GameCharacterController;
import com.example.simulation.action.ActionLog;

/**
 * Dieser Befehl markiert das Ende eines Zuges und bricht die Befehlsausführung für den aktuellen Spieler im aktuellen
 * Zug ab.
 * Sollte NICHT direkt über den {@link com.example.manager.Controller} verfügbar sein.
 */
public class EndTurnCommand extends Command {

    @Override
    public ActionLog onExecute(GameCharacterController controller) {
        return null;
    }

    @Override
    public boolean endsTurn() {
        return true;
    }
}
