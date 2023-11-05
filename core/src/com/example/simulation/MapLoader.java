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
    private int width = 0;
    private int height = 0;

    /**
     * Loads a Map from the asset-directory
     * Assumes that all Tiles on the map are directly or indirectly anchored.
     * The Map file has t be encoded in JSON.
     *
     * @param mapName Name of the map without type as String
     */
    List<List<IntVector2>> loadMap(String mapName) {
        JsonReader reader = new JsonReader();
        JsonValue map;
        try {
            //attempt to load map from jar
            map = reader.parse(getClass().getClassLoader().getResourceAsStream("maps/" + mapName + ".json"));
        } catch (Exception e) {
            map = null;
        }
        if (map == null) {
            try {
                //attempt to load map from external maps dir
                map = reader.parse(new FileHandle(Paths.get("./maps/" + mapName + ".json").toFile()));
            } catch (Exception e) {
                throw new RuntimeException("Could not find or load map:" + mapName);
            }
        }

        width = map.get("width").asInt();
        height = map.get("height").asInt();
        //board = new Tile[width][height];

        JsonValue tileData = map.get("layers").get(0).get("data");

        // List<List<IntVector2>> spawnpoints = new LinkedList<>();
        Map<Integer, List<IntVector2>> teams = new TreeMap<>();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int type = tileData.get(i + (height - j - 1) * width).asInt();
                if (type > 100) {
                    //int team = type - 101; //teams starting at 0
                    if (teams.containsKey(type)) {
                        teams.get(type).add(new IntVector2(i, j));
                    } else {
                        teams.put(type, new LinkedList<>());
                        teams.get(type).add(new IntVector2(i, j));
                    }
                    //while (spawnpoints.size() <= team)
                    //    spawnpoints.add(new LinkedList<>()); //Increase list of spawnpoints as necessary
                    //spawnpoints.get(team).add(new IntVector2(i, j)); // Add current tile
                } else
                    switch (type) {
                        case 0:
                            break;
                        case 1:
                            //board[i][j] = new Tile(i, j, this, true);
                            break;
                        default:
                            //board[i][j] = new Tile(i, j, this, false);
                    }
            }
        }

        List<List<IntVector2>> spawns = new LinkedList<>(teams.values());

        return spawns;

    }
}
