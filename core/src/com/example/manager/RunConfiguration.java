package com.example.manager;

import com.example.simulation.GameState;
import com.example.ui.hud.UiMessenger;

import java.util.ArrayList;

public class RunConfiguration {

    //Todo add default values
    public GameState.GameMode gameMode = GameState.GameMode.Normal;
    public int inventorySize = 6;

    public GameState.GameMode[] getGameModes() {
        return GameState.GameMode.values();
    }

    public boolean gui = false;

    public AnimationLogProcessor animationLogProcessor = null;

    public UiMessenger uiMessenger = null;

    public InputProcessor inputProcessor = null;

    public String mapName;

    public boolean replay = false;

    public int teamCount;

    public ArrayList<Class<? extends Player>> players;

    public boolean isValid(){

        if(teamCount<=0){
            System.err.println("RunConfig: TeamCount is not valid. TCount: " + teamCount);
           return false;
        }
        if(mapName==null){
            return false;
        }
        if(players==null){
            return false;
        }
        if(gameMode == null){
            return false;
        }

        return true;
    }

    public String toString(){
        String nl = "\n";
        String output = "";

       output+= "GameMode: " + gameMode + nl;
      output+=  "Gui: " + gui +nl;

      output+= "AnimationLogProcessor: " + checkNullToString(animationLogProcessor)+nl;
      output+= "InputProcessor: " + checkNullToString(inputProcessor)+nl;
      output+= "mapName: " + mapName+nl;
      output+= "teamCount: " + teamCount+nl;
      output+= "players: " + checkNullToString(players)+nl;

      return output;
    }

    private String checkNullToString(Object n){
       if(n!=null){
           return n.toString();
       }
       else {
           return null;
       }
    }
}
