package com.gatdsen.simulation.campaign;

import com.gatdsen.simulation.GameState;
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
    public void init(GameState state) {

    }

    @Override
    public void executeTurn(GameState state, Controller controller) {
        turnCount++;
    }

    @Override
    public String getSkin(int characterIndex) {
        return "coolCatSkin";
    }
}