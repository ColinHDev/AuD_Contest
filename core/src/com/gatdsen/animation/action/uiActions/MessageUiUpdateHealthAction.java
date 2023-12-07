package com.gatdsen.animation.action.uiActions;

import com.gatdsen.ui.hud.UiMessenger;

public class MessageUiUpdateHealthAction extends MessageUiAction{
    private final int team;
    private final int health;

    public MessageUiUpdateHealthAction(float start, UiMessenger uiMessenger, int team, int health) {
        super(start, uiMessenger);
        this.team = team;
        this.health = health;
    }

    @Override
    protected void runAction(float oldTime, float current) {
        uiMessenger.setPlayerHealth(team, health);
        endAction(oldTime);
    }
}
