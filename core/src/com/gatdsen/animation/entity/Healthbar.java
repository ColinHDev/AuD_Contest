package com.gatdsen.animation.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.gatdsen.ui.assets.AssetContainer;

public class Healthbar extends Entity{
    private ProgressBar healthProgress;

    public Healthbar(int maxHealth) {


        this.healthProgress = new ProgressBar(0, maxHealth, 1, false, AssetContainer.MainMenuAssets.skin);
        healthProgress.setSize(200, 50);
        healthProgress.setValue(maxHealth);
    }

    public void changeHealth(int curHealth) {
        healthProgress.setValue(curHealth);
    }

    @Override
    public void draw(Batch batch, float deltaTime, float parentAlpha) {
        healthProgress.draw(batch, parentAlpha);
        super.draw(batch, deltaTime, parentAlpha);
    }

    @Override
    protected void setPos(Vector2 pos) {
        super.setPos(pos);
        healthProgress.setPosition(pos.x, pos.y);
    }
}
