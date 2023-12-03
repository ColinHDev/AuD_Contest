package com.gatdsen.manager.command;

import com.gatdsen.manager.player.PlayerHandler;
import com.gatdsen.simulation.action.ActionLog;

public class MissNextTurnCommand extends EndTurnCommand {

    @Override
    protected ActionLog onExecute(PlayerHandler playerHandler) {
        // TODO: playerHandler.missNextTurn();
        return super.onExecute(playerHandler);
    }
}
