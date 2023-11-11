package com.example.manager;

import com.example.manager.command.Command;
import com.example.manager.command.EndTurnCommand;
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
            handler.create(state);
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

            Player currentPlayer = players[currentPlayerIndex];
            GameState stateCopy = state.copy();
            Controller controller = new Controller(this, gcController, stateCopy, currentPlayer.getType() == Player.PlayerType.Human ? HUMAN_CONTROLLER_USES : AI_CONTROLLER_USES);

            executor.waitForCompletion();
            Thread futureExecutor;
            Future<?> future;
            switch (currentPlayer.getType()) {
                case Human:
                    future = executor.execute(() -> {
                        Thread.currentThread().setName("Run_Thread_Player_Human");
                        simulation.setTurnTimer(new Timer(1000 * HUMAN_EXECUTION_TIMEOUT));
                        currentPlayer.executeTurn(stateCopy, controller);
                    });
                    futureExecutor = new Thread(() -> {
                        inputGenerator.activateTurn((HumanPlayer) currentPlayer);
                        try {
                            Thread.currentThread().setName("Future_Executor_Player_Human");
                            if (isDebug) future.get();
                            else
                                future.get(HUMAN_EXECUTION_TIMEOUT + HUMAN_EXECUTION_GRACE_PERIODE, TimeUnit.MILLISECONDS);
                        } catch (InterruptedException e) {
                            future.cancel(true);//Executor was interrupted: Interrupt Player
                            System.out.println("bot was interrupted");
                            e.printStackTrace(System.err);
                        } catch (ExecutionException e) {
                            System.err.println("human player failed with exception: " + e.getCause());
                            e.printStackTrace();
                        } catch (TimeoutException e) {
                            future.cancel(true);
                            executor.forceStop();
                            System.err.println("player" + currentPlayerIndex + "(" + currentPlayer.getName() + ") computation surpassed timeout");
                        }
                        inputGenerator.endTurn();
                        //Add Empty command to break command Execution
                        try {
                            commandQueue.put(new EndTurnCommand(gcController));
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    break;
                case AI:
                    future = executor.execute(() -> {
                        Thread.currentThread().setName("Run_Thread_Player_" + currentPlayer.getName());
                        simulation.setTurnTimer(new Timer(1000 * AI_EXECUTION_TIMEOUT));
                        currentPlayer.executeTurn(stateCopy, controller);
                    });
                    futureExecutor = new Thread(() -> {
                        Thread.currentThread().setName("Future_Executor_Player_" + currentPlayer.getName());
                        try {
                            if (isDebug) future.get();
                            else
                                future.get(AI_EXECUTION_TIMEOUT + AI_EXECUTION_GRACE_PERIODE, TimeUnit.MILLISECONDS);
                        } catch (InterruptedException e) {
                            future.cancel(true);//Executor was interrupted: Interrupt Bot
                            System.out.println("bot was interrupted");
                            e.printStackTrace(System.err);
                        } catch (ExecutionException e) {
                            System.out.println("bot failed with exception: " + e.getCause());
                            e.printStackTrace();
                            System.err.println("The failed player has been penalized!");
                            simulation.penalizeCurrentPlayer();
                        } catch (TimeoutException e) {
                            future.cancel(true);
                            executor.forceStop();

                            System.out.println("player" + currentPlayerIndex + "(" + currentPlayer.getName() + ") computation surpassed timeout");
                            System.err.println("The failed player has been penalized!");
                            simulation.penalizeCurrentPlayer();
                        }
                        //Add Empty command to break command Execution
                        try {
                            commandQueue.put(new EndTurnCommand(gcController));
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    break;
                default:
                    throw new IllegalStateException("Player of type: " + currentPlayer.getType() + " can not be executed by the Manager");
            }

            futureExecutor.start();
            ActionLog log = simulation.clearAndReturnActionLog();
            if (saveReplay)
                gameResults.addActionLog(log);
            if (gui && currentPlayer.getType() == Player.PlayerType.Human) {
                //Contains Action produced by entering new turn
                animationLogProcessor.animate(log);
            }
            try {
                while (true) {

                    Command nextCmd = commandQueue.take();
                    if (nextCmd.endsTurn()) break;
                    //Contains action produced by the commands execution
                    log = nextCmd.run();
                    if (log == null) continue;
                    if (saveReplay)
                        gameResults.addActionLog(log);
                    if (gui) {
                        animationLogProcessor.animate(log);
                        //animationLogProcessor.awaitNotification(); ToDo: discuss synchronisation for human players
                    }
                }
            } catch (InterruptedException e) {
                System.err.println("Interrupted while processing cmds");
                e.printStackTrace(System.err);
                if (pendingShutdown) {
                    futureExecutor.interrupt();
                    break;
                }
                throw new RuntimeException(e);
            }
            controller.deactivate();

            //Contains actions produced by ending the turn (after last command is executed)
            ActionLog finalLog = simulation.endTurn();
            if (saveReplay)
                gameResults.addActionLog(finalLog);
            if (gui) {
                animationLogProcessor.animate(finalLog);
                animationLogProcessor.awaitNotification();
            }
            if (pendingShutdown) {
                executor.shutdown();
                futureExecutor.interrupt();
                break;
            }
            try {
                futureExecutor.join(); //Wait for the executor to shutdown to prevent spamming the executor service
            } catch (InterruptedException e) {
                System.out.print("Interrupted while shutting down future executor\n");
                e.printStackTrace(System.err);
                if (pendingShutdown) {
                    futureExecutor.interrupt();
                    break;
                }
                throw new RuntimeException(e);
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
