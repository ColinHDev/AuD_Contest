package com.gatdsen.manager.command;

import com.gatdsen.manager.player.PlayerHandler;
import com.gatdsen.simulation.action.ActionLog;

/**
 * Dieser Befehl markiert das Ende eines Zuges und bricht die Befehlsausführung für den aktuellen Spieler im aktuellen
 * Zug ab, ähnlich wie {@link EndTurnCommand}. Zusätzlich wird der Spieler aber den darauffolgenden Zug aussetzen.
 * <p>
 * Sollte NICHT direkt über den {@link com.gatdsen.manager.Controller} verfügbar sein.
 */
public class MissNextTurnCommand extends EndTurnCommand {

    @Override
    protected ActionLog onExecute(PlayerHandler playerHandler) {
        // TODO: playerHandler.missNextTurn();
        return super.onExecute(playerHandler);
    }
}
