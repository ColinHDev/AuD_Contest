package com.gatdsen.simulation;

import com.gatdsen.simulation.action.Action;
import com.gatdsen.simulation.action.ProjectileAction;
import com.gatdsen.simulation.action.TowerAttackAction;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

/**
 * Speichert einen Tower. Beinhalet Methoden zum ausführen von Tower-Aktionen.
 */
public class Tower extends Tile {

    // ToDo: lookup table for upgraded values

    /**
     * Speichert die verschiedenen Typen von Türmen
     */
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
        DAMAGE_VALUES[TowerType.BASIC_TOWER.ordinal()] = 35;
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

    private final PlayerState playerState;
    private final TowerType type;
    private int level;
    private int cooldown;
    private List<Tile> inRange;
    private final Tile[][] board;
    private final List<PathTile> pathInRange = new ArrayList<>();

    /**
     * Erstellt einen Tower an der angegebenen Position.
     *
     * @param playerState der PlayerState, zu dem der Tower gehört
     * @param type        der Typ des Towers
     * @param x           x-Koordinate
     * @param y           y-Koordinate
     * @param board       die Map, auf der der Tower steht
     */
    public Tower(PlayerState playerState, TowerType type, int x, int y, Tile[][] board) {
        super(x, y);
        this.playerState = playerState;
        this.type = type;
        this.level = 1;
        this.cooldown = getRechargeTime();
        this.board = board;
        this.inRange = getNeighbours(getRange(), board);
    }

    /**
     * Erstellt eine Kopie eines Towers.
     *
     * @param original der zu kopierende Tower
     */
    public Tower(Tower original) {
        this(original.playerState, original.type, original.pos.x, original.pos.y, null);
        this.level = original.level;
        this.cooldown = original.cooldown;
        this.inRange = original.inRange;
    }

    /**
     * Erstellt eine Kopie des Towers.
     *
     * @return eine Kopie des Towers
     */
    @Override
    protected Tile copy() {
        return new Tower(this);
    }

    /**
     * Setzt die Liste der Tiles, die in Reichweite des Towers sind.
     */
    private void setPathList() {
        for (Tile tile : inRange) {
            if (tile instanceof PathTile pathTile) {
                pathInRange.add(pathTile);
            }
        }
        pathInRange.sort(Comparator.comparingInt(PathTile::getIndex));
    }

    /**
     * Gibt die Liste der Tiles, die in Reichweite des Towers sind, zurück.
     *
     * @return Liste der Tiles, die in Reichweite des Towers sind
     */
    private List<PathTile> getPathInRange() {
        return pathInRange;
    }

    /**
     * Gibt den Preis für ein Upgrade des Towers zurück
     *
     * @param type  Typ des Towers
     * @param level Level des Towers
     * @return Preis für ein Upgrade des Towers
     */
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

    /**
     * Gibt den maximalen Level des Towers zurück
     *
     * @return maximaler Level des Towers
     */
    public static int getMaxLevel() {
        return MAX_LEVEL;
    }

    /**
     * Gibt den Typ des Towers zurück
     *
     * @return Typ des Towers
     */
    public TowerType getType() {
        return type;
    }

    /**
     * Gibt den Preis für ein Upgrade des Towers zurück
     *
     * @return Preis für ein Upgrade des Towers
     */
    public int getUpgradePrice() {
        return getUpgradePrice(type, level);
    }

    /**
     * Gibt den Level des Towers zurück
     *
     * @return Level des Towers
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gibt den Damage-Wert des Towers zurück
     *
     * @return Damage-Wert des Towers
     */
    public int getDamage() {
        return getDamage(type);
    }

    /**
     * Gibt den Range-Wert des Towers zurück
     *
     * @return Range-Wert des Towers
     */
    public int getRange() {
        return getRange(type);
    }

    /**
     * Gibt den RechargeTime-Wert des Towers zurück
     *
     * @return RechargeTime-Wert des Towers
     */
    public int getRechargeTime() {
        return getRechargeTime(type);
    }

    /**
     * Gibt den Preis des Towers zurück
     *
     * @return Preis des Towers
     */
    public int getPrice() {
        return getPrice(type);
    }

    /**
     * Upgraded den Tower
     */
    void upgrade() {
        // ToDo: implement upgrade after christmas task
        this.inRange = getNeighbours(getRange() + level, board);
        ++level;
    }

    /**
     * Führt einen Angriff aus, wenn möglich.
     *
     * @param head Kopf der Action-Liste
     * @return neuer Kopf der Action-Liste
     */
    public Action attack(Action head) {
        if (getRechargeTime() > 0) {
            --cooldown;
            return head;
        }
        setPathList();

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
        if(target != null) {
            head.addChild(new TowerAttackAction(0, pos, target.getPosition(), type.ordinal(), playerState.getIndex()));
            Path path = new LinearPath(getPosition().toFloat(), target.getPosition().toFloat(), 1);
            head.addChild(new ProjectileAction(0, ProjectileAction.ProjectileType.STANDARD_TYPE, path, playerState.getIndex()));

            head = target.updateHealth(getDamage(), head);
            cooldown = getRechargeTime();
        }
        return head;
    }

    /**
     * Führt einen Tower-Tick aus.
     *
     * @param head Kopf der Action-Liste
     * @return neuer Kopf der Action-Liste
     */
    Action tick(Action head) {
        return attack(head);
    }
}
