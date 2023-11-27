package com.example.ui.menu.layout;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BasicLefthandLayer extends Layer{


	public BasicLefthandLayer(Vector2 anchorPosition){
		this.anchorPosition = new Vector2(anchorPosition);
	}
	@Override
	public void calculateElementPositions() {
		int elementIndex =0;
		for (Actor actor: layerElements) {
			actor.setPosition(anchorPosition.x+elementDistance*elementIndex,anchorPosition.y);
			elementIndex++;
		}
	}


}
