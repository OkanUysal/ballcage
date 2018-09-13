package uysal.okan.ballcage.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

public class GdxUtils {

    public static String GAME_NAME = "[#960019]BALL CAGE";

    public static int star = 0;
    public static int highScore = 0;

    public static boolean loadAd = false;

    public static long adsTime = 0;

    public static void clearScreen() {
        clearScreen(Color.BLACK);
    }

    //public static void burlywood() {clearScreen(new Color(255 / 255f,211 / 255f,135 / 255f, 1f));}

    public static void clearScreen(Color color) {
        // clear screen
        // DRY - Don't repeat yourself
        // WET - Waste everyone's time
        Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public static int[] gameBlockArray(int stage) {
        int[] array = null;
        switch (stage) {
            case 1:
                array = new int[]{1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1};
                break;
            case 2:
                array = new int[]{1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1};
                break;
            case 3:
                array = new int[]{1, 0, 2, 0, 0, 0, 0, 0, 0, 2, 0, 1};
                break;
            case 4:
                array = new int[]{1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1};
                break;
            case 5:
                array = new int[]{1, 0, 2, 0, 0, 0, 0, 0, 0, 2, 0, 1};
                break;
            case 6:
                array = new int[]{1, 0, 2, 0, 0, 0, 0, 0, 0, 2, 0, 1};
                break;
            case 7:
                array = new int[]{0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0};
                break;
            case 8:
                array = new int[]{3, 0, 0, 0, 0, 0, -1, 0, 1, 0, 0, 0};
                break;
            case 9:
                array = new int[]{-3, 2, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0};
                break;
            case 10:
                array = new int[]{1, 0, 0, 2, 0, 2, 0, 0, 1, 0, 0, -2};
                break;
            case 11:
                array = new int[]{0, 1, 0, 0, 2, -2, 0, 0, -4, 1, 0, 0};
                break;
            case 12:
                array = new int[]{0, 0, 2, 0, 0, 0, 0, 0, 4, 0, 0, 0};
                break;
            case 13:
                array = new int[]{-2, 0, 0, 0, 0, 0, 0, 0, 3, 1, 0, 4};
                break;
            case 14:
                array = new int[]{1, 0, 3, 0, 0, 0, 0, 0, 0, 2, 0, 1};
                break;
            case 15:
                array = new int[]{3, 0, 0, 0, 1, 0, 0, 2, 0, 0, 0, 3};
                break;
            case 16:
                array = new int[]{1, 0, 2, 0, 0, 0, 0, 0, 0, 2, 0, 1};
                break;
            case 17:
                array = new int[]{1, 0, 2, 0, 0, 0, 0, 0, 0, 3, 0, 4};
                break;
            case 18:
                array = new int[]{0, -1, 1, 0, 0, -1, 0, 0, 0, 0, 3, 0};
                break;
            case 19:
                array = new int[]{0, 0, 0, 1, -1, 2, 2, -1, 1, 0, 0, 0};
                break;
            case 20:
                array = new int[]{1, 2, 0, 3, 4, 0, 0, 0, 0, 0, 0, 0};
                break;
            case 21:
                array = new int[]{1, -1, 2, -1, 0, -1, -1, 0, -1, 2, -1, 1};
                break;
            case 22:
                array = new int[]{1, 0, 2, 0, 2, 0, 0, 0, 0, 0, 2, -1};
                break;
            case 23:
                array = new int[]{2, -3, 0, 2, 0, 1, 1, 2, 0, 0, -2, 0};
                break;
            case 24:
                array = new int[]{3, -4, 3, 1, 1, 0, 0, 0, -1, 0, 0, 2};
                break;
            case 25:
                array = new int[]{0, 1, 0, 0, 0, 0, 0, 0, 0, 2, 3, 0};
                break;
            case 26:
                array = new int[]{0, 1, 0, 0, 3, 1, 0, 0, 0, 0, 2, 0};
                break;
            case 27:
                array = new int[]{3, 1, 0, -2, 0, 2, 0, 0, 0, 0, 0, 2};
                break;
            case 28:
                array = new int[]{0, 0, 1, 0, 0, 0, 0, -3, 2, 1, 0, 3};
                break;
            case 29:
                array = new int[]{0, 0, 3, 2, 0, 0, 1, -3, 3, 0, 0, 0};
                break;
        }

        return array;
    }

    public static float[][] gameBallArray(int stage) {
        float[][] array = null;
        switch (stage) {
            case 1:
                array = new float[][]{{0, 5, 3, 1, -1.5f}};
                break;
            case 2:
                array = new float[][]{{0, 5, 3, 1, -1.5f}, {0, 7, 11, 2, 3.5f}};
                break;
            case 3:
                array = new float[][]{{0, 5, 3, 3, -2}, {1, 6, 15, -3, 2}};
                break;
            case 4:
                array = new float[][]{{0, 5, 3, 3, -2}, {0, 6, 3, 3, 3}, {0, 6, 15, -3, 2}};
                break;
            case 5:
                array = new float[][]{{0, 5, 3, 3, -2}, {0, 6, 3, 3, 3}, {1, 6, 15, -3, 2}};
                break;
            case 6:
                array = new float[][]{{0, 5, 3, 3, -2}, {0, 6, 3, 3, 3},{1, 5, 4, -1, 1},{1, 6, 4, -3, -3}};
                break;
            case 7:
                array = new float[][]{{0, 5, 7, 3, -2}, {0, 6, 6, 3, 3}, {0, 5, 6, 1.5f, -2}};
                break;
            case 8:
                array = new float[][]{{2f, 1f, 10f, -0.9f, 0.38f}, {0f, 7f, 10f, -2.87f, -0.82f}};
                break;
            case 9:
                array = new float[][]{{1f, 11f, 1f, -1.71f, -1.42f}, {0f, 1f, 13f, -0.37f, 2.47f}, {0f, 11f, 3f, 0.42f, 0.84f}, {4f, 4f, 9f, 2.27f, 1.41f}};
                break;
            case 10:
                array = new float[][]{{0f, 1f, 13f, -1.78f, 0.91f}, {4f, 5f, 10f, 2.17f, -1.06f}, {1f, 3f, 14f, 1.74f, -2.96f}};
                break;
            case 11:
                array = new float[][]{{0f, 1f, 8f, -2.77f, -2.17f}, {0f, 9f, 14f, 2.82f, 0.92f}, {1f, 10f, 3f, -1.4f, -0.42f}};
                break;
            case 12:
                array = new float[][]{{1f, 11f, 9f, -1.64f, -1.39f}, {3f, 11f, 2f, 1.72f, 2.57f}};
                break;
            case 13:
                array = new float[][]{{3f, 1f, 16f, -2.85f, 0.43f}, {2f, 3f, 13f, 0.64f, 1.66f}, {0f, 6f, 10f, -2.38f, 2.73f}};
                break;
            case 14:
                array = new float[][]{{0, 5, 7, 3, -2}, {0, 6, 6, 3, 3}, {1, 5, 6, 1.5f, -2}, {2, 5, 5, 3, -2}};
                break;
            case 15:
                array = new float[][]{{0, 5, 14, 3, -2}, {1, 6, 1, 3, 3}, {2, 5, 1, 1.5f, -2}, {2, 5, 15, -2, -2}};
                break;
            case 16:
                array = new float[][]{{0, 5, 7, 3, -2}, {1, 6, 6, 3, 3}, {4, 5, 7, 3, -3}};
                break;
            case 17:
                array = new float[][]{{0, 5, 7, 3, -2}, {1, 10, 8, 3, 3}, {2, 6, 8, 3.5f, -2}, {3, 9, 7, 1.5f, 2}};
                break;
            case 18:
                array = new float[][]{{0, 1, 1, 3, -2},{2, 5, 5, 3, -2}};
                break;
            case 19:
                array = new float[][]{{0, 6, 7, 3, -2}, {0, 5, 8, 3.5f, -2}, {1, 6, 8, 1.5f, 2}, {1, 5, 7, 1, -2}};
                break;
            case 20:
                array = new float[][]{{0, 8, 9, 3, -2}, {1, 6, 10, 3.5f, -2}, {2, 8, 10, 1.5f, 2}, {3, 7, 9, 1, -2}};
                break;
            case 21:
                array = new float[][]{{0, 6, 7, 3, -2}, {0, 5, 8, 3.5f, -2}, {1, 6, 8, 1.5f, 2}, {1, 5, 7, 1, -2}};
                break;
            case 22:
                array = new float[][]{{1f, 3f, 16f, 0.18f, 2.16f}, {1f, 9f, 7f, 1.21f, -1.39f}, {0f, 10f, 8f, -2.87f, 0.71f}, {4f, 10f, 11f, -0.51f, -0.57f}, {1f, 5f, 12f, 1.2f, 1.35f}};
                break;
            case 23:
                array = new float[][]{{0f, 7f, 6f, -0.84f, -2.0f}, {4f, 10f, 16f, -0.94f, 0.36f}, {0f, 4f, 2f, -2.26f, 2.01f}, {1f, 6f, 5f, 2.91f, -0.88f}};
                break;
            case 24:
                array = new float[][]{{4f, 5f, 16f, -0.86f, 1.7f}, {0f, 10f, 7f, 1.59f, 2.38f}, {1f, 9f, 6f, -0.65f, 1.44f}, {0f, 2f, 2f, -0.44f, 2.88f}, {2f, 3f, 8f, -2.72f, 0.61f}};
                break;
            case 25:
                array = new float[][]{{0f, 9f, 5f, -1.48f, -2.87f}, {1f, 5f, 3f, -0.17f, 1.35f}, {2f, 4f, 4f, -0.61f, -0.46f}, {4f, 2f, 2f, -2.88f, -2.77f}};
                break;
            case 26:
                array = new float[][]{{2f, 3f, 1f, -1.07f, 0.51f}, {0f, 3f, 2f, 2.2f, 2.67f}, {0f, 1f, 15f, 0.04f, 1.69f}, {1f, 10f, 1f, -1.02f, -2.89f}};
                break;
            case 27:
                array = new float[][]{{1f, 2f, 12f, -0.81f, 0.65f}, {2f, 6f, 16f, 1.22f, -2.63f}, {0f, 4f, 15f, 1.78f, 1.77f}, {1f, 5f, 10f, 1.14f, -1.05f}};
                break;
            case 28:
                array = new float[][]{{1f, 5f, 11f, -0.27f, 2.66f}, {4f, 5f, 11f, 1.92f, -1.94f}, {0f, 5f, 10f, -0.21f, 2.86f}, {0f, 1f, 3f, 1.16f, 1.38f}, {2f, 5f, 16f, 0.61f, -1.18f}};
                break;
            case 29:
                array = new float[][]{{4f, 5f, 16f, -0.76f, -0.79f}, {0f, 6f, 3f, -0.7f, -1.88f}, {2f, 1f, 5f, -0.43f, 0.15f}, {2f, 5f, 1f, -2.74f, -0.96f}, {1f, 10f, 16f, -0.78f, -0.89f}};
                break;
        }
        return array;
    }

    private GdxUtils() {}
}
