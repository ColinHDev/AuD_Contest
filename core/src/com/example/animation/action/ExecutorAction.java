package com.example.animation.action;

public class ExecutorAction extends Action{
    private float end = -1;
    private final ExecutionHandler handler;

    public interface ExecutionHandler{
        float onExecute();
    }

    public ExecutorAction(float start, ExecutionHandler handler) {
        super(start);
        this.handler = handler;
    }

    @Override
    protected void runAction(float oldTime, float current) {
        if (end<0){ end = delay + handler.onExecute();}
        if(current > end) endAction(end);
    }


}
