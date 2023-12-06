package com.gatdsen.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gatdsen.animation.entity.AnimatedEntity;
import com.gatdsen.ui.assets.AssetContainer;
import com.gatdsen.ui.assets.AssetContainer.IngameAssets.GameTowerAnimationType;

import static com.gatdsen.ui.assets.AssetContainer.IngameAssets.gameTowerAnimations;
//import com.gatdsen.ui.assets.AssetContainer;

public class GameTower extends AnimatedEntity {

    private int level = 1;
    private int type = 1;
    private Animation<TextureRegion> idleAnimation = gameCharacterAnimations[GameCharacterAnimationType.ANIMATION_TYPE_IDLE.ordinal()];
    public Animation<TextureRegion> attackAnimation;

    private boolean attacking = false;
    private GameTowerAnimationType currentAnimation = GameTowerAnimationType.ANIMATION_TYPE_IDLE;

    public GameTower(int level, int type) {
        super(gameTowerAnimations[GameTowerAnimationType.ANIMATION_TYPE_IDLE.ordinal()]);

        attackAnimation = gameCharacterAnimations[GameCharacterAnimationType.ANIMATION_TYPE_IDLE.ordinal()];
        this.level = level;
        this.type = type;

    }

    // Animation auf Angriff ändern und Timer für Länge starten
    public void attack() {
        if (attackAnimation != null) {
            attacking = true;
            setAnimation(attackAnimation);
            resetAccTime();
        }
    }

    @Override
    public void draw(Batch batch, float deltaTime, float parentAlpha) {
        super.draw(batch, deltaTime, parentAlpha);

        // Angriffs-Animation zurücksetzen, wenn sie durchgelaufen ist.
        if (attacking && attackAnimation.isAnimationFinished(getAccTime())) {
            attacking = false;
            setAnimation(idleAnimation);
            resetAccTime();
        }
    }
}
