package com.example.animation.action;

import com.badlogic.gdx.math.Vector2;
import com.example.animation.entity.Entity;
import com.example.simulation.Path;

/**
 * Simple Animator Action that sets the angle of a Entity.
 */
public class RotateAction extends Action {

    private Entity target;
    private Vector2 angle;
    private float endTime;
    private Path path;

    /**
     * When only given an angle, the {@link RotateAction} is performed once.
     *
     * @param start  Starting time/Delay of the Action
     * @param target {@link Entity} to be rotated
     * @param angle  Vector the angle is gathered from
     */
    public RotateAction(float start, Entity target, Vector2 angle) {
        super(start);
        this.target = target;
        this.endTime = start;
        this.angle = angle;
    }

    /**
     * If the {@link RotateAction} is created with a {@link Path}, it will change the angle of {@link Entity} till it ended.
     *
     * @param start    Starting time/Delay of the Action
     * @param target   {@link Entity} to be rotated
     * @param duration Amount of time the Path takes to end
     * @param path     {@link Path} the angle/direction is used from
     */
    public RotateAction(float start, Entity target, float duration, Path path) {
        super(start);
        this.target = target;
        this.path = path;
        this.endTime = duration + start;
    }

    @Override
    protected void runAction(float oldTime, float current) {

        if (target != null) {
            if (path != null) {
                float time = Math.min(endTime, current);
                if (!path.getDir(time - delay).isZero())
                    this.target.setRelAngle(path.getDir(time - delay).angleDeg());
            } else {
                target.setRelAngle(this.angle.angleDeg());
                endAction(oldTime);
            }
        }

        if (current > endTime) {
            endAction(endTime);
        }
    }

    public void setTarget(Entity target) {
        this.target = target;
    }
}
