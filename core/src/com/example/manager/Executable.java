package com.example.manager;

import com.example.ui.hud.UiMessenger;

import java.util.ArrayList;
import java.util.List;

public abstract class Executable {


    protected static final int REQUIRED_THREAD_COUNT = 2;

    enum Status {
        INITIALIZED,
        SCHEDULED,
        ACTIVE,
        PAUSED,
        COMPLETED,
        ABORTED
    }

    protected final Object schedulingLock = new Object();

    private Status status = Status.INITIALIZED;

    protected final ArrayList<CompletionHandler<Executable>> completionListeners = new ArrayList<>();

    protected final InputProcessor inputGenerator;

    protected final AnimationLogProcessor animationLogProcessor;

    protected final boolean gui;

    private final List<HumanPlayer> humanList = new ArrayList<>();

    private Thread simulationThread;
    protected final UiMessenger uiMessenger;
    protected boolean pendingShutdown = false;

    protected GameConfig config;
    protected final boolean saveReplay;


    protected Executable(GameConfig config) {
        this.config = config;
        gui = config.gui;
        saveReplay = config.replay;
        animationLogProcessor = config.animationLogProcessor;
        inputGenerator = config.inputProcessor;
        uiMessenger = config.uiMessenger;
    }


    public abstract void start();

    protected void setStatus(Status newStatus) {
        status = newStatus;
    }



    public void addCompletionListener(CompletionHandler<Executable> handler) {
        completionListeners.add(handler);
        if (status == Status.COMPLETED) handler.onComplete(this);
    }

    public void dispose() {
        pendingShutdown = true;
    }

    public List<HumanPlayer> getHumanList() {
        return humanList;
    }

    public Status getStatus() {
        return status;
    }

    protected void schedule() {
        synchronized (schedulingLock) {
            if (status == Status.INITIALIZED)
                setStatus(Status.SCHEDULED);
        }
    }

    protected void crashHandler(Thread thread, Throwable throwable) {
        System.err.println("Error in game: " + this);
        System.err.println("Error in thread: " + thread);
        throwable.printStackTrace();
        System.err.println("Game crashed during execution\nIf you see this message, please forward all console logs to wettbewerb@acagamics.de");
        Manager.getManager().stop(this);
    }

    protected void pause() {
        synchronized (schedulingLock) {
            if (status == Status.ACTIVE)
                setStatus(Status.PAUSED);
        }
    }

    protected void resume() {
        synchronized (schedulingLock) {
            if (status != Status.PAUSED) return;
            setStatus(Status.ACTIVE);
            schedulingLock.notify();
        }
    }

    protected void abort() {
        synchronized (schedulingLock) {
            setStatus(Status.ABORTED);
            dispose();
        }
    }

    protected abstract String[] getPlayerNames();

    public abstract boolean shouldSaveReplay();

    public abstract GameResults getGameResults();

    @Override
    public String toString() {
        return "Executable{" +
                "status=" + status +
                ", completionListeners=" + completionListeners +
                ", inputGenerator=" + inputGenerator +
                ", animationLogProcessor=" + animationLogProcessor +
                ", gui=" + gui +
                ", humanList=" + humanList +
                ", simulationThread=" + simulationThread +
                ", uiMessenger=" + uiMessenger +
                ", pendingShutdown=" + pendingShutdown +
                ", config=" + config +
                '}';
    }
}
