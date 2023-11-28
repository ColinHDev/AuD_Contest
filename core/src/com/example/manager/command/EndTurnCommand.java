package com.example.manager.command;

import com.example.simulation.GameCharacterController;
import com.example.simulation.action.ActionLog;

/**
 * Dieser Befehl markiert das Ende eines Zuges und bricht die Befehlsausführung für den aktuellen Spieler im aktuellen
 * Zug ab.
 * Sollte NICHT direkt über den {@link com.example.manager.Controller} verfügbar sein.
 */
public class EndTurnCommand extends Command {

    public enum EndTurnPunishment {
        // Der Spieler erhält keine Strafe
        NONE,
        // Der Spieler muss den nächsten Zug aussetzen
        MISS_TURN,
        // Der Spieler wird disqualifiziert und das Spiel beendet
        DISQUALIFY
    }

    private final EndTurnPunishment punishment;

    /**
     * Erstellt einen neuen Befehl, der das Ende des aktuellen Zuges markiert.
     * Der Spieler erhält keine Strafe.
     */
    public EndTurnCommand() {
        this(EndTurnPunishment.NONE);
    }

    /**
     * Erstellt einen neuen Befehl, der das Ende des aktuellen Zuges markiert.
     * @param punishment Die Strafe, die der Spieler auf Basis des Zuges erhält
     */
    public EndTurnCommand(EndTurnPunishment punishment) {
        this.punishment = punishment;
    }

    @Override
    public ActionLog onExecute(GameCharacterController controller) {
        return null;
    }

    @Override
    public boolean endsTurn() {
        return true;
    }
}
