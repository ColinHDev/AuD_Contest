package com.gatdsen.ui.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Timer;
import com.gatdsen.ui.assets.AssetContainer;
import com.gatdsen.ui.menu.buttons.ColoredLabelWithBackground;

/**
 * Class representing and drawing the TimerSprite and the remaining time.
 */
public class TurnTimer extends Table {

	Image timerImage;
	Label timeDisplay;
	Timer timer;

	protected int turnTime;
	protected int time;
	protected float currInterval;

	protected Timer.Task countdown;
	public TurnTimer(TextureRegion timerTexture) {


		this.timer = new Timer();
		time = 0;

		this.timerImage = new Image(timerTexture);
		timeDisplay = new ColoredLabelWithBackground("", AssetContainer.MainMenuAssets.skin, Color.RED, new TextureRegionDrawable(AssetContainer.IngameAssets.pixel));

		//add both to the horizontal group for drawing them next to each other
		add(timeDisplay).width(44);
		timeDisplay.setFontScale(3);
		this.turnTime = 0;
		currInterval = 1;
		this.countdown = new CountdownTask();
	}

	public void setCurrentTime(int seconds){
		timeDisplay.setText(seconds);
		time=seconds;
	}

	/**
	 * Returns max Turn time.
	 * @return
	 */
	public int getTurnTime(){

		return turnTime;

	}


	/**
	 * Returns current time.
 	 * @return
	 */
	public int getTime(){

		return time;

	}

	public void startTimer(int turnTime){
		stopTimer();
		timer.clear();
		//schedule the countdown task to count down the turn time
		timer.scheduleTask(countdown,1,1);
		this.turnTime = turnTime;
		setCurrentTime(turnTime);
		timer.start();
	}

	public void stopTimer(){

		timer.stop();
		setCurrentTime(0);
	}

	private void tick(){
		time--;
		timeDisplay.setText(Math.max(0,time));
	}

	/**
	 * changes the speed at wich the timer counts down
	 * If the player is human, only allow to speedup wait time.
	 * @param
	 */
	//public void changeInterval(float interval){

	//	timer.clear();
	//		if(time>turnTime){
	//			//reset the speedup time, if the turn waitTime has passed for a human player
	//			int remTime = time-turnTime;

	//			timer.scheduleTask(new Timer.Task() {
	//				@Override
	//				public void run() {
	//					changeInterval(1f);
	//				}
	//			},remTime/interval);
	//		}
	//		else{
	//			interval = 1;
	//		}
	//	}
	//}

	private class CountdownTask extends Timer.Task{
		@Override
		public void run() {
			tick();
		}
	}
}