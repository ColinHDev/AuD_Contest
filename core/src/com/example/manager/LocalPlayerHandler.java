package com.example.manager;

import com.example.manager.command.Command;
import com.example.manager.command.CommandHandler;
import com.example.manager.player.Bot;
import com.example.manager.player.HumanPlayer;
import com.example.manager.player.Player;
import com.example.manager.player.PlayerHandler;
import com.example.simulation.GameState;

import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public final class LocalPlayerHandler implements PlayerHandler {

    private final Class<? extends Player> playerClass;
    private PlayerThread playerThread;

    public LocalPlayerHandler(Class<? extends Player> playerClass) {
        this.playerClass = playerClass;
    }

    @Override
    public boolean isHumanPlayer() {
        return playerClass.isInstance(HumanPlayer.class);
    }

    @Override
    public boolean isBotPlayer() {
        return playerClass.isInstance(Bot.class);
    }

    @Override
    public Future<?> init(GameState gameState, boolean isDebug) {
        playerThread = new PlayerThread(playerClass, isDebug);
        return new FutureTask<>(
                () -> playerThread.init(gameState),
                null
        );
    }

    @Override
    public Future<?> executeTurn(GameState gameState, CommandHandler commandHandler) {
        return new FutureTask<>(
                () -> {
                    Controller controller = playerThread.executeTurn(gameState);
                    Command command;
                    do {
                        try {
                            command = controller.commands.take();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        commandHandler.handleCommand(command);
                    } while (!command.endsTurn());
                },
                null
        );
    }

    @Override
    public void dispose() {

    }
}
