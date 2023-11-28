package com.gatdsen.manager;

import com.gatdsen.simulation.GameState;

import java.util.Arrays;

public class SingleGameRun extends Run {

    private float[] scores;


    public SingleGameRun(Manager manager, RunConfiguration runConfig) {
        super(manager, runConfig);
        Executable game = runConfig.gameMode == GameState.GameMode.Replay ? new ReplayGame(new GameConfig(runConfig)) : new Game(new GameConfig(runConfig));
        game.addCompletionListener(this::onGameCompletion);
        addGame(game);
    }

    public void onGameCompletion(Executable exec) {
        if (isCompleted()) throw new RuntimeException("In a single game run only one game may complete");
        if (exec instanceof Game) {
            Game game = (Game) exec;
            scores = game.getScores();
        }
        complete();
    }


    @Override
    public float[] getScores() {
        return scores;
    }

    @Override
    public String toString() {
        return "SingleGameRun{" +
                "super=" + super.toString() +
                ", scores=" + Arrays.toString(scores) +
                '}';
    }
}
