package com.gatdsen.ui.menu;

import com.gatdsen.ui.GADS;
import com.gatdsen.ui.menu.attributes.Attribute;
import com.gatdsen.ui.menu.attributes.MapAttribute;
import com.gatdsen.ui.menu.attributes.PlayerAttribute;

public class GamemodeNormalScreen extends AttributeScreen {

    /**
     * Konstruktor, welcher Kamera, Viewports, Stage und SpriteBatch initialisiert. ruft setupMenuScreen auf, um UI für Hauptmenü einzurichten
     * ruft setupMenuScreen auf, um das UI für das Hauptmenü einzurichten.
     *
     * @param gameInstance Die Instanz des Hauptspiels (GADS), die diesem Bildschirm gehört.
     */
    public GamemodeNormalScreen(GADS gameInstance) {
        super(gameInstance);
    }

    /**
     * Gibt die Attribute für den Spielmodus Normal zurück
     *
     * @return Ein Array von Attributen, das angibt, welche Attribute benötigt werden
     */
    @Override
    Attribute[] getAttributes() {
        return new Attribute[]{new PlayerAttribute(0), new PlayerAttribute(1), new MapAttribute()};
    }


    /**
     * Gibt den nächsten Bildschirmzustand für den Spielmodus Normal zurück
     *
     * @return Der nächste Bildschirmzustand (ScreenState)
     */
    @Override
    GADS.ScreenState getNext() {
        return GADS.ScreenState.INGAMESCREEN;
    }

    /**
     * Gibt den vorherigen Bildschirmzustand für den Spielmodus Normal zurück.
     *
     * @return Der vorherige Bildschirmzustand (ScreenState).
     */
    @Override
    GADS.ScreenState getPrev() {
        return GADS.ScreenState.MAINSCREEN;
    }

    /**
     * Gibt den Titelstring für die Überschrift zurück
     *
     * @return Der Titel
     */
    @Override
    String getTitelString() {
        return "Spielmodus Normal";
    }
}