package com.gatdsen.ui.menu.specificRunConfig;

import com.gatdsen.manager.player.Player;
import com.gatdsen.manager.RunConfiguration;

/**
 * RunConfig for ChristmasMode
 */
public class ChristmasModeConfig extends ModeSpecificRunConfiguration{
	public ChristmasModeConfig(RunConfiguration settings, String christmasMapName, Class<? extends Player> christmasBot) {
		super(settings);
		this.mapName =christmasMapName;
		teamCount = 4;

		//add christmasBots and selected Player
		this.players = settings.players;
		for(int i=0;i<teamCount-1;i++){
			players.add(christmasBot);
		}
		//use the remaining settings from menu Button input
		passSettings(settings);
	}

}
