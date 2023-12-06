package com.gatdsen.animation.action.uiActions;

public class MessageUiCurrencyAction extends MessageUiAction{


    @Override
    protected void runAction(float oldTime, float current) {
        uiMessenger.setBankBalance();
    }
}
