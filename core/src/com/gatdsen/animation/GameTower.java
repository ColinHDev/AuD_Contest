package com.gatdsen.animation;

import com.gatdsen.animation.entity.AnimatedEntity;
import com.gatdsen.ui.assets.AssetContainer;
import com.gatdsen.ui.assets.AssetContainer.IngameAssets.GameTowerAnimationType;

import static com.gatdsen.ui.assets.AssetContainer.IngameAssets.gameTowerAnimations;
//import com.gatdsen.ui.assets.AssetContainer;

public class GameTower extends AnimatedEntity {

    private int level = 1;
    private int type = 1;
    private GameTowerAnimationType currentAnimation = GameTowerAnimationType.ANIMATION_TYPE_IDLE;

    public GameTower(int level, int type) {
        super(gameTowerAnimations[GameTowerAnimationType.ANIMATION_TYPE_IDLE.ordinal()]);

        this.level = level;
        this.type = type;

    }
}
