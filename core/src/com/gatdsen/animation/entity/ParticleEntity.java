package com.gatdsen.animation.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

public class ParticleEntity extends Entity implements Disposable, Pool.Poolable {
    private ParticleEffectPool.PooledEffect effect;

    private static final Pool<ParticleEntity> particleEntityPool = new Pool<ParticleEntity>() {

        @Override
        protected ParticleEntity newObject() {
            return new ParticleEntity();
        }
    };
    private boolean loop = false;

    private ParticleEntity() {
    }

    public static ParticleEntity getParticleEntity(ParticleEffectPool pool) {
        ParticleEntity entity = particleEntityPool.obtain();
        entity.effect = pool.obtain();
        entity.effect.setPosition(entity.getPos().x, entity.getPos().y);
        return entity;
    }

    @Override
    protected void setPos(Vector2 pos) {
        super.setPos(pos);
        effect.setPosition(pos.x, pos.y);
    }

    @Override
    public void setRelAngle(float angle) {
        float oldAngle = getAngle();
        super.setRelAngle(angle);
        float diff = angle - oldAngle;
//        ToDo: Implement dynamic rotation
//        for (ParticleEmitter emitter : effect.getEmitters()
//                ) {
//            ParticleEmitter.ScaledNumericValue emitterRotation = emitter.getRotation();
//            emitterRotation.setHigh(emitterRotation.getHighMin(), em);
//        }
    }

    @Override
    public void setScale(Vector2 scale) {
        super.setScale(scale);
        effect.scaleEffect(scale.x, scale.y, 1);
    }

    @Override
    public void draw(Batch batch, float deltaTime, float parentAlpha) {
        super.draw(batch, deltaTime, parentAlpha);
        effect.draw(batch, deltaTime);
        if (loop && effect.isComplete())
            effect.reset(false);
    }



    public void free () {
        particleEntityPool.free(this);
    }

    @Override
    public void dispose() {
        if (effect != null)
            effect.free();
        effect = null;
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }


    @Override
    public void reset() {
        if (parent != null) parent.remove(this);
        loop = false;
        dispose();
    }
}
