package com.gatdsen.ui.hud;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.gatdsen.ui.assets.AssetContainer;

/**
 * Creates an Image that is only drawn for a limited amount of time.
 * After that it removes itself from the stage.
 */
public class ImagePopup extends Image {

	float drawDuration;
	float currentTime;

	float width;
	float height;
	private TextureRegion sprite;

	private Color outlineColor;
	private float outlineThickness;


	/**
	 * Creates an {@link ImagePopup} wich is drawn for a limited amount of time.
	 * @param image to Draw
	 * @param posX
	 * @param posY
	 * @param initalDelay in seconds
	 * @param drawDuration in seconds
	 */

	public ImagePopup(TextureRegion image,float posX,float posY, float initalDelay,float drawDuration){
		this(image,posX,posY,initalDelay,drawDuration,-1,-1,null);
	}
	public ImagePopup(TextureRegion image,float posX,float posY, float initalDelay,float drawDuration,float prefWidth,float prefHeight, Color outlinecolor){
		this(image,posX,posY,initalDelay,drawDuration,prefWidth,prefHeight,outlinecolor,1);
	}
	public ImagePopup(TextureRegion image,float posX,float posY, float initalDelay,float drawDuration,float prefWidth,float prefHeight, Color outlinecolor,float outlineThickness){
		super(image);
		setPosition(posX,posY);
		this.currentTime = -initalDelay;
		this.drawDuration = drawDuration;
		if(prefHeight>0) {
			this.height = prefHeight;
		}
		else {
			this.height = getPrefHeight();
		}
		if(prefWidth>0){

			this.width=prefWidth;
		}
		else {
			this.width = getPrefWidth();
		}

		this.sprite = image;
		this.outlineColor = outlinecolor;
		this.outlineThickness = outlineThickness;
	}
	/**
	 * Creates an {@link ImagePopup} wich is drawn for a limited amount of time.
	 * @param image to draw
	 * @param drawDuration in seconds
	 */
	public ImagePopup(TextureRegion image,float drawDuration){
		this(image,0,0,0,drawDuration);
	}


	public ImagePopup(TextureRegion image,float drawDuration,float width,float height,Color outlinecolor){
		this(image,0,0,0,drawDuration,width,height,outlinecolor);
	}
	public ImagePopup(TextureRegion image,float drawDuration,float width,float height){
		this(image,0,0,0,drawDuration,width,height,null);
	}
	public ImagePopup(TextureRegion image,float drawDuration,float width,float height,Color outlinecolor,float outlineThickness) {
		this(image,0,0,0,drawDuration,width,height,outlinecolor,outlineThickness);
	}

	/**
	 * Returns the width for sizing the {@link com.badlogic.gdx.scenes.scene2d.ui.Container}
	 */
	public float getWidthForContainer(){
		return this.width;
	}

	/**
	 * Returns the height for sizing the {@link com.badlogic.gdx.scenes.scene2d.ui.Container}
	 */
	public float getHeightForContainer(){
		return this.height;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if(drawDuration>0||currentTime<0) {
			currentTime += Gdx.graphics.getDeltaTime();
		}
		if(currentTime>=0) {

			Color teamColor = outlineColor;
			if(teamColor !=null) {
				//todo draw background with a sprite
				batch.flush();

				ShaderProgram shader = AssetContainer.IngameAssets.lookupOutlineShader;
				batch.setShader(shader);
				shader.setUniformf("outline_color", teamColor);
				shader.setUniformf("line_thickness", outlineThickness);
				Texture texture = sprite.getTexture();
				shader.setUniformf("tex_size", new Vector2(texture.getWidth(), texture.getHeight()));
				shader.setUniformi("u_skin", 1);
				Gdx.gl.glActiveTexture(GL20.GL_TEXTURE1);
				TextureRegion skinFrame = sprite;
				skinFrame.getTexture().bind();
				shader.setUniformf("flipped", 0);
				shader.setUniformf("v_skinBounds",
						skinFrame.getU(),
						skinFrame.getV(),
						skinFrame.getU2() - skinFrame.getU(),
						skinFrame.getV2() - skinFrame.getV());
				Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
				super.draw(batch, parentAlpha);
				batch.flush();
				batch.setShader(null);
			}
			super.draw(batch, parentAlpha);
			if(drawDuration>0&&currentTime>drawDuration){
				remove();
			}

		}
	}

}
