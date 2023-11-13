package com.example.animation.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.example.ui.assets.AssetContainer;

public class Healthbar extends Entity{
    private ProgressBar healthProgress;

    public Healthbar(int maxHealth) {


        this.healthProgress = new ProgressBar(0, maxHealth, 1, false, AssetContainer.IngameAssets.healthbarStyle);
    }

    public void changeHealth(int curHealth) {
        healthProgress.setValue(curHealth);
    }

    @Override
    public void draw(Batch batch, float deltaTime, float parentAlpha) {
        healthProgress.draw(batch, parentAlpha);
        super.draw(batch, deltaTime, parentAlpha);
    }
}
