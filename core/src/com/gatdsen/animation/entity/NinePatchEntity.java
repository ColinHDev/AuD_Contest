package com.gatdsen.animation.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Vector2;

public class NinePatchEntity extends Entity {


	NinePatch ninePatch;
	Vector2 maxSize;
	Vector2 size;
	Vector2 origin;

	boolean flipped;
	public NinePatchEntity(NinePatch ninePatch, Vector2 size){
		this.ninePatch = ninePatch;
		this.maxSize=size;
		this.size = size;
		flipped = false;
	}

	@Override
	public void draw(Batch batch, float deltaTime, float parentAlpha) {
		super.draw(batch, deltaTime, parentAlpha);
		Vector2 scale = this.getScale();
		ninePatch.draw(batch,
				getPos().x - origin.x,
				getPos().y - origin.y,
				origin.x,
				origin.y,
				size.x,
				size.y,
				scale.x,
				(flipped ? -1 : 1) * (scale.y),
				getAngle());
	}

	public void setOrigin(Vector2 origin){
		this.origin = new Vector2(origin);
	}

	public void setColor(Color c) {
		ninePatch.setColor(c);
	}
	public Color getColor(){
		return ninePatch.getColor();
	}

	public Vector2 getSize(){
		return new Vector2(size);
	}


	/**
	 * sets the scale but tries to change the size of the ninepatch, until it is too small
	 * BEWARE! Currently only checks for the width, because we only need it for the aim indicator
	 */
	public void setScale(Vector2 scale){

		Vector2 potSize=new Vector2(maxSize).scl(scale);

		//if the rescale size is smaller than the ninepatch min width, we need to begin changing the scale in regards to the maxSize
		//-> otherwise the ninepatch breaks/does not display correctly
		if (potSize.x<ninePatch.getRightWidth()){

			//not exactly sure why it works dis way, i should go sleep
			//i want the scale in relation to the min size
			super.setScale(new Vector2(scale.x*maxSize.x/4,scale.y));
		}
		else {
			//if the ninepatch size is bigger than the minimum, the scaled size can be used.
			//this way the texture is not stretched, and the ninepatch can be used correctly
			size = potSize;
			super.setScale(new Vector2(1,1));
		}
	}
	public void setFlipped(boolean flip){
		this.flipped = flip;
	}


	//copied from spritentity

	@Override
	public void setRelAngle(float angle) {
		angle = ((angle % 360) + 360) % 360;
		super.setRelAngle(angle);
	}

	public Vector2 getMaxSize(){
		return new Vector2(maxSize);
	}
}
