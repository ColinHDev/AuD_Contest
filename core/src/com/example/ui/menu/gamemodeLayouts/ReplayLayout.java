package com.example.ui.menu.gamemodeLayouts;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.example.ui.menu.Menu;

public class ReplayLayout extends GamemodeLayout{
	public ReplayLayout(Skin skin, Menu menuInstance) {
		super(skin, menuInstance);
		positionButtons(menuInstance);
	}

	@Override
	protected void positionButtons(Menu menu) {

		add(menu.getFileChooserButton());


	}
}
