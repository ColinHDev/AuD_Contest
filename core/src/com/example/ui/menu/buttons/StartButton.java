package com.example.ui.menu.buttons;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.example.ui.menu.Menu;

public class StartButton extends TextButton {
	public StartButton(String text, Skin skin, Menu menu) {
		super(text, skin);
		this.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				menu.startGame();
			}
		});
	}
}
