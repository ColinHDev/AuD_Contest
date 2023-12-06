package com.gatdsen.manager.player.data;

import com.gatdsen.manager.player.Player;

public class BotInformation extends PlayerInformation {

    protected final String studentName;
    protected final int matrikel;

    public BotInformation(Player.PlayerType type, String name, String studentName, int matrikel) {
        super(type, name);
        this.studentName = studentName;
        this.matrikel = matrikel;
    }

    public String getStudentName() {
        return studentName;
    }

    public int getMatrikel() {
        return matrikel;
    }
}
