package com.example.ui.menu.specificRunConfig;

import com.example.manager.RunConfiguration;
import com.example.simulation.StaticGameState;

public class ExamAdmissionConfig extends ModeSpecificRunConfiguration{
	public ExamAdmissionConfig(RunConfiguration settings) {
		super(settings);

		settings.gameMode = StaticGameState.GameMode.Exam_Admission;
		mapName = "";


		passSettings(settings);
	}
}
