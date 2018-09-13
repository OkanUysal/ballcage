package uysal.okan.ballcage;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.TimeUtils;

import uysal.okan.ballcage.config.GameConfig;
import uysal.okan.ballcage.screens.loading.LoadingScreen;
import uysal.okan.ballcage.util.GdxUtils;

public class BallCage extends Game {

	private final AdController adController;
	private AssetManager assetManager;
	private SpriteBatch batch;

	public BallCage(AdController adController) {
		this.adController = adController;
	}

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		GdxUtils.adsTime = TimeUtils.millis();

		adController.connect();

		assetManager = new AssetManager();
		assetManager.getLogger().setLevel(Logger.DEBUG);

		batch = new SpriteBatch();

		GameConfig.PREFS = Gdx.app.getPreferences(BallCage.class.getSimpleName());

		getAdController().showBanner();

		setScreen(new LoadingScreen(this));
	}

	@Override
	public void dispose() {
		assetManager.dispose();
		batch.dispose();
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public AdController getAdController() {
		return adController;
	}
}
