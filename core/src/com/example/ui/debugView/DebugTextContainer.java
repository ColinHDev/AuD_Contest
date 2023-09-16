package com.example.ui.debugView;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.example.simulation.action.Action;
import com.example.simulation.action.ActionLog;
import com.example.ui.menu.buttons.ColoredLabelWithBackground;

import java.util.Iterator;
import java.util.Stack;


/**
 * Class for Storing the most recent ActionLogs, laying them out and drawing.
 */
public class DebugTextContainer extends VerticalGroup {


		Viewport viewport;

		Skin skin;

		Color textcolor = Color.WHITE;
		float fontscale = 1f;

		//size before elements get removed
		//percent of the viewport
		//currently 3/4 of the screen can be covered
		float maxSizePercent = 0.75f;

		public DebugTextContainer(Skin skin, Viewport viewport) {
			super();
			this.skin = skin;
			this.viewport = viewport;

			//text should align on the top left
			this.columnLeft();
			this.top();
			this.left();
			this.setFillParent(true);

		}

	/**
	 * Adds a String to the Bottom of the TextView
	 * @param string
	 */
	public void add(String string){
		//Create a ColoredLabel to set a specific Color and Background for the label
		ColoredLabelWithBackground label = new ColoredLabelWithBackground(string,skin, textcolor);
		label.setFontScale(fontscale);
		addActor(label);
		adjustSize();
	}

	/** Removes the top Elements once the configured size is reached
	 *
 	 */
	private void adjustSize(){
		while(getPrefHeight()>viewport.getWorldHeight()*maxSizePercent){
		if(!this.getChildren().isEmpty()) {
				removeActorAt(0, false);
			}
		}
	}

		public void addActionLog(ActionLog log) {
			Action root = log.getRootAction();
			//Iterate over the log
			Iterator<Action> currentIterator = log.getRootAction().iterator();
			//Stack to store the position/Iterator where we left of, after descending
			Stack<Iterator<Action>> iteratorStack = new Stack<>();

			iteratorStack.push(currentIterator);

			while (!iteratorStack.isEmpty()) {
				currentIterator = iteratorStack.pop();

				while (currentIterator.hasNext()) {
					Action currentAction = currentIterator.next();
					add(currentAction.toString());
					//if the current Action has children, descend into that list to iterate over them
					if (!currentAction.getChildren().isEmpty()) {
						//remember current position
						iteratorStack.push(currentIterator);
						//go to child of the action
						currentIterator = currentAction.getChildren().iterator();
					}
				}
			}
		}

	}