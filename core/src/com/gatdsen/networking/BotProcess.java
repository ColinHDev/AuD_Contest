package com.gatdsen.networking;

import com.gatdsen.manager.PlayerThread;
import com.gatdsen.manager.command.Command;
import com.gatdsen.manager.player.Player;
import com.gatdsen.networking.data.CommunicatedInformation;
import com.gatdsen.networking.data.GameInformation;
import com.gatdsen.networking.data.TurnInformation;
import com.gatdsen.networking.rmi.ProcessCommunicator;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.BlockingQueue;

/**
 * Diese Klasse repräsentiert den Prozess, auf welchem der Bot eines Spielers ausgeführt wird.
 */
public class BotProcess {

    private final Class<? extends Player> playerClass;
    private final String host;
    private final int port;
    private final String remoteReferenceName;

    private ProcessCommunicator communicator;
    private PlayerThread playerThread = null;

    public BotProcess(Class<? extends Player> playerClass, String host, int port, String remoteReferenceName) {
        this.playerClass = playerClass;
        this.host = host;
        this.port = port;
        this.remoteReferenceName = remoteReferenceName;
        Runtime.getRuntime().addShutdownHook(new Thread(this::dispose));
    }

    public void run() {
        Registry registry;
        try {
            registry = LocateRegistry.getRegistry(host, port);
        } catch (RemoteException e) {
            throw new RuntimeException(
                    "There was no Remote Object Registry to get at the given host \"" + (host == null ? "localhost" : host) + "\" and port \"" + port + "\".",
                    e
            );
        }
        try {
            communicator = (ProcessCommunicator) registry.lookup(remoteReferenceName);
        } catch (NotBoundException e) {
            throw new RuntimeException(
                    "There was no Remote Reference bound under the name \"" + remoteReferenceName + "\" at the Remote Object Registry at host \"" + (host == null ? "localhost" : host) + "\" and port \"" + port + "\".",
                    e
            );
        } catch (RemoteException e) {
            throw new RuntimeException(
                    "The connection with the Remote Object Registry at host \"" + (host == null ? "localhost" : host) + "\" and port \"" + port + "\" failed.",
                    e
            );
        }

        while (true) {
            CommunicatedInformation information;
            try {
                information = communicator.dequeueInformation();
            } catch (RemoteException e) {
                throw new RuntimeException("Could not dequeue information from the parent process.");
            }
            if (information instanceof GameInformation) {
                playerThread = new PlayerThread(playerClass, ((GameInformation) information).isDebug());
                BlockingQueue<Command> commands = playerThread.init(((GameInformation) information).state(), ((GameInformation) information).seed());
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

    public void dispose() {
        if (playerThread != null) {
            playerThread.dispose();
        }
        System.out.println("BotProcess of player \"" + playerClass.getName() + "\" on process with pid " + ProcessHandle.current().pid() + " is shutting down");
    }
}
