package bots;

import com.gatdsen.manager.Controller;
import com.gatdsen.manager.Manager;
import com.gatdsen.manager.StaticGameState;
import com.gatdsen.manager.player.Bot;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * MalBot is Part of a test that validates the SecurityPolicy:
 * <p>
 * MalBot invokes multiple security-relevant actions. The test only completes successfully,
 * if MalBot receives a SecurityException for every attempt of breaking the Policy.
 */

public class MalBot extends Bot {

    private boolean first = true;

    public static final String[] ILLEGAL_IMPORTS = {
            Manager.class.getName(),
            "java.lang.reflect",
            "java.net",
    };


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
    public void init(StaticGameState state) {
        runExperiment();
    }

    @Override
    public void executeTurn(StaticGameState state, Controller controller) {
        if (first) {
            first = false;
            runExperiment();
        }
    }

    private void runExperiment() {
        try {
            Object instance = Manager.class.getDeclaredConstructors()[0].newInstance();
        } catch (SecurityException | IllegalAccessException ignored) {
        } catch (InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }

        try {
            Field privateField = Manager.class.getDeclaredField("threadPoolExecutor");
            privateField.setAccessible(true);

        } catch (SecurityException ignored) {
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        try {
            File file = new File("");
            file.exists();

        } catch (SecurityException ignored) {
        }

        Socket socket;
        try {
            socket = new Socket();
            socket.bind(new InetSocketAddress(InetAddress.getByName("localhost"), 1085));
            socket.close();
        } catch (SecurityException ignored) {
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        try {
            Object instance = Manager.getManager();
        } catch (SecurityException e) {
        }
    }
}
