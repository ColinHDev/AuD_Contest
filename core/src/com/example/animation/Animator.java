package com.example.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.example.animation.action.*;
import com.example.animation.action.Action;
import com.example.animation.action.uiActions.*;
import com.example.animation.entity.*;
import com.example.simulation.GameState;
import com.example.simulation.action.*;
import com.example.manager.AnimationLogProcessor;
import com.example.ui.assets.AssetContainer.IngameAssets;
import com.example.ui.hud.UiMessenger;


import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Kernklasse für die Visualisierung des Spielgeschehens.
 * Übersetzt {@link GameState GameState} und {@link ActionLog ActionLog}
 * des {@link com.example.simulation Simulation-Package} in für libGDX renderbare Objekte
 */
public class Animator implements Screen, AnimationLogProcessor {
    private AnimatorCamera camera;
    private GameState state;
    private final UiMessenger uiMessenger;

    private Viewport viewport;
    private Viewport backgroundViewport;

    private SpriteEntity background;
    private final GameState.GameMode gameMode;


    private final Batch batch;

    private final Entity root;

    private TileMap map;

    private final BlockingQueue<ActionLog> pendingLogs = new LinkedBlockingQueue<>();


    private final Object notificationObject = new Object();

    private final List<Action> actionList = new LinkedList<>();


    public AnimatorCamera getCamera() {
        return this.camera;
    }


    interface ActionConverter {
        public ExpandedAction apply(com.example.simulation.action.Action simAction, Animator animator);
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
                    }
                };


        public static Action convert(com.example.simulation.action.Action simAction, Animator animator) {
//            System.out.println("Converting " + simAction.getClass());
            ExpandedAction expandedAction = map.getOrDefault(simAction.getClass(), (v, w) -> {
                        System.err.println("Missing Converter for Action of type " + simAction.getClass());
                        return new ExpandedAction(new IdleAction(simAction.getDelay(), 0));
                    })
                    .apply(simAction, animator);
            expandedAction.tail.setChildren(extractChildren(simAction, animator));

            return expandedAction.head;
        }

        private static Action[] extractChildren(com.example.simulation.action.Action action, Animator animator) {
            int childCount = action.getChildren().size();
            if (childCount == 0) return new Action[]{};

            Action[] children = new Action[childCount];
            int i = 0;
            Iterator<com.example.simulation.action.Action> iterator = action.iterator();
            while (iterator.hasNext()) {
                com.example.simulation.action.Action curChild = iterator.next();
                children[i] = convert(curChild, animator);
                i++;
            }

            return children;
        }



        private static ExpandedAction convertTurnStartAction(com.example.simulation.action.Action action, Animator animator) {
            TurnStartAction startAction = (TurnStartAction) action;

            //ToDo: make necessary changes on Turnstart

            //ui Action

            return new ExpandedAction(new IdleAction(0,0));
        }


        private static ExpandedAction convertGameOverAction(com.example.simulation.action.Action action, Animator animator) {
            GameOverAction winAction = (GameOverAction) action;
            MessageUiGameEndedAction gameEndedAction;
            if (winAction.getTeam() < 0) {
                gameEndedAction = new MessageUiGameEndedAction(0, animator.uiMessenger, true);
            } else {
                if (animator.gameMode == GameState.GameMode.Campaign || animator.gameMode == GameState.GameMode.Exam_Admission) {
                    //ToDo rewrite this to fit your PvP structure
                    if (winAction.getTeam() == 0) {
                        //if the player 0 (human or bot of student) has not won then display defeat
                        gameEndedAction = new MessageUiGameEndedAction(0, animator.uiMessenger, true, winAction.getTeam(), Color.CYAN);
                    } else {
                        gameEndedAction = new MessageUiGameEndedAction(0, animator.uiMessenger, false, winAction.getTeam(), Color.CYAN);
                    }
                } else {
                    gameEndedAction = new MessageUiGameEndedAction(0, animator.uiMessenger, true, winAction.getTeam(), Color.CYAN);

                }
            }

            return new ExpandedAction(gameEndedAction);
        }

        private static ExpandedAction convertDebugPointAction(com.example.simulation.action.Action action, Animator animator) {
            DebugPointAction debugPointAction = (DebugPointAction) action;

            DestroyAction<Entity> destroyAction = new DestroyAction<Entity>(debugPointAction.getDuration(), null, null, animator.root::remove);

            SummonAction<Entity> summonAction = new SummonAction<Entity>(action.getDelay(), destroyAction::setTarget, () -> {
                SpriteEntity entity;
                if (debugPointAction.isCross()) {
                    entity = new SpriteEntity(IngameAssets.cross_marker);
                    entity.setSize(new Vector2(3, 3));
                    debugPointAction.getPos().sub(1, 1);
                } else {
                    entity = new SpriteEntity(IngameAssets.pixel);
                }
                entity.setRelPos(debugPointAction.getPos());
                entity.setColor(debugPointAction.getColor());
                animator.root.add(entity);
                return entity;
            });

            summonAction.setChildren(new Action[]{destroyAction});


            return new ExpandedAction(summonAction, destroyAction);
        }


        private static ExpandedAction convertScoreAction(com.example.simulation.action.Action action, Animator animator) {
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
    public Animator(Viewport viewport, GameState.GameMode gameMode, UiMessenger uiMessenger) {
        this.gameMode = gameMode;
        this.uiMessenger = uiMessenger;
        this.batch = new SpriteBatch();
        this.root = new Entity();

        setupView(viewport);

        setup();
        // assign textures to tiles after processing game Stage
        //put sprite information into gameStage?
    }

    @Override
    public void init(GameState state, String[] playerNames, String[][] skins) {
        synchronized (root) {
            this.state = state;
            map = new TileMap(state);
            root.clear();
            root.add(map);

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
        this.backgroundViewport = new FillViewport(259, 128);
        //center camera once
        //camera.position.set(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f-12,0);
        //camera.zoom = 1;
        //viewport.setCamera(camera);
        //viewport.update(400,400);


        this.camera = new AnimatorCamera(30, 30f * width / height);
        this.viewport.setCamera(camera);
        camera.zoom = 1f;
        camera.position.set(new float[]{0, 0, 0});
        this.backgroundViewport.update(width, height);
        this.viewport.update(width, height, true);
        camera.update();

    }

    /**
     * Animates the logs actions
     *
     * @param log Queue aller {@link com.example.simulation.action.Action animations-relevanten Ereignisse}
     */
    public void animate(ActionLog log) {
        pendingLogs.add(log);
    }

    private Action convertAction(com.example.simulation.action.Action action) {
        return ActionConverters.convert(action, this);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        if (actionList.isEmpty()) {
            if (!pendingLogs.isEmpty()) {
                actionList.add(convertAction(pendingLogs.poll().getRootAction()));
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

