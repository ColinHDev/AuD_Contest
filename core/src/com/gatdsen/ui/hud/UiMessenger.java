package com.gatdsen.ui.hud;

import com.badlogic.gdx.graphics.Color;
import com.gatdsen.simulation.GameState;
import com.gatdsen.ui.menu.Hud;


/**
 * This class is planned as an Interface between the Animator and the HudElements.
 * It provides the Animator an easy way to communicate/call Hud functions.
 */
public class UiMessenger {

	private Hud hud;

	public UiMessenger(Hud hud){
		this.hud = hud;
	}

	/**
	 * Applies the necessary changes to the Hud for a new turn.
	 * @param team
	 */
	public void turnChanged(GameState state, int team){
		//Todo update/notify every element so it sets its status to that of the current player
	}

	/**
	 * Will call {@link Hud#createTurnChangePopup(Color)} to temporarily draw it to the Hud.
	 * @param outlinecolor  Teamcolor of the current/new Player -> could be implemented
	 */
	private void drawTurnChangePopup(Color outlinecolor){
		hud.createTurnChangePopup(outlinecolor);
	}

	/**
	 * Changes the rendering speed of animation
	 * @param speed
	 */
	public void changeAnimationPlaybackSpeed(float speed){
		hud.setRenderingSpeed(speed);
	}

	/**
	 * Pass the the turnTime to the Hud for displaying it
	 * @param time
	 */
	public void setTurnTimeLeft(int time){
		hud.setTurntimeRemaining(time);
	}

	/**
	 * Starts the turn timer for specified time.
	 * @param
	 */
	public void startTurnTimer(int turnTime,boolean currPlayerIsHuman){
		if(currPlayerIsHuman) {
			hud.startTurnTimer(turnTime);
		}
		else{
			stopTurnTimer();
		}
	}

	public void stopTurnTimer(){
		hud.stopTurnTimer();
	}

	public void gameEnded(boolean won, int team,boolean isDraw,Color color){
		hud.gameEnded(won,team,isDraw);

	}

	public void teamScore(int team, float score) {
		hud.adjustScores(team, score);
	}

	/**
	 * Setzt das Guthaben der Bank für einen bestimmten Spieler
	 *
	 * @param playerID Die ID des Spielers, dessen Bankguthaben aktualisiert wird
	 * @param balance   Der neue Kontostand für den Spieler
	 */
	public void setBankBalance (int playerID, int balance){
		hud.setBankBalance(playerID,balance);
	}

	/**
	 * Aktualisiert die Leben eines bestimmten Spielers im HUD
	 *
	 * @param playerID Die ID des Spielers, dessen Leben aktualisiert wird
	 * @param health   Der neue Lebenswert für den Spieler
	 */
	public void setPlayerHealth(int playerID, int health){
		hud.setPlayerHealth(playerID, health);
	}
}