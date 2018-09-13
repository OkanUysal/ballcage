package uysal.okan.ballcage.config;

import com.badlogic.gdx.Preferences;

/**
 * Created by goran on 22/08/2016.
 */
public class GameConfig {

    public static final float WIDTH = 540f; // pixels
    public static final float HEIGHT = 960f; // pixels

    public static final float HUD_WIDTH = 720f; // world units
    public static final float HUD_HEIGHT = 1280f; // world units

    public static final float WORLD_WIDTH = 9.0f; // world units
    public static final float WORLD_HEIGHT = 16.0f; // world units

    public static final float GAME_MENU_HEIGHT = 210f;

    public static final float WORLD_CENTER_X = WORLD_WIDTH / 2f; // world units
    public static final float WORLD_CENTER_Y = WORLD_HEIGHT / 2f; // world units

    public static final float PIXELS_TO_METERS = 100f;
    public static final float DEGREES_TO_RADIANS = (float)(Math.PI/180);

    public static int level = 1;
    public static int maxLevel = 29;
    public static int ballvar = 0;

    public static Preferences PREFS;
    public static final String STAR_KEY = "star";
    public static final String STAGE_KEY = "stage";
    public static final String HIGHSCORE_KEY = "highscore";


    private GameConfig() {}
}
