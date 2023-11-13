package com.example.simulation;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MapLoader {
    static MapLoader mapLoader = null;


    int width = 0;
    int height = 0;

    static MapLoader getInstance() {
        if (mapLoader == null) {
            mapLoader = new MapLoader();
        }
        return mapLoader;
    }

    /**
     * Loads a Map from the asset-directory
     * Assumes that all Tiles on the map are directly or indirectly anchored.
     * The Map file has t be encoded in JSON.
     *
     * @param mapName Name of the map without type as String
     */
    GameState.MapTileType[][] loadMap(String mapName) {
        JsonReader reader = new JsonReader();
        JsonValue json;
        GameState.MapTileType[][] map;
        try {
            //attempt to load map from jar
            json = reader.parse(getClass().getClassLoader().getResourceAsStream("maps/" + mapName + ".json"));
        } catch (Exception e) {
            json = null;
        }
        if (json == null) {
            try {
                //attempt to load map from external maps dir
                json = reader.parse(new FileHandle(Paths.get("./maps/" + mapName + ".json").toFile()));
            } catch (Exception e) {
                throw new RuntimeException("Could not find or load map:" + mapName);
            }
        }

        width = json.get("width").asInt();
        height = json.get("height").asInt();
        map = new GameState.MapTileType[width][height];

        JsonValue tileData = json.get("layers").get(0).get("data");

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int type = tileData.get(i + (height - j - 1) * width).asInt();
                    map[i][j] = GameState.MapTileType.values()[type-2];
            }
        }

        return map;

    }
}
