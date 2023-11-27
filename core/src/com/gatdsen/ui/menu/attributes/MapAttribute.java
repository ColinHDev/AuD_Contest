package com.gatdsen.ui.menu.attributes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.gatdsen.manager.RunConfiguration;

public class MapAttribute extends Attribute {

    SelectBox<String> mapSelectBox;

    /**
     * Gibt den UI-Actor für das Attribut zurück.
     *
     * @param skin Das Skin-Objekt für die Benutzeroberfläche.
     * @return Ein Actor-Objekt, das das UI-Element für das Attribut repräsentiert.
     */
    @Override
    public Actor getContent(Skin skin) {
        Table mapTable = new Table();
        Label textLabelMap = new Label("Karte:", skin);
        textLabelMap.setAlignment(Align.center);
        mapSelectBox = new SelectBox<>(skin);
        Array<String> mapNames = loadFileNames("assets/res/maps", "json");
        mapSelectBox.setItems(mapNames);
        mapTable.columnDefaults(0).width(100);
        mapTable.columnDefaults(1).width(100);
        mapTable.add(textLabelMap).colspan(4).pad(10).center();
        mapTable.add(mapSelectBox).colspan(4).pad(10).width(80).row();
        return mapTable;
    }

    /**
     * Konfiguriert die RunConfiguration unter Berücksichtigung dieses Attributs und gibt die aktualisierte Konfiguration zurück.
     *
     * @param runConfiguration Die aktuelle RunConfiguration.
     * @return Die aktualisierte RunConfiguration nach der Konfiguration des Attributs.
     */
    @Override
    public RunConfiguration getConfig(RunConfiguration runConfiguration) {
        runConfiguration.mapName = mapSelectBox.getSelected();
        return runConfiguration;
    }

    /**
     * Setzt die RunConfiguration basierend auf den Attributinformationen.
     *
     * @param runConfiguration Die RunConfiguration, die konfiguriert wird.
     */
    @Override
    public void setConfig(RunConfiguration runConfiguration) {
        mapSelectBox.setSelected(runConfiguration.mapName);
    }

    /**
     * Liest die Namen der gesuchten Dateien aus dem angegebenen Ordner, die die gesuchte Erweiterung haben, in ein Array.
     *
     * @param folderPath Der Pfad zum Ordner, aus dem die Karten geladen werden sollen.
     * @param fileExtension Die Erweiterung der gesuchten Dateien.
     * @return Eine Array-Liste von Karten-Namen ohne Dateierweiterung.
     */
    private Array<String> loadFileNames(String folderPath, String fileExtension) {
        Array<String> mapNames = new Array<>();
        FileHandle folder = Gdx.files.internal(folderPath);

        if (folder.isDirectory()) {
            for (FileHandle file : folder.list()) {
                if (file.extension().equals(fileExtension)) {
                    String mapName = file.nameWithoutExtension();
                    mapNames.add(mapName);
                }
            }
        }
        return mapNames;
    }
}
