package com.gatdsen.manager.player;

import com.gatdsen.manager.Controller;
import com.gatdsen.simulation.GameState;

import java.util.Random;

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
     * Wird vor Beginn des Spiels aufgerufen. Besitzt eine sehr hohe maximale Berechnungszeit von TBA ms.
     * Diese Funktion kann daher verwendet werden, um Variablen zu initialisieren und
     * einmalig, sehr rechenaufwändige Operationen durchzuführen.
     * @param state Der {@link GameState Spielzustand} zu Beginn des Spiels
     */
    public abstract void init(GameState state);

    /**
     * Wird aufgerufen, wenn der Spieler einen Zug für einen seiner Charaktere durchführen soll.
     * Der {@link GameState Spielzustand} state reflektiert dabei den Zug des Spielers ohne Verzögerung.
     * Der Controller ermöglicht die Steuerung des Charakters, welcher am Zug ist.
     * Die übergebene Controller-Instanz deaktiviert sich nach Ende des Zuges permanent.
     * @param state Der {@link GameState Spielzustand} während des Zuges
     * @param controller Der {@link Controller Controller}, zum Charakter gehört, welcher am Zug ist
     */
    public abstract void executeTurn(GameState state, Controller controller);

    /**
     * Wird für interne Zwecke verwendet und besitzt keine Relevanz für die Bot-Entwicklung.
     * @return What kind of implementation the Player is
     */
    public abstract PlayerType getType();

    /**
     * @param characterIndex Index des Charakters
     * @return der Name des Skins, der der angegebene Charakter im UI modus haben soll
     */
    public String getSkin(int characterIndex){
        switch (new Random().nextInt(4)) {
            case 1:
                return "orangeCatSkin";

            case 2:
                return "yinYangSkin";

            case 3:
                return "mioSkin";

            default:
                return "coolCatSkin";
        }
    }

}
