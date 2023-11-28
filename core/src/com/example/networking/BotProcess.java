package com.example.networking;

import com.example.manager.CompletionHandler;
import com.example.manager.PlayerThread;
import com.example.manager.command.Command;
import com.example.manager.player.Player;
import com.example.networking.data.CommunicatedInformation;
import com.example.networking.data.GameInformation;
import com.example.networking.data.TurnInformation;
import com.example.networking.rmi.ProcessCommunicator;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.BlockingQueue;

/**
 * Diese Klasse repräsentiert den Prozess, auf welchem der Bot eines Spielers ausgeführt wird.
 */
public class BotProcess {

    private CompletionHandler<BotProcess> completionListener;
    private Class<? extends Player> playerClass;

    private ProcessCommunicator communicator;

    public BotProcess(CompletionHandler<BotProcess> completionListener, Class<? extends Player> playerClass) {
        this.completionListener = completionListener;
        this.playerClass = playerClass;
    }

    public void start() {
        Registry registry = null;
        for (int attempts = 0; registry == null && attempts < 5; attempts++) {
            try {
                registry = LocateRegistry.getRegistry(ProcessPlayerHandler.registryPort);
                communicator = (ProcessCommunicator) registry.lookup(ProcessPlayerHandler.stubNamePrefix + ProcessHandle.current().pid());
            } catch (RemoteException | NotBoundException e) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }
            }
        }
        if (registry == null) {
            throw new RuntimeException("Could not connect to Parent Process of the main game through.");
        }

        PlayerThread playerThread = null;
        while (true) {
            CommunicatedInformation information;
            try {
                information = communicator.dequeueInformation();
            } catch (RemoteException e) {
                throw new RuntimeException("Could not dequeue information from the parent process.");
            }
            if (information instanceof GameInformation) {
                playerThread = new PlayerThread(playerClass, ((GameInformation) information).isDebug());
                BlockingQueue<Command> commands = playerThread.init(((GameInformation) information).state());
                Command command;
                do {
                    try {
                        command = commands.take();
                        communicator.queueCommand(command);
                    } catch (InterruptedException|RemoteException e) {
                        throw new RuntimeException(e);
                    }
                } while (!command.endsTurn());
            } else if (information instanceof TurnInformation) {
                if (playerThread == null) {
                    throw new RuntimeException("Received TurnInformation before GameInformation.");
                }
                BlockingQueue<Command> commands = playerThread.executeTurn(((TurnInformation) information).state());
                Command command;
                do {
                    try {
                        command = commands.take();
                        communicator.queueCommand(command);
                    } catch (InterruptedException | RemoteException e) {
                        throw new RuntimeException(e);
                    }
                } while (!command.endsTurn());
            }
        }
    }

    protected void complete() {
        completionListener.onComplete(this);
        completionListener = null;
    }

    public void dispose() {
        completionListener = null;
    }
}
