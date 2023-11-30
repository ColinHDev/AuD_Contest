package com.gatdsen.simulation.action;

import java.io.Serializable;

/**
 * Representiert eine Sammlung aller UI-relevanten {@link Action Actions},
 * die während eines bestimmten Teils der Simulation angesammelt wurden.
 * Normalerweise ist dies die Ausführung eines einzelnen Befehls.
 */
public class ActionLog implements Serializable {
    private final Action rootAction;

    /**
     * Erstellt ein neues ActionLog. Aufgrund der Struktur von {@link Action Actions}.
     * Dies führt zu einer Baumstruktur mit der rootAction an ihrer Wurzel.
     * Beim Wiedergeben des Protokolls wird der Baum in einer Weise durchlaufen, bei der auf
     * jedem Knoten jeder Zweig parallel ausgeführt wird und sich bei folgenden Knoten weiter aufteilen kann.
     * Darüber hinaus können selbst parallele Zweige unterschiedliche Ausführungszeiten haben.
     *
     * @param rootAction die erste Aktion des Protokolls
     */
    public ActionLog(Action rootAction) {
        this.rootAction = rootAction;
    }

    /**
     * Gibt die Wurzel dieses baumartig strukturierten Protokolls zurück.
     *
     * @return Die rootAction
     */
    public Action getRootAction() {
        return rootAction;
    }
}
