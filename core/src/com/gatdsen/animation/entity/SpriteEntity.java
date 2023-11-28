package com.gatdsen.animation.entity;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Verhält sich wie ein Entity mit dem Unterschied, dass beim draw() Aufruf
 * eine Textur mit der absoluten Position des Entity gerendert wird
 */
public class SpriteEntity extends Entity {

    public interface ShaderHandler {
        void beforeDraw(Batch batch);
    }

    private ShaderHandler shaderHandler;
    private TextureRegion textureRegion;

    private boolean visible = true;

    private Color color = null;
    private Vector2 size = null;


    /**
     * When mirror is set to true, the character will be flipped once the angle is 180 or higher on the x axis
     */

    private boolean mirror = false;

    private Vector2 origin = new Vector2();

    public SpriteEntity(TextureRegion textureRegion) {
        super();
        this.textureRegion = textureRegion;
    }

    public SpriteEntity(TextureRegion textureRegion, Vector2 relPos, Vector2 size) {
        super();
        this.textureRegion = textureRegion;
        setRelPos(relPos);
        setSize(size);
    }

    @Override
    public void draw(Batch batch, float deltaTime, float parentAlpha) {
        if (visible) {
            if (shaderHandler != null) shaderHandler.beforeDraw(batch);
            if (color != null)
                batch.setColor(color);

            Vector2 scale = getScale();
            if (size == null)
                batch.draw(textureRegion,
                        getPos().x - origin.x,
                        getPos().y - origin.y,
                        origin.x,
                        origin.y,
                        textureRegion.getRegionWidth(),
                        textureRegion.getRegionHeight(),
                        (isFlipped() ? -1 : 1) * (scale.x),
                        (scale.y),
                        getAngle());
            else
                batch.draw(textureRegion,
                        getPos().x - origin.x,
                        getPos().y - origin.y,
                        origin.x,
                        origin.y,
                        size.x,
                        size.y,
                        (isFlipped() ? -1 : 1) * (scale.x),
                        (scale.y),
                        getAngle());

            //    batch.draw(textureRegion, getPos().x, getPos().y, 0, 0, size.x, size.y, scale.x, scale.y, getRotationAngle());

            if (color != null)
                batch.setColor(Color.WHITE);
            if (shaderHandler != null) {
                batch.flush();
                batch.setShader(null);
            }
        }
        super.draw(batch, deltaTime, parentAlpha);
    }


    public Vector2 getSize() {
        if (size != null) {
            return new Vector2(size);
        }
        return new Vector2(0, 0);
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }


    public Vector2 getOrigin() {
        return origin;
    }

    public void setOrigin(Vector2 origin) {
        this.origin = origin;
    }


    /**
     * Returns the center of the rendered Sprite as a Vector.
     * This is calculated with the Sprites Scale
     *
     * @return Vector of the sprite center.
     */
    public Vector2 getSpriteCenter() {
        if (this.textureRegion != null) {
            return new Vector2(textureRegion.getRegionWidth() / 2f, textureRegion.getRegionHeight() / 2f);
        }
        return new Vector2(0, 0);
    }

    /**
     * Will tint the sprite with the specified color during rendering.
     * Null will not tint the sprite.
     *
     * @param color specified tint of the sprite
     */
    public void setColor(Color color) {
        this.color = color;
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    /**
     * @return specified tint of the sprite
     */
    public Color getColor() {
        return color;
    }

    public void setMirror(boolean mirror) {
        this.mirror = mirror;
    }

    /**
     * Sets the angle of this entity
     *
     * @param angle angle in degrees
     */
    @Override
    public void setRelAngle(float angle) {
        angle = ((angle % 360) + 360) % 360;
        if (angle >= 90f && angle <= 270f) {
            super.setRelAngle(mirror ? angle - 180 : angle);
            setRelFlipped(mirror);
        } else {
            super.setRelAngle(angle);
            setRelFlipped(false);
        }
    }

    public ShaderHandler getShaderHandler() {
        return shaderHandler;
    }

    public void setShaderHandler(ShaderHandler shaderHandler) {
        this.shaderHandler = shaderHandler;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
