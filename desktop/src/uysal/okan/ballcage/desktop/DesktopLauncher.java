package uysal.okan.ballcage.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import uysal.okan.ballcage.AdController;
import uysal.okan.ballcage.BallCage;
import uysal.okan.ballcage.config.GameConfig;

public class DesktopLauncher {

	private static final AdController AD_CONTROLLER = new DesktopAdController();

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = (int) GameConfig.WIDTH / 2;
		config.height = (int) GameConfig.HEIGHT / 2;
		new LwjglApplication(new BallCage( AD_CONTROLLER), config);
	}
}
