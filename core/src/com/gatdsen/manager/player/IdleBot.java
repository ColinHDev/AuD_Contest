package com.gatdsen.manager.player;

import com.gatdsen.manager.Controller;
import com.gatdsen.manager.StaticGameState;

/**
 * Dieser Bot repräsentiert einen Spieler, der keine Aktionen ausführt.
 */
public class IdleBot extends Bot {

    @Override
    public String getName() {
        return "IdleBot";
    }

    @Override
    public int getMatrikel() {
        return 1337;
    }

    @Override
    public String getStudentName() {
        return "Gadsen: 2023";
    }

    @Override
    public void init(StaticGameState state) {
    }

    @Override
    public void executeTurn(StaticGameState state, Controller controller) {
    }
}
