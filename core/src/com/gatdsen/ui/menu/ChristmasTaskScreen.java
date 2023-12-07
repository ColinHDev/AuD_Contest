package com.gatdsen.ui.menu;

import com.gatdsen.ui.GADS;
import com.gatdsen.ui.menu.attributes.Attribute;
import com.gatdsen.ui.menu.attributes.PlayerAttribute;

public class ChristmasTaskScreen extends AttributeScreen {

    /**
     * Konstruktor für die Klasse AttributeScreen
     *
     * @param gameInstance Eine Instanz des GADS-Spiels
     */
    public ChristmasTaskScreen(GADS gameInstance) {
        super(gameInstance);
    }

    @Override
    Attribute[] getAttributes() {
        return new Attribute[]{new PlayerAttribute(0)};
    }

    @Override
    String getTitelString() {
        return "Weihnachtsaufgabe";
    }

    @Override
    GADS.ScreenState getNext() {
        return GADS.ScreenState.INGAMESCREEN;
    }

    @Override
    GADS.ScreenState getPrev() {
        return GADS.ScreenState.MAINSCREEN;
    }
}