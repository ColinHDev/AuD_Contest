package com.gatdsen.ui.menu.buttons;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;

/**
 * Class that starts a FileChooser to select a File, when pressed.
 * Not the fanciest way but works rn
 */
public class FileChooserButton extends TextButton {

	private final String labelText = "Selected Replay: ";
	private final FileDialog fileDialog;
	private String selectedFilePath = "";

	public FileChooserButton(Skin skin) {
		super("Select a Replay to start", skin);

		JFrame frame = new JFrame();
		fileDialog = new FileDialog(frame, "Choose a file", FileDialog.LOAD);

		fileDialog.setFilenameFilter(new FilenameFilter() {
			@Override
			public boolean accept(File file, String s) {
				return s.endsWith(".replay");
			}
		});

		this.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				dialogSelectFile();
			}
		});
	}

	private void dialogSelectFile() {
		fileDialog.setVisible(true);
		String filename = fileDialog.getFile();
		String path = fileDialog.getDirectory();
		if (filename == null) {
			System.out.println("You cancelled the choice");
		}
		else {
			setSelected(filename,path);
			System.out.println("You chose " +path+ filename);
		}
	}

	private void setSelected(String selectedName,String selectedFilePath) {

		setText(labelText + selectedName);
		this.selectedFilePath = selectedFilePath+selectedName;
	}


	public String getSelectedFilePath(){
		return selectedFilePath;
	}
}
