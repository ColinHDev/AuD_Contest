package com.example.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.example.manager.Manager;
import com.example.manager.RunConfiguration;
import com.example.ui.ConfigScreen;
import com.example.ui.GADS;
import com.example.ui.assets.AssetContainer;

import javax.swing.*;
import java.util.ArrayList;

public class GamemodeNormalScreen extends ConfigScreen {
    Table playerChooseTable;
    Table menuTable;
    Table navigationTable;
    Table mapTable;
    private RunConfiguration passedRunConfig;
    private Image title;
    private Viewport menuViewport;
    private Viewport backgroundViewport;
    private GADS gameInstance;
    private Stage mainMenuStage;
    private Camera camera;
    private TextureRegion backgroundTextureRegion;
    private SpriteBatch menuSpriteBatch;

    private MainScreen mainScreen;

    /**
     * setzt Eingaben auf die mainMenuStage. Sorgt dafür, dass Benutzereingaben während des Menüs verarbeitet werden.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(mainMenuStage);
    }

    /**
     * Konstruktor, welcher Kamera, Viewports, Stage und SpriteBatch initialisiert. ruft setupMenuScreen auf, um UI für Hauptmenü einzurichten
     * Ruft setupMenuScreen auf, um das UI für das Hauptmenü einzurichten.
     *
     * @param gameInstance Die Instanz des Hauptspiels (GADS), die diesem Bildschirm gehört.
     */
    public GamemodeNormalScreen(GADS gameInstance) {

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
     * Erstellt Nachrichten Box für Informationen.
     *
     * @param infoMessage Text Inhalt des Fensters
     * @param titleBar    Titel des Fensters
     */
    public static void infoBox(String infoMessage, String titleBar) {
        JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * erstellt die Spielmodus "Normal" Seite mithilfe der Tabelle aus LibGDX mit zugehörigem Titel und Schaltflächen
     */
    public void setupMenuScreen() {

        Manager.getPossiblePlayers();

        Skin skin = AssetContainer.MainMenuAssets.skin;
        TextureRegion titleSprite = AssetContainer.MainMenuAssets.titleSprite;
        Manager.NamedPlayerClass[] availableBots = Manager.getPossiblePlayers();
        Label titelLabel = new Label("Normaler Spielmodus", skin);
        titelLabel.setAlignment(Align.center);
        Label textLabel1 = new Label("Spieler 1:", skin);
        titelLabel.setAlignment(Align.center);
        Label textLabel2 = new Label("Spieler 2:", skin);
        titelLabel.setAlignment(Align.center);
        Label textLabel3 = new Label("Karte:", skin);
        titelLabel.setAlignment(Align.center);
        final SelectBox<Manager.NamedPlayerClass> player1SelectBox = new SelectBox<>(skin);
        player1SelectBox.setItems(availableBots);
        final SelectBox<Manager.NamedPlayerClass> player2SelectBox = new SelectBox<>(skin);
        player2SelectBox.setItems(availableBots);
        final SelectBox<String> mapSelectBox = new SelectBox<>(skin);
        Array<String> mapNames = loadFileNames("assets/res/maps", "json");
        mapSelectBox.setItems(mapNames);
        TextButton backButton = new TextButton("Zurück", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameInstance.setScreen(GADS.ScreenState.MAINSCREEN, null);
            }
        });
        TextButton startGameButton = getStartButton(skin, player1SelectBox, player2SelectBox, mapSelectBox);

        menuTable = new Table(skin);
        playerChooseTable = new Table(skin);
        navigationTable = new Table(skin);
        mapTable = new Table(skin);
        menuTable.setFillParent(true);
        menuTable.center();
        menuTable.add(titelLabel).colspan(4).pad(10).row();
        playerChooseTable.columnDefaults(0).width(100);
        playerChooseTable.columnDefaults(1).width(100);
        playerChooseTable.add(textLabel1).colspan(4).pad(10);
        playerChooseTable.add(player1SelectBox).colspan(4).pad(10).row();
        playerChooseTable.add(textLabel2).colspan(4).pad(10);
        playerChooseTable.add(player2SelectBox).colspan(4).pad(10).row();
        menuTable.add(playerChooseTable).row();
        mapTable.columnDefaults(0).width(100);
        mapTable.columnDefaults(1).width(100);
        mapTable.add(textLabel3).colspan(4).pad(10);
        mapTable.add(mapSelectBox).colspan(4).pad(10);
        menuTable.add(mapTable).row();
        navigationTable.add(backButton).colspan(4).pad(10).width(200);
        navigationTable.add(startGameButton).colspan(4).pad(10).width(200);
        menuTable.add(navigationTable);
        mainMenuStage.addActor(menuTable);
    }

    /**
     * Erstellt und gibt einen "Start"-Button zurück, der auf Benutzerinteraktion reagiert.
     * @param skin Der Skin für das UI-Element.
     * @param player1SelectBox Die SelectBox für Spieler 1 mit ausgewählten Spielerklassen.
     * @param player2SelectBox Die SelectBox für Spieler 2 mit ausgewählten Spielerklassen
     * @param mapSelectBox Die SelectBox für die Auswahl der Spielkarte.
     * @return Der konfigurierte "Start"-Button.
     */
    private TextButton getStartButton(Skin skin, SelectBox<Manager.NamedPlayerClass> player1SelectBox, SelectBox<Manager.NamedPlayerClass> player2SelectBox, SelectBox<String> mapSelectBox) {
        TextButton startGameButton = new TextButton("Start", skin);
        startGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                runConfiguration.players = new ArrayList<>();
                runConfiguration.players.add (player1SelectBox.getSelected().getClassRef());
                runConfiguration.players.add (player2SelectBox.getSelected().getClassRef());
                runConfiguration.mapName = mapSelectBox.getSelected();

                gameInstance.setScreen(GADS.ScreenState.INGAMESCREEN, runConfiguration);
            }
        });
        return startGameButton;
    }

    /**
     * Liest die Namen der gesuchten Dateien aus dem angegebenen Ordner, die die gesuchte Erweiterung haben in ein Array.
     * @param folderPath Der Pfad zum Ordner, aus dem die Karten geladen werden sollen.
     * @return Eine Array-Liste von Karten-Namen ohne Dateierweiterung.
     */
    private Array<String> loadFileNames(String folderPath, String fileExtension) {
        Array<String> mapNames = new Array<>();
        FileHandle folder = Gdx.files.internal(folderPath);

        if (folder.isDirectory()) {
            for (FileHandle file : folder.list()) {
                if (file.extension().equals(fileExtension)) {
                    String mapName = file.nameWithoutExtension();
                    mapNames.add(mapName);
                }
            }
        }
        return mapNames;
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
