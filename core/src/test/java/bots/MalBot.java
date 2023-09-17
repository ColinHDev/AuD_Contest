package bots;

import com.example.manager.Bot;
import com.example.manager.Controller;
import com.example.manager.Manager;
import com.example.simulation.GameState;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

/**
 * MalBot is Part of a test that validates the SecurityPolicy:
 * <p>
 * MalBot invokes multiple security-relevant actions. The test only completes successfully,
 * if MalBot receives a SecurityException for every attempt of breaking the Policy.
 */

public class MalBot extends Bot {

    private boolean first = true;

    public static ArrayList<String> failedExperiments = new ArrayList<>();


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
        runExperiment(false, state);
    }

    @Override
    protected void executeTurn(GameState state, Controller controller) {
        if (first) {
            first = false;
            runExperiment(true, state);
        }
    }

    private void runExperiment(boolean inTurn, GameState state) {
        boolean caught = false;

        try {
            Object instance = Manager.class.newInstance();
        } catch (SecurityException | IllegalAccessException e) {
            caught = true;
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        if (!caught) failedExperiments.add(failureMessage(1, inTurn, state));


        caught = false;
        try {
            Field privateField = Manager.class.getDeclaredField("threadPoolExecutor");
            privateField.setAccessible(true);

        } catch (SecurityException e) {
            caught = true;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        if (!caught) failedExperiments.add(failureMessage(2, inTurn, state));


        caught = false;
        try {
            File file = new File("");
            file.exists();

        } catch (SecurityException e) {
            caught = true;
        }
        if (!caught) failedExperiments.add(failureMessage(3, inTurn, state));

        caught = false;
        Socket socket;
        try {
            socket = new Socket();
            socket.bind(new InetSocketAddress(InetAddress.getByName("localhost"), 1085));
            socket.close();
        } catch (SecurityException e) {
            caught = true;
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        if (!caught) failedExperiments.add(failureMessage(4, inTurn, state));

        caught = false;
        try {
            Manager.getManager();
        } catch (SecurityException e) {
            caught = true;
        }
        if (!caught) failedExperiments.add(failureMessage(5, inTurn, state));

    }

    private String failureMessage(int id, boolean inTurn, GameState state) {
        return "Failure{" +
                "id=" + id +
                "inTurn=" + inTurn +
                ", state=" + state +
                '}';
    }
}
