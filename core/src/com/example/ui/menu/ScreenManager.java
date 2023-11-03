package com.example.ui.menu;

import com.example.ui.GADS;

public class ScreenManager {
    private GADS gameInstance;
    private MainScreen mainScreen;
    private GamemodeNormalScreen gamemodeNormalScreen;

    public ScreenManager (GADS gameInstance){
        this.gameInstance  = gameInstance;
        mainScreen = new MainScreen(gameInstance);
        gamemodeNormalScreen = new GamemodeNormalScreen(gameInstance);
    }
    public void setMainScreen(){
        gameInstance.setScreen(mainScreen);
    }
    public void setGamemodeNormalScreen(){
        gameInstance.setScreen(gamemodeNormalScreen);
    }
    public MainScreen getMainScreen(){
        return mainScreen;
    }
    public GamemodeNormalScreen getGamemodeNormalScreen (){
        return gamemodeNormalScreen;
    }
}
