package com.gatdsen.ui.menu.attributes;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gatdsen.manager.RunConfiguration;

public abstract class Attribute {
    /**
     * Gibt den UI-Actor für das Attribut zurück
     *
     * @param skin Das Skin-Objekt für die Benutzeroberfläche
     * @return Ein Actor-Objekt, das das UI-Element für das Attribut repräsentiert
     */
    public abstract Actor getContent(Skin skin);

    /**
     * Konfiguriert die RunConfiguration unter Berücksichtigung dieses Attributs und gibt die aktualisierte Konfiguration zurück
     *
     * @param runConfiguration Die aktuelle RunConfiguration
     * @return Die aktualisierte RunConfiguration nach der Konfiguration des Attributs
     */
    public abstract RunConfiguration getConfig(RunConfiguration runConfiguration);

    /**
     * Setzt die RunConfiguration basierend auf den Attributinformationen
     *
     * @param runConfiguration Die RunConfiguration, die konfiguriert wird
     */
    public abstract void setConfig(RunConfiguration runConfiguration);
}
