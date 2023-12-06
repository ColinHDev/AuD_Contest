package com.gatdsen.manager.player;

import com.gatdsen.manager.Controller;
import com.gatdsen.manager.StaticGameState;
import com.gatdsen.manager.player.data.PlayerInformation;
import com.gatdsen.simulation.GameState;

/**
 * Die Basisklasse für alle Implementierungen von Spielern.
 * Ein Spieler repräsentiert einen Teilnehmer des Spiels.
 */
public abstract class Player {

    /**
     * @return Der (Anzeige-) Name des Spielers
     */
    public abstract String getName();

    /**
     * Wird vor Beginn des Spiels aufgerufen.
     * Diese Methode kann daher verwendet werden, um Variablen zu initialisieren und einmalig, sehr rechenaufwändige
     * Operationen durchzuführen.
     * @param state Der {@link StaticGameState Spielzustand} zu Beginn des Spiels
     */
    public abstract void init(StaticGameState state);

    /**
     * Wird aufgerufen, wenn der Spieler seinen Zug für die aktuelle Runde durchführen soll.
     * <p>
     * Der {@link StaticGameState Spielzustand} reflektiert dabei den Zustand des Spiels vor dem Zug des Spielers. Der
     * Zustand ist statisch, das heißt bei Aufrufen des {@link Controller Controllers} werden diese Änderungen nicht im
     * Spielzustand in dieser Runde reflektiert, sondern erst in der nächsten Runde, wenn man den neuen Spielzustand erhält.
     * <p>
     * Der Controller ermöglicht dir die Steuerung, um Aktionen, wie bspw. das Platzieren von Türmen, auszuführen. Die
     * übergebene Controller-Instanz deaktiviert sich nach Ende des Zuges permanent.
     * @param state Der {@link StaticGameState Spielzustand} vor der Ausführung des aktuellen Zuges
     * @param controller Der {@link Controller Controller}, um Aktionen auszuführen
     */
    public abstract void executeTurn(StaticGameState state, Controller controller);

    public enum PlayerType {
        Human,
        AI
    }

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
