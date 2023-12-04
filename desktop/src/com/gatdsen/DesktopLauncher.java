package com.gatdsen;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.gatdsen.manager.*;
import com.gatdsen.manager.player.Bot;
import com.gatdsen.manager.player.Player;
import com.gatdsen.simulation.GameState;
import com.gatdsen.ui.GADS;
import org.apache.commons.cli.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {

    private static final Options cliOptions = new Options();

    static {

        cliOptions.addOption(Option
                .builder("?")
                .longOpt("help")
                .desc("Prints this list").build());

        cliOptions.addOption(Option
                .builder("m")
                .longOpt("map")
                .hasArg()
                .desc("(Required for -n) String name of the map without extension").build());

        cliOptions.addOption(Option
                .builder("p")
                .longOpt("players")
                .hasArg()
                .desc("(Required for -n) Names of the bots class files without extension in format \"Bot1 Bot2 Bot3\" \n Attention: Case-sensitive!").build());

        cliOptions.addOption(Option
                .builder("g")
                .longOpt("gamemode")
                .hasArg()
                .type(Number.class)
                .desc("GameMode to be played (Default: 0)\n" +
                        "  0 - Normal\n" +
                        "  1 - Campaign\n" +
                        "  2 - Exam Admission\n" +
                        "  3 - Tournament: Phase 1\n" +
                        "  4 - Tournament: Phase 2").build());

        cliOptions.addOption(Option.builder("n")
                .longOpt("nogui")
                .desc("Runs the simulation without animation").build());

        cliOptions.addOption(Option
                .builder("k")
                .longOpt("key")
                .hasArg()
                .desc("When printing results, they will be encased by the given key, ensuring authenticity").build());

        cliOptions.addOption(Option
                .builder("r")
                .longOpt("replay")
                .desc("Saves replay and results of the matches (WIP)").build());

    }

    public static void main(String[] args) {

        CommandLineParser parser = new DefaultParser();
        CommandLine params;
        try {
            for (String arg : args
            ) {
                if (arg.equals("-?") || arg.equals("--help")) {
                    printHelp();
                    return;
                }
            }

            params = parser.parse(cliOptions, args);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            printHelp();
            return;
        }
        RunConfiguration runConfig = new RunConfiguration();
        runConfig.gui = !params.hasOption("n");
        runConfig.mapName = params.getOptionValue("m", null);
        if (params.hasOption("p"))
            runConfig.players = Manager.getPlayers(params.getOptionValue("p").trim().split("\\s+"), !runConfig.gui);
        if (params.hasOption("r"))
            runConfig.replay = true;
        int gameMode = Integer.parseInt(params.getOptionValue("g", "0"));
        if (gameMode < 0 || gameMode >= GameState.GameMode.values().length) {
            System.err.println("Valid GameModes range from 0 to 4");
            printHelp();
            return;
        }
        runConfig.gameMode = GameState.GameMode.values()[gameMode];
        if (runConfig.gameMode == GameState.GameMode.Tournament_Phase_1)
            runConfig.teamCount = 4;
        if (runConfig.gui) {
            if(runConfig.players!=null) runConfig.teamCount = runConfig.players.size();
            Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
            config.setForegroundFPS(60);
            config.setTitle("Gadsen: Tower Defense");
            config.setWindowIcon(Files.FileType.Classpath, "icon/icon.png");
            new Lwjgl3Application(new GADS(runConfig), config);
        } else {
            Manager.setSystemReservedProcessorCount(1);
            boolean invalidConfig = false;
            if (runConfig.gameMode != GameState.GameMode.Exam_Admission && runConfig.mapName == null) {
                System.err.println("Param -m is required for no GUI mode (except Exam Admission)");
                invalidConfig = true;
            }
            if (runConfig.players == null) {
                System.err.println("Param -p is required for no GUI mode");
                invalidConfig = true;
            } else if (runConfig.gameMode == GameState.GameMode.Campaign){
                if( runConfig.players.size() != 1) {
                    System.err.println("The Campaign can only be played by exactly one player");
                    invalidConfig = true;
                }

            }  else if (runConfig.gameMode == GameState.GameMode.Exam_Admission){
                if( runConfig.players.size() != 1) {
                    System.err.println("The Exam Admission can only be played by exactly one player");
                    invalidConfig = true;
                }
            }else if (runConfig.players.size() < 2) {
                System.err.println("At least two players are required");
                invalidConfig = true;
            }
            if (invalidConfig) {
                printHelp();
                return;
            }
            Manager manager = Manager.getManager();
            Run run = manager.startRun(runConfig);
            Object lock = new Object();
            synchronized (lock) {
                run.addCompletionListener((tmp) -> {
                    synchronized (lock) {
                        lock.notify();
                    }
                });
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            printResults(run, params.getOptionValue("k", ""));
        }
    }

    private static void printResults(Run run, String key) {
        StringBuilder builder = new StringBuilder();
        if (key != null && key.length() > 0) builder.append("<").append(key).append(">");
        switch (run.getGameMode()) {
            case Normal:
            case Tournament_Phase_1:
                builder.append("\nScores:\n");
                int i = 0;
                for (Class<? extends Player> cur : run.getPlayers()) {
                    String name = "";
                    int matrikel = 0;
                    if (Bot.class.isAssignableFrom(cur))
                        try {
                            Bot player = (Bot) cur.getDeclaredConstructors()[0].newInstance();
                            name = player.getStudentName();
                            matrikel = player.getMatrikel();
                        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                            System.err.println("Unable to fetch Player instance");
                        }
                    builder.append(String.format("%-10s (%s, %d) :  %-6f%n", cur.getName(), name, matrikel, run.getScores()[i++]));
                }
                builder.append("\n");
                break;
            case Campaign:
                if (run.getScores()[0] > 0) builder.append("passed");
                else builder.append("failed");
                break;
            case Exam_Admission:
                StringBuilder scoreBuilder = new StringBuilder();
                scoreBuilder.append("\nScores:\n");
                int j = 0;
                for (Class<? extends Player> cur : run.getPlayers()) {
                    String name = "";
                    int matrikel = 0;
                    if (Bot.class.isAssignableFrom(cur))
                    try {
                        Bot player = (Bot) cur.getDeclaredConstructors()[0].newInstance();
                        name = player.getStudentName();
                        matrikel = player.getMatrikel();
                    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                        System.err.println("Unable to fetch Player instance");
                    }
                    scoreBuilder.append(String.format("%-10s (%-10s, %-6d) :  %-6f%n", cur.getName(), name, matrikel, run.getScores()[j++]));
                }
                System.out.println(scoreBuilder);
                if (run.getScores()[0] >= 420) builder.append("passed");
                else builder.append("failed");
                break;
            default:
                builder.append(Arrays.toString(run.getPlayers().toArray()));
                builder.append("\n");
                builder.append(Arrays.toString(run.getScores()));
        }
        if (key != null && key.length() > 0) builder.append("<").append(key).append(">");
        System.out.println(builder.toString());
    }

    private static void printHelp() {
        String header = "\n\n";
        String footer = "\nPlease report issues at wettbewerb@acagamics.de";

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar example-1.0.0.jar", header, cliOptions, footer, true);
    }
}
