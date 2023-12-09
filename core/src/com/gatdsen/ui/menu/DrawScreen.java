package com.gatdsen.ui.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.gatdsen.ui.GADS;
import com.gatdsen.ui.assets.AssetContainer;

public class DrawScreen extends BaseMenuScreen{
    /**
     * Konstruktor für die Klasse BaseMenuScreen
     *
     * @param gameInstance Eine Instanz des GADS-Spiels
     */
    public DrawScreen(GADS gameInstance) {
        super(gameInstance);
        this.backgroundTextureRegion = AssetContainer.IngameAssets.drawDisplay;
    }

    @Override
    String getTitelString() {
        return null;
    }

    @Override
    Actor getContent(Skin skin) {
        return null;
    }

    @Override
    GADS.ScreenState getNext() {
        return null;
    }

    @Override
    GADS.ScreenState getPrev() {
        return null;
    }
}
