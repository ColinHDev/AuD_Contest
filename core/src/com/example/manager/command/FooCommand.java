package com.example.manager.command;

import com.example.simulation.GameCharacterController;
import com.example.simulation.action.ActionLog;

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
