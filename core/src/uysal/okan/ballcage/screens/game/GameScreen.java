package uysal.okan.ballcage.screens.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;

import uysal.okan.ballcage.BallCage;
import uysal.okan.ballcage.util.GdxUtils;

public class GameScreen implements Screen {

    private final BallCage game;
    private final AssetManager assetManager;
    private final int stage;
    private boolean showAd = false;

    private GameController controller;
    private GameRenderer renderer;

    public GameScreen(BallCage game, int stage) {
        this.game = game;
        this.stage = stage;
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        controller = new GameController(game);
        renderer = new GameRenderer(game, game.getBatch(), assetManager, controller, stage);
        game.getAdController().showBanner();
    }

    @Override
    public void render(float delta) {
        if(!showAd && GdxUtils.loadAd) {
            game.getAdController().ShowBanner();
            showAd = true;
        }
        controller.update(delta);
        renderer.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
