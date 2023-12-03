package com.gatdsen.manager;

import com.gatdsen.manager.player.Bot;
import com.gatdsen.simulation.GameState;
import com.gatdsen.simulation.Simulation;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.gatdsen.manager.PlayerThread.AI_EXECUTE_INIT_TIMEOUT;
import static com.gatdsen.manager.PlayerThread.AI_EXECUTE_TURN_TIMEOUT;

public class TestBotMissTurn {

    private final Simulation dummySimulation = new Simulation(GameState.GameMode.Normal, "map1", 2);

    @Test
    public void testMissTurn() {
        testBot(MissTurnThroughInitException.class);
        Assert.assertEquals("Bot threw an exception in its init() method, got its executeTurn() method called twice and due to a missed turn, should only executed 1 turn, instead of " + MissTurnThroughInitException.executedTurns, 1, MissTurnThroughInitException.executedTurns);

        testBot(MissTurnThroughExecuteTurnException.class);
        Assert.assertEquals("Bot threw an exception in its executeTurn() method, got its executeTurn() method called twice and due to a missed turn, should only executed 1 turn, instead of " + MissTurnThroughExecuteTurnException.executedTurns, 1, MissTurnThroughExecuteTurnException.executedTurns);

        testBot(MissTurnThroughInitTimeout.class);
        Assert.assertEquals("Bot timeouted in its init() method, got its executeTurn() method called twice and due to a missed turn, should only executed 1 turn, instead of " + MissTurnThroughInitTimeout.executedTurns, 1, MissTurnThroughInitTimeout.executedTurns);

        testBot(MissTurnThroughExecuteTurnTimeout.class);
        Assert.assertEquals("Bot timeouted in its executeTurn() method, got its executeTurn() method called twice and due to a missed turn, should only executed 1 turn, instead of " + MissTurnThroughExecuteTurnTimeout.executedTurns, 1, MissTurnThroughExecuteTurnTimeout.executedTurns);
    }

    private void testBot(Class<? extends Bot> botClass) {
        LocalPlayerHandler playerHandler = new LocalPlayerHandler(botClass, null);
        awaitFuture(playerHandler.create(command -> command.run(playerHandler)));
        awaitFuture(playerHandler.init(dummySimulation.getState(), false, 1337, command -> command.run(playerHandler)));
        awaitFuture(playerHandler.executeTurn(dummySimulation.getState(), command -> command.run(playerHandler)));
        awaitFuture(playerHandler.executeTurn(dummySimulation.getState(), command -> command.run(playerHandler)));
    }

    private void awaitFuture(Future<?> future) {
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            Assert.fail(e.getMessage());
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
            return "Test Bot";
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
            while (System.currentTimeMillis() - startTime < AI_EXECUTE_INIT_TIMEOUT * 1.5);
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
                while (System.currentTimeMillis() - startTime < AI_EXECUTE_TURN_TIMEOUT * 1.5);
            }
        }
    }
}
