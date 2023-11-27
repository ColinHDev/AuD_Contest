package com.gatdsen.manager.command;

import com.gatdsen.simulation.GameCharacterController;
import com.gatdsen.simulation.action.ActionLog;

/**
 * Example Command. Should be replaced by commands, representing the respective API calls
 */
//ToDo: remove
public class FooCommand extends Command{
    private int i;

    public FooCommand(GameCharacterController controller, int i) {
        super(controller);
        this.i = i;
    }

    @Override
    public ActionLog onExecute() {
        return controller.foo(i);
    }
}
