package com.gatdsen.ui;

import com.badlogic.gdx.Screen;
import com.gatdsen.manager.run.config.RunConfiguration;

abstract public class ConfigScreen implements Screen {
   protected RunConfiguration runConfiguration;

    /**
     * Gibt eine Kopie der aktuellen RunConfiguration zurück.
     * @return Eine Kopie der aktuellen RunConfiguration.
     */
    protected RunConfiguration getRunConfiguration() {
        return runConfiguration.copy();
    }

    /**
     * Setzt die RunConfiguration auf eine Kopie des übergebenen RunConfiguration-Objekts.
     * @param runConfiguration Die RunConfiguration, die als Basis für die Kopie verwendet wird.
     */
    protected void setRunConfiguration(RunConfiguration runConfiguration) {
        this.runConfiguration = runConfiguration.copy();
    }
}