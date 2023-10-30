package com.example.networking.rmi;

import com.example.manager.command.Command;

import java.rmi.RemoteException;

/**
 * Die Schnittstelle für Javas Remote Method Invocation (RMI).
 * Über diese Schnittstelle können die Prozesse miteinander kommunizieren.
 * Sie wird von {@link ProcessCommunicatorImpl} implementiert.
 */
public interface ProcessCommunicator {

    /**
     * Fügt einen {@link Command} in die Warteschlange ein.
     * @param command Der einzufügende Befehl
     * @throws RemoteException Wird geworfen, wenn ein Fehler bei der Kommunikation auftritt
     */
    void queueCommand(Command command) throws RemoteException;

    /**
     * Entfernt einen {@link Command} aus der Warteschlange.
     * @return Der entfernte Befehl
     * @throws RemoteException Wird geworfen, wenn ein Fehler bei der Kommunikation auftritt
     */
    Command dequeueCommand() throws RemoteException;
}
