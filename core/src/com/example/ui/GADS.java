package com.example.ui;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;
import com.example.manager.RunConfiguration;
import com.example.ui.assets.GADSAssetManager;
import com.example.ui.menu.GamemodeNormalScreen;
import com.example.ui.menu.MainScreen;

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
	private Screen[] screens;
	private Screen currentScreen;

	public enum ScreenState {
		MAINSCREEN,
		NORMALMODESCREEN,
		INGAMESCREEN
	}

	public GADS(RunConfiguration runConfig) {
		this.runConfig = runConfig;
		screens = new Screen[ScreenState.values().length];
	}

	@Override
	public void create() {

		//ToDo: Ladebildschirm

		//size of the viewport is subject to change
		assetManager = new GADSAssetManager();
		initScreens();
		setScreen(ScreenState.MAINSCREEN);

	}

	private void initScreens() {
		for (ScreenState state : ScreenState.values()) {
			screens[state.ordinal()] = createScreen(state);
		}
	}

	private Screen createScreen(ScreenState state) {
		switch (state) {
			case MAINSCREEN:
				return new MainScreen(this);
			case INGAMESCREEN:
				return new InGameScreen(this, runConfig);
			case NORMALMODESCREEN:
				return new GamemodeNormalScreen(this);
			default:
				return null;
		}
	}

	public void setScreen(ScreenState screenState) {
		if (screens[screenState.ordinal()] == null) {
			initScreens();
		}
		setScreen(screens[screenState.ordinal()]);
	}

	public void setScreen(Screen screen) {
		currentScreen = screen;
		super.setScreen(screen);
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
}
