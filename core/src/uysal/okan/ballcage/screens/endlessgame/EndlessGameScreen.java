package uysal.okan.ballcage.screens.endlessgame;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;

import uysal.okan.ballcage.BallCage;
import uysal.okan.ballcage.screens.game.GameController;
import uysal.okan.ballcage.screens.game.GameRenderer;
import uysal.okan.ballcage.util.GdxUtils;

public class EndlessGameScreen implements Screen {

    private final BallCage game;
    private final AssetManager assetManager;
    private boolean showAd = false;

    private EndlessGameController controller;
    private EndlessGameRenderer renderer;

    public EndlessGameScreen(BallCage game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        controller = new EndlessGameController(game);
        renderer = new EndlessGameRenderer(game, game.getBatch(), assetManager, controller);
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
