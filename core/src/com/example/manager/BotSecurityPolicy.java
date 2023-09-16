package com.example.manager;

import java.security.*;

@SuppressWarnings("removal")
public class BotSecurityPolicy extends Policy {

    @Override
    public boolean implies(ProtectionDomain domain, Permission permission) {
        if (Thread.currentThread().getThreadGroup().equals(Game.PLAYER_THREAD_GROUP)){
            return false;
        }
        return true;
    }
}
