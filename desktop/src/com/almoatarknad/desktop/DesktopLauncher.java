package com.almoatarknad.desktop;

import com.almoatarknad.ActionResolver;
import com.almoatarknad.MainGame;
import com.almoatarknad.ads.IActivityRequestHandler;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	private static DesktopLauncher app;
	public static void main (String[] arg) {
		if(app == null)
			app = new DesktopLauncher();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = MainGame.WIDTH;
		config.height = MainGame.HEIGHT;
		config.useGL30 = false;
		new LwjglApplication(new MainGame(null), config);
	}
}
