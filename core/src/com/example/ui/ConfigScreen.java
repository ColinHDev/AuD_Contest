package com.example.ui;

import com.badlogic.gdx.Screen;
import com.example.manager.RunConfiguration;

abstract public class ConfigScreen implements Screen {
   protected RunConfiguration runConfiguration;

    protected RunConfiguration getRunConfiguration() {
        return runConfiguration.copy();
    }

    protected void setRunConfiguration(RunConfiguration runConfiguration) {
        this.runConfiguration = runConfiguration.copy();
    }
}