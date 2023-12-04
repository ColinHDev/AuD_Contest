package com.gatdsen.manager;

import com.gatdsen.manager.command.*;
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

    /**
     * Platziert einen neuen Turm auf dem Spielfeld
     * @param x x-Koordinate, an der der Turm platziert werden soll
     * @param y y-Koordinate, an der der Turm platziert werden soll
     * @param type Typ des Turms, der platziert werden soll
     */
    public void placeTower(int x, int y, Tower.TowerType type) {
        queue(new PlaceTowerCommand(x, y, type));
    }

    /**
     * Verbessert einen Turm auf dem Spielfeld
     * @param x x-Koordinate, an der sich der Turm befindet
     * @param y y-Koordinate, an der sich der Turm befindet
     */
    public void upgradeTower(int x, int y) {
        queue(new UpgradeTowerCommand(x, y));
    }

    /**
     * Verkauft einen Turm auf dem Spielfeld
     * @param x x-Koordinate, an der sich der Turm befindet
     * @param y y-Koordinate, an der sich der Turm befindet
     */
    /* TODO:
    public void sellTower(int x, int y) {
        queue(new SellTowerCommand(x, y));
    }*/

    /**
     * Internal utility method.
     * Controls the remaining uses and submits cmd to the game.
     *
     * @param command the command to be queued
     */
    private void queue(Command command) {
        if (!isDeactivated()) {
            commands.add(command);
        }
    }

    /**
     * Markiert das Ende des aktuellen Zuges für diesen Controller und deaktiviert diesen, sodass keine weiteren
     * {@link Command}s mehr ausgeführt werden können.
     */
    void endTurn() {
        if (!isDeactivated()) {
            commands.add(new EndTurnCommand());
            deactivate();
        }
    }

    /**
     * Markiert das Ende des aktuellen Zuges für diesen Controller und deaktiviert diesen, ähnlich wie
     * {@link Controller#endTurn()}. Zusätzlich wird der Spieler aber muss der Spieler den darauffolgenden Zug aussetzen.
     */
    void missNextTurn() {
        if (!isDeactivated()) {
            commands.add(new MissNextTurnCommand());
            deactivate();
        }
    }

    /**
     * Markiert das Ende des aktuellen Zuges für diesen Controller und deaktiviert diesen, ähnlich wie
     * {@link Controller#endTurn()}. Zusätzlich wird der Spieler aber disqualifiziert, sodass das Spiel abgebrochen
     * werden kann.
     */
    void disqualify() {
        if (!isDeactivated()) {
            commands.add(new DisqualifyCommand());
            deactivate();
        }
    }

    /**
     * Gibt an, ob dieser Controller deaktiviert ist.
     * @return true, wenn dieser Controller deaktiviert ist, sonst false
     */
    private boolean isDeactivated() {
        return uses == -1;
    }

    /**
     * Deaktiviert diesen Controller, sodass keine weiteren {@link Command}s mehr ausgeführt werden können.
     */
    private void deactivate() {
        uses = -1;
    }
}
