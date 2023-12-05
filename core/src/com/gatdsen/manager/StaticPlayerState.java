package com.gatdsen.manager;

import com.gatdsen.simulation.PlayerState;
import com.gatdsen.simulation.Tile;
import com.gatdsen.simulation.PathTile;
import com.gatdsen.simulation.Tower;

/**
 * Diese Klasse repräsentiert den aktuellen Zustand eines Spielers.
 * Dies umfasst sowohl Attribute, wie dessen Leben und Geld, als auch dessen Map.
 * <p>
 * Wie es der Name bereits andeutet, ist diese Klasse statisch. Das bedeutet, dass sie sich nicht bspw. über die
 * Dauer eines Zuges verändert.
 */
public final class StaticPlayerState {

    private final PlayerState state;

    StaticPlayerState(PlayerState state) {
        this.state = state;
    }

    /**
     * @return Die Id des Spielers, welche verwendet wird, um den Spieler bspw. aus einer Liste
     * von {@link StaticPlayerState}s zu identifizieren.
     */
    public int getId() {
        return state.getIndex();
    }

    /**
     * @return Die Anzahl der verbleibenden Leben des Spielers
     */
    public int getHealth() {
        return state.getHealth();
    }

    /**
     * @return Die Anzahl des angesparten Geldes des Spielers
     */
    public int getMoney() {
        return state.getMoney();
    }

    /**
     * Gibt einen zwei-dimensionalen Array von {@link Tile}s zurück, indexiert anhand ihrer x- und y-Koordinaten.
     * <p> - {@link PathTile} Instanzen symbolisieren dabei einen Pfad.
     * <p> - {@link Tower} Instanzen symbolisieren einen Tower.
     * <p> - {@code null} symbolisiert ein leeres Feld, an welchem sich weder ein Pfad noch ein Tower befindet. An diesen
     * Feldern können neue Türme platziert werden.
     * <p>
     * Das Array ist über die x- und y-Koordinate des {@link Tile}s indexiert.
     * {@code map[x][y]} gibt also das {@link Tile} an der Position {@code (x, y)} zurück.
     * @return Die Map des Spielers
     */
    public Tile[][] getBoard() {
        return state.getBoard();
    }
}
