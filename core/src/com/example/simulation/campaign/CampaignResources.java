package com.example.simulation.campaign;

import com.example.manager.Player;

import java.util.ArrayList;

public class CampaignResources {

    public static ArrayList<Class<? extends Player>> getEnemies(String map) {
        ArrayList<Class<? extends Player>> enemies = new ArrayList<>();
        switch (map) {
            case "level1_1":
            case "level1_2":
            case "level1_3":
                enemies.add(Level1Bot.class);
                break;
        }
        return enemies;
    }

    public static int[] getMoney(String map) {
        switch (map) {
            case "level1_1":
                return new int[]{100, 100};
            case "level1_2":
                return new int[]{50, 100};
            case "level1_3":
                return new int[]{200, 100};
        }
        return null;
    }
}
