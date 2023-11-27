package com.example.ui.menu.buttons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;

/**
 * Class that creates a label, that updates its int Value based on the belonging slider.
 */
public class SliderLabel extends ColoredLabelWithBackground {
	Slider sliderInstance;

	String textValue;

	public SliderLabel(String text, Skin skin, Slider slider) {
		super(text,skin, Color.WHITE);
		this.sliderInstance = slider;
		this.textValue = text;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if(this.sliderInstance!=null) {
			setText(textValue + (int) sliderInstance.getValue() + " ");
		}

	}

	public void setSlider(Slider slider){
		this.sliderInstance = slider;
	}

	public void setTextValue(String newText){
		this.textValue = newText;
	}


}