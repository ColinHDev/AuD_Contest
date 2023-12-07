package com.gatdsen.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gatdsen.animation.action.Action;
import com.gatdsen.animation.action.*;
import com.gatdsen.animation.action.uiActions.MessageUiCurrencyAction;
import com.gatdsen.animation.action.uiActions.MessageUiGameEndedAction;
import com.gatdsen.animation.action.uiActions.MessageUiScoreAction;
import com.gatdsen.animation.action.uiActions.MessageUiUpdateHealthAction;
import com.gatdsen.animation.entity.Entity;
import com.gatdsen.animation.entity.ParticleEntity;
import com.gatdsen.animation.entity.SpriteEntity;
import com.gatdsen.animation.entity.TileMap;
import com.gatdsen.manager.AnimationLogProcessor;
import com.gatdsen.simulation.GameState;
import com.gatdsen.simulation.IntVector2;
import com.gatdsen.simulation.LinearPath;
import com.gatdsen.simulation.Path;
import com.gatdsen.simulation.action.*;
import com.gatdsen.ui.assets.AssetContainer.IngameAssets;
import com.gatdsen.ui.hud.UiMessenger;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Kernklasse für die Visualisierung des Spielgeschehens.
 * Übersetzt {@link GameState GameState} und {@link ActionLog ActionLog}
 * des {@link com.gatdsen.simulation Simulation-Package} in für libGDX renderbare Objekte
 */
public class Animator implements Screen, AnimationLogProcessor {
    private AnimatorCamera camera;
    private GameState state;
    private final UiMessenger uiMessenger;

    private Viewport viewport;
    private Viewport backgroundViewport;

    private SpriteEntity background;


    private final Batch batch;

    private final Entity root;

    public TileMap[] playerMaps;

    private static GameTower[][][] towers;
    public static GameEnemy[][][] enemies;
    public static GameEnemy[][][] temp_enemies;

    // TODO: BlockingQueue<ActionLog> muss BlockingQueue<Action> sein - gez. Corny
    private final BlockingQueue<com.gatdsen.simulation.action.Action> pendingLogs = new LinkedBlockingQueue<>();


    private final Object notificationObject = new Object();

    private final List<Action> actionList = new LinkedList<>();


    public AnimatorCamera getCamera() {
        return this.camera;
    }


    interface ActionConverter {
        public ExpandedAction apply(com.gatdsen.simulation.action.Action simAction, Animator animator);
    }

    private static class Remainder {
        private float remainingDelta;
        private Action[] actions;

        public Remainder(float remainingDelta, Action[] actions) {
            this.remainingDelta = remainingDelta;
            this.actions = actions;
        }

        public float getRemainingDelta() {
            return remainingDelta;
        }

        public Action[] getActions() {
            return actions;
        }
    }

    /**
     * One Simulation Action may be sliced into multiple Animation Actions to keep generalization high
     */
    private static class ExpandedAction {
        Action head;
        Action tail;

        public ExpandedAction(Action head) {
            this.head = head;
            this.tail = head;
        }

        public ExpandedAction(Action head, Action tail) {
            this.head = head;
            this.tail = tail;
        }
    }

    public static class ActionConverters {

        private static final Map<Class<?>, ActionConverter> map =
                new HashMap<Class<?>, ActionConverter>() {
                    {
                        put(InitAction.class, ((simAction, animator) -> new ExpandedAction(new IdleAction(0, 0))));
                        put(TurnStartAction.class, ActionConverters::convertTurnStartAction);
                        put(GameOverAction.class, ActionConverters::convertGameOverAction);
                        put(DebugPointAction.class, ActionConverters::convertDebugPointAction);
                        put(ScoreAction.class, ActionConverters::convertScoreAction);
                        put(UpdateCurrencyAction.class, ActionConverters::convertUpdateCurrencyAction);
                        put(UpdateHealthAction.class, ActionConverters::convertUpdateHealthAction);

                        // Gegner Actions
                        put(EnemySpawnAction.class, ActionConverters::convertEnemySpawnAction);
                        put(EnemyMoveAction.class, ActionConverters::convertEnemyMoveAction);
                        put(EnemyUpdateHealthAction.class, ActionConverters::convertEnemyUpdateHealthAction);
                        put(EnemyDefeatAction.class, ActionConverters::convertEnemyDefeatAction);

                        // Tower Actions
                        put(TowerPlaceAction.class, ActionConverters::convertTowerPlaceAction);
                        put(TowerAttackAction.class, ActionConverters::convertTowerAttackAction);
                        put(ProjectileAction.class, ActionConverters::convertProjectileAction);
                        put(TowerDestroyAction.class, ActionConverters::convertTowerDestroyAction);
                    }
                };

        public static Action convert(com.gatdsen.simulation.action.Action simAction, Animator animator) {
            //System.out.println("Converting " + simAction.getClass());
            ExpandedAction expandedAction = map.getOrDefault(simAction.getClass(), (v, w) -> {
                        System.err.println("Missing Converter for Action of type " + simAction.getClass());
                        return new ExpandedAction(new IdleAction(simAction.getDelay(), 0));
                    })
                    .apply(simAction, animator);
            expandedAction.tail.setChildren(extractChildren(simAction, animator));

            return expandedAction.head;
        }

        private static Action[] extractChildren(com.gatdsen.simulation.action.Action action, Animator animator) {
            int childCount = action.getChildren().size();
            if (childCount == 0) return new Action[]{};

            Action[] children = new Action[childCount];
            int i = 0;
            Iterator<com.gatdsen.simulation.action.Action> iterator = action.iterator();
            while (iterator.hasNext()) {
                com.gatdsen.simulation.action.Action curChild = iterator.next();
                children[i] = convert(curChild, animator);
                i++;
            }

            return children;
        }


        private static ExpandedAction convertTurnStartAction(com.gatdsen.simulation.action.Action action, Animator animator) {
            for (int i = 0; i < enemies.length; i++) {
                for (int j = 0; j < enemies[i].length; j++) {
                    temp_enemies[i][j] = enemies[i][j].clone();
                }
            }
            enemies = new GameEnemy[2][100][100];

            return new ExpandedAction(new IdleAction(0,0));
        }

        private static ExpandedAction convertEnemySpawnAction(com.gatdsen.simulation.action.Action action, Animator animator) {
            EnemySpawnAction spawnAction = (EnemySpawnAction) action;

            SummonAction<GameEnemy> spawnEnemy = new SummonAction<>(
                    spawnAction.getDelay(),
                    (GameEnemy enemy) -> {
                        enemies[spawnAction.getTeam()][spawnAction.getPos().x][spawnAction.getPos().y] = enemy;
                    },
                    () -> {
                        GameEnemy enemy = new GameEnemy(spawnAction.getLevel());
                        enemy.setRelPos(spawnAction.getPos().x * animator.playerMaps[0].getTileSize() + animator.playerMaps[spawnAction.getTeam()].getPos().x,
                                spawnAction.getPos().y * animator.playerMaps[0].getTileSize() + animator.playerMaps[spawnAction.getTeam()].getPos().y);

                        animator.root.add(enemy);
                        return enemy;
                    }
            );

            return new ExpandedAction(spawnEnemy);
        }

        // ToDo: Path Berechnung in den Sim-Teil verschieben
        private static ExpandedAction convertEnemyMoveAction(com.gatdsen.simulation.action.Action action, Animator animator) {
            EnemyMoveAction moveAction = (EnemyMoveAction) action;
            int tileSize = animator.playerMaps[0].getTileSize();
            GameEnemy enemy = temp_enemies[moveAction.getTeam()][moveAction.getPos().x][moveAction.getPos().y];

            Vector2 mapPos = animator.playerMaps[moveAction.getTeam()].getPos();

            Vector2 start = new Vector2(moveAction.getPos().x * tileSize + mapPos.x, moveAction.getPos().y * tileSize + mapPos.y);
            Vector2 end = new Vector2(moveAction.getDes().x * tileSize + mapPos.x, moveAction.getDes().y * tileSize + mapPos.y);

            Path enemyPath = new LinearPath(start, end, 500);

            MoveAction moveEnemy = new MoveAction(moveAction.getDelay(), enemy, enemyPath.getDuration(), enemyPath);
            ExecutorAction changeArray = new ExecutorAction(0, () -> {
                synchronized (Animator.enemies) {
                    Animator.enemies[moveAction.getTeam()][moveAction.getDes().x][moveAction.getDes().y] =
                            Animator.temp_enemies[moveAction.getTeam()][moveAction.getPos().x][moveAction.getPos().y];
                }
                return 0;
            });

            moveEnemy.setChildren(new Action[]{changeArray});

            return new ExpandedAction(moveEnemy, changeArray);
        }

        private static ExpandedAction convertEnemyUpdateHealthAction(com.gatdsen.simulation.action.Action action, Animator animator) {
            EnemyUpdateHealthAction updateHealth = (EnemyUpdateHealthAction) action;

            ExecutorAction changeHealth = new ExecutorAction(updateHealth.getDelay(), () -> {
                enemies[updateHealth.getTeam()][updateHealth.getPos().x][updateHealth.getPos().y].healthbar.changeHealth(updateHealth.getNewHealth());
                return 0;
            });

            return new ExpandedAction(changeHealth);
        }


        private static ExpandedAction convertEnemyDefeatAction(com.gatdsen.simulation.action.Action action, Animator animator) {
            EnemyDefeatAction defeatAction = (EnemyDefeatAction) action;

            DestroyAction<GameEnemy> killEnemy = new DestroyAction<>(
                    defeatAction.getDelay(),
                    enemies[defeatAction.getTeam()][defeatAction.getPos().x][defeatAction.getPos().y],
                    null,
                    (GameEnemy enemy) -> {
                        animator.root.remove(enemies[defeatAction.getTeam()][defeatAction.getPos().x][defeatAction.getPos().y]);
                        enemies[defeatAction.getTeam()][defeatAction.getPos().x][defeatAction.getPos().y] = null;
                    }
            );

            return new ExpandedAction(killEnemy);
        }

        private static ExpandedAction convertTowerPlaceAction(com.gatdsen.simulation.action.Action action, Animator animator) {
            TowerPlaceAction placeAction = (TowerPlaceAction) action;

            SummonAction<GameTower> summonTower = new SummonAction<GameTower>(action.getDelay(), target -> {
                towers[placeAction.getTeam()][placeAction.getPos().x][placeAction.getPos().y] = target;
            }, () -> {
                GameTower tower = new GameTower(1, placeAction.getType());
                tower.setRelPos(placeAction.getPos().x * animator.playerMaps[0].getTileSize() + animator.playerMaps[placeAction.getTeam()].getPos().x,
                        placeAction.getPos().y * animator.playerMaps[0].getTileSize() + animator.playerMaps[placeAction.getTeam()].getPos().y);
                animator.root.add(tower);

                return tower;
            });

            return new ExpandedAction(summonTower);
        }

        private static ExpandedAction convertTowerAttackAction(com.gatdsen.simulation.action.Action action, Animator animator) {
            TowerAttackAction towerAttack = (TowerAttackAction) action;
            GameTower tower = towers[towerAttack.getTeam()][towerAttack.getPos().x][towerAttack.getPos().y];

            ExecutorAction attack = new ExecutorAction(towerAttack.getDelay(), () -> {
                tower.attack();
                Animation<TextureRegion> animation = tower.attackAnimation;
                return animation != null ? animation.getAnimationDuration() : 0;
            });

            return new ExpandedAction(attack);
        }

        private static ExpandedAction convertProjectileAction(com.gatdsen.simulation.action.Action action, Animator animator) {
            ProjectileAction projectileAction = (ProjectileAction) action;
            TileMap board = animator.playerMaps[projectileAction.getTeam()];
            Path path = new AnimatorPath(projectileAction.getPath(), board.getPos(), board.getTileSize());

            // Target muss null sein, da das Projektil dem converter nicht bekannt ist. Es wird erst in summonProjectile erstellt.
            MoveAction moveProjectile = new MoveAction(0, null, path.getDuration(), path);
            RotateAction rotateProjectile = new RotateAction(0, null, path.getDuration(), path);

            DestroyAction<Entity> destroyProjectile = new DestroyAction<>(0, null, null, animator.root::remove);

            // Hier können jetzt die Targets für die anderen Actions gesetzt werden
            SummonAction<Entity> summonProjectile = new SummonAction<>(action.getDelay(), projectile -> {
                moveProjectile.setTarget(projectile);
                rotateProjectile.setTarget(projectile);
                destroyProjectile.setTarget(projectile);
            }, () -> {
                Entity projectile = Projectiles.summon(projectileAction.getType());
                projectile.setRelPos(projectileAction.getPath().getPos(0));
                animator.root.add(projectile);
                return projectile;
            });

            summonProjectile.setChildren(new Action[]{moveProjectile, rotateProjectile});

            // Effekt bei Treffer
            ExpandedAction effects;
            switch (projectileAction.getType()) {
                //case STANDARD_TYPE:
                    //effects = generateParticle(IngameAssets.explosionParticle, path.getPos(path.getDuration()), 10f, animator);
                    //moveProjectile.setChildren(new Action[]{destroyProjectile, effects.head});
                    //break;
                default:
                    moveProjectile.setChildren(new Action[]{destroyProjectile});
            }

            // Die Action ist unterteilt in: Projektil erzeugen, dann bewegen & rotieren und anschließend mit effekt zerstören
            return new ExpandedAction(summonProjectile, destroyProjectile);
        }

        private static ExpandedAction generateParticle(ParticleEffectPool effect, Vector2 pos, float dur, Animator animator) {
            DestroyAction<ParticleEntity> destroyParticle = new DestroyAction<>(dur, null, null, (particle) -> {
                animator.root.remove(particle);
                particle.free();
            });

            SummonAction<ParticleEntity> summonParticle = new SummonAction<>(0, destroyParticle::setTarget, () -> {
                ParticleEntity particle = ParticleEntity.getParticleEntity(effect);
                particle.setLoop(false);
                particle.setRelPos(pos);
                animator.root.add(particle);

                return particle;
            });

            summonParticle.setChildren(new Action[]{destroyParticle});
            return new ExpandedAction(summonParticle, destroyParticle);
        }

        private static ExpandedAction convertTowerDestroyAction(com.gatdsen.simulation.action.Action action, Animator animator) {
            TowerDestroyAction destroyAction = (TowerDestroyAction) action;

            DestroyAction<GameTower> destroyTower = new DestroyAction<GameTower>(
                    destroyAction.getDelay(),
                    towers[destroyAction.getTeam()][destroyAction.getPos().x][destroyAction.getPos().y],
                    null,
                    (GameTower tower) -> {
                        animator.root.remove(towers[destroyAction.getTeam()][destroyAction.getPos().x][destroyAction.getPos().y]);
                        towers[destroyAction.getTeam()][destroyAction.getPos().x][destroyAction.getPos().y] = null;
                    }
            );

            return new ExpandedAction(destroyTower);
        }

        private static ExpandedAction convertUpdateCurrencyAction(com.gatdsen.simulation.action.Action action, Animator animator) {
            UpdateCurrencyAction updateCurrency = (UpdateCurrencyAction) action;

            MessageUiCurrencyAction currencyAction = new MessageUiCurrencyAction(
                    updateCurrency.getDelay(),
                    animator.uiMessenger,
                    updateCurrency.getTeam(),
                    updateCurrency.getNewCurrency());

            return new ExpandedAction(currencyAction);
        }

        private static ExpandedAction convertUpdateHealthAction(com.gatdsen.simulation.action.Action action, Animator animator) {
            UpdateHealthAction updateHealth = (UpdateHealthAction) action;

            MessageUiUpdateHealthAction setHealth = new MessageUiUpdateHealthAction(
                    updateHealth.getDelay(),
                    animator.uiMessenger,
                    updateHealth.getTeam(),
                    updateHealth.getNewHealth());

            return new ExpandedAction(setHealth);
        }


        private static ExpandedAction convertGameOverAction(com.gatdsen.simulation.action.Action action, Animator animator) {
            GameOverAction winAction = (GameOverAction) action;
            MessageUiGameEndedAction gameEndedAction;
            if (winAction.getTeam() < 0) {
                gameEndedAction = new MessageUiGameEndedAction(0, animator.uiMessenger, true);
            } else {
                gameEndedAction = new MessageUiGameEndedAction(0, animator.uiMessenger, true, winAction.getTeam());
            }

            return new ExpandedAction(gameEndedAction);
        }

        private static ExpandedAction convertDebugPointAction(com.gatdsen.simulation.action.Action action, Animator animator) {
            DebugPointAction debugPointAction = (DebugPointAction) action;

            DestroyAction<Entity> destroyAction = new DestroyAction<Entity>(debugPointAction.getDuration(), null, null, animator.root::remove);

            SummonAction<Entity> summonAction = new SummonAction<Entity>(action.getDelay(), destroyAction::setTarget, () -> {
                SpriteEntity entity;
                if (debugPointAction.isCross()) {
                    entity = new SpriteEntity(IngameAssets.cross_marker);
                    entity.setSize(new Vector2(3, 3));
                    debugPointAction.getPos().sub(new IntVector2(1, 1));
                } else {
                    entity = new SpriteEntity(IngameAssets.pixel);
                }
                entity.setRelPos(debugPointAction.getPos().toFloat());
                entity.setColor(debugPointAction.getColor());
                animator.root.add(entity);
                return entity;
            });

            summonAction.setChildren(new Action[]{destroyAction});


            return new ExpandedAction(summonAction, destroyAction);
        }


        private static ExpandedAction convertScoreAction(com.gatdsen.simulation.action.Action action, Animator animator) {
            ScoreAction scoreAction = (ScoreAction) action;
            //ui Action
            MessageUiScoreAction indicateScoreChangeAction = new MessageUiScoreAction(0, animator.uiMessenger, scoreAction.getTeam(), scoreAction.getNewScore());

            return new ExpandedAction(indicateScoreChangeAction);
        }

        //ToDo: Add game specific actions

    }


    /**
     * Setzt eine Welt basierend auf den Daten in state auf und bereitet diese für nachfolgende Animationen vor
     *
     * @param viewport viewport used for rendering
     */
    public Animator(Viewport viewport, UiMessenger uiMessenger) {
        this.uiMessenger = uiMessenger;
        this.batch = new SpriteBatch();
        this.root = new Entity();

        towers = new GameTower[2][100][100];
        enemies = new GameEnemy[2][100][100];
        temp_enemies = new GameEnemy[2][100][100];
        setupView(viewport);

        setup();
        // assign textures to tiles after processing game Stage
        //put sprite information into gameStage?
    }

    @Override
    public void init(GameState state, String[] playerNames, String[][] skins) {
        synchronized (root) {
            this.state = state;
            playerMaps = new TileMap[state.getPlayerCount()];
            playerMaps[0] = new TileMap(state, 0);
            playerMaps[0].setRelPos(2.5f * 200, 0);
            playerMaps[1] = new TileMap(state, 1);
            playerMaps[1].setRelPos(27.5f * 200, 0);
            root.clear();
            root.add(playerMaps[0]);
            root.add(playerMaps[1]);

            //ToDo: initialize based on gamestate data
        }
    }

    private void setup() {


        background = new SpriteEntity(
                IngameAssets.background,
                new Vector2(-backgroundViewport.getWorldWidth() / 2, -backgroundViewport.getWorldHeight() / 2),
                new Vector2(259, 128));


    }

    /**
     * Takes care of setting up the view for the user. Creates a new camera and sets the position to the center.
     * Adds it to the given Viewport.
     *
     * @param newViewport Viewport instance that animator will use to display the game.
     */
    private void setupView(Viewport newViewport) {

        int height = Gdx.graphics.getHeight();
        int width = Gdx.graphics.getWidth();
        this.viewport = newViewport;
        //this.backgroundViewport = new FitViewport(viewport.getWorldWidth(), viewport.getWorldHeight());
        this.backgroundViewport = new FillViewport(259, 128);
        //center camera once
        //camera.position.set(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f-12,0);
        //camera.zoom = 1;
        //viewport.setCamera(camera);
        //viewport.update(400,400);


        this.camera = new AnimatorCamera(viewport.getWorldWidth(), viewport.getWorldHeight());
        this.viewport.setCamera(camera);
        camera.zoom = 1f;
        camera.position.set(new float[]{1000, 1000, 0});    // nutzlos, viewport.update zentriert die Kamera
        this.backgroundViewport.update(width, height);
        this.viewport.update(width, height, false);
        camera.update();

    }

    /**
     * Animates the logs actions
     *
     * @param log Queue aller {@link com.gatdsen.simulation.action.Action animations-relevanten Ereignisse}
     */
    public void animate(ActionLog log) {
        pendingLogs.addAll(log.getRootActions());
    }

    private Action convertAction(com.gatdsen.simulation.action.Action action) {
        return ActionConverters.convert(action, this);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        if (actionList.isEmpty()) {
            if (!pendingLogs.isEmpty()) {
                actionList.add(convertAction(pendingLogs.poll()));
            } else {
                synchronized (notificationObject) {
                    notificationObject.notifyAll();
                }
            }
        }


        ListIterator<Action> iter = actionList.listIterator();
        Stack<Remainder> remainders = new Stack<>();
        while (iter.hasNext()) {
            Action cur = iter.next();
            float remainder = cur.step(delta);
            if (remainder >= 0) {
                iter.remove();
                //Schedule children to run for the time not consumed by their parent
                Action[] children = cur.getChildren();
                if (children != null && children.length > 0) remainders.push(new Remainder(remainder, children));
            }
        }

        //Process the completed actions children with their respective remaining times
        while (!remainders.empty()) {
            Remainder curRemainder = remainders.pop();
            float remainingDelta = curRemainder.getRemainingDelta();
            Action[] actions = curRemainder.getActions();
            for (Action cur : actions) {
                float remainder = cur.step(remainingDelta);
                if (remainder >= 0) {
                    //Schedule children to run for the time that's not consumed by their parent
                    Action[] children = cur.getChildren();
                    if (children != null && children.length > 0) remainders.push(new Remainder(remainder, children));
                } else {
                    //Add the child to the list of running actions if not completed in the remaining time
                    actionList.add(cur);
                }
            }
        }


        camera.updateMovement(delta);
        camera.update();


        backgroundViewport.apply();
        //begin drawing elements of the SpriteBatch

        batch.setProjectionMatrix(backgroundViewport.getCamera().combined);
        batch.begin();
        background.draw(batch, delta, 1);
        batch.setProjectionMatrix(camera.combined);
        //tells the batch to render in the way specified by the camera
        // e.g. Coordinate-system and Viewport scaling
        viewport.apply();

        //ToDo: make one step in the scheduled actions


        //recursively draw all entities by calling the root group
        synchronized (root) {
            root.draw(batch, delta, 1);
        }
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

        viewport.update(width, height, true);
        backgroundViewport.update(width, height);
        viewport.getCamera().update();
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

    @Override
    public void dispose() {
        batch.dispose();
        //disposes the atlas for every class because it is passed down as a parameter, no bruno for changing back to menu
        //textureAtlas.dispose();

    }


    @Override
    public void awaitNotification() {
        synchronized (notificationObject) {
            try {
                notificationObject.wait();
            } catch (InterruptedException ignored) {
            }
        }
    }

}

