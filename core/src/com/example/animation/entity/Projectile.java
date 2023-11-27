package com.example.animation.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Repräsentiert ein Projektil einer Waffe
 */
public class Projectile extends AnimatedEntity {

    public Projectile(Animation<TextureRegion> animation) {
        super(animation);
        setOrigin(new Vector2(0.5f, 0.5f).scl(getSize()));
    }
}
