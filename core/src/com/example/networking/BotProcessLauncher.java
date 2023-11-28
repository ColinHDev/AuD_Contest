package com.example.networking;

import com.example.manager.Manager;
import com.example.manager.player.Player;
import org.apache.commons.cli.*;

/**
 * Diese Klasse enthält die main-Methode, welche zum Start eines Bot-Prozesses verwendet wird.
 */
public class BotProcessLauncher {

    private static final Options cliOptions = new Options();

    static {
        cliOptions.addOption(Option
                .builder("?")
                .longOpt("help")
                .desc("Prints this list").build());

        cliOptions.addOption(Option
                .builder("p")
                .longOpt("player")
                .hasArg()
                .desc("Name of the bot class file without extension in format \"MyBot\" \n Attention: Case-sensitive!").build());

        cliOptions.addOption(Option
                .builder("host")
                .hasArg()
                .desc("Host of the Java RMI Remote Registry").build());

        cliOptions.addOption(Option
                .builder("port")
                .hasArg()
                .desc("Port of the Java RMI Remote Registry").build());

        cliOptions.addOption(Option
                .builder("reference")
                .hasArg()
                .desc("Name to associate with the Remote Reference in the Java RMI Remote Registry").build());
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

        if (!params.hasOption("p")) {
            System.err.println("Missing required option: -p");
            printHelp();
            return;
        }
        Class<? extends Player> playerClass = Manager.getPlayer(params.getOptionValue("p").trim(), false);

        String host = null;
        int port = ProcessPlayerHandler.registryPort;
        if (params.hasOption("host")) {
            host = params.getOptionValue("host").trim();
        }
        if (params.hasOption("port")) {
            try {
                port = Integer.parseInt(params.getOptionValue("port").trim());
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number: " + params.getOptionValue("port").trim());
                printHelp();
                return;
            }
        }
        if (!params.hasOption("reference")) {
            System.err.println("Missing required option: -reference");
            printHelp();
            return;
        }
        String remoteReferenceName = params.getOptionValue("reference").trim();;

        BotProcess botProcess;
        Object lock = new Object();
        synchronized (lock) {
            botProcess = new BotProcess(
                    (process) -> {
                        synchronized (lock) {
                            lock.notify();
                        }
                    },
                    playerClass, host, port, remoteReferenceName
            );
            botProcess.start();
            try {
                lock.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void printHelp() {
        String header = "\n\n";
        String footer = "\nPlease report issues at wettbewerb@acagamics.de";

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar example-1.0.0.jar", header, cliOptions, footer, true);
    }
}
