package com.gatdsen.manager.map;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

public final class MapRetriever {

    private static final String MAP_FILE_EXTENSION = ".json";
    private static final String INTERNAL_MAP_DIRECTORY = "/maps/";
    private static final String EXTERNAL_MAP_DIRECTORY = "maps/";

    static {
        File externalMapDirectory = new File(EXTERNAL_MAP_DIRECTORY);
        if (!externalMapDirectory.isDirectory()) {
            System.err.println("Warning: No map directory found at " + externalMapDirectory.getAbsolutePath());
        }
    }

    private static final MapRetriever instance = new MapRetriever();

    public static MapRetriever getInstance() {
        return instance;
    }

    private Map[] maps;

    private MapRetriever() {
        Map[] internalMaps = getInternalMaps();
        Map[] externalMaps = getExternalMaps();

        maps = new Map[internalMaps.length + externalMaps.length];
        System.arraycopy(internalMaps, 0, maps, 0, internalMaps.length);
        System.arraycopy(externalMaps, 0, maps, internalMaps.length, externalMaps.length);

        if (maps.length == 0) {
            maps = new Map[]{new Map("No maps could be loaded", 0)};
        }
    }

    public Map[] getMaps() {
        return maps;
    }

    public String[] getMapNames() {
        String[] mapNames = new String[maps.length];
        for (int i = 0; i < maps.length; i++) {
            mapNames[i] = maps[i].getName();
        }
        return mapNames;
    }

    private Map[] getInternalMaps() {
        URI uri;
        try {
            uri = Objects.requireNonNull(getClass().getResource(INTERNAL_MAP_DIRECTORY)).toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        Path mapDirectory;
        FileSystem fileSystem = null;
        try {
            mapDirectory = Paths.get(uri);
        } catch (FileSystemNotFoundException e) {
            try {
                fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            mapDirectory = fileSystem.getPath(INTERNAL_MAP_DIRECTORY);
        }
        Stream<Path> mapStream = null;
        try {
            mapStream = Files.list(mapDirectory);
            return mapStream
                    .filter(path -> path.getFileName().toString().endsWith(MAP_FILE_EXTENSION))
                    .map(path -> createGameMap(path.getFileName().toString(), getClass().getResourceAsStream(INTERNAL_MAP_DIRECTORY + path.getFileName())))
                    .toArray(Map[]::new);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (fileSystem != null) {
                try {
                    fileSystem.close();
                } catch (IOException ignored) {
                }
            }
            if (mapStream != null) {
                mapStream.close();
            }
        }
    }

    private Map[] getExternalMaps() {
        File[] files = new File(EXTERNAL_MAP_DIRECTORY).listFiles();
        if (files == null) {
            return new Map[0];
        }
        return Arrays.stream(files)
                .filter(file -> file.getName().endsWith(MAP_FILE_EXTENSION))
                .map(file -> {
                    try {
                        return createGameMap(file.getName(), new FileInputStream(file));
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toArray(Map[]::new);
    }

    private Map createGameMap(String fileName, InputStream fileStream) {
        JsonValue map = readJsonValueFromInputStream(fileStream);

        int width = map.get("width").asInt();
        int height = map.get("height").asInt();

        JsonValue tileData = map.get("layers").get(0).get("data");
        HashMap<Integer,Integer> teams=new LinkedHashMap<>();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int type = tileData.get(i + (height - j - 1) * width).asInt();
                if (type > 100) {
                    if(teams.containsKey(type)){
                        teams.replace(type,teams.get(type)+1);
                    }
                    else{teams.put(type,1);}
                }
            }
        }

        ArrayList<Integer> spawnpoints = new ArrayList<>(teams.values());
        //get lowest number of spawnpoints
        spawnpoints.sort(Comparator.naturalOrder());
        if (spawnpoints.isEmpty()) {
            spawnpoints.add(0);
        }
        return new Map(fileName.replace(MAP_FILE_EXTENSION, ""), spawnpoints.get(0), spawnpoints.size());
    }

    private JsonValue readJsonValueFromInputStream(InputStream inputStream) {
        return new JsonReader().parse(inputStream);
    }
}
