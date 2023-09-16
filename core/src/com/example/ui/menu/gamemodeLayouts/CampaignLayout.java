package com.example.ui.menu.gamemodeLayouts;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.example.simulation.GameState;
import com.example.ui.menu.Menu;

public class CampaignLayout extends GamemodeLayout{
	public CampaignLayout(Skin skin, Menu menuInstance) {
		super(skin, menuInstance);
		positionButtons(menu);
	}

	@Override
	protected void positionButtons(Menu menu) {
		this.add(getMapSelector()).colspan(getDefaultColspan()).pad(getDefaultPadding());

		row();

		//set playercount to 1
		this.add(getBotSelector()).colspan(getDefaultColspan());


		menu.setMaps(getMapSelector(), GameState.GameMode.Campaign);


		getBotSelector().resizeTable(1);
	}
}
