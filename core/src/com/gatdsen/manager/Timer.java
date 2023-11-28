package com.gatdsen.manager;

import java.util.concurrent.TimeUnit;

public class Timer {

    private final long start;
    private final long duration;

    public Timer(long durationNanos){
        this.duration = durationNanos;
        start = System.nanoTime();
    }

    public long getRemainingNanos(){
        return duration + start - System.nanoTime();
    }

    public long getRemainingTime(TimeUnit unit){
        return unit.convert(getRemainingNanos(), TimeUnit.NANOSECONDS);
    }
}
