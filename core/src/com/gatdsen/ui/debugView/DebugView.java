package com.gatdsen.ui.debugView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.*;
import com.gatdsen.simulation.action.ActionLog;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Class representing the Debug Hud Element for Viewing current Actiondetails
 */
public class DebugView {


	private final Batch batch;
	private final Viewport viewport;

	private final Stage stage;
	private final DebugTextContainer debugText;
	private boolean isEnabled;
	private final LinkedBlockingQueue<ActionLog> blockingLogQueue = new LinkedBlockingQueue<>();

	public DebugView(Skin skin){


		viewport = new ExtendViewport(600,600);
		viewport.setCamera(new OrthographicCamera(30,30*(Gdx.graphics.getHeight()*1f/Gdx.graphics.getWidth())));
		viewport.apply();


		this.batch = new SpriteBatch();

		stage = new Stage(viewport,batch);
		debugText = new DebugTextContainer(skin,viewport);
		stage.addActor(debugText);
		isEnabled = false;

	}


	/**
	 * Adds an Action Log to the Debugview.
	 * @param log
	 */
	public void add(ActionLog log){
		blockingLogQueue.add(log);
	}

	public void draw() {

			//gather a queuedLog and add them to the table
			ActionLog log = blockingLogQueue.poll();
			if(log!=null) {
				debugText.addActionLog(log);
			}


		//draw the text
		if(stage!=null&&isEnabled) {
			viewport.apply(true);
			batch.setProjectionMatrix(viewport.getCamera().combined);

			stage.draw();
		}
	}

	public Viewport getViewport() {
		return viewport;
	}

	public void toggleDebugView() {
		isEnabled = !isEnabled;
	}

}
