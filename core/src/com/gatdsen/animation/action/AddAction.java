package com.gatdsen.animation.action;

import com.gatdsen.animation.entity.Entity;

public class AddAction extends Action{

    private final Entity target;
    private final Entity toAdd;

    public AddAction(float delay, Entity target, Entity toAdd) {
        super(delay);
        this.target = target;
        this.toAdd = toAdd;
    }

    @Override
    protected void runAction(float oldTime, float current) {
        if (target != null) target.add(toAdd);
        endAction(oldTime);
    }
}
