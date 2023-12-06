package com.gatdsen.manager.command;

import com.gatdsen.manager.player.PlayerHandler;
import com.gatdsen.simulation.action.ActionLog;

/**
 * Dieser Befehl verkauft einen Turm, der sich auf dem Spielfeld befindet
 */
public class SellTowerCommand extends TowerCommand {

    /**
     * Erstellt einen neuen Befehl, der einen Turm verkauft.
     * @param x x-Koordinate des Turms, der verkauft werden soll
     * @param y y-Koordinate des Turms, der verkauft werden soll
     */
    public SellTowerCommand(int x, int y) {
        super(x, y);
    }

    @Override
    protected ActionLog onExecute(PlayerHandler playerHandler) {
        return playerHandler.getPlayerController().sellTower(x, y);
    }
}
