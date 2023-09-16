package com.example.animation.action.uiActions;

import com.badlogic.gdx.graphics.Color;
import com.example.ui.hud.UiMessenger;


/**
 * Animator Action for notifying the Ui that the Game is Over
 */
public class MessageUiGameEndedAction extends MessageUiAction{

	boolean won;
	boolean draw;
	int team;

	Color color;
	public MessageUiGameEndedAction(float start, UiMessenger uiMessenger, boolean won, int team,Color color) {
		super(start, uiMessenger);
		this.won = won;
		this.team = team;
		this.color = color;

	}
	public MessageUiGameEndedAction(float start,UiMessenger uiMessenger,boolean isDraw){
		super(start, uiMessenger);
		draw = isDraw;
		this.color = null;

	}

	@Override
	protected void runAction(float oldTime, float current) {
		uiMessenger.gameEnded(won,team,draw,color);
		endAction(oldTime);
	}
}
