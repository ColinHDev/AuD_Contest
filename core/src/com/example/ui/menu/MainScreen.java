package com.example.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.example.manager.Manager;
import com.example.manager.RunConfiguration;
import com.example.ui.GADS;
import com.example.ui.assets.AssetContainer;

import javax.swing.*;

public class MainScreen implements Screen {
    Table menuTable;
    private RunConfiguration passedRunConfig;
    private Image title;
    private Viewport menuViewport;
    private Viewport backgroundViewport;
    private GADS gameInstance;
    private Stage mainMenuStage;
    private Camera camera;
    private TextureRegion backgroundTextureRegion;
    private SpriteBatch menuSpriteBatch;

    /**
     * setzt Eingaben auf die mainMenuStage. Sorgt dafür, dass Benutzereingaben während des Menüs verarbeitet werden.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(mainMenuStage);
    }

    /**
     * Konstruktor, welcher Kamera, Viewports, Stage und SpriteBatch initialisiert. ruft setupMenuScreen auf, um UI für Hauptmenü einzurichten
     *
     * @param gameInstance
     */
    public MainScreen(GADS gameInstance) {

        this.gameInstance = gameInstance;
        TextureRegion titleSprite = AssetContainer.MainMenuAssets.titleSprite;

        this.backgroundTextureRegion = AssetContainer.MainMenuAssets.background;
        this.camera = new OrthographicCamera(30, 30 * (Gdx.graphics.getHeight() * 1f / Gdx.graphics.getWidth()));
        //set the viewport, world with and height are currently the one of the title sprite, so the table is always on screen
        //the world sizes are roughly estimating the table size in title image width, no way of getting the size of the button table/it did not really work out
        menuViewport = new ExtendViewport(titleSprite.getRegionWidth() / 3f, titleSprite.getRegionWidth() + 100, camera);
        backgroundViewport = new FillViewport(backgroundTextureRegion.getRegionWidth(), backgroundTextureRegion.getRegionHeight());
        mainMenuStage = new Stage(menuViewport);

        menuSpriteBatch = new SpriteBatch();
        //create a table, holds ui widgets like buttons and textfields

        setupMenuScreen();
    }

    /**
     * Erstellt Nachrichten Box für Informationen.
     *
     * @param infoMessage Text Inhalt des Fensters
     * @param titleBar    Titel des Fensters
     */
    public static void infoBox(String infoMessage, String titleBar) {
        JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * erstellt die Titel Seite mithilfe der Tabelle aus LibGDX mit zugehörigem Titel und Schaltflächen
     */
    public void setupMenuScreen() {

        Skin skin = AssetContainer.MainMenuAssets.skin;
        TextureRegion titleSprite = AssetContainer.MainMenuAssets.titleSprite;
        Manager.NamedPlayerClass[] availableBots = Manager.getPossiblePlayers();

        Label titelLabel = new Label("Titel", skin);
        titelLabel.setAlignment(Align.center);
        TextButton normalGameModeButton = new TextButton("Normal", skin);
        normalGameModeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameInstance.setScreen(GADS.ScreenState.NORMALMODESCREEN);
            }
        });
        TextButton exitButton = new TextButton("Beenden", skin);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        menuTable = new Table(skin);
        menuTable.setFillParent(true);
        menuTable.center();
        menuTable.add(titelLabel).colspan(4).pad(10);
        menuTable.row();
        menuTable.add(normalGameModeButton).colspan(4).pad(10).width(200);
        menuTable.row();
        menuTable.add(exitButton).colspan(4).pad(10).width(200);
        mainMenuStage.addActor(menuTable);
    }

    /**
     * Aktualisieren der Darstellung des Hauptmenüs.
     * Rendert die Hintergrundtextur und Benutzeroberfläche wird aktualisiert und gezeichnet.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        camera.update();

        backgroundViewport.apply(true);
        menuSpriteBatch.setProjectionMatrix(backgroundViewport.getCamera().combined);
        menuSpriteBatch.begin();
        this.menuSpriteBatch.draw(backgroundTextureRegion, 0, 0);

        menuSpriteBatch.end();
        menuViewport.apply(true);
        menuSpriteBatch.setProjectionMatrix(menuViewport.getCamera().combined);
        mainMenuStage.act(delta);
        mainMenuStage.draw();
    }

    /**
     * Passt die Viewports bei Änderung der Bildschirmgröße an die neue Auflösung an, um Hauptmenü und Hintergrund korrekt anzuzeigen.
     *
     * @param width  Breite des Bildschirms nach Änderung
     * @param height Höhe des Bildschirms nach Änderung
     */
    @Override
    public void resize(int width, int height) {
        menuViewport.update(width, height, true);

        menuViewport.apply();
        backgroundViewport.update(width, height, true);

        backgroundViewport.apply();
        camera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    /**
     * gibt Speicher frei, der von mainMenuStage genutzt wurde
     */
    @Override
    public void dispose() {
        mainMenuStage.dispose();
    }
}
