package com.example.ui.menu.gamemodeLayouts;

import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.example.ui.menu.GameMap;
import com.example.ui.menu.Menu;
import com.example.ui.menu.buttons.BotSelectorTable;
import com.example.ui.menu.buttons.TeamAmountSlider;

public abstract class GamemodeLayout extends Table {

	private int defaultColspan;
	private int defaultPadding;
	Menu menu;
	public GamemodeLayout(Skin skin, Menu menuInstance){
		super(skin);
		setDefaultColspan(4);
		setDefaultPadding(10);
		menu = menuInstance;
	}

	protected abstract void positionButtons(Menu menu);


	public void setDefaultColspan(int colspan){

		this.defaultColspan = colspan;
	}
	public void setDefaultPadding(int padding){
		this.defaultPadding = padding;
	}

	public int getDefaultColspan() {
		return defaultColspan;
	}

	public int getDefaultPadding() {
		return defaultPadding;
	}

	public BotSelectorTable getBotSelector(){
		return menu.getBotSelector();

	}
	public SelectBox<GameMap> getMapSelector(){
		return menu.getMapSelector();
	}

	public TeamAmountSlider getTeamAmountSlider(){
		return menu.getTeamAmountSlider();
	}
}
