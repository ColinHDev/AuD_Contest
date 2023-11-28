package com.gatdsen.manager;

import com.gatdsen.simulation.GameState;
import com.gatdsen.simulation.action.ActionLog;

public interface AnimationLogProcessor {

    void init(GameState state, String[] playerNames, String[][] skins);

    void animate(ActionLog log);

    void awaitNotification();
}
