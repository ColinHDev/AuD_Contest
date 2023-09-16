package com.example.ui.menu.buttons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import java.awt.*;

public class ColoredLabelWithBackground extends Label {

	final	Color defaultBackgroundColor = new Color(0,0,0,0.5f);
	final Color defaultLabelColor = new Color(Color.WHITE);

	boolean enableBackground;
	Drawable background;
	Color backgroundColor;

	public ColoredLabelWithBackground(CharSequence text, Skin skin){
		super(text,skin);
		this.setColor(defaultLabelColor);
		this.enableBackground = false;
	}
	public ColoredLabelWithBackground(CharSequence text, Skin skin, float[] colors) {
		this(text,skin,colors[0],colors[1],colors[2],colors[3]);

	}
	public ColoredLabelWithBackground(CharSequence text, Skin skin, Color color) {
		this(text,skin,color.r,color.g,color.b,color.a);

	}

	public ColoredLabelWithBackground(CharSequence text, Skin skin, Color color,Drawable background) {
		this(text,skin,color.r,color.g,color.b,color.a);
		setBackground(background);
	}

	/**
	 * Farbiges Label mit Hintergrund
	 * @param text Labeltext
	 * @param skin zu verwendender Skin
	 * @param r Rot-Wert der Labelfarbe
	 * @param g Grün-Wert der Labelfarbe
	 * @param b Blau-Wert der Labelfarbe
	 * @param a Alpha-Wert der Labelfarbe
	 * @param br Rot-Wert der Hintergrundfarbe
	 * @param bg Grün-Wert der Hintergrundfarbe
	 * @param bb Blau-Wert der Hintergrundfarbe
	 * @param ba Alpha-Wert der Hintergrundfarbe
	 */
	public ColoredLabelWithBackground(CharSequence text, Skin skin, float r, float g, float b, float a, float br, float bg, float bb,float ba){

		super(text,skin);

		//skin/style des Labels ändern um mit der gewünschten farbe, die skinfarbe zu überschreiben
		setColor(new Color(r,g,b,a));


		//Hintergrund erstellen
		this.enableBackground = true;
		this.backgroundColor = new Color(br,bg,bb,ba);
		this.background= skin.getDrawable("base");

	}
	public ColoredLabelWithBackground(CharSequence text,Skin skin, float r, float g, float b,float a){
		this(text,skin,r,g,b,a,0,0,0,0);
		this.backgroundColor = defaultBackgroundColor;
	}

	 void drawBackground (Batch batch, float parentAlpha, float x, float y) {
		if (background == null || !enableBackground) return;
		batch.setColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a * parentAlpha);
		background.draw(batch, x, y, getWidth(), getHeight());
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		drawBackground(batch,parentAlpha,this.getX(),this.getY());
		super.draw(batch, parentAlpha);
	}


	public void  enableBackground(boolean enable){
		this.enableBackground = enable;
	}

	public void setBackground(Drawable background){
		if(background!=null) {
			this.background = background;
		}
	}

	@Override
	public void setColor(Color color) {
		if(color!=null){
			LabelStyle style = new LabelStyle(getStyle());
			style.fontColor = color;
			this.setStyle(style);
		}

	}

	@Override
	public void setColor(float r, float g, float b, float a) {
		this.setColor(new Color(r,g,b,a));
	}

	@Override
	public Color getColor(){
		return new Color(getStyle().fontColor);
	}

}
