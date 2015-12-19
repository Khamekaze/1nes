package com.almoatarknad.screen;

import com.almoatarknad.MainGame;
import com.almoatarknad.grid.Grid;
import com.almoatarknad.state.GameState;
import com.almoatarknad.state.TitleState;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TitleScreen extends Screen {
	
	private TitleState title;
	private GameState game;
	
	public TitleScreen(TitleState title) {
		this.title = title;
		this.title.reset();
	}
	
	public TitleScreen(TitleState title, GameState game) {
		this.title = title;
		this.title.reset();
		this.game = game;
	}
	
	public TitleScreen() {
		title = new TitleState();
	}

	@Override
	public void create() {
		
		Gdx.input.setInputProcessor(inputManager);
		camera.position.set(MainGame.WIDTH / 2, MainGame.HEIGHT / 2, 0);
		camera.update();
		inputManager.update();
	}

	@Override
	public void update() {
//		camera.position.set(MainGame.WIDTH / 2, MainGame.HEIGHT / 2, 0);
		camera.update();
		inputManager.update();
		title.update();
		
	}

	@Override
	public void render(SpriteBatch sb) {
		sb.setProjectionMatrix(camera.combined);
		title.render(sb);
	}

	@Override
	public void resize(int width, int height) {
		viewPort.update(width, height);
		camera.translate(0, 0);
	}

	@Override
	public void dispose() {
		title = null;
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

}
