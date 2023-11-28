package com.gatdsen.ui.menu.layout;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;

public class ActorLayout extends Actor{


	private ArrayList<Layer> allLayers;

	private float layerOffset;
	private float topBorder;

	private Vector2 viewportSize;

	public ActorLayout(){
		this.allLayers = new ArrayList<Layer>();
		layerOffset = 20;
		viewportSize = Vector2.Zero;

	}

	public ActorLayout(Vector2 viewportSize){
		this();
		this.viewportSize = viewportSize;
	}
	public void addLayer(Layer layer){
		addLayer(allLayers.size(),layer);
	}
	public void addLayer(int index, Layer layer){
		allLayers.add(index, layer);
		layer.setHeight((-index*layerOffset)+viewportSize.y);
	}

	public void draw(Batch batch, float parentAlpha){
		for (Layer layer: allLayers) {
			layer.draw(batch,parentAlpha);
		}
	}
}
