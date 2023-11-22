
package com.example.simulation;


import java.util.ArrayList;
import java.util.List;

public class Tower extends Tile{

    @Override
    protected Tile copy() {
        return new Tower(type, pos.x, pos.y);
    }

    public enum TowerType {
        BASIC_TOWER,
        AOE_TOWER,
        SNIPER_TOWER
    }

    TowerType type;
    int damage;
    int range;
    int rechargeTime;
    List<Enemy> enemiesInRange = new ArrayList<>();


    public Tower(TowerType type, int x, int y) {
        super(x, y);
        this.type = type;



        switch (type) {
            case BASIC_TOWER -> {
                damage = 1;
                range = 2;
                rechargeTime = 0;
            }
            case AOE_TOWER -> {
                damage = 2;
                range = 1;
                rechargeTime = 1;
            }

            case SNIPER_TOWER -> {
                damage = 3;
                range = 3;
                rechargeTime = 2;
            }

            default -> System.out.println("This case should not be reached");
        }

    }

    private void setEnemyList(Tile[][] board){
        Tile[][] inRange = this.getNeighbours(range, board);
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

    private List<Enemy> getEnemiesInRange() {
        return enemiesInRange;
    }
}
