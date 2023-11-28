package com.gatdsen.networking.rmi;

import com.gatdsen.manager.command.Command;
import com.gatdsen.networking.data.CommunicatedInformation;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Die Implementierung von {@link ProcessCommunicator}, über welche die Prozesse miteinander kommunizieren können.
 */
public class ProcessCommunicatorImpl implements ProcessCommunicator {

    private final BlockingQueue<CommunicatedInformation> informationQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<Command> commandQueue = new LinkedBlockingQueue<>();

    @Override
    public void queueInformation(CommunicatedInformation information) {
        informationQueue.add(information);
    }

    @Override
    public CommunicatedInformation dequeueInformation() {
        try {
            return informationQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void queueCommand(Command command) {
        commandQueue.add(command);
    }

    @Override
    public Command dequeueCommand() {
        try {
            return commandQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
