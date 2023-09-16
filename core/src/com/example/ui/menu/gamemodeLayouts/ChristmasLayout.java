package com.example.ui.menu.gamemodeLayouts;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.example.ui.menu.Menu;

public class ChristmasLayout extends GamemodeLayout{

	public ChristmasLayout(Skin skin, Menu menuInstance) {
		super(skin, menuInstance);
		positionButtons(menuInstance);
	}

	@Override
	protected void positionButtons(Menu menu) {
		menu.getBotSelector().resizeTable(1);
		this.add(menu.getBotSelector()).colspan(getDefaultColspan());
	}
}
