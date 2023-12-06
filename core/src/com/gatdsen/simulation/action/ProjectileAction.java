package com.gatdsen.simulation.action;

import com.gatdsen.simulation.Path;

/**
 * Spezialisierte Klasse von {@link Action} die ein Projektil während seiner Lebenszeit beschreibt
 */
public class ProjectileAction extends TeamAction {

    /**
     * Typen von Projektilen
     */
    public enum ProjectileType {
        STANDARD_TYPE
    }

    private final ProjectileType type;
    private final Path path;

    /**
     * Speichert das Ereignis, dass ein bestimmtes Projektil erstellt wird,
     * entlang eines bestimmten Pfades reist und schließlich zerstört wird.
     *
     * @param delay nicht-negativer zeitbasierter Offset zu seinem Elternteil in Sekunden
     * @param type  Type des Projektils
     * @param path  Ein {@link Path}, der die Position des Projektils für jeden Zeitstempel zwischen 0 und Dauer zurückgibt
     */
    public ProjectileAction(float delay, ProjectileType type, Path path, int team) {
        super(delay, team);
        this.type = type;
        this.path = path;
    }

    /**
     * @return Ein {@link Path}, der die Position des Projektils für jeden Zeitstempel zwischen 0 und Dauer zurückgibt
     */
    public Path getPath() {
        return path;
    }

    /**
     * @return Die Dauer des Ereignisses in Sekunden
     */
    public float getDuration() {
        return path.getDuration();
    }

    /**
     * @return Type des Projektils
     */
    public ProjectileType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "ProjectileAction{" +
                "type=" + type +
                ", path=" + path +
                ", duration=" + getDuration() +
                '}' + super.toString();
    }
}
