package com.example.manager;

import bots.MalBot;
import com.example.manager.IdleBot;
import com.example.manager.Manager;
import com.example.manager.Run;
import com.example.manager.RunConfiguration;
import com.example.simulation.GameState;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class TestPolicy {
    private final long COMPLETION_TIMEOUT = 10000;
    final Object lock = new Object();

    @Test
    public void TestMalBot() {
        RunConfiguration config = new RunConfiguration();
        config.gameMode = GameState.GameMode.Normal;
        config.mapName = "MangoMap";
        config.teamCount = 2;
        config.players = new ArrayList<>();
        config.players.add(MalBot.class);
        config.players.add(IdleBot.class);
        Manager manager = Manager.getManager();
        Run run = manager.startRun(config);
        synchronized (lock) {
            run.addCompletionListener(r -> {
                synchronized (lock) {
                    lock.notify();
                }
            });
            try {
                lock.wait(COMPLETION_TIMEOUT);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Assert.assertTrue(String.format("The bot should not be able to use any system resources or reflections.\n" +
                "failedExperiments:%s\n" +
                "Var-Dump:%s", MalBot.failedExperiments, manager), MalBot.failedExperiments.isEmpty());
    }
}
