package com.gatdsen.manager;

import com.gatdsen.manager.command.Command;
import com.gatdsen.manager.player.Bot;
import com.gatdsen.manager.player.Player;
import com.gatdsen.manager.player.PlayerHandler;
import com.gatdsen.manager.player.data.PlayerInformation;
import com.gatdsen.networking.ProcessPlayerHandler;
import com.gatdsen.simulation.PlayerController;
import com.gatdsen.simulation.GameState;
import com.gatdsen.simulation.PlayerState;
import com.gatdsen.simulation.Simulation;
import com.gatdsen.simulation.action.ActionLog;
import com.gatdsen.simulation.campaign.CampaignResources;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class Game extends Executable {

    private static final long BASE_SEED = 345342624;

    private static final AtomicInteger gameNumber = new AtomicInteger(0);
    private static final boolean isDebug;

    static {
        isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().contains("-agentlib:jdwp");
        if (isDebug) System.err.println("Warning: Debugger engaged; Disabling Bot-Timeout!");
    }

    protected final Object schedulingLock = new Object();

    private GameResults gameResults;
    private Simulation simulation;
    private GameState state;
    private PlayerHandler[] playerHandlers;

    private long seed = BASE_SEED;

    private float[] scores;

    private Thread simulationThread;

    protected Game(GameConfig config) {
        super(config);
        gameResults = new GameResults(config);
        gameResults.setStatus(getStatus());
    }

    private void create() {
        simulation = new Simulation(config.gameMode, config.mapName, config.playerCount);
        state = simulation.getState();
        if (saveReplay)
            gameResults.setInitialState(state);

        playerHandlers = new PlayerHandler[config.playerCount];
        Future<?>[] futures = new Future[playerHandlers.length];
        for (int playerIndex = 0; playerIndex < config.playerCount; playerIndex++) {
            PlayerHandler playerHandler;
            Class<? extends Player> playerClass = config.players[playerIndex];
            if (Bot.class.isAssignableFrom(playerClass)) {
                playerHandler = new ProcessPlayerHandler(playerClass, gameNumber.get(), playerIndex);
            } else {
                if (!gui) {
                    throw new RuntimeException("HumanPlayers can't be used without GUI to capture inputs");
                }
                playerHandler = new LocalPlayerHandler(playerClass, playerIndex, inputGenerator);
            }

            playerHandlers[playerIndex] = playerHandler;
            playerHandler.setPlayerController(simulation.getController(playerIndex));
            futures[playerIndex] = playerHandler.create(command -> command.run(playerHandler));
        }
        awaitFutures(futures);
        for (PlayerHandler playerHandler : playerHandlers) {
            seed += playerHandler.getSeedModifier();
        }
        for (int playerIndex = 0; playerIndex < config.playerCount; playerIndex++) {
            PlayerHandler playerHandler = playerHandlers[playerIndex];
            futures[playerIndex] = playerHandler.init(state, isDebug, seed, command -> command.run(playerHandler));
        }
        awaitFutures(futures);
        gameResults.setPlayerNames(getPlayerNames());
        config = null;
    }

    public void start() {
        synchronized (schedulingLock) {
            if (getStatus() == Status.ABORTED) return;
            setStatus(Status.ACTIVE);
            gameNumber.getAndIncrement();
            create();
            //Init the Log Processor
            if (gui) animationLogProcessor.init(state.copy(), getPlayerNames(), new String[][]{});
            //Run the Game
            simulationThread = new Thread(this::run);
            simulationThread.setName("Game_Simulation_Thread");
            simulationThread.setUncaughtExceptionHandler(this::crashHandler);
            simulationThread.start();
        }
    }

    @Override
    protected void setStatus(Status newStatus) {
        super.setStatus(newStatus);
        if (gameResults!= null) gameResults.setStatus(newStatus);
    }

    /**
     * @return The state of the underlying simulation
     */
    public GameState getState() {
        return state;
    }

    /**
     * Controls Player Execution
     */
    private void run() {
        Thread.currentThread().setName("Game_Thread_" + gameNumber.get());
        while (!pendingShutdown && state.isActive()) {
            synchronized (schedulingLock) {
                if (getStatus() == Status.PAUSED)
                    try {
                        schedulingLock.wait();

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
            }

            PlayerState[] playerStates = state.getPlayerStates();
            Future<?>[] futures = new Future[playerHandlers.length];
            for (int playerIndex = 0; playerIndex < playerHandlers.length; playerIndex++) {
                // Wenn der PlayerState des Spielers deaktiviert ist, da er bspw. keine Leben mehr hat oder
                // disqualifiziert wurde, wird der Spieler übersprungen und dessen executeTurn() nicht aufgerufen.
                if (playerStates[playerIndex].isDeactivated()) {
                    continue;
                }

                ActionLog firstLog = simulation.clearAndReturnActionLog();
                if (saveReplay)
                    gameResults.addActionLog(firstLog);
                if (gui) {
                    animationLogProcessor.animate(firstLog);
                }

                PlayerHandler playerHandler = playerHandlers[playerIndex];
                playerHandler.setPlayerController(simulation.getController(playerIndex));
                futures[playerIndex] = playerHandler.executeTurn(
                        state,
                        (Command command) -> {
                            // Contains action produced by the commands execution
                            ActionLog log = command.run(playerHandler);
                            if (log == null) {
                                return;
                            }
                            if (saveReplay) {
                                gameResults.addActionLog(log);
                            }
                            if (gui) {
                                animationLogProcessor.animate(log);
                                // ToDo: discuss synchronisation for human players
                                // animationLogProcessor.awaitNotification();
                            }
                        }
                );
                ActionLog log = simulation.clearAndReturnActionLog();
                if (saveReplay) {
                    gameResults.addActionLog(log);
                }
                if (gui && playerHandler.isHumanPlayer()) {
                    //Contains Action produced by entering new turn
                    animationLogProcessor.animate(log);
                }
            }
            awaitFutures(futures);
            if (inputGenerator != null) {
                inputGenerator.endTurn();
            }
            //Contains actions produced by ending the turn (after last command is executed)
            ActionLog finalLog = simulation.endTurn();
            if (saveReplay) {
                gameResults.addActionLog(finalLog);
            }
            if (gui) {
                animationLogProcessor.animate(finalLog);
                animationLogProcessor.awaitNotification();
            }
        }
        scores = state.getHealth();
        setStatus(Status.COMPLETED);
        for (CompletionHandler<Executable> completionListener : completionListeners) {
            completionListener.onComplete(this);
        }
    }

    @Override
    public void dispose() {
        //Shutdown all running threads
        super.dispose();
        if (simulationThread != null) {
            simulationThread.interrupt();
        }
        if (state!=null) scores = state.getHealth();
        simulation = null;
        state = null;
        simulationThread = null;
        gameResults = null;
        for (PlayerHandler playerHandler : playerHandlers) {
            playerHandler.dispose();
        }
    }

    protected String[] getPlayerNames() {
        String[] names = new String[playerHandlers.length];
        for (int i = 0; i < playerHandlers.length; i++) {
            PlayerInformation information = playerHandlers[i].getPlayerInformation();
            names[i] = information != null ? information.getName() : "Player " + i;
        }
        return names;
    }

    private void awaitFutures(Future<?>[] futures) {
        for (Future<?> future : futures) {
            if (future == null) {
                continue;
            }
            try {
                future.get();
            } catch (InterruptedException|ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public float[] getScores() {
        return scores;
    }

    public boolean shouldSaveReplay() {
        return super.saveReplay;
    }

    public GameResults getGameResults() {
        return gameResults;
    }

    @Override
    public String toString() {
        return "Game{" +
                "status=" + getStatus() +
                ", completionListeners=" + super.completionListeners +
                ", inputGenerator=" + inputGenerator +
                ", animationLogProcessor=" + animationLogProcessor +
                ", gui=" + gui +
                ", gameResults=" + gameResults +
                ", simulation=" + simulation +
                ", state=" + state +
                /*", players=" + Arrays.toString(players) +*/
                ", simulationThread=" + simulationThread +
                ", uiMessenger=" + uiMessenger +
                ", pendingShutdown=" + pendingShutdown +
                ", config=" + config +
                '}';
    }
}
