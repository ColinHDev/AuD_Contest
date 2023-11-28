package com.example.manager.command;

import com.example.manager.player.data.PlayerInformation;
import com.example.simulation.GameCharacterController;
import com.example.simulation.action.ActionLog;

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
