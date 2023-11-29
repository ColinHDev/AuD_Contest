
package com.gatdsen.simulation;


import java.util.ArrayList;
import java.util.List;

public class Tower extends Tile{

    // ToDo: lookup table for upgraded values

    public enum TowerType {
        BASIC_TOWER,
        AOE_TOWER,
        SNIPER_TOWER;
    }

    static final int MAX_LEVEL = 3;
    static final int[] DAMAGE_VALUES = new int[TowerType.values().length];
    static final int[] RANGE_VALUES = new int[TowerType.values().length];
    static final int[] RECHARGE_TIME_VALUES = new int[TowerType.values().length];
    static final int[] PRICE_VALUES = new int[TowerType.values().length];
    static {
        DAMAGE_VALUES[TowerType.BASIC_TOWER.ordinal()] = 1;
        DAMAGE_VALUES[TowerType.AOE_TOWER.ordinal()] = 2;
        DAMAGE_VALUES[TowerType.SNIPER_TOWER.ordinal()] = 3;

        RANGE_VALUES[TowerType.BASIC_TOWER.ordinal()] = 2;
        RANGE_VALUES[TowerType.AOE_TOWER.ordinal()] = 1;
        RANGE_VALUES[TowerType.SNIPER_TOWER.ordinal()] = 3;

        RECHARGE_TIME_VALUES[TowerType.BASIC_TOWER.ordinal()] = 0;
        RECHARGE_TIME_VALUES[TowerType.AOE_TOWER.ordinal()] = 1;
        RECHARGE_TIME_VALUES[TowerType.SNIPER_TOWER.ordinal()] = 2;

        PRICE_VALUES[TowerType.BASIC_TOWER.ordinal()] = 80;
        PRICE_VALUES[TowerType.AOE_TOWER.ordinal()] = 9999;
        PRICE_VALUES[TowerType.SNIPER_TOWER.ordinal()] = 9999;
    }

    TowerType type;
    int level;

    public Tower(TowerType type, int x, int y) {
        super(x, y);
        this.type = type;
        this.level = 1;
    }

    public Tower(Tower original) {
        this(original.type, original.pos.x, original.pos.y);
        this.level = original.level;
    }

    @Override
    protected Tile copy() {
        return new Tower(this);
    }

    public static int getUpgradePrice(TowerType type, int level) {
        return (int) (getPrice(type) * (Math.pow(1.25, level) - 0.5));
    }

    /**
     * Gibt den Damage-Wert des Towers zurück
     * @param type Typ des Towers
     * @return Damage-Wert des Towers
     */
    public static int getDamage(TowerType type) {
        return DAMAGE_VALUES[type.ordinal()];
    }

    /**
     * Gibt den Range-Wert des Towers zurück
     * @param type Typ des Towers
     * @return Range-Wert des Towers
     */
    public static int getRange(TowerType type) {
        return RANGE_VALUES[type.ordinal()];
    }

    /**
     * Gibt den RechargeTime-Wert des Towers zurück
     * @param type Typ des Towers
     * @return RechargeTime-Wert des Towers
     */
    public static int getRechargeTime(TowerType type) {
        return RECHARGE_TIME_VALUES[type.ordinal()];
    }

    /**
     * Gibt den Preis des Towers zurück
     * @param type Typ des Towers
     * @return Preis des Towers
     */
    public static int getPrice(TowerType type) {
        return PRICE_VALUES[type.ordinal()];
    }

    public static int getMaxLevel() {
        return MAX_LEVEL;
    }

    public TowerType getType() {
        return type;
    }

    public int getUpgradePrice() {
        return getUpgradePrice(type, level);
    }

    public int getLevel() {
        return level;
    }

    public int getDamage() {
        return getDamage(type);
    }

    public int getRange() {
        return getRange(type);
    }

    public int getRechargeTime() {
        return getRechargeTime(type);
    }

    public int getPrice() {
        return getPrice(type);
    }

    void upgrade() {
        ++level;
    }

    private final List<Enemy> enemiesInRange = new ArrayList<>();
    private void setEnemyList(Tile[][] board){
        Tile[][] inRange = this.getNeighbours(getRange(), board);
        for (Tile[] tiles : inRange) {
            for (Tile tile : tiles) {
                if (tile instanceof PathTile && !((PathTile) tile).enemies.isEmpty()) {
                    enemiesInRange.addAll(((PathTile) tile).enemies);
                }
            }
        }
    }

    private List<Enemy> getEnemiesInRange() {
        return enemiesInRange;
    }
}
