package com.gatdsen.ui.menu;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.*;
import com.gatdsen.animation.Animator;
import com.gatdsen.animation.AnimatorCamera;
import com.gatdsen.manager.*;
import com.gatdsen.manager.run.config.RunConfiguration;
import com.gatdsen.simulation.GameState;
import com.gatdsen.simulation.action.Action;
import com.gatdsen.simulation.action.ActionLog;
import com.gatdsen.ui.ConfigScreen;
import com.gatdsen.ui.GADS;
import com.gatdsen.ui.assets.AssetContainer;
import com.gatdsen.ui.debugView.DebugView;

/**
 * Der Screen welcher ein aktives Spiel anzeigt.
 */
public class InGameScreen extends ConfigScreen implements AnimationLogProcessor {

    private final Manager manager;
    private Viewport gameViewport;
    private float worldWidth = 50 * 200;
    private float worldHeight = 30 * 200;

    private float renderingSpeed = 1;

    //should HUD be handled by GADS
    private Hud hud;
    private Animator animator;
    private final GADS gameManager;
    private Run run;

    private DebugView debugView;

    public InGameScreen(GADS instance) {

        gameManager = instance;
        gameViewport = new FitViewport(worldWidth, worldHeight + 500);

        hud = new Hud(this, gameViewport);

        debugView = new DebugView(AssetContainer.MainMenuAssets.skin);

        setupInput();

        manager = Manager.getManager();
        animator = new Animator(gameViewport, hud.getUiMessenger());
    }

    @Override
    protected void setRunConfiguration(RunConfiguration runConfiguration) {
        //update runconfig
        super.setRunConfiguration(runConfiguration);
        this.runConfiguration.gui = true;
        this.runConfiguration.animationLogProcessor = this;
        this.runConfiguration.uiMessenger = hud.getUiMessenger();
        this.runConfiguration.inputProcessor = hud.getInputHandler();

        run = manager.startRun(this.runConfiguration);
        if (run == null) {
            throw new RuntimeException("Can't start game with an invalid RunConfiguration!");
        }
    }

    //gets called when the screen becomes the main screen of GADS
    @Override
    public void show() {
        hud.show();
        animator.show();
    }

    public void setRenderingSpeed(float speed) {
        //negative deltaTime is not allowed
        if (speed >= 0) this.renderingSpeed = speed;
    }

    @Override
    public void render(float delta) {
        hud.tick(delta);
        animator.render(renderingSpeed * delta);
        hud.draw();
        debugView.draw();
    }

    @Override
    public void init(GameState state, String[] playerNames, String[][] skins) {
        //ToDo the game is starting remove waiting screen etc.

        animator.init(state, playerNames, skins);

        int tileSize = animator.playerMaps[0].getTileSize();

        Vector2[] positionTileMaps = new Vector2[]{animator.playerMaps[0].getPos(), animator.playerMaps[1].getPos()};

        hud.setPlayerNames(playerNames);
        hud.newGame(state, positionTileMaps, tileSize, animator.playerMaps[0]);
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
        hud.resizeViewport(width, height);
        gameViewport.update(width, height);
        debugView.getViewport().update(width, height);

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

    public void setupInput() {

        //animator als actor?
        //       simulation als actor?
        Gdx.input.setInputProcessor(hud.getInputProcessor());

    }

    /**
     * Converts Viewport/Screen-Coordinates to World/Ingame-Position
     *
     * @param coordinates to convert.
     * @return Vector with World-Coordinate
     */
    public Vector2 toWorldCoordinates(Vector2 coordinates) {
        Vector3 position = gameViewport.unproject(new Vector3(coordinates.x, coordinates.y, 0));
        return new Vector2(position.x, position.y);
    }

    //this section handles the input
    public void processInputs(float[] ingameCameraDirection, float zoomPressed) {
        AnimatorCamera camera = animator.getCamera();
        camera.setDirections(ingameCameraDirection);
        camera.setZoomPressed(zoomPressed);
    }

    public void resetCamera() {
        animator.getCamera().resetCamera();
    }

    public void toggleCameraMove() {
        animator.getCamera().toggleCanMoveToVector();
    }

    public void toggleDebugView() {
        debugView.toggleDebugView();
        hud.toggleDebugOutlines();
    }

    public void moveCameraByOffset(Vector2 offset) {
        animator.getCamera().moveByOffset(offset);
    }

    /**
     * Calls AnimatorCamera function to Zoom.
     *
     * @param zoom Value that shall be added to the zoom
     */
    public void zoomCamera(float zoom) {
        AnimatorCamera camera = animator.getCamera();
        camera.addZoomPercent(zoom);
    }

    public void skipTurnStart() {
        hud.skipTurnStart();
    }
}
