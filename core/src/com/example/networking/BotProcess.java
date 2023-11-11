package com.example.networking;

import com.example.manager.CompletionHandler;
import com.example.manager.player.Player;
import com.example.networking.rmi.ProcessCommunicator;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

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
    }

    protected void complete() {
        completionListener.onComplete(this);
        completionListener = null;
    }

    public void dispose() {
        completionListener = null;
    }
}
