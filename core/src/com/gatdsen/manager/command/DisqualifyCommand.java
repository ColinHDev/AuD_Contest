package com.gatdsen.manager.command;

import com.gatdsen.manager.player.PlayerHandler;
import com.gatdsen.simulation.action.ActionLog;

/**
 * Dieser Befehl markiert das Ende eines Zuges und bricht die Befehlsausführung für den aktuellen Spieler im aktuellen
 * Zug ab, ähnlich wie {@link EndTurnCommand}. Zusätzlich wird der Spieler aber disqualifiziert, sodass das Spiel
 * abgebrochen werden kann.
 * <p>
 * Sollte NICHT direkt über den {@link com.gatdsen.manager.Controller} verfügbar sein.
 */
public class DisqualifyCommand extends EndTurnCommand {

    @Override
    protected ActionLog onExecute(PlayerHandler playerHandler) {
        playerHandler.getPlayerController().disqualify();
        return super.onExecute(playerHandler);
    }
}
