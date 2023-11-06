package com.example.animation;

import com.example.animation.entity.AnimatedEntity;
import com.example.ui.assets.AssetContainer.IngameAssets.GameCharacterAnimationType;

import static com.example.ui.assets.AssetContainer.IngameAssets.gameCharacterAnimations;
//import com.example.ui.assets.AssetContainer;

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
