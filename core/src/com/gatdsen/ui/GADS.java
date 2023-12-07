package com.gatdsen.ui;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;
import com.gatdsen.manager.run.config.RunConfiguration;
import com.gatdsen.ui.assets.GADSAssetManager;
import com.gatdsen.ui.menu.GamemodeNormalScreen;
import com.gatdsen.ui.menu.InGameScreen;
import com.gatdsen.ui.menu.MainScreen;
import com.gatdsen.ui.menu.ChristmasTaskScreen;

/**
 * GADS ist die verantwortliche Klasse im LifeCycle der Anwendung.
 * Definiert das Verhalten der Anwendung bei LifeCycle-Events wie
 * {@link com.badlogic.gdx.ApplicationListener#create() Starten},
 * {@link com.badlogic.gdx.ApplicationListener#render() Rendern eines Frames} oder
 * {@link com.badlogic.gdx.ApplicationListener#resize(int, int)} () Änderung der Fenstergröße}.
 */
public class GADS extends Game {
    GADSAssetManager assetManager;
    private RunConfiguration runConfig;
    private ConfigScreen[] screens;
    private Screen currentScreen;

    /**
     * wird verwendet um Zustand des Bildschirms zu "speichern"
     */
    public enum ScreenState {
        MAINSCREEN,
        NORMALMODESCREEN,
        CHRISTMASTASKSCREEN,
        INGAMESCREEN,
        LOADSCREEN
    }

    /**
     * GADS Konstruktor
     *
     * @param runConfig kommt von DesktopLauncher.java
     */
    public GADS(RunConfiguration runConfig) {
        this.runConfig = runConfig;
        screens = new ConfigScreen[ScreenState.values().length];
    }

    /**
     * erzeugt Screen und setzt ihn auf MAINSCREEN
     */
    @Override
    public void create() {
        assetManager = new GADSAssetManager();
        initScreens();
        setScreen(ScreenState.MAINSCREEN, runConfig);
    }

    /**
     * Erstellt Array mit den Screen Objekten.
     */
    private void initScreens() {
        for (ScreenState state : ScreenState.values()) {
            screens[state.ordinal()] = createScreen(state);
        }
    }

    /**
     * Wird verwendet, um neue Instanz der Klasse Screen zu erstellen.
     *
     * @param state aktueller Zustand des Bildschirms
     * @return Bildschirm anhand der enum von ScreenState
     */
    private ConfigScreen createScreen(ScreenState state) {
        switch (state) {
            case MAINSCREEN:
                return new MainScreen(this);
            case INGAMESCREEN:
                return new InGameScreen(this);
            case NORMALMODESCREEN:
                return new GamemodeNormalScreen(this);
            case CHRISTMASTASKSCREEN:
                return new ChristmasTaskScreen(this);
            default:
                return null;
        }
    }

    /**
     * Erstellt Screen, falls noch nicht erstellt, ansonsten setzt es Bildschirm auf die Eingabe
     *
     * @param screenState Zustand des Bildschirms
     */
    public void setScreen(ScreenState screenState, RunConfiguration runConfiguration) {
        if (screens[screenState.ordinal()] == null) {
            initScreens();
        }
        setScreen(screens[screenState.ordinal()], runConfiguration);
    }

    /**
     * setzt Screen auf Screen durch Nutzen der Super-Klasse
     *
     * @param screen may be {@code null}
     */
    public void setScreen(ConfigScreen screen, RunConfiguration runConfiguration) {
        if (runConfiguration != null){
            screen.setRunConfiguration(runConfiguration);
        }
        currentScreen = screen;
        super.setScreen(screen);
    }

    /**
     * Löscht den Bildschirm und ruft render-Methode der Super-Klasse auf, um Bildschirm zu aktualisieren
     */
    public void render() {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        assetManager.update();
        super.render();
    }

    /**
     * wenn der Bildschirm nicht leer ist, löscht es Screen-Objekt.
     * assetManager wird entladen und das Spiel geschlossen.
     */
    @Override
    public void dispose() {
        if (screen != null) this.screen.dispose();
        assetManager.unloadAtlas();
        System.exit(0);
    }
}
