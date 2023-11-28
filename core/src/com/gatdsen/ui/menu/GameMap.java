package com.gatdsen.ui.menu;

public class GameMap {

	private final String mapName;
	private final int numberOfSpawnpoints;
	private final int numberOfTeams;


	public GameMap(String name, int spawnpoints,int numberOfTeams){
		this.mapName=name;
		this.numberOfSpawnpoints=spawnpoints;
		this.numberOfTeams = numberOfTeams;

	}

	public GameMap(String name,int spawnpoints){
		this(name,spawnpoints,0);
	}


	public String getName(){
		return this.mapName;
	}

	public String toString(){
		return getName();
	}

	/**
	 * Returns the number of spawnpoints per team
	 * @return
	 */
	public int getNumberOfSpawnpoints(){
		return this.numberOfSpawnpoints;
	}
	public int getNuberOfTeams(){return this.numberOfTeams;}
}
