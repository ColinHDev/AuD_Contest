package com.gatdsen.manager.command;

import com.gatdsen.manager.player.data.PlayerInformation;
import com.gatdsen.simulation.GameCharacterController;
import com.gatdsen.simulation.action.ActionLog;

public final class PlayerInformationCommand extends Command {

    public final PlayerInformation information;

    public PlayerInformationCommand(PlayerInformation information) {
        this.information = information;
    }

    @Override
    public ActionLog onExecute(GameCharacterController controller) {
        return null;
    }
}
