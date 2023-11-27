package com.example.animation.action;

public class DestroyAction<T> extends Action{


    private T target;
    private final DestroyListener<T> listener;
    private final Destroyer<T> destroyer;


    public interface DestroyListener<T>{
        void onDestroy(T destroyedEntity);

    }

    public interface Destroyer<T>{
        void destroy(T toDestroy);
    }

    public DestroyAction(float start, T target, DestroyListener<T> listener, Destroyer<T> destroyer) {
        super(start);
        this.target = target;
        this.listener = listener;
        this.destroyer = destroyer;
    }

    @Override
    protected void runAction(float oldTime, float current) {
        if (destroyer != null) destroyer.destroy(target);
        if (listener != null) listener.onDestroy(target);
        endAction(oldTime);
    }

    public void setTarget(T target) {
        this.target = target;
    }
}
