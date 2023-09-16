package com.example.manager;

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
    protected final PlayerType getType() {
        return PlayerType.AI;
    }

    protected Random rnd;

    public final void setRnd(long seed){
        if (rnd == null) rnd = new Random(seed);
    }
}
