package com.gatdsen.manager.command;

import com.gatdsen.manager.player.PlayerHandler;
import com.gatdsen.simulation.action.ActionLog;

/**
 * Dieser Befehl verbessert einen Turm, der sich auf dem Spielfeld befindet
 */
public class UpgradeTowerCommand extends TowerCommand {

    /**
     * Erstellt einen neuen Befehl, der einen Turm verbessert.
     * @param x x-Koordinate des Turms, der verbessert werden soll
     * @param y y-Koordinate des Turms, der verbessert werden soll
     */
    public UpgradeTowerCommand(int x, int y) {
        super(x, y);
    }

    @Override
    protected ActionLog onExecute(PlayerHandler playerHandler) {
        return playerHandler.getPlayerController().upgradeTower(x, y);
    }
}
