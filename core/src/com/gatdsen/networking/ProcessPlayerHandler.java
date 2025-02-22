package com.gatdsen.networking;

import com.gatdsen.manager.command.Command;
import com.gatdsen.manager.command.CommandHandler;
import com.gatdsen.manager.concurrent.ThreadExecutor;
import com.gatdsen.manager.player.Player;
import com.gatdsen.manager.player.PlayerHandler;
import com.gatdsen.networking.data.CreatePlayerInformation;
import com.gatdsen.networking.data.GameInformation;
import com.gatdsen.networking.data.TurnInformation;
import com.gatdsen.networking.rmi.ProcessCommunicator;
import com.gatdsen.networking.rmi.ProcessCommunicatorImpl;
import com.gatdsen.simulation.GameState;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Future;

public final class ProcessPlayerHandler extends PlayerHandler {

    public static final int registryPort = 1099;
    public static final String stubNamePrefix = "ProcessCommunicator_";

    private final ThreadExecutor executor = new ThreadExecutor();

    private final String remoteReferenceName;

    private Registry registry;
    private ProcessCommunicator communicator;
    private Process process;

    public ProcessPlayerHandler(Class<? extends Player> playerClass, int gameId, int playerIndex) {
        super(playerClass, playerIndex);
        remoteReferenceName = stubNamePrefix + gameId + "_" + playerIndex;
        createRegistry();
        createProcess();
    }

    private void createRegistry() {
        try {
            // createRegistry() wirft eine RemoteException, wenn an dem Port bereits ein Registry-Objekt existiert
            registry = LocateRegistry.createRegistry(registryPort);
        } catch (RemoteException e) {
            // In diesem Fall wird die bereits existierende Registry verwendet
            try {
                registry = LocateRegistry.getRegistry(registryPort);
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        }

        // Lokale Instanz des ProcessCommunicator-Objekts
        ProcessCommunicator localCommunicator = new ProcessCommunicatorImpl();
        try {
            // Exportieren des Objekts, damit es von anderen Prozessen verwendet werden kann
            communicator = (ProcessCommunicator) UnicastRemoteObject.exportObject(localCommunicator, 0);
            registry.rebind(remoteReferenceName, communicator);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void createProcess() {
        ProcessBuilder builder = new ProcessBuilder();
        builder.inheritIO();
        File currentJar;
        try {
            currentJar = new File(ProcessPlayerHandler.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        builder.command(
                // Starten der JVM in der main() vom BotProcessLauncher
                "java", "-cp", currentJar.getPath(), BotProcessLauncher.class.getName(),
                // Angabe der Klasse des Spielers
                "-p", playerClass.getSimpleName(),
                // Angabe des Ports der Remote Object Registry
                "-port", String.valueOf(registryPort),
                // Angabe des Namens, unter dem die Remote Reference im Remote Object Registry gebunden ist
                "-reference", remoteReferenceName
        );
        try {
            process = builder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Future<?> create(CommandHandler commandHandler) {
        try {
            communicator.queueInformation(new CreatePlayerInformation());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        return executor.execute(() -> {
            Command command;
            do {
                try {
                    command = communicator.dequeueCommand();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                commandHandler.handleCommand(command);
            } while (!command.endsTurn());
        });
    }

    @Override
    public Future<?> init(GameState gameState, boolean isDebug, long seed, CommandHandler commandHandler) {
        try {
            communicator.queueInformation(new GameInformation(gameState, isDebug, seed, playerIndex));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        return executor.execute(() -> {
            Command command;
            do {
                try {
                    command = communicator.dequeueCommand();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                commandHandler.handleCommand(command);
            } while (!command.endsTurn());
        });
    }

    @Override
    protected Future<?> onExecuteTurn(GameState gameState, CommandHandler commandHandler) {
        try {
            communicator.queueInformation(new TurnInformation(gameState));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        return executor.execute(() -> {
            Command command;
            do {
                try {
                    command = communicator.dequeueCommand();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                commandHandler.handleCommand(command);
            } while (!command.endsTurn());
        });
    }

    @Override
    public void dispose() {
        executor.interrupt();
        try {
            registry.unbind(remoteReferenceName);
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
        process.destroy();
    }
}
