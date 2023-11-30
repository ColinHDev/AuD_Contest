package com.gatdsen.ui.menu;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gatdsen.manager.player.HumanPlayer;
import com.gatdsen.manager.RunConfiguration;
import com.gatdsen.simulation.GameState;
import com.gatdsen.simulation.Simulation;
import com.gatdsen.ui.assets.AssetContainer;
import com.gatdsen.ui.hud.*;

/**
 * Class for taking care of the User Interface.
 * Input Handling during the game.
 * Displaying health, inventory
 */
public class Hud implements Disposable {

    private Stage stage;
	private InputHandler inputHandler;
	private InputMultiplexer inputMultiplexer;

	private TurnTimer turnTimer;
	private Table layoutTable;
	private Container<ImagePopup> turnPopupContainer;
	private InGameScreen inGameScreen;

	private TextureRegion turnChangeSprite;

	private float turnChangeDuration;

	private UiMessenger uiMessenger;

	private FastForwardButton fastForwardButton;

	private RunConfiguration runConfiguration;
	private float renderingSpeed = 1;

	private boolean debugVisible;

	private float[] scores;

	private String[] names;

	private ScoreView scoreView;
	public Hud(InGameScreen ingameScreen, RunConfiguration runConfig) {
		this.runConfiguration = runConfig;

		this.inGameScreen = ingameScreen;

		this.uiMessenger = new UiMessenger(this);

		int viewportSizeX = 1028;
		int viewportSizeY = 1028;
		float animationSpeedupValue = 8;
		turnChangeDuration = 2;
		turnChangeSprite = AssetContainer.IngameAssets.turnChange;


		Camera cam = new OrthographicCamera(viewportSizeX,viewportSizeY);
		//Viewport entweder extend oder Fit -> noch nicht sicher welchen ich nehmen soll
		Viewport viewport= new ExtendViewport(viewportSizeX,viewportSizeY,cam);


		stage = new Stage(viewport);
        layoutTable = setupLayoutTable();

		inputHandler = setupInputHandler(ingameScreen,this);
		inputHandler.setUiMessenger(uiMessenger);

		turnTimer = new TurnTimer(AssetContainer.IngameAssets.turnTimer);
		turnTimer.setCurrentTime(0);
		fastForwardButton =	setupFastForwardButton(uiMessenger, animationSpeedupValue);

		turnPopupContainer = new Container<ImagePopup>();
		layoutHudElements();

		//Combine input from both processors
		inputMultiplexer = new InputMultiplexer();
		//needed for input for the simulation
		inputMultiplexer.addProcessor(inputHandler);
		//input for the ui buttons
		inputMultiplexer.addProcessor(stage);

		stage.addActor(layoutTable);

		scoreView = new ScoreView(null);

	}



	private InputHandler setupInputHandler(InGameScreen ingameScreen,Hud h){
		return new InputHandler(ingameScreen,h);
	}


	/**
	 * Creates a Table for the Button/Element Layout and applies some Settings.
	 * @return
	 */
    private Table setupLayoutTable(){
       Table table = new Table(AssetContainer.MainMenuAssets.skin);

        table.setFillParent(true);
		//align the table to the left of the stage
		table.center();
		return table;
    }

	public void setupScoreboard(GameState game){

		//ToDo read player count and assign individual colors
		ScoreBoard scores = new ScoreBoard(new Color[]{Color.WHITE, Color.WHITE},names, game);

		this.scores = game.getHealth();

		scoreView.addScoreboard(scores);

	}

	public void setPlayerNames(String[] names){
		this.names = names;
	}


	/**
	 * Places Hud Elements inside the Table to define their positions on the screen.
	 */
	private void layoutHudElements() {
		float padding = 10;

		//currently setting the element size of elements in their class file: hardcoded
		//changing the size via the table/actor methods does not really work. could be a fault of not implementing the ui elementparents correctly
		//-> yet it is a bit too much work for now
		//Todo Refactor resizing of every Ui element



		//set a fixed size for the turnPopupContainer, so it will not change the layout, once the turn Sprite is added
		layoutTable.add(turnPopupContainer).pad(padding).expandX().expandY().size(750,750).fill();
		layoutTable.row();
		layoutTable.add(fastForwardButton).pad(padding).left().bottom().size(64,64);

		layoutTable.add(turnTimer).pad(padding).right().bottom();
	}


	/**
	 * Creates a {@link FastForwardButton} with the correct sprites.
	 * @param uiMessenger
	 * @param speedUp
	 * @return
	 */
	private FastForwardButton setupFastForwardButton(UiMessenger uiMessenger,float speedUp){

		FastForwardButton button = new FastForwardButton(new TextureRegionDrawable(AssetContainer.IngameAssets.fastForwardButton),
				new TextureRegionDrawable(AssetContainer.IngameAssets.fastForwardButtonPressed),
				new TextureRegionDrawable(AssetContainer.IngameAssets.fastForwardButtonChecked),
				uiMessenger,speedUp);
		return button;
	}

	/**
	 * Input Processor handling all of the Inputs meant to be sent to {@link Simulation} via {@link HumanPlayer}
	 * @return
	 */
	public InputHandler getInputHandler() {
		return inputHandler;
	}

	/**
	 * Returns all Input Processors inside a Multiplexer.
	 * @return
	 */
	public InputProcessor getInputProcessor(){
		return inputMultiplexer;
	}

	public void draw() {
		//apply the viewport, so the glViewport is using the correct settings for drawing

		stage.getViewport().apply(true);
       	stage.draw();
		   if(scoreView!=null){
			   scoreView.draw();
		   }
	}

	protected void tick(float delta) {
		inputHandler.tick(delta);
        stage.act(delta);
	}



	/**
	 * Creates a Turn Change Popup for {@link Hud#turnChangeDuration} second, with a hardcoded height of 300,300
	 */
	public void createTurnChangePopup(Color outlinecolor) {
		drawImagePopup(new ImagePopup(turnChangeSprite,turnChangeDuration/renderingSpeed,turnChangeSprite.getRegionWidth()*8,turnChangeSprite.getRegionHeight()*8,outlinecolor),false);
	}

	public void drawImagePopup(ImagePopup image,boolean center){
		if(turnPopupContainer.hasChildren()) {
			turnPopupContainer.removeActorAt(0,false);
		}
		turnPopupContainer.setActor(image);
		if(center) {
			turnPopupContainer.center();
		}
		else {
			turnPopupContainer.top();
		}
		image.setScaling(Scaling.fit);
		turnPopupContainer.fill();
		turnPopupContainer.maxSize(image.getWidthForContainer(),image.getHeightForContainer());
	}

	public void resizeViewport(int width, int height) {
		stage.getViewport().update(width, height, true);
		if(scoreView!=null) {
			scoreView.getViewport().update(width, height, true);
		}
	}

	public UiMessenger getUiMessenger() {
		return uiMessenger;
	}

	/**
	 * Changes the animation playback speed. And adjustes the turn wait time.
	 * @param speed Will multiply with the normal playback.
	 */
	public void setRenderingSpeed(float speed){
		inGameScreen.setRenderingSpeed(speed);
		inputHandler.turnChangeSpeedup(speed);
		this.renderingSpeed = speed;

	}

	/**
	 * Sets the value of the remaining turn time to display.
	 * @param time
	 */
	public void setTurntimeRemaining(int time){
		turnTimer.setCurrentTime(time);
	}


	public void startTurnTimer(int seconds){
		turnTimer.startTimer(seconds);
	}

	public void stopTurnTimer(){
		turnTimer.stopTimer();
	}


	@Override
	public void dispose() {
		stage.dispose();
	}


	public void toggleDebugOutlines() {
		this.debugVisible = !debugVisible;

		this.layoutTable.setDebug(debugVisible);
	}

	public void toggleScores(){
		if(scoreView!=null) {
			scoreView.toggleEnabled();
		}
	}

	public void adjustScores(float[] scores){
		this.scores = scores;

		if(scoreView!=null){
			scoreView.adjustScores(scores);
		}

	}

	public void adjustScores(int team, float score){
		this.scores[team] = score;

		if(scoreView!=null){
			scoreView.adjustScores(scores);
		}

	}




	public void gameEnded(boolean won,int team,boolean isDraw) {
		gameEnded(won,team,isDraw,null);
	}

	/**
	 * Creates a popup Display for displaying the GameOver Situation and Tints the Screen in a semi-Transparent Black
	 * @param won
	 * @param team
	 */
	public void gameEnded(boolean won,int team,boolean isDraw, Color color){

		//create a pixel with a set color that will be used as Background
		Pixmap pixmap = new Pixmap(1,1, Pixmap.Format.RGBA8888);
		//set the color to black
		pixmap.setColor(0,0,0,0.5f);
		pixmap.fill();
		layoutTable.setBackground( new TextureRegionDrawable(new Texture(pixmap)));
		pixmap.dispose();

		ImagePopup display;

		//determine sprite
		if(isDraw){
			display = new ImagePopup(AssetContainer.IngameAssets.drawDisplay,-1,
					AssetContainer.IngameAssets.drawDisplay.getRegionWidth()*2,
					AssetContainer.IngameAssets.drawDisplay.getRegionHeight()*2);
		}
		else if(won){
			display= new ImagePopup(AssetContainer.IngameAssets.victoryDisplay,-1,
					AssetContainer.IngameAssets.victoryDisplay.getRegionWidth()*2,
					AssetContainer.IngameAssets.victoryDisplay.getRegionHeight()*2,color,2f);
		}
		else {
			display = new ImagePopup(AssetContainer.IngameAssets.lossDisplay, -1,
					AssetContainer.IngameAssets.lossDisplay.getRegionWidth()*2,
					AssetContainer.IngameAssets.lossDisplay.getRegionHeight()*2,color,2f);
		}
		drawImagePopup(display,true);

	}

	public void skipTurnStart() {
		if (turnPopupContainer.getActor() != null)
			turnPopupContainer.getActor().remove();
	}


	public void newGame(GameState state){
		layoutTable.setBackground((Drawable) null);
		if(turnPopupContainer.hasChildren()) {
			turnPopupContainer.removeActorAt(0, false);
		}
		setupScoreboard(state);
	}
}
