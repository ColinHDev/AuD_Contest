package com.example.networking;

import com.example.manager.Manager;
import com.example.manager.Player;
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
        Class<? extends Player> player = Manager.getPlayer(params.getOptionValue("p").trim(), false);

        BotProcess botProcess;
        Object lock = new Object();
        synchronized (lock) {
            botProcess = new BotProcess(
                    (process) -> {
                        synchronized (lock) {
                            lock.notify();
                        }
                    },
                    player
            );
            botProcess.start();
            try {
                lock.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        botProcess.dispose();
    }

    private static void printHelp() {
        String header = "\n\n";
        String footer = "\nPlease report issues at wettbewerb@acagamics.de";

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar example-1.0.0.jar", header, cliOptions, footer, true);
    }
}
