package com.gatdsen.manager.player;

import com.badlogic.gdx.Input;
import com.gatdsen.manager.Controller;
import com.gatdsen.manager.PlayerThread;
import com.gatdsen.manager.StaticGameState;
import com.gatdsen.simulation.IntVector2;
import com.gatdsen.simulation.Tower;

import java.util.Arrays;

public class HumanPlayer extends Player {

    enum Key {
        KEY_CHARACTER_TILE_UP,
        KEY_CHARACTER_TILE_DOWN,
        KEY_CHARACTER_TILE_LEFT,
        KEY_CHARACTER_TILE_RIGHT,

        KEY_CHARACTER_TOWER_PLACE,
        KEY_CHARACTER_TOWER_UPGRADE,

        KEY_CHARACTER_END_TURN;

        private static Key fromKeycode(int keycode) {
            return switch (keycode) {
                case HumanPlayer.KEY_CHARACTER_TILE_UP -> Key.KEY_CHARACTER_TILE_UP;
                case HumanPlayer.KEY_CHARACTER_TILE_DOWN -> Key.KEY_CHARACTER_TILE_DOWN;
                case HumanPlayer.KEY_CHARACTER_TILE_LEFT -> Key.KEY_CHARACTER_TILE_LEFT;
                case HumanPlayer.KEY_CHARACTER_TILE_RIGHT -> Key.KEY_CHARACTER_TILE_RIGHT;
                case HumanPlayer.KEY_CHARACTER_TOWER_PLACE -> Key.KEY_CHARACTER_TOWER_PLACE;
                case HumanPlayer.KEY_CHARACTER_TOWER_UPGRADE -> Key.KEY_CHARACTER_TOWER_UPGRADE;
                case HumanPlayer.KEY_CHARACTER_END_TURN -> Key.KEY_CHARACTER_END_TURN;
                default -> null;
            };
        }
    }

    private static final int KEY_CHARACTER_TILE_UP = Input.Keys.UP;
    private static final int KEY_CHARACTER_TILE_DOWN = Input.Keys.DOWN;
    private static final int KEY_CHARACTER_TILE_LEFT = Input.Keys.LEFT;
    private static final int KEY_CHARACTER_TILE_RIGHT = Input.Keys.RIGHT;

    private static final int KEY_CHARACTER_TOWER_PLACE = Input.Keys.P;
    private static final int KEY_CHARACTER_TOWER_UPGRADE = Input.Keys.U;

    private static final int KEY_CHARACTER_END_TURN = Input.Keys.X;

    private static final float NO_TICK = -10000.0f;

    private final float[] lastTick = new float[Key.values().length];
    private static final float[] tickSpeed = new float[Key.values().length]; // in Hz

    static {
        tickSpeed[Key.KEY_CHARACTER_TILE_UP.ordinal()] = 0.1f;
        tickSpeed[Key.KEY_CHARACTER_TILE_DOWN.ordinal()] = 0.1f;
        tickSpeed[Key.KEY_CHARACTER_TILE_LEFT.ordinal()] = 0.1f;
        tickSpeed[Key.KEY_CHARACTER_TILE_RIGHT.ordinal()] = 0.1f;

        tickSpeed[Key.KEY_CHARACTER_TOWER_PLACE.ordinal()] = 0.1f;
        tickSpeed[Key.KEY_CHARACTER_TOWER_UPGRADE.ordinal()] = 0.1f;

        tickSpeed[Key.KEY_CHARACTER_END_TURN.ordinal()] = 0.1f;
    }

    /**
     * Die Dauer des Zuges, die der {@link HumanPlayer} hat, in Sekunden.
     */
    private static final int turnDuration = PlayerThread.HUMAN_EXECUTE_TURN_TIMEOUT / 1000;

    private final IntVector2 selectedTile = new IntVector2(0, 0);

    private StaticGameState state;
    private Controller controller;

    @Override
    public String getName() {
        return "Human";
    }

    @Override
    public void init(StaticGameState state) {
    }

    /**
     * Started den Zug des {@link HumanPlayer} und erlaubt es diesem mithilfe von Tasteneingaben, zu bewegen.
     * Der Zug dauert {@link HumanPlayer#turnDuration} Sekunden.
     *
     * @param state      Der {@link StaticGameState Spielzustand} während des Zuges
     * @param controller Der {@link Controller Controller}, der zum Charakter gehört
     */
    @Override
    public void executeTurn(StaticGameState state, Controller controller) {
        this.state = state;
        this.controller = controller;
        Arrays.fill(lastTick, NO_TICK);

        synchronized (this) {
            try {
                this.wait(turnDuration * 1000L);
            } catch (InterruptedException ignored) {
            }
        }
    }

    /**
     * Ruft den {@link Controller} des {@link HumanPlayer} auf, um einen Turm auf dem Spielfeld zu platzieren.
     * @param x x-Koordinate, an der der Turm platziert werden soll
     * @param y y-Koordinate, an der der Turm platziert werden soll
     * @param type Typ des Turms, der platziert werden soll
     */
    public void placeTower(int x, int y, Tower.TowerType type) {
        controller.placeTower(x, y, type);
    }

    /**
     * Ruft den {@link Controller} des {@link HumanPlayer} auf, um einen Turm auf dem Spielfeld zu verbessern.
     * @param x x-Koordinate, an der sich der Turm befindet
     * @param y y-Koordinate, an der sich der Turm befindet
     */
    public void upgradeTower(int x, int y) {
        controller.upgradeTower(x, y);
    }

    /**
     * Endet den aktuellen Zug des {@link HumanPlayer} vorzeitig.
     * Wird aufgerufen, wenn {@link HumanPlayer#KEY_CHARACTER_END_TURN} gedrückt wird.
     */
    public void endCurrentTurn() {
        // Benachrichtigt sich selbst, damit das wait() in executeTurn() beendet wird
        synchronized (this) {
            this.notify();
        }
    }

    /**
     * Wird aufgerufen, wenn eine Taste gedrückt wird.
     * @param keycode Der Keycode der Taste, siehe {@link Input.Keys}
     */
    public void processKeyDown(int keycode) {
        Key key = Key.fromKeycode(keycode);
        if (key == null) {
            return;
        }
        lastTick[key.ordinal()] = -tickSpeed[key.ordinal()];
        execute(key);
    }

    /**
     * Wird aufgerufen, wenn eine Taste losgelassen wird, nachdem sie gedrückt wurde.
     * @param keycode Der Keycode der Taste, siehe {@link Input.Keys}
     */
    public void processKeyUp(int keycode) {
        Key key = Key.fromKeycode(keycode);
        if (key == null) {
            return;
        }
        lastTick[key.ordinal()] = NO_TICK;
        execute(key);
    }

    private void execute(Key key) {
        switch (key) {
            case KEY_CHARACTER_TILE_UP:
                selectedTile.y = Math.max(0, selectedTile.y - 1);
                break;
            case KEY_CHARACTER_TILE_DOWN:
                selectedTile.y = Math.min(selectedTile.y + 1, state.getBoardSizeY() - 1);
                break;
            case KEY_CHARACTER_TILE_LEFT:
                selectedTile.x = Math.max(0, selectedTile.x - 1);
                break;
            case KEY_CHARACTER_TILE_RIGHT:
                selectedTile.x = Math.min(selectedTile.x + 1, state.getBoardSizeX() - 1);
                break;
            case KEY_CHARACTER_TOWER_PLACE:
                placeTower(selectedTile.x, selectedTile.y, Tower.TowerType.BASIC_TOWER);
                break;
            case KEY_CHARACTER_TOWER_UPGRADE:
                upgradeTower(selectedTile.x, selectedTile.y);
                break;
            case KEY_CHARACTER_END_TURN:
                endCurrentTurn();
                break;
        }
    }

    public void tick(float delta) {
        for (Key key : Key.values()) {
            int index = key.ordinal();
            if (lastTick[index] > (NO_TICK/2)) {
                lastTick[index] += delta;
                while (lastTick[index] >= 0.0f) {
                    lastTick[index] -= tickSpeed[index];
                    execute(key);
                }
            }
        }
    }

    @Override
    public PlayerType getType() {
        return PlayerType.HUMAN;
    }

    public int getTurnDuration() {
        return turnDuration;
    }
}
