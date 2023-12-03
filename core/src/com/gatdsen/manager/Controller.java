package com.gatdsen.manager;

import com.gatdsen.manager.command.Command;
import com.gatdsen.manager.command.DisqualifyCommand;
import com.gatdsen.manager.command.EndTurnCommand;
import com.gatdsen.manager.command.MissNextTurnCommand;
import com.gatdsen.simulation.Tower;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Provides an access-controlled interface to send commands to players
 * <p>
 * Ermöglicht die Kontrolle eines bestimmten Charakters.
 * Ist nur für einen einzelnen Zug gültig und deaktiviert sich nach Ende des aktuellen Zuges.
 */
public final class Controller {

    final BlockingQueue<Command> commands = new ArrayBlockingQueue<>(256);
    private int uses;

    Controller(int uses) {
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

    public void placeTower(int x, int y, Tower.TowerType type) {
        //queue(new Command.PlaceTower(x, y));
    }

    public void upgradeTower(int x, int y) {
        //queue(new Command.PlaceTower(x, y, type, id));
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
    void endTurn() {
        commands.add(new EndTurnCommand());
        deactivate();
    }

    /**
     * Markiert das Ende des aktuellen Zuges für diesen Controller und deaktiviert diesen, ähnlich wie
     * {@link Controller#endTurn()}. Zusätzlich wird der Spieler aber für den nächsten Zug disqualifiziert.
     */
    void missNextTurn() {
        commands.add(new MissNextTurnCommand());
        deactivate();
    }

    /**
     * Markiert das Ende des aktuellen Zuges für diesen Controller und deaktiviert diesen, ähnlich wie
     * {@link Controller#endTurn()}. Zusätzlich wird der Spieler aber disqualifiziert, sodass das Spiel abgebrochen
     * werden kann.
     */
    void disqualify() {
        commands.add(new DisqualifyCommand());
        deactivate();
    }

    /**
     * Deaktiviert diesen Controller, sodass keine weiteren {@link Command}s mehr ausgeführt werden können.
     */
    private void deactivate() {
        uses = -1;
    }
}
