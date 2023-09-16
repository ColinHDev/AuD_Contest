package bots;

import com.example.manager.Bot;
import com.example.manager.Controller;
import com.example.simulation.GameState;

import java.util.concurrent.*;

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
    protected void init(GameState state) {
        new Thread();
    }

    @Override
    protected void executeTurn(GameState state, Controller controller) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
    }
}
