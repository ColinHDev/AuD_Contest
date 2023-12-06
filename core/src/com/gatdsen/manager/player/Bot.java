package com.gatdsen.manager.player;

import com.gatdsen.manager.player.data.BotInformation;

import java.util.Random;

/**
 * Die Basisklasse für alle Bot-Implementationen.
 * Erbt von dieser Klasse, wenn ihr einen Bot implementieren wollt.
 */
public abstract class Bot extends Player {

    /**
     * Ein Zufallszahlengenerator, der verwendet werden sollte, wenn man in seinem Bot Zufallszahlen verwenden möchte.
     * <p>
     * Der Seed dieses Generators berechnet sich pro Spiel aus dem Quelltext der Klassen der teilnehmenden Spieler.
     * Dadurch gibt dieser Zufallszahlengenerator immer die gleichen Zufallszahlen für die gleichen Spieleinstellungen
     * zurück.
     */
    protected Random random;

    /**
     * @return Der vollständige Name des Botautors im Format: "Vorname(n) Nachname"
     */
    public abstract String getStudentName();

    /**
     * @return Eure Matrikelnummer
     */
    public abstract int getMatrikel();

    /**
     * Wird für interne Zwecke verwendet und besitzt keine Relevanz für die Bot-Entwicklung.
     * @return Die Art der Implementierung des Spielers, hier ein Bot
     */
    @Override
    public final PlayerType getType() {
        return PlayerType.AI;
    }

    /**
     * Wird für interne Zwecke verwendet und besitzt keine Relevanz für die Bot-Entwicklung.
     * @param seed Der Seed, der für das aktuelle Spiel gilt und als Basis für den Zufallszahlengenerator des Bots ver-
     *             wendet werden kann.
     */
    public final void setRandomSeed(long seed){
        if (random == null) {
            random = new Random(seed);
        }
    }

    /**
     * Wird für interne Zwecke verwendet und besitzt keine Relevanz für die Bot-Entwicklung.
     * @return Die Informationen über den Bot
     */
    public final BotInformation getPlayerInformation() {
        return new BotInformation(getType(), getName(), getStudentName(), getMatrikel());
    }
}
