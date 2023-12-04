package com.gatdsen.manager;

import com.gatdsen.manager.command.Command;
import com.gatdsen.manager.command.CommandHandler;
import com.gatdsen.manager.concurrent.ThreadExecutor;
import com.gatdsen.manager.player.Player;
import com.gatdsen.manager.player.PlayerHandler;
import com.gatdsen.simulation.GameState;
import org.lwjgl.Sys;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;

public final class LocalPlayerHandler extends PlayerHandler {

    private final ThreadExecutor executor = new ThreadExecutor();
    private final PlayerThread playerThread = new PlayerThread();
    private final InputProcessor inputGenerator;

    public LocalPlayerHandler(Class<? extends Player> playerClass, InputProcessor inputGenerator) {
        super(playerClass);
        this.inputGenerator = inputGenerator;
    }

    @Override
    public Future<?> create(CommandHandler commandHandler) {
        return executor.execute(() -> {
            BlockingQueue<Command> commands = playerThread.create(playerClass, inputGenerator);
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
    public Future<?> init(GameState gameState, boolean isDebug, long seed, CommandHandler commandHandler) {
        return executor.execute(() -> {
            BlockingQueue<Command> commands = playerThread.init(gameState, isDebug, seed);
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
    protected Future<?> onExecuteTurn(GameState gameState, CommandHandler commandHandler) {
        return executor.execute(() -> {
            System.out.println("onExecuteTurn()");
            BlockingQueue<Command> commands = playerThread.executeTurn(gameState);
            System.out.println("onExecuteTurn() - got commands");
            Command command;
            do {
                try {
                    System.out.println("onExecuteTurn() - waiting for command");
                    command = commands.take();
                    System.out.println("onExecuteTurn() - got command");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                commandHandler.handleCommand(command);
            } while (!command.endsTurn());
            System.out.println("onExecuteTurn() - done");
        });
    }

    @Override
    public void dispose() {
        executor.interrupt();
        playerThread.dispose();
    }
}
