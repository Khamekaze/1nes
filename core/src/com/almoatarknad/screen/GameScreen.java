package com.almoatarknad.screen;

import com.almoatarknad.MainGame;
import com.almoatarknad.state.GameState;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameScreen extends Screen {
	
	private GameState game;

	@Override
	public void create() {
		game = new GameState();
		camera.position.set(0, 0, 0);
		Gdx.input.setInputProcessor(inputManager);
		camera.update();
	}

	@Override
	public void update() {
		camera.position.set(MainGame.WIDTH / 2, MainGame.HEIGHT / 2, 0);
		camera.update();
		inputManager.update();
		game.update();
	}

	@Override
	public void render(SpriteBatch sb) {
		sb.begin();
		sb.setProjectionMatrix(camera.combined);
		game.render(sb);
		sb.end();
	}

	@Override
	public void resize(int width, int height) {
		viewPort.update(width, height);
		camera.translate(MainGame.WIDTH / 2, MainGame.HEIGHT / 2, 0);
		
	}

	@Override
	public void dispose() {

	}

	@Override
	public void pause() {
		game.getPrefs().flush();
	}

	@Override
	public void resume() {
		
	}

}
