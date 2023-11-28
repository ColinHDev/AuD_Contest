package com.example.networking.rmi;

import com.example.manager.command.Command;
import com.example.networking.data.CommunicatedInformation;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Die Schnittstelle für Javas Remote Method Invocation (RMI).
 * Über diese Schnittstelle können die Prozesse miteinander kommunizieren.
 * Sie wird von {@link ProcessCommunicatorImpl} implementiert.
 */
public interface ProcessCommunicator extends Remote {

    /**
     * Fügt eine {@link CommunicatedInformation} in die Warteschlange ein.
     * @param information Die einzufügende Information
     * @throws RemoteException Wird geworfen, wenn ein Fehler bei der Kommunikation auftritt
     */
    void queueInformation(CommunicatedInformation information) throws RemoteException;

    /**
     * Entfernt eine {@link CommunicatedInformation} aus der Warteschlange.
     * Diese Methode blockiert, bis eine Information verfügbar ist.
     * @return Die entfernte Information
     * @throws RemoteException Wird geworfen, wenn ein Fehler bei der Kommunikation auftritt
     */
    CommunicatedInformation dequeueInformation() throws RemoteException;

    /**
     * Fügt einen {@link Command} in die Warteschlange ein.
     * @param command Der einzufügende Befehl
     * @throws RemoteException Wird geworfen, wenn ein Fehler bei der Kommunikation auftritt
     */
    void queueCommand(Command command) throws RemoteException;

    /**
     * Entfernt einen {@link Command} aus der Warteschlange.
     * Diese Methode blockiert, bis ein Befehl verfügbar ist.
     * @return Der entfernte Befehl
     * @throws RemoteException Wird geworfen, wenn ein Fehler bei der Kommunikation auftritt
     */
    Command dequeueCommand() throws RemoteException;
}
