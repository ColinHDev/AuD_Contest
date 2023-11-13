package com.example.manager;

import com.example.manager.command.Command;

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
     * Deaktiviert diesen Controller (Wenn der Zug vorbei ist). Wird von internen Komponenten genutzt, um den Zugfolge der Charaktere zu steuern.
     */
    protected void deactivate() {
        uses = -1;
    }
}
