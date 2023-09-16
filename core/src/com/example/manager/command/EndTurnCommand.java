package com.example.manager.command;

import com.example.simulation.GameCharacterController;
import com.example.simulation.action.ActionLog;

public class EndTurnCommand extends Command{
    public EndTurnCommand(GameCharacterController controller) {
        super(controller);
        isEndTurn = true;
    }

    @Override
    public ActionLog onExecute() {
        return null;
    }

}
