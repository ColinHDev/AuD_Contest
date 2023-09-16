package com.example.ui.menu.gamemodeLayouts;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.example.simulation.GameState;
import com.example.ui.menu.Menu;

public class ExamAdmissionLayout extends GamemodeLayout{
	public ExamAdmissionLayout(Skin skin, Menu menuInstance) {
		super(skin, menuInstance);
		positionButtons(menuInstance);
	}

	@Override
	protected void positionButtons(Menu menu) {
		getTeamAmountSlider().setRanges(1);

		row();

		//set playercount to 1
		this.add(getBotSelector()).colspan(getDefaultColspan());


		menu.setMaps(getMapSelector(), GameState.GameMode.Exam_Admission);


		getBotSelector().resizeTable(1);
	}
}
