
package com.example.simulation;


import java.util.ArrayList;
import java.util.List;

public class Tower {

    public enum TowerType {
        BASIC_TOWER,
        AOE_TOWER,
        SNIPER_TOWER
    }

    TowerType type;
    int damage;
    int range;
    int rechargeTime;
    Tile pos;
    List<Enemy> enemiesInRange = new ArrayList<>();


    public Tower(TowerType type, Tile pos) {
        this.type = type;
        this.pos = pos;

        switch (type) {
            case BASIC_TOWER -> {
                damage = 1;
                range = 2;
                rechargeTime = 0;
            }
            case AOE_TOWER -> {
            }

            case SNIPER_TOWER -> {
            }

            default -> System.out.println("This case should not be reached");
        }

    }

    /**
     * Nimmt alle Tiles im Umkreis von Radius von Tower und fügt die Gegner zur enemylist des Towers hinzu
     * @param board
     */
    public void setEnemyList(Tile[][] board){
        Tile[][] inRange = pos.getNeighbours(range, board);
        for (Tile[] tiles : inRange) {
            for (Tile tile : tiles) {
                if (tile instanceof PathTile && !((PathTile) tile).enemies.isEmpty()) {
                    enemiesInRange.addAll(((PathTile) tile).enemies);
                }
            }
        }
    }

    public TowerType getType() {
        return type;
    }

    public int getDamage() {
        return damage;
    }

    public int getRange() {
        return range;
    }

    public int getRechargeTime() {
        return rechargeTime;
    }

    public Tile getPos() {
        return pos; //Return x and y from Tile?
    }

    public List<Enemy> getEnemiesInRange() {
        return enemiesInRange;
    }
}
