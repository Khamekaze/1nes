package com.almoatarknad.screen;

import com.almoatarknad.MainGame;
import com.almoatarknad.state.GameState;
import com.almoatarknad.state.TitleState;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameScreen extends Screen {
	
	private GameState game;
	private TitleState title;
	
	public GameScreen(TitleState title) {
		this.title = title;
	}
	
	public GameScreen(TitleState title, GameState game) {
		this.game = game;
		this.title = title;
	}
	
	public GameScreen() {
//		title = new TitleState();
//		game = new GameState(title);
	}

	@Override
	public void create() {
		camera.position.set(MainGame.WIDTH / 2, MainGame.HEIGHT / 2, 0);
		Gdx.input.setInputProcessor(inputManager);
		inputManager.update();
		camera.update();
	}

	@Override
	public void update() {
		camera.position.set(MainGame.WIDTH / 2, MainGame.HEIGHT / 2, 0);
		camera.update();
		inputManager.update();
		game.update();
		inputManager.update();
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
		camera.translate(0, 0);
		
	}

	@Override
	public void dispose() {
		game.saveState();
//		game = null;
	}

	@Override
	public void pause() {
//		game.saveState();
	}

	@Override
	public void resume() {
		
	}

}
