package com.example.simulation.campaign;

import com.example.manager.StaticGameState;
import com.example.manager.Controller;

public class Level1Bot extends CampaignBot {

    @Override
    public void executeTurn(StaticGameState state, Controller controller) {
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
