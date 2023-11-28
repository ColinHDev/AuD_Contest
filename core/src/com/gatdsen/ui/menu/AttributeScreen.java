package com.gatdsen.ui.menu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gatdsen.manager.RunConfiguration;
import com.gatdsen.ui.GADS;
import com.gatdsen.ui.menu.attributes.Attribute;

public abstract class AttributeScreen extends BaseMenuScreen {

    Attribute [] attributes;

    /**
     * Konstruktor für die Klasse AttributeScreen
     *
     * @param gameInstance Eine Instanz des GADS-Spiels
     */
    public AttributeScreen(GADS gameInstance) {
        super(gameInstance);

    }

    /**
     * Gibt den Inhalt der Benutzeroberfläche als Actor für den Attributsbildschirm zurück
     *
     * @param skin Das Skin-Objekt für die Benutzeroberfläche
     * @return Ein Actor-Objekt, das den Inhalt für den Attributsbildschirm repräsentiert
     */
    @Override
    Actor getContent(Skin skin) {
        Table attributeTable = new Table();
        attributes = (getAttributes());

        for (Attribute attribute : attributes) {
            attributeTable.add(attribute.getContent(skin)).center().row();
        }
        return attributeTable;
    }

    /**
     * Gibt die RunConfiguration unter Berücksichtigung der Attribute zurück
     *
     * @return Eine RunConfiguration mit den konfigurierten Attributen
     */
    @Override
    protected RunConfiguration getRunConfiguration() {
        RunConfiguration runConfiguration = super.getRunConfiguration();
        for (Attribute attribute : attributes) {
            attribute.getConfig(runConfiguration);
        }
        return runConfiguration;
    }

    /**
     * Setzt die RunConfiguration unter Berücksichtigung der Attribute
     *
     * @param runConfiguration Die RunConfiguration, die als Basis für die Kopie verwendet wird
     */
    @Override
    protected void setRunConfiguration(RunConfiguration runConfiguration) {
        super.setRunConfiguration(runConfiguration);
        for (Attribute attribute : attributes) {
            attribute.setConfig(runConfiguration);
        }
    }

    /**
     * Gibt ein Array von Attributen zurück, die für diesen Bildschirm konfiguriert werden sollen
     *
     * @return Ein Array von Attributen
     */
    abstract Attribute[] getAttributes();
}
