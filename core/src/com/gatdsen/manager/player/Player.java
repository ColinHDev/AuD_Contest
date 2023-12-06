package com.gatdsen.manager.player;

import com.gatdsen.manager.Controller;
import com.gatdsen.manager.StaticGameState;
import com.gatdsen.manager.player.data.PlayerInformation;
import com.gatdsen.simulation.GameState;

public abstract class Player {

    public enum PlayerType {
        Human,
        AI
    }

    /**
     * @return Der (Anzeige-) Name des Spielers
     */
    public abstract String getName();

    /**
     * Wird vor Beginn des Spiels aufgerufen. Besitzt eine sehr hohe maximale Berechnungszeit von TODO ms.
     * Diese Methode kann daher verwendet werden, um Variablen zu initialisieren und einmalig, sehr rechenaufwändige
     * Operationen durchzuführen.
     * @param state Der {@link GameState Spielzustand des Spielers} zu Beginn des Spiels
     */
    public abstract void init(StaticGameState state);

    /**
     * Wird aufgerufen, wenn der Spieler einen Zug für einen seiner Charaktere durchführen soll.
     * Der {@link GameState Spielzustand} state reflektiert dabei den Zug des Spielers ohne Verzögerung.
     * Der Controller ermöglicht die Steuerung des Charakters, welcher am Zug ist.
     * Die übergebene Controller-Instanz deaktiviert sich nach Ende des Zuges permanent.
     * @param state Der {@link GameState Spielzustand} während des Zuges
     * @param controller Der {@link Controller Controller}, zum Charakter gehört, welcher am Zug ist
     */
    public abstract void executeTurn(StaticGameState state, Controller controller);

    /**
     * Wird für interne Zwecke verwendet und besitzt keine Relevanz für die Bot-Entwicklung.
     * @return Die Art der Implementierung des Spielers
     */
    public abstract PlayerType getType();

    /**
     * Wird für interne Zwecke verwendet und besitzt keine Relevanz für die Bot-Entwicklung.
     * @return Die Informationen über den Spieler
     */
    public PlayerInformation getPlayerInformation() {
        return new PlayerInformation(getType(), getName());
    }
}
