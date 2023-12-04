package com.gatdsen.ui.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.gatdsen.simulation.GameState;
import com.gatdsen.ui.assets.AssetContainer.IngameAssets;
import com.gatdsen.ui.assets.AssetContainer.IngameAssets.GameCharacterAnimationType;
import com.gatdsen.ui.assets.AssetContainer.MainMenuAssets;

import java.io.File;
import java.util.*;

public class GADSAssetManager {
    //dedicatod to loading and mangaing assets used in the application

    //Pfad an dem sich der Textureatlas mit Assets relevant für das Spiel
    public static final String resourceDirectory = "";
    public static final String atlas = resourceDirectory + "texture_atlas/TextureAtlas.atlas";

    public final String skin = resourceDirectory + "uiUtility/skin.json";
    public final String font = resourceDirectory + "uiUtility/lsans-15.fnt";

    public static final String particleGroup = "particle/";

    public static final String outlineShader = resourceDirectory + "shader/outline.frag";
    public static final String lookupShader = resourceDirectory + "shader/lookup.frag";
    public static final String lookupOutlineShader = resourceDirectory + "shader/lookupOutline.frag";


    public static final String skin_compressed = "lookupBase/cat_example/skin_compressed.png";

    public static final String skin_uncompressed = "lookupBase/cat_example/skin_uncompressed.png";

    private boolean finishedLoading = false;

    //path to background
    //public final String background = resourceDirectory + "background/GADSBG.png";
    private final AssetManager manager;

    public GADSAssetManager() {
        manager = new AssetManager();
        loadFiles();
    }


    //ToDo: Make private after migrating to animation branch
    public void loadFiles() {
        loadFont();
        loadTextures();
        loadSkin();
        loadShader();
        loadParticles();

        //ToDo: Implement Loading screen and remove the 2 following statements
        manager.finishLoading();
        moveToContainer();
    }

    /**
     * Lädt den Texturen Atlas mit Assets relevant für das Ingame
     * mithilfe des Assetmanagers.
     * <p>
     * Methode wird erst beendet, sobald der Atlas geladen ist.
     * Solange der Manager diesen noch nicht fertig geladen hat, blockiert die Methode
     * </p>
     */
    public void loadTextures() {
        manager.load(skin_compressed, Texture.class);
        manager.load(skin_uncompressed, Texture.class);
        manager.load(atlas, TextureAtlas.class);
    }

    public void loadSkin() {
        manager.load(skin, Skin.class);
    }

    public void loadFont() {
        manager.load(font, BitmapFont.class);
    }

    private void loadShader() {
        manager.load(outlineShader, ShaderProgram.class);
        manager.load(lookupShader, ShaderProgram.class);
        manager.load(lookupOutlineShader, ShaderProgram.class);
    }

    private void loadParticles() {
        ParticleEffectLoader.ParticleEffectParameter particleEffectParameter = new ParticleEffectLoader.ParticleEffectParameter();
        particleEffectParameter.atlasFile = atlas;
        particleEffectParameter.atlasPrefix = particleGroup;

        //manager.load(slimeParticle, ParticleEffect.class, particleEffectParameter);
    }


    public void unloadAtlas() {
        manager.unload(atlas);
    }

    private void moveToContainer() {
        TextureAtlas atlas = manager.get(this.atlas, TextureAtlas.class);

        //Main Menu
        MainMenuAssets.background = atlas.findRegion("background/mainTitleBackground_new");
        MainMenuAssets.titleSprite = atlas.findRegion("background/mainTitleBackground_new");
        MainMenuAssets.skin = manager.get(skin, Skin.class);

        //Ingame

        IngameAssets.background = atlas.findRegion("background/WeihnachtsBG");

        IngameAssets.tileTextures = new TextureRegion[]{
                atlas.findRegion("Tileset/grass_tile"),
                atlas.findRegion("Tileset/water_tile"),

                // Path Tiles siehe TileMap
                //0: Spawn nach oben
                atlas.findRegion("Tileset/water_tile"),
                //1: Spawn nach rechts
                atlas.findRegion("Tileset/water_tile"),
                //2: Spawn nach unten
                atlas.findRegion("Tileset/water_tile"),
                //3: Spawn nach rechts
                atlas.findRegion("Tileset/water_tile"),
                //4: Ziel nach unten
                atlas.findRegion("Tileset/water_tile"),
                //5: Ecke rechts Oben
                atlas.findRegion("Tileset/path_right_up_tile"),
                //6: vertikale Gerade
                atlas.findRegion("Tileset/path_vertical_tile"),
                //7: Ecke links Oben
                atlas.findRegion("Tileset/path_left_up_tile"),
                //8: Ziel nach links
                atlas.findRegion("Tileset/water_tile"),
                //9: Ecke rechts Unten
                atlas.findRegion("Tileset/path_right_down_tile"),
                //10: horizontale Gerade
                atlas.findRegion("Tileset/path_horizontal_tile"),
                //11: Ziel nach oben
                atlas.findRegion("Tileset/water_tile"),
                //12: Ecke links Unten
                atlas.findRegion("Tileset/path_left_down_tile"),
                //13: Ziel nach rechts
                atlas.findRegion("Tileset/water_tile")
        };

        IngameAssets.gameCharacterAnimations = new AtlasAnimation[GameCharacterAnimationType.values().length];

//        IngameAssets.gameCharacterAnimations[GameCharacterAnimationType.ANIMATION_TYPE_IDLE.ordinal()] =
//                new AtlasAnimation(1 / 10f, atlas.findRegions("cat/catIdle"), Animation.PlayMode.LOOP);
//
//        IngameAssets.gameCharacterAnimations[GameCharacterAnimationType.ANIMATION_TYPE_WALKING.ordinal()] =
//                new AtlasAnimation(1 / 10f, atlas.findRegions("cat/catWalking"), Animation.PlayMode.LOOP);

        // Provisorium ToDo: entfernen
        IngameAssets.turnTimer = atlas.findRegion("background/mainTitleBackground");
        IngameAssets.turnChange = atlas.findRegion("background/mainTitleBackground");
        IngameAssets.fastForwardButtonPressed = atlas.findRegion("background/mainTitleBackground");
        IngameAssets.fastForwardButtonChecked = atlas.findRegion("background/mainTitleBackground");
        IngameAssets.fastForwardButton = atlas.findRegion("background/mainTitleBackground");

        IngameAssets.outlineShader = manager.get(outlineShader, ShaderProgram.class);

        IngameAssets.lookupShader = manager.get(lookupShader, ShaderProgram.class);

        IngameAssets.lookupOutlineShader = manager.get(lookupOutlineShader, ShaderProgram.class);

        //IngameAssets.mioSkin = new AtlasAnimation(1 / 10f, atlas.findRegions("skin/mioSkin"), Animation.PlayMode.LOOP);

        //IngameAssets.coolCat = new AtlasAnimation(1f, atlas.findRegions("example_group/coolCat"), Animation.PlayMode.LOOP);


        //IngameAssets.slimeParticle = new ParticleEffectPool(manager.get(slimeParticle, ParticleEffect.class), 1, 10);

        //IngameAssets.weaponIcons.put(WeaponType.COOKIE, IngameAssets.cookieIcon);
        //  IngameAssets.weaponIcons.put(WeaponType.SUGAR_CANE, IngameAssets.sugarCaneIcon);

        IngameAssets.compressedBaseSkin = manager.get(skin_compressed);
        IngameAssets.uncompressedBaseSkin = manager.get(skin_uncompressed);

        loadSkins();

        createCircleTexture();

        //create aim indicator circle

        finishedLoading = true;
    }


    public void createCircleTexture() {
        Vector2 maxSize = new Vector2(64, 7);
        float circleOpacity = 0.5f;

        //todo find betterway to load circle with size parameters
        //could not be done bcause of threading context inside of aim indicator -> graphic operations need to be done on graphic thread
        // -> could theoretically be done, but this is easier
        //only problem being hardcoded size

        final TextureRegion[] circleTexture = new TextureRegion[1];
        //get max size of the aim indicator
        int circleSize = (int) maxSize.x;
        //create a Pixmap for drawing the circle texture
        Pixmap circle = new Pixmap(2 * circleSize + 1, 2 * circleSize + 1, Pixmap.Format.RGBA8888);
        //circle Color
        Color color = new Color(Color.WHITE);
        //circle alpha
        color.a = circleOpacity;
        circle.setColor(color);
        //draw circle at
        circle.drawCircle(circleSize, circleSize, circleSize);

        IngameAssets.aimCircle =
                new TextureRegion(new Texture(circle));


    }

    public float update() {
        if (manager.update() && !finishedLoading) {
            moveToContainer();
        }
        return manager.getProgress();
    }

    private static void loadSkins() {
        int[] size = new int[2];
        int[][][] skinEncoding = generateSkinEncoding(IngameAssets.compressedBaseSkin, IngameAssets.uncompressedBaseSkin, size);
        Map<String, Animation<TextureRegion>> skins = new HashMap<>();
        skins.put("mioSkin", IngameAssets.mioSkin);
        Map<String, Map<Integer, Texture>> namedFrames = new HashMap<>();
        File skinDir = new File("skins");
        System.out.println(new File("").getAbsolutePath());
        if (skinDir.exists()) {

            for (File skinFile : Objects.requireNonNull(skinDir.listFiles(path -> path.getName().endsWith(".png") || path.getName().endsWith(".jpg")))) {
                try {
                    String fullName = skinFile.getName();
                    int lastPointIndex = fullName.lastIndexOf('.');
                    String noEndingName = fullName.substring(0, lastPointIndex);
                    int lastUnderscoreIndex = fullName.lastIndexOf('_');
                    int index = -1;
                    if (lastUnderscoreIndex > 1) {
                        String indexString = noEndingName.substring(lastUnderscoreIndex + 1);
                        try {
                            index = Integer.parseInt(indexString);
                            noEndingName = noEndingName.substring(0, lastUnderscoreIndex);
                        } catch (NumberFormatException ignored) {
                        }

                    }
                    Pixmap src = new Pixmap(Gdx.files.absolute(skinFile.getAbsolutePath()));
                    Pixmap frame = compressSkin(skinEncoding, src, size);
                    Map<Integer, Texture> frames = namedFrames.computeIfAbsent(noEndingName, k -> new HashMap<>());
                    frames.put(index, new Texture(frame));

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            for (String name :
                    namedFrames.keySet()) {
                if (skins.containsKey(name)) {
                    System.err.println("Warning: Skin with name " + name + " is hidden by another skin!");
                    continue;
                }
                Map<Integer, Texture> indexedFrames = namedFrames.get(name);
                int[] indices = new int[indexedFrames.size()];
                int i=0;
                for (Integer cur: indexedFrames.keySet())
                    indices[i++] = cur;
                int s = indexedFrames.size();
                Array<TextureRegion> frames = new Array<>(indexedFrames.size());
                frames.size = indexedFrames.size();
                Arrays.sort(indices);
                i =0;
                for (int cur: indices) frames.set(i++, new TextureRegion(indexedFrames.get(cur)));
                skins.put(name, new IndexedAnimation<>(1 / 10f, frames, indices, Animation.PlayMode.LOOP));
            }

        } else {
            System.err.println("Warning: No Skin-Dir found at " + skinDir.getAbsolutePath());
        }
        IngameAssets.skins = skins;
    }

    private static Pixmap compressSkin(int[][][] skinEncoding, Pixmap src, int[] size) {

            Pixmap result = new Pixmap(size[0], size[1], Pixmap.Format.RGBA8888);

            for (int x = 0; x < size[0]; x++)
                for (int y = 0; y < size[1]; y++) {
                    int[] pos = skinEncoding[x][y];
                    if (!Arrays.equals(pos, new int[]{-1, -1}))
                        result.drawPixel(x, y, src.getPixel(pos[0], pos[1]));
                }
            return result;
    }

    /**
     * Generates the encoding used for compressing skins
     *
     * @param compressedBaseSkin     A compressed Skin colored with the reference colors
     * @param uncompressedBaseSkin   An uncompressed Skin colored with the reference colors
     * @param out_compressedSkinSize an int[2] Array where the size of the compressed skin will be written to
     * @return Maps positions on the compressed skin to positions on the uncompressed skin, where colors should be retrieved from
     */
    private static int[][][] generateSkinEncoding(Texture compressedBaseSkin, Texture uncompressedBaseSkin, int[] out_compressedSkinSize) {

        int width = compressedBaseSkin.getWidth();
        int width2 = uncompressedBaseSkin.getWidth();
        out_compressedSkinSize[0] = width;
        int height = compressedBaseSkin.getHeight();
        int height2 = uncompressedBaseSkin.getHeight();
        out_compressedSkinSize[1] = height;
        int[][][] skinEncoding = new int[width][height][2];
        int[] defaultPos = new int[]{-1, -1};
        TextureData compressedRaster = compressedBaseSkin.getTextureData();
        TextureData uncompressedRaster = uncompressedBaseSkin.getTextureData();

        if (!compressedRaster.isPrepared()) compressedRaster.prepare();
        if (!uncompressedRaster.isPrepared()) uncompressedRaster.prepare();

        Pixmap compressedPixmap = compressedRaster.consumePixmap();
        Pixmap uncompressedPixmap = uncompressedRaster.consumePixmap();

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++) {
                int colorA = compressedPixmap.getPixel(x, y);
                if (colorA == 0) continue;
                int[] pos = defaultPos;
                for (int x2 = 0; x2 < width2; x2++)
                    for (int y2 = 0; y2 < height2; y2++) {
                        if (colorA == uncompressedPixmap.getPixel(x2, y2)) {
                            pos = new int[]{x2, y2};
                        }
                    }
                skinEncoding[x][y] = pos;
            }
        return skinEncoding;
    }
}
