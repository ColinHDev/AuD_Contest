package com.gatdsen.simulation;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.nio.file.Paths;

/**
 * Lädt eine Map aus dem Asset-Verzeichnis.
 * Benutzt das Singleton-Pattern.
 */
public class MapLoader {
    static MapLoader mapLoader = null;

    /**
     * Gibt die einzige Instanz dieser Klasse zurück.
     *
     * @return die einzige Instanz dieser Klasse
     */
    static MapLoader getInstance() {
        if (mapLoader == null) {
            mapLoader = new MapLoader();
        }
        return mapLoader;
    }

    /**
     * Lädt eine Map aus dem Asset-Verzeichnis.
     * Geht davon aus, dass alle Tiles auf der Map direkt oder indirekt verankert sind.
     * Die Map-Datei muss in JSON kodiert sein.
     *
     * @param mapName Name der Map ohne Typ als String
     * @return die Map als 2D-Array von {@link GameState.MapTileType}
     */
    GameState.MapTileType[][] loadMap(String mapName) {
        JsonReader reader = new JsonReader();
        JsonValue json;
        GameState.MapTileType[][] map;
        try {
            // attempt to load map from jar
            json = reader.parse(getClass().getClassLoader().getResourceAsStream("maps/" + mapName + ".json"));
        } catch (Exception e) {
            json = null;
        }
        if (json == null) {
            try {
                // attempt to load map from external maps dir
                json = reader.parse(new FileHandle(Paths.get("./maps/" + mapName + ".json").toFile()));
            } catch (Exception e) {
                throw new RuntimeException("Could not find or load map:" + mapName);
            }
        }

        int width = json.get("width").asInt();
        int height = json.get("height").asInt();
        map = new GameState.MapTileType[width][height];

        JsonValue tileData = json.get("layers").get(0).get("data");

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int type = tileData.get(i + (height - j - 1) * width).asInt();
                map[i][j] = GameState.MapTileType.values()[type - 2];
            }
        }

        return map;
    }
}
