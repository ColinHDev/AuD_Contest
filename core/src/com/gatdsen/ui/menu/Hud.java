package com.gatdsen.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gatdsen.animation.entity.TileMap;
import com.gatdsen.simulation.GameState;
import com.gatdsen.ui.assets.AssetContainer;
import com.gatdsen.ui.hud.*;

/**
 * Class for taking care of the User Interface.
 * Input Handling during the game.
 * Displaying health, inventory
 */
public class Hud implements Disposable {

    private static Stage stage;
    private final InputHandler inputHandler;
    private final InputMultiplexer inputMultiplexer;
    private final TurnTimer turnTimer;
    private final Table layoutTable;
    private final Container<ImagePopup> turnPopupContainer;
    private final InGameScreen inGameScreen;
    private final TextureRegion turnChangeSprite;
    private final float turnChangeDuration;
    private final UiMessenger uiMessenger;
    private float renderingSpeed = 1;
    private boolean debugVisible;
    private float[] scores;
    private String[] names;
    private final ScoreView scoreView;
    private TextButton nextRoundButton;
    private final Skin skin = AssetContainer.MainMenuAssets.skin;
    Viewport hudViewport;
    private int player0Balance;
    private int player1Balance;
    private ProgressBar healthBarPlayer0;
    private ProgressBar healthBarPlayer1;

    /**
     * Initialisiert das HUD-Objekt
     *
     * @param ingameScreen Die Instanz der InGameScreen-Klasse
     * @param gameViewport Die Viewport-Instanz für das Spiel
     */
    public Hud(InGameScreen ingameScreen, Viewport gameViewport) {
        this.inGameScreen = ingameScreen;
        hudViewport = new FitViewport(gameViewport.getWorldWidth() / 10, gameViewport.getWorldHeight() / 10);
        this.uiMessenger = new UiMessenger(this);
        float animationSpeedupValue = 8;
        turnChangeDuration = 2;
        turnChangeSprite = AssetContainer.IngameAssets.turnChange;
        stage = new Stage(hudViewport);
        layoutTable = setupLayoutTable();
        inputHandler = setupInputHandler(ingameScreen, this);
        inputHandler.setUiMessenger(uiMessenger);
        turnTimer = new TurnTimer(AssetContainer.IngameAssets.turnTimer);
        turnTimer.setCurrentTime(0);
        turnPopupContainer = new Container<ImagePopup>();
        layoutHudElements();
        // Kombination von Eingaben von beiden Prozessoren (Spiel und UI)
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(inputHandler); // für die Simulation benötigt
        inputMultiplexer.addProcessor(stage); // für die UI-Buttons benötigt
        stage.addActor(layoutTable);
        scoreView = new ScoreView(null);
    }

    /**
     * Erstellt einen InputHandler und gibt ihn zurück
     *
     * @param ingameScreen Die Instanz der InGameScreen-Klasse
     * @param h            Das Hud-Objekt
     * @return Ein neues InputHandler-Objekt
     */
    private InputHandler setupInputHandler(InGameScreen ingameScreen, Hud h) {
        return new InputHandler(ingameScreen, h);
    }

    /**
     * Konfiguriert und gibt eine Tabelle für das Layout zurück
     *
     * @return Eine neu konfigurierte Table-Instanz
     */
    private Table setupLayoutTable() {
        Table table = new Table(AssetContainer.MainMenuAssets.skin);

        table.setFillParent(true);
        table.columnDefaults(0).width(100);
        table.columnDefaults(1).width(100);
        table.columnDefaults(2).width(100);
        table.columnDefaults(3).width(100);
        table.columnDefaults(4).width(100);
        table.columnDefaults(5).width(100);
        table.columnDefaults(6).width(100);
        table.center().top();
        return table;
    }

    /**
     * Setzt das Scoreboard für das Spiel auf
     *
     * @param game Die GameState-Instanz für das Spiel
     */
    public void setupScoreboard(GameState game) {

        //ToDo read player count and assign individual colors
        ScoreBoard scores = new ScoreBoard(new Color[]{Color.WHITE, Color.WHITE}, names, game);

        this.scores = game.getHealth();

        scoreView.addScoreboard(scores);

    }

    /**
     * Setzt die Namen der Spieler
     *
     * @param names Ein Array mit den Namen der Spieler
     */
    public void setPlayerNames(String[] names) {
        this.names = names;
    }

    /**
     * Konfiguriert die HUD-Elemente und deren Anordnung
     */
    private void layoutHudElements() {
        float padding = 10;
        int health = 300;

        Label player0BalanceLabel = new Label("$" + player0Balance, skin);

        Label player1BalanceLabel = new Label("$" + player1Balance, skin);

        healthBarPlayer0 = new ProgressBar(0, health, 1, false, skin);
        healthBarPlayer0.setValue(health);
        healthBarPlayer1 = new ProgressBar(0, health, 1, false, skin);
        healthBarPlayer1.setValue(health);

        Label invisibleLabel = new Label("", skin);
        layoutTable.add(invisibleLabel);
        layoutTable.add(invisibleLabel);
        layoutTable.add(invisibleLabel);
        layoutTable.add(turnTimer).row();

        nextRoundButton = new TextButton("Zug beenden", skin);
        nextRoundButton.addListener(new ChangeListener() {
            /**
             * Wird aufgerufen, wenn der Button geklickt wird
             *
             * @param event Das ChangeEvent
             * @param actor Das Actor-Objekt, das das Änderungsereignis ausgelöst hat
             */
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                inputHandler.endTurn();
                System.out.println("Neue Runde startet");
            }
        });
        layoutTable.add(healthBarPlayer0).pad(padding);
        layoutTable.add(player0BalanceLabel).pad(padding);
        layoutTable.add(invisibleLabel);
        layoutTable.add(nextRoundButton);
        layoutTable.add(invisibleLabel);
        layoutTable.add(healthBarPlayer1).pad(padding);
        layoutTable.add(player1BalanceLabel).pad(padding);
    }

    /**
     * Erstellt einen FastForwardButton und gibt ihn zurück
     *
     * @param uiMessenger Der UiMessenger für die Kommunikation
     * @param speedUp     Die Geschwindigkeitssteigerung für die Schnellvorlauf-Funktion
     * @return Ein neues FastForwardButton-Objekt
     */
    private FastForwardButton setupFastForwardButton(UiMessenger uiMessenger, float speedUp) {

        FastForwardButton button = new FastForwardButton(new TextureRegionDrawable(AssetContainer.IngameAssets.fastForwardButton),
                new TextureRegionDrawable(AssetContainer.IngameAssets.fastForwardButtonPressed),
                new TextureRegionDrawable(AssetContainer.IngameAssets.fastForwardButtonChecked),
                uiMessenger, speedUp);
        return button;
    }

    /**
     * Gibt den InputHandler zurück
     *
     * @return Der InputHandler für das HUD
     */
    public InputHandler getInputHandler() {
        return inputHandler;
    }

    /**
     * Gibt den InputProcessor zurück
     *
     * @return Der InputProcessor für das HUD
     */
    public InputProcessor getInputProcessor() {
        return inputMultiplexer;
    }

    /**
     * Zeichnet das HUD und die ScoreView
     */
    public void draw() {

        stage.getViewport().apply(true);
        stage.draw();
        if (scoreView != null) {
            scoreView.draw();
        }
    }

    /**
     * Aktualisiert den InputHandler und die Stage basierend auf dem Zeitdelta
     *
     * @param delta Das Zeitdelta seit dem letzten Frame
     */
    protected void tick(float delta) {
        inputHandler.tick(delta);
        stage.act(delta);
    }

    /**
     * Erstellt ein Popup für den Spielzugwechsel mit der angegebenen Umrandungsfarbe
     *
     * @param outlinecolor Die Farbe für die Umrandung
     */
    public void createTurnChangePopup(Color outlinecolor) {
        drawImagePopup(new ImagePopup(turnChangeSprite, turnChangeDuration / renderingSpeed, turnChangeSprite.getRegionWidth() * 8, turnChangeSprite.getRegionHeight() * 8, outlinecolor), false);
    }

    /**
     * Zeichnet das gegebene Bildpopup und positioniert es entsprechend den Parametern
     *
     * @param image  Das zu zeichnende Bildpopup
     * @param center Bestimmt, ob das Popup zentriert oder oben platziert wird
     */
    public void drawImagePopup(ImagePopup image, boolean center) {
        if (turnPopupContainer.hasChildren()) {
            turnPopupContainer.removeActorAt(0, false);
        }
        turnPopupContainer.setActor(image);
        if (center) {
            turnPopupContainer.center();
        } else {
            turnPopupContainer.top();
        }
        image.setScaling(Scaling.fit);
        turnPopupContainer.fill();
        turnPopupContainer.maxSize(image.getWidthForContainer(), image.getHeightForContainer());
    }

    /**
     * Ändert die Größe des Viewports basierend auf der angegebenen Breite und Höhe
     *
     * @param width  Die neue Breite des Viewports
     * @param height Die neue Höhe des Viewports
     */
    public void resizeViewport(int width, int height) {
        stage.getViewport().update(width, height, true);
        if (scoreView != null) {
            scoreView.getViewport().update(width, height, true);
        }
    }

    /**
     * Gibt den UiMessenger zurück
     *
     * @return Der UiMessenger für die Kommunikation
     */
    public UiMessenger getUiMessenger() {
        return uiMessenger;
    }

    /**
     * Setzt die Rendering-Geschwindigkeit für das HUD
     *
     * @param speed Die neue Rendering-Geschwindigkeit
     */
    public void setRenderingSpeed(float speed) {
        inGameScreen.setRenderingSpeed(speed);
        inputHandler.turnChangeSpeedup(speed);
        this.renderingSpeed = speed;
    }

    /**
     * Setzt die verbleibende Zeit für den aktuellen Spielzug
     *
     * @param time Die verbleibende Zeit in Sekunden
     */
    public void setTurntimeRemaining(int time) {
        turnTimer.setCurrentTime(time);
    }

    /**
     * Startet den Spielzug-Timer mit der angegebenen Dauer in Sekunden
     *
     * @param seconds Die Dauer des Spielzug-Timers in Sekunden
     */
    public void startTurnTimer(int seconds) {
        turnTimer.startTimer(seconds);
    }

    /**
     * Stoppt den laufenden Spielzug-Timer
     */
    public void stopTurnTimer() {
        turnTimer.stopTimer();
    }

    /**
     * dispose für das Hud
     */
    @Override
    public void dispose() {
        stage.dispose();
    }

    /**
     * Schaltet die Sichtbarkeit der Debug-Linien ein oder aus
     */
    public void toggleDebugOutlines() {
        this.debugVisible = !debugVisible;

        this.layoutTable.setDebug(debugVisible);
    }

    /**
     * Schaltet die Anzeige der Punktzahlen ein oder aus, sofern vorhanden
     */
    public void toggleScores() {
        if (scoreView != null) {
            scoreView.toggleEnabled();
        }
    }

    /**
     * Passt die Punktzahlen im HUD basierend auf dem gegebenen Array an
     *
     * @param scores Ein Array mit den neuen Punktzahlen
     */
    public void adjustScores(float[] scores) {
        this.scores = scores;

        if (scoreView != null) {
            scoreView.adjustScores(scores);
        }
    }

    /**
     * Passt die Punktzahl für das angegebene Team im HUD an
     *
     * @param team  Das Team, dessen Punktzahl angepasst wird
     * @param score Die neue Punktzahl für das Team
     */
    public void adjustScores(int team, float score) {
        this.scores[team] = score;

        if (scoreView != null) {
            scoreView.adjustScores(scores);
        }
    }

    /**
     * Zeigt das Ergebnis des Spiels an, einschließlich eines Hintergrundbilds und Popup-Fensters
     *
     * @param won    Gibt an, ob das Team gewonnen hat
     * @param team   Das betroffene Team
     * @param isDraw Gibt an, ob das Spiel unentschieden endete
     */
    public void gameEnded(boolean won, int team, boolean isDraw) {

        //create a pixel with a set color that will be used as Background
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        //set the color to black
        pixmap.setColor(0, 0, 0, 0.5f);
        pixmap.fill();
        layoutTable.setBackground(new TextureRegionDrawable(new Texture(pixmap)));
        pixmap.dispose();

        ImagePopup display;

        //determine sprite
        if (isDraw) {
            display = new ImagePopup(AssetContainer.IngameAssets.drawDisplay, -1,
                    AssetContainer.IngameAssets.drawDisplay.getRegionWidth() * 2,
                    AssetContainer.IngameAssets.drawDisplay.getRegionHeight() * 2);
        } else if (won) {
            display = new ImagePopup(AssetContainer.IngameAssets.victoryDisplay, -1,
                    AssetContainer.IngameAssets.victoryDisplay.getRegionWidth() * 2,
                    AssetContainer.IngameAssets.victoryDisplay.getRegionHeight() * 2, new Color(Color.WHITE), 2f);
        } else {
            display = new ImagePopup(AssetContainer.IngameAssets.lossDisplay, -1,
                    AssetContainer.IngameAssets.lossDisplay.getRegionWidth() * 2,
                    AssetContainer.IngameAssets.lossDisplay.getRegionHeight() * 2, new Color(Color.WHITE), 2f);
        }
        drawImagePopup(display, true);
    }

    /**
     * Entfernt das Popup am Start eines neuen Spielzugs
     */
    public void skipTurnStart() {
        if (turnPopupContainer.getActor() != null)
            turnPopupContainer.getActor().remove();
    }

    /**
     * Startet ein neues Spiel mit den gegebenen Parametern
     *
     * @param gameState             Der Zustand des neuen Spiels
     * @param arrayPositionTileMaps Die Positionen der TileMaps im Array
     * @param tileSize              Die Größe der Tiles
     * @param tileMap               Die TileMap des Spiels
     */
    public void newGame(GameState gameState, Vector2[] arrayPositionTileMaps, int tileSize, TileMap tileMap) {
        Group group = new Group();
        stage.addActor(group);

        int numberOfTeams = gameState.getPlayerCount();
        TextButton[] teamButtons;
        teamButtons = new TextButton[numberOfTeams];

        for (int i = 0; i < numberOfTeams; i++) {
            teamButtons[i] = tileMapButton(i, tileMap);
            teamButtons[i].setSize((gameState.getBoardSizeX() * tileSize) / 10.0f, (gameState.getBoardSizeY() * tileSize) / 10.0f);
            group.addActor(teamButtons[i]);
            teamButtons[i].setPosition((arrayPositionTileMaps[i].x) / 10.0f, (arrayPositionTileMaps[i].y) / 10.0f);
            teamButtons[i].setColor(Color.CLEAR);
        }
        layoutTable.setBackground((Drawable) null);
        if (turnPopupContainer.hasChildren()) {
            turnPopupContainer.removeActorAt(0, false);
        }
        setupScoreboard(gameState);
    }

    /**
     * Erstellt und gibt einen TextButton für die TileMap eines Teams zurück
     *
     * @param team    Das Team, zu dem der Button gehört
     * @param tileMap Die TileMap des Spiels
     * @return Der erstellte TextButton
     */
    private TextButton tileMapButton(int team, TileMap tileMap) {
        TextButton tileMapButton = new TextButton("", skin);

        tileMapButton.addListener(new ClickListener() {
            /**
             * Wird aufgerufen, wenn der Button geklickt wird
             *
             * @param event Das InputEvent
             * @param x Die x-Position der Berührung
             * @param y Die y-Position der Berührung
             * @param pointer Der Zeiger
             * @param button Die gedrückte Taste
             * @return true, wenn das Event konsumiert wird; false, wenn es weitergeleitet wird
             */
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                int posX = (int) ((x / tileMap.getTileSize()) * 10);
                int posY = (int) ((y / tileMap.getTileSize()) * 10);
                if (button == Input.Buttons.RIGHT) {
                    System.out.println("Rechtsklick - x: " + posX + " y: " + posY + " Spieler: " + team);
                    inputHandler.playerFieldRightClicked(team, posX, posY);
                    return true;
                } else if (button == Input.Buttons.LEFT) {
                    System.out.println("Linksklick - x: " + posX + " y: " + posY + " Spieler: " + team);
                    inputHandler.playerFieldLeftClicked(team, posX, posY);
                    return true;
                }
                return false;
            }
        });
        return tileMapButton;
    }

    /**
     * Setzt den InputProcessor auf die Stage, um das HUD anzuzeigen.
     */
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Setzt das Bankguthaben für den angegebenen Spieler
     *
     * @param playerID Die ID des Spielers (aktuell nur 0 oder 1)
     * @param balance  Das neue Bankguthaben
     */
    public void setBankBalance(int playerID, int balance) {
        if (playerID == 0) {
            player0Balance = balance;
        } else if (playerID == 1) {
            player1Balance = balance;
        }
    }

    public void setPlayerHealth(int playerID, int health) {
        if (playerID == 0) {
            healthBarPlayer0.setValue(health);
        } else if (playerID == 1) {
            healthBarPlayer1.setValue(health);
        }
    }

    /*
    //ToDo implementieren!
    public void initPlayerHealth(int playerID, int health) {
        if (health <= 0){
            health = 100;
        }
            if (playerID == 0) {
                healthBarPlayer0 = new ProgressBar(0, health, 1, false, skin);
                healthBarPlayer0.setValue(health);
            } else if (playerID == 1) {
                healthBarPlayer1 = new ProgressBar(0, health, 1, false, skin);
                healthBarPlayer1.setValue(health);
            }
    }
     */
}