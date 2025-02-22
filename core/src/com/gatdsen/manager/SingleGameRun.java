package com.gatdsen.manager;

import com.gatdsen.manager.run.config.RunConfiguration;
import com.gatdsen.simulation.GameState;

import java.util.Arrays;

public class SingleGameRun extends Run {

    private float[] scores;


    public SingleGameRun(Manager manager, RunConfiguration runConfig) {
        super(manager, runConfig);
        GameConfig gameConfig = runConfig.asGameConfig();
        Executable game = runConfig.gameMode == GameState.GameMode.Replay ? new ReplayGame(gameConfig) : new Game(gameConfig);
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
