
package com.gatdsen.simulation;


import com.gatdsen.simulation.action.Action;
import com.gatdsen.simulation.action.ProjectileAction;
import com.gatdsen.simulation.action.TowerAttackAction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Tower extends Tile {

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
    int cooldown;
    List<Tile> inRange;
    Tile[][] board;

    public Tower(TowerType type, int x, int y, Tile[][] board) {
        super(x, y);
        this.type = type;
        this.level = 1;
        this.cooldown = getRechargeTime();
        this.board = board;
        this.inRange = getNeighbours(getRange(), board);
    }

    public Tower(Tower original) {
        this(original.type, original.pos.x, original.pos.y, null);
        this.level = original.level;
        this.cooldown = original.cooldown;
        this.inRange = original.inRange;
    }

    @Override
    protected Tile copy() {
        return new Tower(this);
    }

    private final List<PathTile> pathInRange = new ArrayList<>();

    private void setPathList(Tile[][] board) {
        for (Tile tile : inRange) {
            if (tile instanceof PathTile pathTile) {
                pathInRange.add(pathTile);
            }
        }
        pathInRange.sort(Comparator.comparingInt(PathTile::getIndex));
    }

    private List<PathTile> getPathInRange() {
        return pathInRange;
    }

    public static int getUpgradePrice(TowerType type, int level) {
        return (int) (getPrice(type) * (Math.pow(1.25, level) - 0.5));
    }

    /**
     * Gibt den Damage-Wert des Towers zurück
     *
     * @param type Typ des Towers
     * @return Damage-Wert des Towers
     */
    public static int getDamage(TowerType type) {
        return DAMAGE_VALUES[type.ordinal()];
    }

    /**
     * Gibt den Range-Wert des Towers zurück
     *
     * @param type Typ des Towers
     * @return Range-Wert des Towers
     */
    public static int getRange(TowerType type) {
        return RANGE_VALUES[type.ordinal()];
    }

    /**
     * Gibt den RechargeTime-Wert des Towers zurück
     *
     * @param type Typ des Towers
     * @return RechargeTime-Wert des Towers
     */
    public static int getRechargeTime(TowerType type) {
        return RECHARGE_TIME_VALUES[type.ordinal()];
    }

    /**
     * Gibt den Preis des Towers zurück
     *
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
        this.inRange = getNeighbours(getRange(), board);
        ++level;
    }

    public Action attack(Tile[][] board, Action head) {
        if (getRechargeTime() > 0) {
            --cooldown;
            return head;
        }
        setPathList(board);
        if (pathInRange.isEmpty()) {
            return head;
        }

        int lastIndex = pathInRange.size() - 1;

        Enemy target = null;
        for (int i = lastIndex; i >= 0; i--) {
            if (!pathInRange.get(i).getEnemies().isEmpty()) {
                target = pathInRange.get(i).getEnemies().get(0);
                break;
            }
        }
        assert target != null;

        // TODO: define Team instead of 0!!!
        head.addChild(new TowerAttackAction(0, pos, target.getPosition(), type.ordinal(), 0));
        Path path = new LinearPath(getPosition().toFloat(), target.getPosition().toFloat(),1);
        head.addChild(new ProjectileAction(0, ProjectileAction.ProjectileType.STANDARD_TYPE,path));

        target.updateHealth(getDamage(), head);
        cooldown = getRechargeTime();
        return head;
    }

    Action tick(Action head) {
        head.addChild(attack(board, head));
        return head;
    }
}
