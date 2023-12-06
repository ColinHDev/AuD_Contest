package com.gatdsen.simulation.campaign;

import com.gatdsen.manager.StaticGameState;
import com.gatdsen.manager.player.Bot;
import com.gatdsen.manager.Controller;

public class CampaignBot extends Bot {

    @Override
    public String getStudentName() {
        return "";
    }

    @Override
    public int getMatrikel() {
        return -1; //Heh, you thought
    }

    @Override
    public String getName() {
        return "Training Bot";
    }

    protected int turnCount = -1;

    @Override
    public void init(StaticGameState state) {

    }

    @Override
    public void executeTurn(StaticGameState state, Controller controller) {
        turnCount++;
    }
}
