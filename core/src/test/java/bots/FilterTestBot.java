package bots;

import com.gatdsen.manager.player.Bot;
import com.gatdsen.manager.Controller;
import com.gatdsen.simulation.GameState;

import java.util.concurrent.*;

/**
 * FilterTestBot is part of a test that validates prohibition of security-relevant actions (like Threading),
 * which are NOT caught by the SecurityPolicy and are instead filtered via import names during loading.
 */
public class FilterTestBot extends Bot {
    @Override
    public String getStudentName() {
        return "Cornelius Zenker";
    }

    @Override
    public int getMatrikel() {
        return -1; //Heh, you thought
    }

    @Override
    public String getName() {
        return "Hacker Gadse";
    }

    @Override
    public void init(GameState state) {
        new Thread();
    }

    @Override
    public void executeTurn(GameState state, Controller controller) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
    }
}
