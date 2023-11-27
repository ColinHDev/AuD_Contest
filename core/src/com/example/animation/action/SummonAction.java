package com.example.animation.action;

public class SummonAction<T> extends Action{

    private final SummonListener<T> listener;
    private final Summoner<T> summoner;

    public interface SummonListener<T>{
        void onSummon(T summonedEntity);

    }

    public interface Summoner<T>{
        T summon();
    }

    public SummonAction(float start, SummonListener<T> listener, Summoner<T> summoner) {
        super(start);
        this.listener = listener;
        this.summoner = summoner;
    }

    @Override
    protected void runAction(float oldTime, float current) {
        T entity = null;
        if (summoner != null) entity = summoner.summon();
        if (listener != null) listener.onSummon(entity);
        endAction(oldTime);
    }
}
