package com.gatdsen.simulation.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representiert eine Sammlung aller UI-relevanten {@link Action Actions},
 * die während eines bestimmten Teils der Simulation angesammelt wurden.
 * Normalerweise ist dies die Ausführung eines einzelnen Befehls.
 */
public class ActionLog implements Serializable {
    private final List<Action> rootActions = new ArrayList<>();

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
        this.rootActions.add(rootAction);
    }

    /**
     * Gibt die Wurzel dieses baumartig strukturierten Protokolls zurück.
     *
     * @return Die rootAction
     */
    public Action getRootAction() {
        return rootActions.get(rootActions.size() - 1);
    }

    /**
     * Gibt die Liste der rootActions zurück.
     *
     * @return Die Liste der rootActions
     */
    public List<Action> getRootActions() {
        return rootActions;
    }

    /**
     * Fügt eine neue rootAction hinzu.
     *
     * @param rootAction Die neue rootAction
     */
    public void addRootAction(Action rootAction) {
        rootActions.add(rootAction);
    }
}
