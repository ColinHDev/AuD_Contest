package com.gatdsen.ui.debugView;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gatdsen.simulation.action.ActionLog;
import com.gatdsen.ui.menu.buttons.ColoredLabelWithBackground;

public class DebugTable extends Table {

	Viewport viewport;

	private VerticalGroup vg;

	public DebugTable(Skin skin, Viewport viewport) {
		super(skin);
		this.viewport = viewport;
		this.vg = new VerticalGroup();
		vg.align(Align.left);
		vg.left();
		vg.setFillParent(true);
	}

	public void addString(String string){
		ColoredLabelWithBackground label = new ColoredLabelWithBackground(string,this.getSkin(), Color.WHITE);
		label.setFontScale(0.7f);

		//label.setWrap(true);
		//if(this.getRows()*label.getHeight()> getParent().getHeight()){
		//	clear();
		//}
		vg.addActor(label);
		add(label).colspan(6).align(Align.left);//.width(this.getWidth()/2);
		row();

		while(vg.getHeight()>viewport.getWorldHeight()*0.75){
			vg.removeActorAt(0,false);
		}


		if(this.getMinHeight()>viewport.getWorldHeight()*0.75){
			this.clear();
		};


	}

	public void addActionLog(ActionLog log){

		//Todo make it so every action is inserted separately
			// does not work with the current implementation i think
		addString(log.toString());

	}


	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		vg.draw(batch,parentAlpha);
	}

	/**
	 * Wird aufgerufen, wenn der Table erneuert/cleared werden soll.
 	 */



	@Override
	public void clear() {
		super.clear();
	}
}
