package com.gatdsen.ui.menu.specificRunConfig;

import com.gatdsen.manager.RunConfiguration;

/**
 * Creates a RunConfig from another, but only replaces null values;
 */
public abstract class ModeSpecificRunConfiguration extends RunConfiguration {

	public ModeSpecificRunConfiguration(RunConfiguration settings){
		super();
	}

	/**
	 * Replaces null variables with passed RunConfiguration.
	 * @param settings
	 */
	public void passSettings(RunConfiguration settings){

		gameMode = settings.gameMode;

		if(!gui){
			gui = settings.gui;
		}

		if(animationLogProcessor!=null){
			animationLogProcessor=settings.animationLogProcessor;
		}

		if(inputProcessor ==null){
			inputProcessor = settings.inputProcessor;
		}

		if(mapName==null){
			mapName = settings.mapName;
		}

		if(teamCount == 0){
			teamCount = settings.teamCount;
		}
		if(players==null){
			players = settings.players;
		}

	}

}
