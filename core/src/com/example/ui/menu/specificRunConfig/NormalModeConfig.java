package com.example.ui.menu.specificRunConfig;

import com.example.manager.RunConfiguration;

/**
 * RunConfig for the NormalGameMode
 */
public class NormalModeConfig extends  ModeSpecificRunConfiguration{


	public NormalModeConfig(RunConfiguration settings) {
		super(settings);
		passSettings(settings);
	}

}
