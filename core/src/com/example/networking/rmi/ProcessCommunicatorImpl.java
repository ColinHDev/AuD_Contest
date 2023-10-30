package com.example.networking.rmi;

import com.example.manager.command.Command;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Die Implementierung von {@link ProcessCommunicator}, über welche die Prozesse miteinander kommunizieren können.
 */
public class ProcessCommunicatorImpl implements ProcessCommunicator {

    BlockingQueue<Command> queuedCommands = new LinkedBlockingQueue<>();

    public void queueCommand(Command command) {
        queuedCommands.add(command);
    }

    public Command dequeueCommand() {
        return queuedCommands.poll();
    }
}
