package com.gatdsen.ui.assets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.gatdsen.simulation.action.ProjectileAction;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains Loaded Instances of Assets
 */
public class AssetContainer {

    /**
     * Loading Screen Assets
     * Will be Loaded immediately before other assets are finished
     */
    public static class LoadingScreenAssets{

    }

    /**
     * Main Menu Assets
     */
    public static class MainMenuAssets{

        public static TextureRegion titleSprite;
        public static TextureRegion background;
        public static Skin skin;
    }



    /**
     * Ingame Assets
     */
    public static class IngameAssets{

        public static final Animation<TextureRegion> EMPTY_ANIMATION = new Animation<>(1f, new TextureRegion(new Texture(new Pixmap(1, 1, Pixmap.Format.Alpha))));;
        public static Animation<TextureRegion> destroyTileAnimation;
        public static TextureRegion victoryDisplay;
        public static TextureRegion lossDisplay;
        public static TextureRegion drawDisplay;

        public static TextureRegion background;
        public static TextureRegion[] tileTextures;
        public static TextureRegion aimingIndicatorSprite;
        public static TextureRegion aimCircle;
        public static ProgressBar.ProgressBarStyle healthbarStyle;
        public static Animation<TextureRegion>[] gameCharacterAnimations;
        public static TextureRegion pixel;
        public static Texture compressedBaseSkin;
        public static Texture uncompressedBaseSkin;
        public static Map<String, Animation<TextureRegion>> skins;

        static {
            Pixmap map = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            map.setColor(Color.WHITE);
            map.drawPixel(0, 0);
            pixel = new TextureRegion(new Texture(map));
        }

        public static TextureRegion cross_marker;

        static {
            Pixmap map = new Pixmap(3, 3, Pixmap.Format.RGBA8888);
            map.setColor(Color.WHITE);
            map.drawLine(1, 0, 1, 2);
            map.drawLine(0, 1, 2, 1);
            cross_marker = new TextureRegion(new Texture(map));
        }

        public static ShaderProgram outlineShader;

        public static ShaderProgram lookupShader;

        public static ShaderProgram lookupOutlineShader;

        //Projectiles
        public static Animation<TextureRegion> Cookie;
        public static Animation<TextureRegion> SugarCane;

        public static Animation<TextureRegion> WaterPistol;
        public static AtlasAnimation Wool;
        public static Animation<TextureRegion> Grenade;
        public static Animation<TextureRegion> BaseballBat;
        public static Animation<TextureRegion> BaseballBatAttack;
        public static AtlasAnimation Miojlnir;
        public static AtlasAnimation WaterBomb;

        public static Animation<TextureRegion> coolCatSkin;
        public static Animation<TextureRegion> orangeCatSkin;
        public static Animation<TextureRegion> yinYangSkin;
        public static Animation<TextureRegion> mioSkin;

        public static Animation<TextureRegion> godseSkin;


        public static Animation<TextureRegion> coolCat;

        public static Animation<TextureRegion> tombstoneAnimation;

        public static ParticleEffectPool slimeParticle;

        public static ParticleEffectPool walkParticle;

        public static ParticleEffectPool damageParticle;
        public static ParticleEffectPool explosionParticle;
        public static ParticleEffectPool waterParticle;
        public static ParticleEffectPool splashParticle;


        public static TextureRegion inventoryCell;

        public static TextureRegion cookieIcon;
        public static TextureRegion sugarCaneIcon;

        public static HashMap<ProjectileAction.ProjectileType, AtlasAnimation> projectiles = new HashMap<ProjectileAction.ProjectileType, AtlasAnimation>() {};

        public static TextureRegion fastForwardButton;
        public static TextureRegion fastForwardButtonPressed;
        public static TextureRegion fastForwardButtonChecked;

        public static TextureRegion turnChange;

        public static TextureRegion turnTimer;
        public static NinePatchDrawable healthBarBackground;
        public enum GameCharacterAnimationType {
            ANIMATION_TYPE_IDLE,
            ANIMATION_TYPE_WALKING,
            ANIMATION_TYPE_FALLING,
            ANIMATION_TYPE_HIT,
            ANIMATION_TYPE_DEATH
        }
    }

}
