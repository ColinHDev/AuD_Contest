package com.example.manager.player;

import com.example.manager.Controller;
import com.example.manager.StaticGameState;

import java.util.Random;

public class IdleBot extends Bot{
    @Override
    public String getName() {
        return "IdleBot";
    }

    @Override
    public int getMatrikel() {
        return 133769;
    }

    @Override
    public String getStudentName() {
        return "Santa";
    }

    private static Random random;

    @Override
    public void init(StaticGameState state) {
        long seed = 420L;
        random = new Random(seed);
    }

    @Override
    public void executeTurn(StaticGameState state, Controller controller) {

    }


}