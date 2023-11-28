package com.example.manager;

import com.example.manager.command.Command;
import com.example.manager.command.CommandHandler;
import com.example.manager.concurrent.ThreadExecutor;
import com.example.manager.player.Bot;
import com.example.manager.player.HumanPlayer;
import com.example.manager.player.Player;
import com.example.manager.player.PlayerHandler;
import com.example.simulation.GameState;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;

public final class LocalPlayerHandler implements PlayerHandler {

    private final ThreadExecutor executor = new ThreadExecutor();

    private final Class<? extends Player> playerClass;
    private PlayerThread playerThread;

    public LocalPlayerHandler(Class<? extends Player> playerClass) {
        this.playerClass = playerClass;
    }

    @Override
    public boolean isHumanPlayer() {
        return HumanPlayer.class.isAssignableFrom(playerClass);
    }

    @Override
    public boolean isBotPlayer() {
        return Bot.class.isAssignableFrom(playerClass);
    }

    @Override
    public Future<?> init(GameState gameState, boolean isDebug, long seed, CommandHandler commandHandler) {
        playerThread = new PlayerThread(playerClass, isDebug);
        return executor.execute(() -> {
            BlockingQueue<Command> commands = playerThread.init(gameState, seed);
            Command command;
            do {
                try {
                    command = commands.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                commandHandler.handleCommand(command);
            } while (!command.endsTurn());
        });
    }

    @Override
    public Future<?> executeTurn(GameState gameState, CommandHandler commandHandler) {
        return executor.execute(() -> {
            BlockingQueue<Command> commands = playerThread.executeTurn(gameState);
            Command command;
            do {
                try {
                    command = commands.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                commandHandler.handleCommand(command);
            } while (!command.endsTurn());
        });
    }

    @Override
    public void dispose() {
        executor.interrupt();
        playerThread.dispose();
    }
}
