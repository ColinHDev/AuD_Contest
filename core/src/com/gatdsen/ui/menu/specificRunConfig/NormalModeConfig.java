package com.gatdsen.ui.menu.specificRunConfig;

import com.gatdsen.manager.RunConfiguration;

/**
 * RunConfig for the NormalGameMode
 */
public class NormalModeConfig extends  ModeSpecificRunConfiguration{


	public NormalModeConfig(RunConfiguration settings) {
		super(settings);
		passSettings(settings);
	}

}
