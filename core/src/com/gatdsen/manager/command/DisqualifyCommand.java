package com.gatdsen.manager.command;

import com.gatdsen.manager.player.PlayerHandler;
import com.gatdsen.simulation.action.ActionLog;

public class DisqualifyCommand extends EndTurnCommand {

    @Override
    protected ActionLog onExecute(PlayerHandler playerHandler) {
        // TODO: playerHandler.getPlayerController().disqualify();
        return super.onExecute(playerHandler);
    }
}
