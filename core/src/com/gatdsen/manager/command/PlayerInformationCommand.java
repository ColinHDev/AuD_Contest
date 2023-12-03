package com.gatdsen.manager.command;

import com.gatdsen.manager.player.PlayerHandler;
import com.gatdsen.manager.player.data.PlayerInformation;
import com.gatdsen.simulation.action.ActionLog;

public final class PlayerInformationCommand extends Command {

    private final PlayerInformation information;
    private final long seedModifier;

    public PlayerInformationCommand(PlayerInformation information, long seedModifier) {
        this.information = information;
        this.seedModifier = seedModifier;
    }

    @Override
    protected ActionLog onExecute(PlayerHandler playerHandler) {
        return null;
    }
}
