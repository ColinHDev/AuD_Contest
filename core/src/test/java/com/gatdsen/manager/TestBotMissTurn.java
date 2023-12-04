package com.gatdsen.manager;

import com.gatdsen.manager.player.Bot;
import com.gatdsen.simulation.GameState;
import com.gatdsen.simulation.Simulation;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TestBotMissTurn {

    private final Simulation dummySimulation = new Simulation(GameState.GameMode.Normal, "map1", 2);

    @Test
    public void testMissTurnThroughInitException() {
        System.out.println("testMissTurnThroughInitException(): start");
        testBot(MissTurnThroughInitException.class);
        Assert.assertEquals("Bot threw an exception in its init() method, got its executeTurn() method called twice and due to a missed turn, should only executed 1 turn, instead of " + MissTurnThroughInitException.executedTurns, 1, MissTurnThroughInitException.executedTurns);
        System.out.println("testMissTurnThroughInitException(): end");
    }

    @Test
    public void testMissTurnThroughExecuteTurnException() {
        System.out.println("testMissTurnThroughExecuteTurnException(): start");
        testBot(MissTurnThroughExecuteTurnException.class);
        Assert.assertEquals("Bot threw an exception in its executeTurn() method, got its executeTurn() method called twice and due to a missed turn, should only executed 1 turn, instead of " + MissTurnThroughExecuteTurnException.executedTurns, 1, MissTurnThroughExecuteTurnException.executedTurns);
        System.out.println("testMissTurnThroughExecuteTurnException(): end");
    }

    //@Test
    public void testMissTurnThroughInitTimeout() {
        System.out.println("testMissTurnThroughInitTimeout(): start");
        testBot(MissTurnThroughInitTimeout.class);
        Assert.assertEquals("Bot timeouted in its init() method, got its executeTurn() method called twice and due to a missed turn, should only executed 1 turn, instead of " + MissTurnThroughInitTimeout.executedTurns, 1, MissTurnThroughInitTimeout.executedTurns);
        System.out.println("testMissTurnThroughInitTimeout(): end");
    }

    //@Test
    public void testMissTurnThroughExecuteTurnTimeout() {
        System.out.println("testMissTurnThroughExecuteTurnTimeout(): start");
        testBot(MissTurnThroughExecuteTurnTimeout.class);
        Assert.assertEquals("Bot timeouted in its executeTurn() method, got its executeTurn() method called twice and due to a missed turn, should only executed 1 turn, instead of " + MissTurnThroughExecuteTurnTimeout.executedTurns, 1, MissTurnThroughExecuteTurnTimeout.executedTurns);
        System.out.println("testMissTurnThroughExecuteTurnTimeout(): end");
    }

    private void testBot(Class<? extends Bot> botClass) {
        LocalPlayerHandler playerHandler = new LocalPlayerHandler(botClass, null);
        System.out.println("create()");
        awaitFuture(playerHandler.create(command -> command.run(playerHandler)));
        System.out.println("init()");
        awaitFuture(playerHandler.init(dummySimulation.getState(), false, 1337, command -> command.run(playerHandler)));
        System.out.println("executeTurn1()");
        awaitFuture(playerHandler.executeTurn(dummySimulation.getState(), command -> command.run(playerHandler)));
        System.out.println("executeTurn2()");
        awaitFuture(playerHandler.executeTurn(dummySimulation.getState(), command -> command.run(playerHandler)));
    }

    private void awaitFuture(Future<?> future) {
        try {
            future.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException e) {
            Assert.fail("While waiting on future: " + e);
        } catch (TimeoutException e) {
            Assert.fail("Waited for 10 seconds on future: " + e);
        }
    }

    private abstract static class TestBot extends Bot {
        @Override
        public String getStudentName() {
            return "Colin";
        }

        @Override
        public int getMatrikel() {
            return -1; //Heh, you thought
        }

        @Override
        public String getName() {
            return getClass().getSimpleName();
        }
    }

    public static class MissTurnThroughInitException extends TestBot {

        public static int executedTurns = 0;

        @Override
        public void init(StaticGameState state) {
            throw new RuntimeException("Bot throws an exception in init()");
        }

        @Override
        public void executeTurn(StaticGameState state, Controller controller) {
            executedTurns++;
            System.out.println("executeTurn from " + getName() + " called with executedTurns = " + executedTurns);
        }
    }

    public static class MissTurnThroughExecuteTurnException extends TestBot {

        public static int executedTurns = 0;

        @Override
        public void init(StaticGameState state) {
        }

        @Override
        public void executeTurn(StaticGameState state, Controller controller) {
            executedTurns++;
            System.out.println("executeTurn from " + getName() + " called with executedTurns = " + executedTurns);
            if (executedTurns == 1) {
                throw new RuntimeException("Bot throws an exception in executeTurn()");
            }
        }
    }

    public static class MissTurnThroughInitTimeout extends TestBot {

        public static int executedTurns = 0;

        @Override
        public void init(StaticGameState state) {
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < PlayerThread.AI_EXECUTE_INIT_TIMEOUT * 1.5);
        }

        @Override
        public void executeTurn(StaticGameState state, Controller controller) {
            executedTurns++;
        }
    }

    public static class MissTurnThroughExecuteTurnTimeout extends TestBot {

        public static int executedTurns = 0;

        @Override
        public void init(StaticGameState state) {
        }

        @Override
        public void executeTurn(StaticGameState state, Controller controller) {
            executedTurns++;
            if (executedTurns == 1) {
                long startTime = System.currentTimeMillis();
                while (System.currentTimeMillis() - startTime < PlayerThread.AI_EXECUTE_TURN_TIMEOUT * 1.5);
            }
        }
    }
}
