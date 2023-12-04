package com.gatdsen.manager.command;

/**
 * Die Basisklasse für alle Befehle, die einen Turm betreffen.
 */
public abstract class TowerCommand extends Command {

    protected final int x;
    protected final int y;

    /**
     * Erstellt einen neuen Befehl, der einen Turm betrifft.
     * @param x x-Koordinate des Turms
     * @param y y-Koordinate des Turms
     */
    public TowerCommand(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
