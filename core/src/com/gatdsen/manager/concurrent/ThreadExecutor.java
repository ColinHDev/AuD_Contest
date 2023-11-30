package com.gatdsen.manager.concurrent;

import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class ThreadExecutor {

    private final Object lock = new Object();

    private final Object completion = new Object();

    private Thread worker;

    private FutureTask<?> target = null;

    public ThreadExecutor() {
        worker = new Thread(this::waitAndExecute);
        worker.start();
    }

    private void waitAndExecute() {
        while (!Thread.interrupted()) {
            synchronized (lock) {
                try {
                    if (target == null)
                        lock.wait();
                } catch (InterruptedException e) {
                    break;
                }
            }
            target.run();
            synchronized (lock) {
                synchronized (completion) {
                    target = null;
                    completion.notifyAll();
                }
            }
        }
    }

    public Future<?> execute(Runnable runnable) {
        FutureTask<?> target = new FutureTask<>(runnable, null);
        synchronized (lock) {
            if (this.target != null) return null;
            this.target = target;
            lock.notify();
        }
        return target;
    }

    @SuppressWarnings("removal")
    public void forceStop() {

        synchronized (lock) {
            synchronized (completion) {
                if (target != null) target.cancel(true);
                else return;
                target = null;
            }
        }
        worker.stop();
        worker = new Thread(this::waitAndExecute);
        worker.start();
    }

    @SuppressWarnings("removal")
    public void interrupt() {
        synchronized (lock) {
            if (target != null) target.cancel(true);
            target = null;
        }
        worker.interrupt();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        if (worker.isAlive())
            synchronized (lock) {
                worker.stop();
            }
    }

    public void waitForCompletion() {
        synchronized (completion) {
            if (target != null) {
                try {
                    completion.wait();
                } catch (InterruptedException ignored) {
                }
            }
        }

    }
}