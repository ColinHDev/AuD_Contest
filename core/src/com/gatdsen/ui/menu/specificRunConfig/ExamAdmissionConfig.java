package com.gatdsen.ui.menu.specificRunConfig;

import com.gatdsen.manager.run.config.RunConfiguration;
import com.gatdsen.simulation.GameState;

public class ExamAdmissionConfig extends ModeSpecificRunConfiguration{
	public ExamAdmissionConfig(RunConfiguration settings) {
		super(settings);

		settings.gameMode = GameState.GameMode.Exam_Admission;
		mapName = "";


		passSettings(settings);
	}
}
