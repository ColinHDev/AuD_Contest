package com.gatdsen.animation;

import com.gatdsen.animation.entity.AnimatedEntity;
import com.gatdsen.ui.assets.AssetContainer.IngameAssets.GameCharacterAnimationType;

import static com.gatdsen.ui.assets.AssetContainer.IngameAssets.gameCharacterAnimations;
//import com.gatdsen.ui.assets.AssetContainer;

public class GameTower extends AnimatedEntity {

    private int level = 1;
    private int type = 1;
    private GameCharacterAnimationType currentAnimation = GameCharacterAnimationType.ANIMATION_TYPE_IDLE;

    public GameTower(int level, int type) {
        super(gameCharacterAnimations[GameCharacterAnimationType.ANIMATION_TYPE_IDLE.ordinal()]);

        this.level = level;
        this.type = type;

    }
}
