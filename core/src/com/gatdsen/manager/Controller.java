package com.gatdsen.manager;

import com.gatdsen.manager.command.*;
import com.gatdsen.simulation.GameCharacterController;
import com.gatdsen.simulation.GameState;

/**
 * Provides an access-controlled interface to send commands to players
 * <p>
 * Ermöglicht die Kontrolle eines bestimmten Charakters.
 * Ist nur für einen einzelnen Zug gültig und deaktiviert sich nach Ende des aktuellen Zuges.
 */
public class Controller {

    private int uses;
    private Game game;
    private GameCharacterController gcController;
    private int team;

    protected Controller(Game game, GameCharacterController gcController, GameState stateCopy, int uses) {
//        System.out.println("Created new Controller: " + this);
        this.game = game;
        this.gcController = gcController;
        this.team = gcController.getTeam();
        this.uses = uses;
    }

    /**
     * @return Das Team, zu welchem dieser Controller gehört.
     */
    public int getTeam() {
        return team;
    }


    //ToDo: write all required Functions similar to the template below, this will become the external API, that is exposed to the Bots
    //ToDo: every Function in the gcController should have one Command as well as a designated call here
    /**
     * This is an exposed function of the API
     * <p>
     * The documentation of this has to be in german
     */
    public void foo(int i) {
        queue(new FooCommand(gcController, i));

    }

    /**
     * Die Zahl an Nutzungen ist für Bots auf 200 beschränkt.
     * Die maximale Zahl sinnvoller Züge beträgt ca. 70
     *
     * @return Die Menge an Befehlen, die dieser Controller noch ausführen kann
     */
    public int getRemainingUses() {
        return uses;
    }


    /**
     * Internal utility method.
     * Controls the remaining uses and submits cmd to the game.
     *
     * @param cmd the command to be queued
     */
    private void queue(Command cmd) {
        if (uses-- > 0) game.queueCommand(cmd);
    }

    /**
     * Deaktiviert diesen Controller (Wenn der Zug vorbei ist). Wird von internen Komponenten genutzt, um den Zugfolge der Charaktere zu steuern.
     */
    protected void deactivate() {
        uses = -1;
        gcController.deactivate();
    }

}
