package com.gatdsen.animation.action;

import com.gatdsen.animation.entity.Entity;
import com.gatdsen.simulation.Path;

public class MoveAction extends Action {

    private Entity target;

    private Path path;

    private float endTime;

    public MoveAction(float delay, Entity target, float duration, Path path) {
        // ToDo: remove duration
        super(delay);
        this.target = target;
        this.path = path;
        this.endTime = delay + duration;
    }

    @Override
    protected void runAction(float oldTime, float current) {
        if (target != null) {
            float time = Math.min(endTime,current);
            target.setRelPos(path.getPos(time - super.delay));
        }
        if (current > endTime) endAction(endTime);
    }

    public void setTarget(Entity target) {
        this.target = target;
    }
}
