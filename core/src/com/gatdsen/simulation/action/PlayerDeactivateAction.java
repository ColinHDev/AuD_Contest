package com.gatdsen.simulation.action;

/**
 * Die Klasse PlayerDeactivateAction ist eine Unterklasse von {@link TeamAction TeamAction}
 * und repräsentiert eine Aktion, bei der ein Spieler deaktiviert wird.
 */
public class PlayerDeactivateAction extends TeamAction {
    private final boolean disqualified;

    /**
     * Konstruktor der Klasse PlayerDeactivateAction.
     *
     * @param delay Verzögerung bis zum Ausführen der Aktion
     * @param team  Team des Gegners
     * @param disqualified ob der Spieler disqualifiziert wurde
     */
    public PlayerDeactivateAction(float delay, int team, boolean disqualified) {
        super(delay, team);
        this.disqualified = disqualified;
    }

    /**
     * @return ob der Spieler disqualifiziert wurde
     */
    public boolean isDisqualified() {
        return disqualified;
    }

    /**
     * @return String der die PlayerDeactivateAction repräsentiert
     */
    @Override
    public String toString() {
        return "PlayerDeactivateAction{" +
                "disqualified=" + disqualified +
                "} " + super.toString();
    }
}
