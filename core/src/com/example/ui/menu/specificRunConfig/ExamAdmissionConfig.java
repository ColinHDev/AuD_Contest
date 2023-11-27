package com.example.ui.menu.specificRunConfig;

import com.example.manager.RunConfiguration;
import com.example.simulation.GameState;

public class ExamAdmissionConfig extends ModeSpecificRunConfiguration{
	public ExamAdmissionConfig(RunConfiguration settings) {
		super(settings);

		settings.gameMode = GameState.GameMode.Exam_Admission;
		mapName = "";


		passSettings(settings);
	}
}
