package com.gatdsen.manager.player;

import com.badlogic.gdx.Input;
import com.gatdsen.manager.Controller;
import com.gatdsen.manager.StaticGameState;
import com.gatdsen.simulation.GameState;

//ToDo migrate to UI
public class HumanPlayer extends Player {

    private static final float NO_TICK = -10000.0f;

    enum Key {
        KEY_CHARACTER_EXAMPLE,

        KEY_CHARACTER_END_TURN
    }

    private int foo =0;


    final int KEY_CHARACTER_EXAMPLE = Input.Keys.Q;

    final int KEY_CHARACTER_END_TURN = Input.Keys.X;


    private float[] lastTick = new float[Key.values().length];

    private static final float[] tickspeed = new float[Key.values().length]; // in Hz

    {
        tickspeed[Key.KEY_CHARACTER_EXAMPLE.ordinal()] = 0.1f;

        tickspeed[Key.KEY_CHARACTER_END_TURN.ordinal()] = 0.1f;
    }

    //amount of time in seconds, the turn of the human player will take
    //if the time limit is reached, the execute turn will wait for turnOverhead seconds
    // to make sure everything is calculated and no GameState inconsistency is created
    private int turnDuration = 60;
    private int turnStartWaitTime = 2;

    private boolean turnInProgress;
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
     * Der Zug dauert {@link HumanPlayer#turnDuration} Sekunden, danach wird für
     * {@link HumanPlayer#turnStartWaitTime} gewartet und dann die Methode beendet.
     *
     * @param state      Der {@link GameState Spielzustand} während des Zuges
     * @param controller Der {@link Controller Controller}, der zum Charakter gehört
     */

    @Override
    public void executeTurn(StaticGameState state, Controller controller) {
        this.state = state;
        this.controller = controller;
        for (int i = 0; i < lastTick.length; i++) lastTick[i] = NO_TICK;

//                System.out.println("Turn has been ended preemptively");


       synchronized (this) {
            try {


                this.wait(turnDuration* 1000L);

            } catch (InterruptedException ignored) {

            }
        }

    }

    //ToDo: make protected after migration to UI
    /**
     * Ends the current turn of the player preemptively.
     * Callen when pressing {@link HumanPlayer#KEY_CHARACTER_END_TURN}.
     * notifies itself, so the wait will end.
     */
    public void endCurrentTurn() {
        synchronized (this) {
            this.notify();
        }
    }

    public void processKeyDown(int keycode) {
//        System.out.println("Received Key: " + keycode);
        switch (keycode) {
            case KEY_CHARACTER_EXAMPLE:
                lastTick[Key.KEY_CHARACTER_EXAMPLE.ordinal()] = -tickspeed[Key.KEY_CHARACTER_EXAMPLE.ordinal()];
                execute(Key.KEY_CHARACTER_EXAMPLE);
                break;
            case KEY_CHARACTER_END_TURN:
                //lastTick[Key.KEY_CHARACTER_END_TURN.ordinal()] = true;
                execute(Key.KEY_CHARACTER_END_TURN);
                break;
        }
    }

    private void execute(Key key) {
//        System.out.println("Commanding Controller: " + controller);
        switch (key) {
            // Qund E für rotieren/zielen mit den Waffen
            case KEY_CHARACTER_EXAMPLE:
                foo += 1;
                foo = foo % 360;
                //controller.foo(foo);
                break;
            case KEY_CHARACTER_END_TURN:
                this.endCurrentTurn();
                break;
        }
    }

    public void tick(float delta) {
        for (Key key : Key.values()) {
            int index = key.ordinal();
            if (lastTick[index] > (NO_TICK/2)) {
                lastTick[index] += delta;
                while (lastTick[index] >= 0.0f) {
                    lastTick[index] -= tickspeed[index];
                    execute(key);
                }
            }
        }
    }


    public void processKeyUp(int keycode) {


        switch (keycode) {
            // Qund E für rotieren/zielen mit den Waffen
            case KEY_CHARACTER_EXAMPLE:
                lastTick[Key.KEY_CHARACTER_EXAMPLE.ordinal()] = NO_TICK;
                break;
        }
    }

    @Override
    public PlayerType getType() {
        return PlayerType.Human;
    }



    public int getTurnDuration(){
        return this.turnDuration;
    }
}
