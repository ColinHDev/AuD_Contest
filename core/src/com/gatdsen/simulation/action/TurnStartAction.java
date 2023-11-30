package com.gatdsen.simulation.action;

/**
 * Spezialisierte Klasse von {@link Action} die anzeigt, dass ein neuer Zug beginnt.
 * Kann eine {@link InitAction} als Wurzel eines {@link ActionLog} ersetzen.
 */
public class TurnStartAction extends Action {

    /**
     * Speichert das Ereignis, dass ein neuer Zug beginnt
     *
     * @param delay nicht-negativer zeitbasierter Offset zu seinem Elternteil in Sekunden
     */
    public TurnStartAction(long delay) {
        super(delay);
    }

    @Override
    public String toString() {
        return "TurnStartAction{} " + super.toString();
    }
}
