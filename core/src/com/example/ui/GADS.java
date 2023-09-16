package com.example.ui;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.ScreenUtils;
import com.example.manager.RunConfiguration;
import com.example.ui.assets.GADSAssetManager;

/**
 * GADS ist die verantwortliche Klasse im LifeCycle der Anwendung.
 * Definiert das Verhalten der Anwendung bei LifeCycle-Events wie
 * {@link com.badlogic.gdx.ApplicationListener#create() Starten},
 * {@link com.badlogic.gdx.ApplicationListener#render() Rendern eines Frames} oder
 * {@link com.badlogic.gdx.ApplicationListener#resize(int,int)} () Änderung der Fenstergröße}.
 */
public class GADS extends Game {
	GADSAssetManager assetManager;
	private RunConfiguration runConfig;


	public GADS(RunConfiguration runConfig) {
		this.runConfig = runConfig;
	}

	public void startGame(RunConfiguration config){
		this.runConfig = config;
		setScreenIngame(config);
	}
	@Override
	public void create() {
		//size of the viewport is subject to change
		assetManager = new GADSAssetManager();

		//ToDo: add Loading Screen

		setScreen(new MenuScreen(this, runConfig));
	}

	public void render() {
		//	clear the screen
		ScreenUtils.clear(0, 0, 0.2f, 1);
		//call assetmanager
		assetManager.update();
		super.render();
	}

	@Override
	public void dispose() {
		if (screen != null) this.screen.dispose();
		assetManager.unloadAtlas();
		//apparently Gdx.app.exit() does not close the game completely
		//probably the runtime survives and needs to be killed via System.exit
		System.exit(0);
	}

	public void setScreenIngame(RunConfiguration runConfig) {
		setScreen(new InGameScreen(this, runConfig));
	}

	public void setScreenMenu() {
		//ToDo we can use runconfig to save the users selection while we are at it
		setScreen(new MenuScreen(this, this.runConfig));
	}


	public String[] getGameModes() {
		//Todo, ~maybe move to simulation?~ -> move to RunConfig

		return new String[]{"Normal","Weihnachtsaufgabe"};
	}
}
