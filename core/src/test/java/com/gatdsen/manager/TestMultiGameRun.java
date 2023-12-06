/*
package com.gatdsen.manager;


import com.gatdsen.manager.player.IdleBot;
import com.gatdsen.simulation.GameState;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TestMultiGameRun {

    private static final long GAME_COMPLETION_TIMEOUT = 1000;
    private static final long COMPLETION_TIMEOUT = 10000;

    private final RunConfiguration runConfig;
    private final Run run;
    private final Manager manager;

    private boolean completed = false;
    private final Object lock = new Object();

    static class TestExample {
        private final RunConfiguration config;

        public TestExample(RunConfiguration config) {
            this.config = config;
        }

    }

    public TestMultiGameRun(TestExample testSet) {
        this.runConfig = testSet.config;
        manager = Manager.getManager();
        run = manager.startRun(testSet.config);
        synchronized (lock) {
            run.addCompletionListener(run -> {
                completed = true;
                synchronized (lock) {
                    lock.notify();
                }
            });
        }
    }


    @Parameterized.Parameters
    public static Collection<TestExample> data() {
        Collection<TestExample> samples = new ArrayList<>();
        RunConfiguration config;

        config = new RunConfiguration();
        config.gameMode = GameState.GameMode.Tournament_Phase_1;
        config.mapName = "christmasMap";
        config.teamCount = 2;
        config.players = new ArrayList<>();
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        samples.add(new TestExample(config));

        config = new RunConfiguration();
        config.gameMode = GameState.GameMode.Tournament_Phase_1;
        config.mapName = "christmasMap";
        config.teamCount = 2;
        config.players = new ArrayList<>();
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        samples.add(new TestExample(config));

        config = new RunConfiguration();
        config.gameMode = GameState.GameMode.Tournament_Phase_1;
        config.mapName = "christmasMap";
        config.teamCount = 2;
        config.players = new ArrayList<>();
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        samples.add(new TestExample(config));

        config = new RunConfiguration();
        config.gameMode = GameState.GameMode.Tournament_Phase_1;
        config.mapName = "christmasMap";
        config.teamCount = 2;
        config.players = new ArrayList<>();
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        samples.add(new TestExample(config));

        config = new RunConfiguration();
        config.gameMode = GameState.GameMode.Tournament_Phase_1;
        config.mapName = "christmasMap";
        config.teamCount = 3;
        config.players = new ArrayList<>();
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        samples.add(new TestExample(config));

        config = new RunConfiguration();
        config.gameMode = GameState.GameMode.Tournament_Phase_1;
        config.mapName = "christmasMap";
        config.teamCount = 3;
        config.players = new ArrayList<>();
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        samples.add(new TestExample(config));

        config = new RunConfiguration();
        config.gameMode = GameState.GameMode.Tournament_Phase_1;
        config.mapName = "christmasMap";
        config.teamCount = 3;
        config.players = new ArrayList<>();
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        samples.add(new TestExample(config));

        config = new RunConfiguration();
        config.gameMode = GameState.GameMode.Tournament_Phase_1;
        config.mapName = "MangoMap";
        config.teamCount = 3;
        config.players = new ArrayList<>();
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        samples.add(new TestExample(config));

        config = new RunConfiguration();
        config.gameMode = GameState.GameMode.Tournament_Phase_1;
        config.mapName = "christmasMap";
        config.teamCount = 4;
        config.players = new ArrayList<>();
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        samples.add(new TestExample(config));

        config = new RunConfiguration();
        config.gameMode = GameState.GameMode.Tournament_Phase_1;
        config.mapName = "christmasMap";
        config.teamCount = 4;
        config.players = new ArrayList<>();
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        samples.add(new TestExample(config));


        config = new RunConfiguration();
        config.gameMode = GameState.GameMode.Tournament_Phase_1;
        config.mapName = "christmasMap";
        config.teamCount = 4;
        config.players = new ArrayList<>();
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        samples.add(new TestExample(config));


        config = new RunConfiguration();
        config.gameMode = GameState.GameMode.Tournament_Phase_1;
        config.mapName = "christmasMap";
        config.teamCount = 4;
        config.players = new ArrayList<>();
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        samples.add(new TestExample(config));


        config = new RunConfiguration();
        config.gameMode = GameState.GameMode.Tournament_Phase_1;
        config.mapName = "MangoMap";
        config.teamCount = 4;
        config.players = new ArrayList<>();
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        samples.add(new TestExample(config));


        config = new RunConfiguration();
        config.gameMode = GameState.GameMode.Tournament_Phase_1;
        config.mapName = "MangoMap";
        config.teamCount = 4;
        config.players = new ArrayList<>();
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        samples.add(new TestExample(config));


        config = new RunConfiguration();
        config.gameMode = GameState.GameMode.Tournament_Phase_1;
        config.mapName = "MangoMap";
        config.teamCount = 4;
        config.players = new ArrayList<>();
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        samples.add(new TestExample(config));

        return samples;
    }

    @Test
    public void testConfig() throws NoSuchFieldException, IllegalAccessException, InterruptedException {
        testStats();
        testCompletion();
    }

    public void testStats() {
        long expectedCount = binCoeff(run.getPlayers().size(), runConfig.teamCount);
        expectedCount *= factorial(runConfig.teamCount);
        Assert.assertEquals("Run contains the wrong manager instance", manager, run.manager);
        Assert.assertEquals("Number of games doesn't equal to the calculated theoretical amount", expectedCount, run.getGames().size());
        Assert.assertEquals("Player aren't equal to the list specified in config", run.getPlayers(), runConfig.players);
        Assert.assertEquals("Run implementation", run.getClass(), ParallelMultiGameRun.class);
        Assert.assertEquals("Wrong Game mode", run.gameMode, runConfig.gameMode);
    }


    public void testCompletion() throws NoSuchFieldException, IllegalAccessException, InterruptedException {
        long timeOut = COMPLETION_TIMEOUT + binCoeff(run.getPlayers().size(), runConfig.teamCount) * factorial(runConfig.teamCount) * GAME_COMPLETION_TIMEOUT;
        System.out.printf("Waiting %d ms for Completion.%n", timeOut);
        synchronized (lock) {
            lock.wait(timeOut);
        }
        Assert.assertTrue(String.format("The run was not concluded within the timeout of %d ms.\n" +
                "Var-Dump:%s", timeOut, this), completed);

        synchronized (this) {
            this.wait(10000);
        }

        Field activeGamesField
                = Manager.class.getDeclaredField("activeGames");
        Field scheduledGamesField
                = Manager.class.getDeclaredField("scheduledGames");
        Field pausedGamesField
                = Manager.class.getDeclaredField("pausedGames");

        activeGamesField.setAccessible(true);
        scheduledGamesField.setAccessible(true);
        pausedGamesField.setAccessible(true);

        ArrayList<Game> scheduledGames = (ArrayList<Game>) scheduledGamesField.get(manager);

        ArrayList<Game> activeGames = (ArrayList<Game>) activeGamesField.get(manager);

        ArrayList<Game> pausedGames = (ArrayList<Game>) pausedGamesField.get(manager);

        Assert.assertEquals("List of scheduled Games in Manager should be empty after run completed", 0, scheduledGames.size());

        Assert.assertEquals("List of active Games in Manager should be empty after run completed", 0, activeGames.size());

        Assert.assertEquals("List of paused Games in Manager should be empty after run completed", 0, pausedGames.size());
    }

    @Override
    public String toString() {
        return "TestMultiGameRun{" +
                "\nrunConfig=" + runConfig +
                "\n, run=" + run +
                "\n, manager=" + manager +
                "\n, completed=" + completed +
                '}';
    }

    private long binCoeff(int n, int k) {
        long res = 1;
        for (int i = 1; i <= k; i++) {
            res = res * (n + 1 - i) / i;
        }
        return res;
    }


    private long factorial(int n) {
        long res = 1;
        for (int i = 1; i <= n; i++) {
            res *= i;
        }
        return res;
    }

}
 */