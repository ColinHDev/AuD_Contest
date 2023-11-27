package com.example.ui.menu.buttons;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;


/**
 * Layout Table with a limited column width.
 */
public class LimitedWidthTable extends Table {

	private	int maxColumnWidth;
	private int currentColumnWidth;
	public LimitedWidthTable(Skin skin){
		this(skin,0);

	}

	public LimitedWidthTable(Skin skin, int columnWidth){
		super(skin);
		maxColumnWidth = columnWidth;
		currentColumnWidth = 0;
	}


	@Override
	public <T extends Actor> Cell<T> add(T actor) {
		if(maxColumnWidth>0) {
			if(currentColumnWidth>=maxColumnWidth){
				this.row();
				currentColumnWidth = 0;
			}
			else{currentColumnWidth++;}
		}
		return super.add(actor);
	}

	/**
	 * Remove children and Listeners from the Table.
	 * Resets {@link LimitedWidthTable#currentColumnWidth}
	 */
	public void resetTable(){
		this.clear();
		this.currentColumnWidth=0;
	}
}
