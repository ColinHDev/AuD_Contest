package com.gatdsen.manager;

import com.gatdsen.manager.player.Player;
import com.gatdsen.simulation.GameState;

import java.util.ArrayList;

public abstract class Run {


    private boolean completed = false;

    private final Object schedulingLock = new Object();
    private final ArrayList<CompletionHandler<Run>> completionListeners = new ArrayList<>();

    protected final Manager manager;

    protected GameState.GameMode gameMode;
    private final ArrayList<Executable> games = new ArrayList<>();

    private boolean disposed = false;
    private final ArrayList<Class<? extends Player>> players;

    public Run(Manager manager, RunConfiguration runConfig) {
        this.players = new ArrayList<>(runConfig.players);
        gameMode = runConfig.gameMode;
        this.manager = manager;
    }

    public static Run getRun(Manager manager, RunConfiguration runConfig) {
        switch (runConfig.gameMode) {
            case Campaign:
            case Replay:
            case Normal:
                return new SingleGameRun(manager, runConfig);
            case Exam_Admission:
            case Tournament_Phase_1:
                return new ParallelMultiGameRun(manager, runConfig);
            case Tournament_Phase_2:
                return new TournamentRun(manager, runConfig);
            default:
                throw new IllegalArgumentException(runConfig.gameMode.toString() + " is not processed by the Run interface");
        }
    }

    public ArrayList<Executable> getGames() {
        return games;
    }

    protected void addGame(Executable game) {
        synchronized (schedulingLock) {
            if (!disposed) {
                games.add(game);
                manager.schedule(game);
            }
        }
    }

    public void dispose() {
        synchronized (schedulingLock) {
            disposed = true;
            for (Executable game : games
            ) {
                manager.stop(game);
            }
        }
    }

    public GameState.GameMode getGameMode() {
        return gameMode;
    }

    public boolean isCompleted() {
        return completed;
    }

    public ArrayList<Class<? extends Player>> getPlayers() {
        return players;
    }

    protected void complete() {
        synchronized (schedulingLock) {
            completed = true;
            for (CompletionHandler<Run> completionListener : completionListeners) {
                completionListener.onComplete(this);
            }
        }
    }

    public abstract float[] getScores();

    public void addCompletionListener(CompletionHandler<Run> handler) {
        synchronized (schedulingLock) {
            completionListeners.add(handler);
            if (completed) new Thread(() -> handler.onComplete(this)).start();
        }
    }

    @Override
    public String toString() {
        return "Run{" +
                "completed=" + completed +
                ", completionListeners=" + completionListeners +
                ", gameMode=" + gameMode +
                ", games=" + games +
                ", players=" + players +
                '}';
    }
}
