package com.gatdsen.simulation.campaign;

import com.gatdsen.simulation.GameState;
import com.gatdsen.manager.Controller;

public class Level1Bot extends CampaignBot {

    @Override
    public void executeTurn(GameState state, Controller controller) {
        super.executeTurn(state, controller);
        switch (turnCount){
            case 0:
                System.out.println("First turn ^^: glhf");
                //Do first turn stuff
            default:
                System.out.println("Turn " + turnCount);
                //Do regular logic
                break;
        }
    }
}
