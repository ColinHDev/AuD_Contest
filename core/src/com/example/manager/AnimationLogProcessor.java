package com.example.manager;

import com.example.simulation.GameState;
import com.example.simulation.action.ActionLog;

public interface AnimationLogProcessor {

    void init(GameState state, String[] playerNames, String[][] skins);

    void animate(ActionLog log);

    void awaitNotification();
}
