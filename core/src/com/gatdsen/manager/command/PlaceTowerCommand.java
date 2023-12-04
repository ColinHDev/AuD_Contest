package com.gatdsen.manager.command;

import com.gatdsen.manager.player.PlayerHandler;
import com.gatdsen.simulation.Tower;
import com.gatdsen.simulation.action.ActionLog;

/**
 * Dieser Befehl platziert einen neuen Turm auf dem Spielfeld.
 */
public class PlaceTowerCommand extends TowerCommand {

    protected final Tower.TowerType type;

    /**
     * Erstellt einen neuen Befehl, der einen neuen Turm platziert.
     * @param x x-Koordinate, an der der Turm platziert werden soll
     * @param y y-Koordinate, an der der Turm platziert werden soll
     * @param type Typ des Turms, der platziert werden soll
     */
    public PlaceTowerCommand(int x, int y, Tower.TowerType type) {
        super(x, y);
        this.type = type;
    }

    @Override
    protected ActionLog onExecute(PlayerHandler playerHandler) {
        return playerHandler.getPlayerController().placeTower(x, y, type);
    }
}
