package com.example.networking;

import com.example.manager.player.Player;
import com.example.manager.player.PlayerHandler;
import com.example.networking.data.GameInformation;
import com.example.networking.data.TurnInformation;
import com.example.networking.rmi.ProcessCommunicator;
import com.example.networking.rmi.ProcessCommunicatorImpl;
import com.example.simulation.GameState;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public final class ProcessPlayerHandler implements PlayerHandler {

    public static final int registryPort = 1099;
    public static final String stubNamePrefix = "ProcessCommunicator_";

    private final Class<? extends Player> playerClass;
    private Process process;

    private Registry registry;
    private ProcessCommunicator communicator;


    public ProcessPlayerHandler(Class<? extends Player> playerClass) {
        this.playerClass = playerClass;
    }

    @Override
    public void create(GameState gameState, boolean isDebug) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.inheritIO();

        File currentJar;
        try {
            currentJar = new File(ProcessPlayerHandler.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        builder.command("java", "-cp", currentJar.getPath(), BotProcessLauncher.class.getName());

        try {
            process = builder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
            registry.rebind(stubNamePrefix + process.pid(), communicator);
            communicator.queueInformation(new GameInformation(gameState, isDebug));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(GameState gameState) {
        try {
            communicator.queueInformation(new TurnInformation(gameState));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dispose() {
        try {
            registry.unbind(stubNamePrefix + process.pid());
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }
}
