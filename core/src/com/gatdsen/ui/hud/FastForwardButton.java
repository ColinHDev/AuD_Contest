package com.gatdsen.ui.hud;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * Button to toggle the Animation speedup
 */
public class FastForwardButton extends Button {

	UiMessenger uiMessenger;
	boolean speedUp;
	float speedUpValue;
	public FastForwardButton(Drawable imageUp,Drawable imageDown,Drawable imageChecked, UiMessenger uiMessenger,float speedUpValue) {
		super(imageUp,imageDown,imageChecked);
		this.uiMessenger = uiMessenger;
		this.speedUpValue=speedUpValue;
		 this.addListener(new ClickListener(){
			 @Override
			 public void clicked(InputEvent event,float x, float y){
				 toggleAnimationSpeed();
			 };
			 });

	}


	private void toggleAnimationSpeed(){
		if(!speedUp) {
			uiMessenger.changeAnimationPlaybackSpeed(speedUpValue);
		}
		else{
			uiMessenger.changeAnimationPlaybackSpeed(1);
		}
		speedUp=!speedUp;
	}





}
