package com.gatdsen.manager.player.data;

import com.gatdsen.manager.player.Player;

import java.io.Serializable;

public class PlayerInformation implements Serializable {

    protected final Player.PlayerType type;
    protected final String name;

    public PlayerInformation(Player.PlayerType type, String name) {
        this.type = type;
        this.name = name;
    }

    public Player.PlayerType getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
