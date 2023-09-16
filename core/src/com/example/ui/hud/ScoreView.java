package com.example.ui.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ScoreView extends Stage{

	ScoreBoard scores;
	boolean isEnabled = false;
	public ScoreView(ScoreBoard scoreBoard){
		super();
		//setup viewport
		Viewport view = new ExtendViewport(500,500);
		view.setCamera(new OrthographicCamera(100,100*(Gdx.graphics.getHeight()*1f/Gdx.graphics.getWidth())));

		scores = scoreBoard;
		setViewport(view);
		addScoreboard(scoreBoard);
	}

	public ScoreBoard getScores() {
		return scores;
	}

	public void addScoreboard(ScoreBoard scores){

		//remove old scoreboard if it exists
		if(this.scores!=null){
			this.scores.remove();
		}

		this.scores=scores;
		//add new scoreboard
		if(this.scores!=null){
			addActor(scores);
			scores.columnRight();
		}

	}

	public void adjustScores(float[] score){
		if(this.scores!=null) {
			scores.adjustScores(score);
		}
	}

	public void toggleEnabled(){
		isEnabled = !isEnabled;
	}

	@Override
	public void draw() {
		if(isEnabled) {
			getViewport().apply(true);
			getBatch().setProjectionMatrix(getViewport().getCamera().combined);
			super.draw();
		}
	}
}