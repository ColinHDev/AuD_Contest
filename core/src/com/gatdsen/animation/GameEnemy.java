package com.gatdsen.animation;

import com.gatdsen.animation.entity.AnimatedEntity;
import com.gatdsen.animation.entity.Healthbar;
import com.gatdsen.ui.assets.AssetContainer;
import com.gatdsen.ui.assets.AssetContainer.IngameAssets.GameEnemyAnimationType;

import static com.gatdsen.ui.assets.AssetContainer.IngameAssets.gameEnemyAnimations;

public class GameEnemy extends AnimatedEntity {
    private int level;
    public Healthbar healthbar;

    public GameEnemy(int level) {
        super(gameEnemyAnimations[GameEnemyAnimationType.ANIMATION_TYPE_IDLE.ordinal()]);
        this.level = level;
        healthbar = new Healthbar(100);

    }
}
