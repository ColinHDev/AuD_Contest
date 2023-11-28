package com.gatdsen.animation.action;

public class IdleAction extends Action{

    private float end;

    public IdleAction(float delay, float duration) {
        super(delay);
        this.end = delay + duration;
    }

    @Override
    protected void runAction(float oldTime, float current) {
        if(current > end) endAction(end);
    }

}
