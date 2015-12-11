package com.almoatarknad.desktop;

import com.almoatarknad.MainGame;
import com.almoatarknad.googleplay.DesktopGoogleServices;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = MainGame.WIDTH;
		config.height = MainGame.HEIGHT;
		config.useGL30 = false;
		new LwjglApplication(new MainGame(new DesktopGoogleServices()), config);
	}
}
