package com.gatdsen.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gatdsen.simulation.action.ProjectileAction;
import com.gatdsen.animation.entity.AnimatedEntity;
import com.gatdsen.animation.entity.Entity;
import com.gatdsen.ui.assets.AssetContainer;

public class Projectiles {


    protected static Entity summon(ProjectileAction.ProjectileType type){
        Entity projectile;
        //configuring of drawing properties [0]:AnimatedEntity.rotate [1]:AnimatedEntity.mirror
        boolean[] settings;
        Animation<TextureRegion> animation;
        AnimatedEntity animatedEntity;
        switch (type){
            case EXAMPLE_PROJECTILE:
                animation = AssetContainer.IngameAssets.projectiles.get(type);
                animatedEntity = new AnimatedEntity(animation);
                animatedEntity.setOrigin(new Vector2(animation.getKeyFrame(0).getRegionWidth()/2f, animation.getKeyFrame(0).getRegionHeight()/2f));
                projectile = animatedEntity;
                break;
            default:
                animation = AssetContainer.IngameAssets.coolCat;
                animatedEntity = new AnimatedEntity(animation);
                animatedEntity.setRotate(true);
                animatedEntity.setMirror(false);
                projectile = animatedEntity;
                System.err.println("Warning: Projectile-Type " + type + " is not Supported!");

        }
        return projectile;
    }
}
