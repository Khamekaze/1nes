package com.almoatarknad.screen;

import com.almoatarknad.MainGame;
import com.almoatarknad.state.SplashState;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SplashScreen extends Screen {
	
	private SplashState splash;

	@Override
	public void create() {
		splash = new SplashState();
		camera.position.set(MainGame.WIDTH / 2, MainGame.HEIGHT / 2, 0);
		camera.update();
		inputManager.update();
	}

	@Override
	public void update() {
		camera.position.set(MainGame.WIDTH / 2, MainGame.HEIGHT / 2, 0);
		camera.update();
		inputManager.update();
		splash.update();
	}

	@Override
	public void render(SpriteBatch sb) {
		sb.setProjectionMatrix(camera.combined);
		splash.render(sb);
	}

	@Override
	public void resize(int width, int height) {
		viewPort.update(width, height);
		camera.translate(0, 0);
	}

	@Override
	public void dispose() {
		splash = null;
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

}
