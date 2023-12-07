package com.gatdsen.ui.menu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.gatdsen.simulation.GameState;
import com.gatdsen.ui.GADS;

public class MainScreen extends BaseMenuScreen {

    /**
     * Konstruktor für die Klasse MainScreen
     *
     * @param gameInstance Die Instanz des Hauptspiels (GADS)
     */
    public MainScreen(GADS gameInstance) {
        super(gameInstance);
    }

    /**
     * Gibt den Inhalt der Benutzeroberfläche als Tabelle für den Hauptbildschirm zurück
     *
     * @param skin Das Skin-Objekt für die Benutzeroberfläche
     * @return Die Tabelle, die den Inhalt für den Hauptbildschirm repräsentiert
     */
    @Override
    Table getContent(Skin skin) {
        Table mainMenuTable = new Table(skin);
        TextButton weihnachtsaufgabeButton = new TextButton("Weihnachtsaufgabe", skin);
        weihnachtsaufgabeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                runConfiguration.gameMode = GameState.GameMode.Normal; //ToDo ändern zu CHRISTMASTASK
                gameInstance.setScreen(GADS.ScreenState.CHRISTMASTASKSCREEN,runConfiguration);
            }
        });
        TextButton normalGameModeButton = new TextButton("Spielmodus Normal", skin);
        normalGameModeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                runConfiguration.gameMode = GameState.GameMode.Normal;
                gameInstance.setScreen(GADS.ScreenState.NORMALMODESCREEN,runConfiguration);
            }
        });
        mainMenuTable.setFillParent(false); //sorgt dafür das die Tabelle nicht auf dem gesamten Screen angezeigt wird
        mainMenuTable.center();
        mainMenuTable.add(weihnachtsaufgabeButton).colspan(4).pad(10).width(200).row();
        mainMenuTable.add(normalGameModeButton).colspan(4).pad(10).width(200);
        return mainMenuTable;
    }

    /**
     * Gibt den nächsten Bildschirmzustand für den Hauptbildschirm zurück
     *
     * @return Der nächste Bildschirmzustand (ScreenState)
     */
    @Override
    GADS.ScreenState getNext() {
        return null;
    }

    /**
     * Gibt den vorherigen Bildschirmzustand für den Hauptbildschirm zurück
     *
     * @return Der vorherige Bildschirmzustand (ScreenState)
     */
    @Override
    GADS.ScreenState getPrev() {
        return null;
    }

    /**
     * Gibt den Titelstring für die Überschrift zurück
     *
     * @return Der Titel
     */
    @Override
    String getTitelString() {
        return "Willkommen zu Gadsen: Tower Defense";
    }
}