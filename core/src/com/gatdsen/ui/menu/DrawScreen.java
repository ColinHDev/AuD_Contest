package com.gatdsen.ui.menu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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
        menuTable.removeActor(title);
        this.titleSprite = null;
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
