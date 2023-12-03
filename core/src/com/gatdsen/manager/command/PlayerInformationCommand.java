package com.gatdsen.manager.command;

import com.gatdsen.manager.player.PlayerHandler;
import com.gatdsen.manager.player.data.PlayerInformation;
import com.gatdsen.simulation.action.ActionLog;

public final class PlayerInformationCommand extends Command {

    private final PlayerInformation information;

    public PlayerInformationCommand(PlayerInformation information) {
        this.information = information;
    }

    @Override
    protected ActionLog onExecute(PlayerHandler playerHandler) {
        return null;
    }
}
