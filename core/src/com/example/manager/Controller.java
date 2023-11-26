package com.example.manager;

import com.example.manager.command.Command;
import com.example.manager.command.EndTurnCommand;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Provides an access-controlled interface to send commands to players
 * <p>
 * Ermöglicht die Kontrolle eines bestimmten Charakters.
 * Ist nur für einen einzelnen Zug gültig und deaktiviert sich nach Ende des aktuellen Zuges.
 */
public class Controller {

    private final Queue<Command> commands = new ArrayBlockingQueue<>(256);
    private int uses;

    protected Controller(int uses) {
        this.uses = uses;
    }

    /**
     * Die Zahl an Nutzungen ist für Bots auf 200 beschränkt.
     * Die maximale Zahl sinnvoller Züge beträgt ca. 70
     *
     * @return Die Menge an Befehlen, die dieser Controller noch ausführen kann
     */
    public int getRemainingUses() {
        return uses;
    }


    /**
     * Internal utility method.
     * Controls the remaining uses and submits cmd to the game.
     *
     * @param command the command to be queued
     */
    private void queue(Command command) {
        if (uses-- > 0) {
            commands.add(command);
        }
    }

    /**
     * Markiert das Ende des aktuellen Zuges für diesen Controller und deaktiviert diesen, sodass keine weiteren
     * {@link Command}s mehr ausgeführt werden können.
     */
    protected void endTurn() {
        commands.add(new EndTurnCommand());
        deactivate();
    }

    /**
     * Markiert das Ende des aktuellen Zuges für diesen Controller und deaktiviert diesen, ähnlich wie
     * {@link Controller#endTurn()}. Zusätzlich wird der Spieler aber für den nächsten Zug disqualifiziert.
     */
    protected void missNextTurn() {
        commands.add(new EndTurnCommand(EndTurnCommand.EndTurnPunishment.MISS_TURN));
        deactivate();
    }

    /**
     * Markiert das Ende des aktuellen Zuges für diesen Controller und deaktiviert diesen, ähnlich wie
     * {@link Controller#endTurn()}. Zusätzlich wird der Spieler aber disqualifiziert, sodass das Spiel abgebrochen
     * werden kann.
     */
    protected void disqualify() {
        commands.add(new EndTurnCommand(EndTurnCommand.EndTurnPunishment.DISQUALIFY));
        deactivate();
    }

    /**
     * Deaktiviert diesen Controller, sodass keine weiteren {@link Command}s mehr ausgeführt werden können.
     */
    private void deactivate() {
        uses = -1;
    }
}
