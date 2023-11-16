package com.example.simulation;
import org.junit.Before;
import org.junit.Test;

public class TestStaticGameState {

    private StaticGameState state;
    private Simulation sim;

    @Before
    public void init() {
        sim = new Simulation(StaticGameState.GameMode.Normal, "map1", 2);
        state = sim.getState();
    }

    /**
     * schaue ob etwas ins Board geladen werden kann
     */
    @Test
    public void testIfBoardFilled() {
        //ToDo: write assertions
    }

}
