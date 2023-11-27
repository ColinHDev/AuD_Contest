package com.example.manager.command;

import com.example.simulation.GameCharacterController;
import com.example.simulation.action.ActionLog;

public final class PlayerInformationCommand extends Command {

    private final String name;
    private final String studentName;
    private final int matrikel;

    // TODO: PlayerInformation record
    public PlayerInformationCommand(String name, String studentName, int matrikel) {
        this.name = name;
        this.studentName = studentName;
        this.matrikel = matrikel;
    }

    @Override
    public ActionLog onExecute(GameCharacterController controller) {
        return null;
    }

    @Override
    public boolean endsTurn() {
        return true;
    }
}
