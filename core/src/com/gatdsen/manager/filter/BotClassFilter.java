package com.gatdsen.manager.filter;

import com.gatdsen.manager.Manager;
import com.gatdsen.manager.player.Bot;
import com.gatdsen.simulation.Simulation;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class BotClassFilter {

    private BotClassFilter() {
    }

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

            "java.lang.reflect",

            "java.net"
    };

    public static String[] getIllegalImports(Class<? extends Bot> bot) {
        List<String> illegalImports = new ArrayList<>();
        String botFileContent = getFileContentFromClass(bot);
        for (Class<?> illegalClass : ILLEGAL_CLASSES) {
            if (botFileContent.contains(illegalClass.getName().replace(".", "/"))) {
                illegalImports.add(illegalClass.getName());
            }
        }
        for (String illegalPackage : ILLEGAL_PACKAGES) {
            if (botFileContent.contains(illegalPackage.replace(".", "/"))) {
                illegalImports.add(illegalPackage);
            }
        }
        return illegalImports.toArray(new String[0]);
    }

    public static String getFileContentFromClass(Class<?> class_) {
        URL inputStream = class_.getResource(class_.getSimpleName() + ".class");
        if (inputStream == null) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream.openStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line).append("\n");
            }
        } catch (IOException e) {
            return "";
        }
        return result.toString();
    }
}
