package com.example.ui.menu;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.*;
import com.example.animation.Animator;
import com.example.animation.AnimatorCamera;
import com.example.manager.*;
import com.example.simulation.StaticGameState;
import com.example.simulation.action.Action;
import com.example.simulation.action.ActionLog;
import com.example.ui.ConfigScreen;
import com.example.ui.GADS;
import com.example.ui.assets.AssetContainer;
import com.example.ui.debugView.DebugView;

/**
 * Der Screen welcher ein aktives Spiel anzeigt.
 */
public class InGameScreen extends ConfigScreen implements AnimationLogProcessor  {

    private final Manager manager;
    private Viewport gameViewport;
    private float worldWidth = 80*12;
    private float worldHeight = 80*12;

    private float renderingSpeed = 1;

    //should HUD be handled by GADS
    private Hud hud;
    private Animator animator;
    private final GADS gameManager;

    private Run run;

    private DebugView debugView;
    public InGameScreen(GADS instance, RunConfiguration runConfig){

        gameManager = instance;
        gameViewport = new FillViewport(worldWidth,worldHeight);

        hud = new Hud(this, runConfig);

        debugView = new DebugView(AssetContainer.MainMenuAssets.skin);

        setupInput();

        manager = Manager.getManager();
        animator = new Animator(gameViewport, runConfig.gameMode, runConfig.uiMessenger);
    }

    @Override
    protected void setRunConfiguration(RunConfiguration runConfiguration) {
        //update runconfig
        runConfiguration.gui = true;
        runConfiguration.animationLogProcessor = this;
        runConfiguration.uiMessenger = hud.getUiMessenger();
        runConfiguration.inputProcessor = hud.getInputHandler();

        super.setRunConfiguration(runConfiguration);
        run = manager.startRun(runConfiguration);
        Executable game = run.getGames().get(0);
    }

//gets called when the screen becomes the main screen of GADS
    @Override
    public void show() {
        animator.show();
    }
    public void setRenderingSpeed(float speed){
        //negative deltaTime is not allowed
        if(speed>=0) this.renderingSpeed = speed;
    }

    @Override
    public void render(float delta) {
        hud.tick(delta);
        animator.render(renderingSpeed*delta);
        hud.draw();
        debugView.draw();
    }

    @Override
    public void init(StaticGameState state, String[] playerNames, String[][] skins) {
        //ToDo the game is starting remove waiting screen etc.

        hud.setPlayerNames(playerNames);
        hud.newGame(state);
        animator.init(state,playerNames, skins);
    }

    /**
     * Forwards the ActionLog to the Animator for processing
     *
     * @param log Queue of all {@link Action animation-related Actions}
     */
    public void animate(ActionLog log) {
        animator.animate(log);
        debugView.add(log);
    }


    @Override
    public void awaitNotification() {
        animator.awaitNotification();
    }

    @Override
    public void resize(int width, int height) {
        animator.resize(width, height);
        hud.resizeViewport(width,height);
        gameViewport.update(width,height);
        debugView.getViewport().update(width,height);

    }

    @Override
    public void pause() {
        animator.pause();
    }

    @Override
    public void resume() {
        animator.resume();
    }

    @Override
    public void hide() {
        animator.hide();
    }

    /**
     * Gets called when the application is destroyed or currently when escape is pressed to return to menu. Not the best but the fastest way rn.
     */
    @Override
    public void dispose() {
        animator.dispose();
        manager.stop(run);
        hud.dispose();
        gameManager.setScreen(GADS.ScreenState.MAINSCREEN, null);
    }
    public void setupInput(){

        //animator als actor?
         //       simulation als actor?
        Gdx.input.setInputProcessor(hud.getInputProcessor());

    }

    /**
     * Converts Viewport/Screen-Coordinates to World/Ingame-Position
     * @param coordinates to convert.
     * @return Vector with World-Coordinate
     */
    public Vector2 toWorldCoordinates(Vector2 coordinates){
        Vector3 position = gameViewport.unproject(new Vector3(coordinates.x,coordinates.y,0));
        return new Vector2(position.x,position.y);
    }

    //this section handles the input
    public void processInputs(float[] ingameCameraDirection,float zoomPressed) {
      AnimatorCamera camera = animator.getCamera();
       camera.setDirections(ingameCameraDirection);
       camera.setZoomPressed(zoomPressed);
    }
    public void resetCamera(){
        animator.getCamera().resetCamera();
    }

    public void toggleCameraMove() {
        animator.getCamera().toggleCanMoveToVector();
    }

    public void toggleDebugView() {
        debugView.toggleDebugView();
        hud.toggleDebugOutlines();
    }
    public void moveCameraByOffset(Vector2 offset){
        animator.getCamera().moveByOffset(offset);
    }

    /**
     * Calls AnimatorCamera function to Zoom.
     * @param zoom Value that shall be added to the zoom
     */
    public void zoomCamera(float zoom){
        AnimatorCamera camera = animator.getCamera();
        camera.addZoomPercent(zoom);
    }

    public void skipTurnStart() {
        hud.skipTurnStart();
    }
}
