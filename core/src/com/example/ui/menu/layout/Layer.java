package com.example.ui.menu.layout;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;

public abstract class Layer {

	final float DEFAULT_ELEMENT_DISTANCE = 20;
	protected ArrayList<Actor> layerElements;
	protected float elementDistance;
	protected Vector2 anchorPosition;


	public Layer (){
		this.layerElements = new ArrayList<Actor>();
		this.elementDistance = DEFAULT_ELEMENT_DISTANCE;
		this.anchorPosition = Vector2.Zero;
	}

	public int getLayerSize(){
		return layerElements.size();
	}

	public Actor getElementAt(int n){
		return layerElements.get(n);
	}


	public void addElement(Actor element){
			addElement(layerElements.size(), element);
	}
	public void addElement(int index, Actor element){
		layerElements.add(index,element);
		calculateElementPositions();
	}

	public  void removeElement(int index){
		layerElements.remove(index);
		calculateElementPositions();
	}
	public void removeElement(Actor element){
		layerElements.remove(element);
		calculateElementPositions();
	}

	public float getElementDistance(){
		return elementDistance;
	}

	/**
	 * Used to define distance of the Elements to eachother inside the layer.
	 * @param distance
	 */
	public void setElementDistance(float distance){
		this.elementDistance = distance;
		calculateElementPositions();
	}

	/**
	 * y-Position of the Layer.
	 * @param v
	 */
	public void setHeight(float v) {
		this.anchorPosition.y = v;
		calculateElementPositions();
	}
	public void draw(Batch batch, float parentAlpha){
		for (Actor element: layerElements) {
			element.draw(batch,parentAlpha);
		}
	}


	/**
	 * Should be used to recalculate the position of Elements, once the amount of elements change.
	 */
	abstract public void calculateElementPositions();


}
