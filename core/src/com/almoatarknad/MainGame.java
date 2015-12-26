package com.almoatarknad;

import com.almoatarknad.ads.AdsController;
import com.almoatarknad.ads.DummyAdsController;
import com.almoatarknad.screen.ScreenManager;
import com.almoatarknad.screen.SplashScreen;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainGame extends ApplicationAdapter {
	
	public static AdsController adsController;
	
	public static int WIDTH = 420, HEIGHT = 700;
	SpriteBatch batch;
	
	public MainGame(AdsController adsController) {
		if(adsController != null) {
			this.adsController = adsController;
		} else {
			this.adsController = new DummyAdsController();
		}
	}
	
	public MainGame() {
		
	}
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		ScreenManager.setScreen(new SplashScreen());
//		ScreenManager.setScreen(new GameScreen());
	}
	
	@Override
	public void render () {
		Gdx.gl.glClearColor(0.01f, 0.01f, 0.01f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		if(ScreenManager.getCurrentScreen() != null)
			ScreenManager.getCurrentScreen().render(batch);
		
		if(ScreenManager.getCurrentScreen() != null)
			ScreenManager.getCurrentScreen().update();
	}
	
	@Override
	public void dispose() {
		if(ScreenManager.getCurrentScreen() != null)
			ScreenManager.getCurrentScreen().dispose();
		batch.dispose();
	}
	
	@Override
	public void resize(int width, int height) {
		if(ScreenManager.getCurrentScreen() != null)
			ScreenManager.getCurrentScreen().resize(width, height);
		
	}
	
	@Override
	public void resume() {
		if(ScreenManager.getCurrentScreen() != null)
			ScreenManager.getCurrentScreen().resume();

	}
	
	@Override
	public void pause() {
		if(ScreenManager.getCurrentScreen() != null)
			ScreenManager.getCurrentScreen().pause();
		
	}
}
