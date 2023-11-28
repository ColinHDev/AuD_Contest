package com.gatdsen.manager.player.data;

import com.gatdsen.manager.player.Player;

import java.io.Serializable;

public class PlayerInformation implements Serializable {

    public final Player.PlayerType type;
    public final String name;

    public PlayerInformation(Player.PlayerType type, String name) {
        this.type = type;
        this.name = name;
    }
}
