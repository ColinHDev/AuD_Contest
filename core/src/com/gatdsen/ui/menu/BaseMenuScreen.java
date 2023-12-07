package com.gatdsen.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gatdsen.manager.run.config.RunConfiguration;
import com.gatdsen.ui.ConfigScreen;
import com.gatdsen.ui.GADS;
import com.gatdsen.ui.assets.AssetContainer;


public abstract class BaseMenuScreen extends ConfigScreen {
    protected RunConfiguration passedRunConfig;
    protected Image title;
    protected Viewport menuViewport;
    protected Viewport backgroundViewport;
    protected GADS gameInstance;
    protected Stage mainMenuStage;
    protected Camera camera;
    protected TextureRegion backgroundTextureRegion;
    protected SpriteBatch menuSpriteBatch;

    /**
     * Konstruktor für die Klasse BaseMenuScreen
     * @param gameInstance Eine Instanz des GADS-Spiels
     */
    public BaseMenuScreen(GADS gameInstance) {
        this.gameInstance = gameInstance;
        TextureRegion titleSprite = AssetContainer.MainMenuAssets.titleSprite;
        this.backgroundTextureRegion = AssetContainer.MainMenuAssets.background;
        this.camera = new OrthographicCamera(30, 30 * (Gdx.graphics.getHeight() * 1f / Gdx.graphics.getWidth()));
        menuViewport = new ExtendViewport(titleSprite.getRegionWidth() / 3f, titleSprite.getRegionWidth() + 100, camera);
        backgroundViewport = new FillViewport(backgroundTextureRegion.getRegionWidth(), backgroundTextureRegion.getRegionHeight());
        mainMenuStage = new Stage(menuViewport);
        menuSpriteBatch = new SpriteBatch();
        setupMenuScreen();
    }

    /**
     * Abstrakte Methode, die einen Titel-String zurückgibt
     * @return den Titel der Seite
     */
    abstract String getTitelString();

    /**
     * Abstrakte Methode, die den Inhalt als Actor für die Benutzeroberfläche zurückgibt
     *
     * @param skin Das Skin-Objekt für die Benutzeroberfläche
     * @return Ein Actor-Objekt, das den Inhalt repräsentiert
     */
    abstract Actor getContent(Skin skin);

    /**
     * Abstrakte Methode, die den nächsten Bildschirmzustand zurückgibt
     *
     * @return Der nächste Bildschirmzustand (ScreenState)
     */
    abstract GADS.ScreenState getNext();

    /**
     * Abstrakte Methode, die den vorherigen Bildschirmzustand zurückgibt
     *
     * @return Der vorherige Bildschirmzustand (ScreenState)
     */
    abstract GADS.ScreenState getPrev();

    /**
     * Initialisiert den Basismenübildschirm
     */
    public void setupMenuScreen() {
        Table menuTable;
        Skin skin = AssetContainer.MainMenuAssets.skin;
        Label titelLabel = new Label(getTitelString(), skin);


        menuTable = new Table(skin);
        menuTable.setFillParent(true);
        menuTable.center();
        menuTable.add(titelLabel).colspan(4).pad(10).center().row();
        menuTable.add(getContent(skin)).center().row();
        Table navigationTable = new Table(skin);

        if (getPrev() != null) {
            TextButton backButton = new TextButton("Zurück", skin);
            backButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    gameInstance.setScreen(GADS.ScreenState.MAINSCREEN, null);
                }
            });
            navigationTable.add(backButton).colspan(4).pad(10).width(200);
        }
        else {
            TextButton exitButton = new TextButton("Beenden", skin);
            exitButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    Gdx.app.exit();
                }
            });
            navigationTable.add(exitButton).colspan(4).pad(10).width(200);
        }
        if (getNext() == GADS.ScreenState.INGAMESCREEN) {
            TextButton startGameButton = new TextButton("Start", skin);
            startGameButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    gameInstance.setScreen(GADS.ScreenState.INGAMESCREEN, getRunConfiguration());
                }
            });
            navigationTable.add(startGameButton).colspan(4).pad(10).width(200);
        } else if (getNext()!= null) {
            TextButton nextGameButton = new TextButton("Weiter", skin);
            nextGameButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    gameInstance.setScreen(GADS.ScreenState.INGAMESCREEN, getRunConfiguration());
                }
            });
            navigationTable.add(nextGameButton).colspan(4).pad(10).width(200);
        }
        menuTable.add(navigationTable);
        mainMenuStage.addActor(menuTable);
    }

    /**
     * setzt Eingaben auf die mainMenuStage. Sorgt dafür, dass Benutzereingaben während des Menüs verarbeitet werden
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(mainMenuStage);
    }

    /**
     * Aktualisieren der Darstellung des Hauptmenüs
     * Rendert die Hintergrundtextur und Benutzeroberfläche wird aktualisiert und gezeichnet
     *
     * @param delta The time in seconds since the last render
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
     * Passt die Viewports bei Änderung der Bildschirmgröße an die neue Auflösung an, um Hauptmenü und Hintergrund korrekt anzuzeigen
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

    /**
     * Pausiert den Bildschirm
     */
    @Override
    public void pause() {
    }

    /**
     * Setzt die Ausführung des Bildschirms fort, nachdem er pausiert wurde
     */
    @Override
    public void resume() {
    }

    /**
     * Versteckt den Bildschirm
     */
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