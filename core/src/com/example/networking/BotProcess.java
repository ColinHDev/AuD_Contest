package com.example.networking;

import com.example.manager.CompletionHandler;
import com.example.manager.Player;

/**
 * Diese Klasse repräsentiert den Prozess, auf welchem der Bot eines Spielers ausgeführt wird.
 */
public class BotProcess {

    private CompletionHandler<BotProcess> completionListener;
    private Class<? extends Player> playerClass;

    public BotProcess(CompletionHandler<BotProcess> completionListener, Class<? extends Player> playerClass) {
        this.completionListener = completionListener;
        this.playerClass = playerClass;
    }

    public void start() {
    }

    protected void complete() {
        completionListener.onComplete(this);
        completionListener = null;
    }

    public void dispose() {
        completionListener = null;
    }
}
