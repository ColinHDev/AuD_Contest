package com.gatdsen.manager.player;

import com.gatdsen.manager.player.data.BotInformation;

import java.util.Random;

/**
 * Superklasse für alle Bot-Implementationen.
 * Erbt von dieser Klasse, wenn ihr einen Bot implementieren wollt.
 */
public abstract class Bot extends Player{

    /**
     * @return Name des Bot-Authors (Euer vollständiger Name)
     */
    public abstract String getStudentName();

    /**
     * @return Eure Matrikel-Nummer
     */
    public abstract int getMatrikel();

    @Override
    public final PlayerType getType() {
        return PlayerType.AI;
    }

    protected Random rnd;

    public final void setRnd(long seed){
        if (rnd == null) rnd = new Random(seed);
    }

    /**
     * Wird für interne Zwecke verwendet und besitzt keine Relevanz für die Bot-Entwicklung.
     * @return Die Informationen über den Bot
     */
    public final BotInformation getPlayerInformation() {
        return new BotInformation(getType(), getName(), getStudentName(), getMatrikel());
    }
}
