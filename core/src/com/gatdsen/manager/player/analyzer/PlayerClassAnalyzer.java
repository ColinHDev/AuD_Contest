package com.gatdsen.manager.player.analyzer;

import com.gatdsen.manager.Manager;
import com.gatdsen.manager.player.Player;
import com.gatdsen.simulation.Simulation;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse analysiert die Klasse eines Spielers, sodass sie für die Analyse der zugrunde liegenden
 * .class Datei verwendet werden kann.
 */
public final class PlayerClassAnalyzer {

    /**
     * Die Klassen, die nicht von Bots importiert werden dürfen.
     */
    private static final Class<?>[] ILLEGAL_CLASSES = {
            // gaTDsen Klassen
            Simulation.class, Manager.class,
            // Threading-Klassen
            Thread.class,
            // Exceptions
            ArrayIndexOutOfBoundsException.class
    };

    /**
     * Die Pakete, die nicht von Bots importiert werden dürfen.
     */
    private static final String[] ILLEGAL_PACKAGES = {
            // gaTDsen Pakete, die nicht von Bots importiert werden sollten
            "com.gatdsen.networking", "com.gatdsen.animation", "com.gatdsen.ui",
            // Threading-Pakete
            "java.util.concurrent",
            // Reflection-Pakete
            "java.lang.reflect",
            // Netzwerk-Pakete, wie die Socket-Klassen
            "java.net"
    };

    private final Class<? extends Player> playerClass;
    private final String[] classFileContent;

    /**
     * Erstellt einen neuen PlayerClassAnalyzer.
     *
     * @param playerClass Die Klasse des Spielers, die analysiert werden soll.
     */
    public PlayerClassAnalyzer(Class<? extends Player> playerClass) {
        this.playerClass = playerClass;
        classFileContent = getFileContentFromClass(playerClass);
    }

    /**
     * Gibt alle Imports zurück, die nicht von Spielern importiert werden dürfen.
     *
     * @return Die Imports, die nicht von Bots importiert werden dürfen.
     */
    public String[] getIllegalImports() {
        List<String> illegalImports = new ArrayList<>();
        String fileContent = String.join("\n", classFileContent);
        for (Class<?> illegalClass : ILLEGAL_CLASSES) {
            if (fileContent.contains(illegalClass.getName().replace(".", "/"))) {
                illegalImports.add(illegalClass.getName());
            }
        }
        for (String illegalPackage : ILLEGAL_PACKAGES) {
            if (fileContent.contains(illegalPackage.replace(".", "/"))) {
                illegalImports.add(illegalPackage);
            }
        }
        return illegalImports.toArray(new String[0]);
    }

    /**
     * Gibt den Seed-Modifier zurück, der für die gegebene Klasse verwendet werden soll.
     * Dieser wird zur Berechnung des Seeds für das Spiel verwendet, indem die Seed-Modifier aller Spieler addiert werden.
     * @return Der Seed-Modifier
     */
    public long getSeedModifier() {
        long seedModifier = 0;
        // Der Seed-Modifier ist die Summe der hashCode()s aller Zeilen der .class Datei
        for (String line : classFileContent) {
            seedModifier += line.hashCode();
        }
        return seedModifier;
    }

    /**
     * Gibt den Inhalt der gegebenen Klasse zugrunde liegenden .class Datei zurück.
     * @param class_ Die Klasse, deren .class Datei zurückgegeben werden soll
     * @return Der Inhalt der .class Datei
     */
    private static String[] getFileContentFromClass(Class<?> class_) {
        URL url = class_.getResource(class_.getSimpleName() + ".class");
        if (url == null) {
            return new String[0];
        }
        List<String> result = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                result.add(line);
            }
        } catch (IOException e) {
            return new String[0];
        }
        return result.toArray(new String[0]);
    }
}
