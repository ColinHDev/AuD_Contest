package com.gatdsen.simulation.action;

/**
 * Spezialisierte Klasse von {@link Action} die kein Ereignis anzeigt,
 * sondern als Wurzel eines {@link ActionLog} verwendet wird
 */
public final class InitAction extends Action {

    /**
     * Funktioniert als überspringbare Aktion, um mehrere Aktionen zu haben,
     * die direkt an der Wurzel eines {@link ActionLog} ausgeführt werden.
     */
    public InitAction() {
        super(0);
    }

    @Override
    public String toString() {
        return "InitAction{} " + super.toString();
    }
}
