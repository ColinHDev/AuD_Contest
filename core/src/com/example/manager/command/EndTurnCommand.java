package com.example.manager.command;

import com.example.simulation.GameCharacterController;
import com.example.simulation.action.ActionLog;

/**
 * Command used for administrative purposes.
 * <p>
 * Marks the end of a turn and breaks command execution for the current player in the current turn.
 * Should NOT be available via the {@link com.example.manager.Controller}.
 */
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
