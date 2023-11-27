package com.example.manager;

import com.example.manager.command.Command;
import com.example.manager.concurrent.ThreadExecutor;
import com.example.manager.player.*;
import com.example.networking.ProcessPlayerHandler;
import com.example.simulation.GameCharacterController;
import com.example.simulation.GameState;
import com.example.simulation.Simulation;
import com.example.simulation.action.ActionLog;
import com.example.simulation.campaign.CampaignResources;

import java.util.Arrays;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Game extends Executable {

    protected final Object schedulingLock = new Object();
    private static final int AI_EXECUTION_TIMEOUT = 500;
    private static final int AI_EXECUTION_GRACE_PERIODE = 100;
    private static final int AI_INIT_TIMEOUT = 1000;
    private static final int AI_CONTROLLER_USES = 200;

    private static final int HUMAN_EXECUTION_TIMEOUT = 30000;
    private static final int HUMAN_EXECUTION_GRACE_PERIODE = 5000;
    private static final int HUMAN_INIT_TIMEOUT = 30000;
    private static final int HUMAN_CONTROLLER_USES = 100000;

    private static final boolean isDebug;

    static {
        isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().contains("-agentlib:jdwp");
        if (isDebug) System.err.println("Warning: Debugger engaged; Disabling Bot-Timeout!");
    }

    private GameResults gameResults;
    private Simulation simulation;
    private GameState state;
    private PlayerHandler[] playerHandlers;

    private float[] scores;

    private static final AtomicInteger gameNumber = new AtomicInteger(0);

    private ThreadExecutor executor;

    private final BlockingQueue<Command> commandQueue = new ArrayBlockingQueue<>(256);
    private Thread simulationThread;


    protected Game(GameConfig config) {
        super(config);
        if (config.gameMode == GameState.GameMode.Campaign) {
            if (config.players.size() != 1) {
                System.err.println("Campaign only accepts exactly 1 player");
                setStatus(Status.ABORTED);
            }
            config.players.addAll(CampaignResources.getEnemies(config.mapName));
            config.teamCount = config.players.size();
        }
        gameResults = new GameResults(config);
        gameResults.setStatus(getStatus());
    }

    private void create() {

        simulation = new Simulation(config.gameMode, config.mapName, config.teamCount);
        state = simulation.getState();
        if (saveReplay)
            gameResults.setInitialState(state);

        playerHandlers = new PlayerHandler[config.teamCount];

        for (int i = 0; i < config.teamCount; i++) {
            PlayerHandler handler;
            Class<? extends Player> playerClass = config.players.get(i);
            if (playerClass.isInstance(Bot.class)) {
                handler = new ProcessPlayerHandler(playerClass);
            } else {
                if (!gui) {
                    throw new RuntimeException("HumanPlayers can't be used without GUI to capture inputs");
                }
                handler = new LocalPlayerHandler(playerClass);
            }
            playerHandlers[i] = handler;
            handler.init(state, isDebug);
        }
        gameResults.setPlayerNames(getPlayerNames());
        config = null;
    }

    public void start() {
        synchronized (schedulingLock) {
            if (getStatus() == Status.ABORTED) return;
            setStatus(Status.ACTIVE);
            executor = new ThreadExecutor();
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
        Thread.currentThread().setName("Game_Thread_" + gameNumber.getAndIncrement());
        while (!pendingShutdown && state.isActive()) {
            synchronized (schedulingLock) {
                if (getStatus() == Status.PAUSED)
                    try {
                        schedulingLock.wait();

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
            }

            ActionLog firstLog = simulation.clearAndReturnActionLog();
            if (saveReplay)
                gameResults.addActionLog(firstLog);
            if (gui) {
                animationLogProcessor.animate(firstLog);
            }

            GameCharacterController gcController = simulation.getController();
            int currentPlayerIndex = gcController.getTeam();

            // TODO: executor.waitForCompletion();
            PlayerHandler playerHandler = playerHandlers[currentPlayerIndex];
            playerHandler.executeTurn(
                    state,
                    (Command command) -> {
                        // Contains action produced by the commands execution
                        ActionLog log = command.run(gcController);
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
                        if (!command.endsTurn()) {
                            return;
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
            );

            // TODO: futureExecutor.start();
            ActionLog log = simulation.clearAndReturnActionLog();
            if (saveReplay) {
                gameResults.addActionLog(log);
            }
            if (gui && playerHandler.isHumanPlayer()) {
                //Contains Action produced by entering new turn
                animationLogProcessor.animate(log);
            }
        }
        scores = state.getScores();
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
        if (executor!= null)
            executor.shutdown();
        if (state!=null) scores = state.getScores();
        simulation = null;
        state = null;
        executor = null;
        simulationThread = null;
        gameResults = null;
    }

    protected void queueCommand(Command cmd) {
        commandQueue.add(cmd);
    }

    protected String[] getPlayerNames() {
        String[] names = new String[players.length];
        int i = 0;
        for (Player p : players) {
            names[i] = p.getName();
            i++;
        }
        return names;

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
                ", players=" + Arrays.toString(players) +
                ", executor=" + executor +
                ", commandQueue=" + commandQueue +
                ", simulationThread=" + simulationThread +
                ", uiMessenger=" + uiMessenger +
                ", pendingShutdown=" + pendingShutdown +
                ", config=" + config +
                '}';
    }
}
