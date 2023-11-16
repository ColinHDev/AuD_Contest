package com.example.manager;

import com.example.simulation.StaticGameState;
import com.example.simulation.action.ActionLog;

public interface AnimationLogProcessor {

    void init(StaticGameState state, String[] playerNames, String[][] skins);

    void animate(ActionLog log);

    void awaitNotification();
}
