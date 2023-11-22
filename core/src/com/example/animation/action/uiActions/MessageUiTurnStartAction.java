package com.example.animation.action.uiActions;

import com.example.simulation.GameState;
import com.example.ui.hud.UiMessenger;

/**
 * Action to signal a Turn start with the current {@link GameCharacter} to the ui
 */
public class MessageUiTurnStartAction extends MessageUiAction {


	private int team;
	GameState state;

	public MessageUiTurnStartAction(float start, UiMessenger uiMessenger, int team, GameState state) {
		super(start, uiMessenger);
		this.team = team;
		this.state = state;
	}

	@Override
	protected void runAction(float oldTime, float current) {
		uiMessenger.turnChanged(state, team);
		endAction(oldTime);
	}
}
