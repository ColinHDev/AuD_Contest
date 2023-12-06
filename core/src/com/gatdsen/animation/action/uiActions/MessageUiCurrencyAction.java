package com.gatdsen.animation.action.uiActions;

import com.gatdsen.ui.hud.UiMessenger;

public class MessageUiCurrencyAction extends MessageUiAction{
    private final int team;
    private final int balance;

    public MessageUiCurrencyAction(float start, UiMessenger uiMessenger, int team, int balance) {
        super(start, uiMessenger);
        this.team = team;
        this.balance = balance;
    }

    @Override
    protected void runAction(float oldTime, float current) {
        uiMessenger.setBankBalance(team, balance);
        endAction(oldTime);
    }
}
