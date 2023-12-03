package com.gatdsen.manager;

import bots.MalBot;
import com.gatdsen.manager.player.analyzer.BotClassAnalyzer;
import org.junit.Assert;
import org.junit.Test;

public class TestPolicy {

    @Test
    public void TestMalBot() {
        /*Simulation dummySimulation = new Simulation(GameState.GameMode.Normal, "map1", 2);

        LocalPlayerHandler playerHandler = new LocalPlayerHandler(MalBot.class);
        Future<?> future = playerHandler.init(dummySimulation.getState(), false, Manager.getSeed(), command -> {});
        try {
            future.get();
        } catch (InterruptedException|ExecutionException e) {
            Assert.fail(e.getMessage());
        }

        future = playerHandler.executeTurn(dummySimulation.getState(), command -> {});
        try {
            future.get();
        } catch (InterruptedException|ExecutionException e) {
            Assert.fail(e.getMessage());
        }*/

        String[] illegalImports = BotClassAnalyzer.getIllegalImports(MalBot.class);
        for (String illegalImport : illegalImports) {
            if (!contains(MalBot.ILLEGAL_IMPORTS, illegalImport)) {
                Assert.fail("MalBot imports " + illegalImport + " but it is unexpected.");
            }
        }
        for (String illegalImport : MalBot.ILLEGAL_IMPORTS) {
            if (!contains(illegalImports, illegalImport)) {
                Assert.fail("MalBot should import " + illegalImport + " but is was not detected.");
            }
        }
    }

    private static boolean contains(String[] array, String element) {
        for (String s : array) {
            if (s.equals(element)) {
                return true;
            }
        }
        return false;
    }
}
