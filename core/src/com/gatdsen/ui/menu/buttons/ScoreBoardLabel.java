package com.gatdsen.ui.menu.buttons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ScoreBoardLabel extends ColoredLabelWithBackground{

	CharSequence name;
	final int maxNameLength = 10;


	public ScoreBoardLabel(CharSequence name, Skin skin, Color color) {
		super(name, skin, color);
		//cut name if it is too long
		if(name.length()>maxNameLength){
			this.name = name.subSequence(0,10);
		}
		else {
			this.name = name;
		}
		adjustScore(0);
	}

	public void adjustScore(float score) {
		setText(name + " : " + score);
	}
}
