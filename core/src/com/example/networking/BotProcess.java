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

    private final Class<? extends Player> playerClass;
    private final String host;
    private final int port;
    private final String remoteReferenceName;

    private ProcessCommunicator communicator;

    public BotProcess(CompletionHandler<BotProcess> completionListener, Class<? extends Player> playerClass, String host, int port, String remoteReferenceName) {
        this.completionListener = completionListener;
        this.playerClass = playerClass;
        this.host = host;
        this.port = port;
        this.remoteReferenceName = remoteReferenceName;
    }

    public void start() {
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

    protected void complete() {
        completionListener.onComplete(this);
        completionListener = null;
    }

    public void dispose() {
        completionListener = null;
    }
}
