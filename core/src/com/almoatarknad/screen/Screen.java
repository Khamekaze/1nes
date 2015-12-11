package com.almoatarknad.screen;

import com.almoatarknad.MainGame;
import com.almoatarknad.input.InputManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class Screen {
	
	public InputManager inputManager = new InputManager();
	public OrthographicCamera camera = new OrthographicCamera(MainGame.WIDTH, MainGame.HEIGHT);
	public Viewport viewPort = new StretchViewport(MainGame.WIDTH, MainGame.HEIGHT, camera);

	public abstract void create();
	
	public abstract void update();
	
	public abstract void render(SpriteBatch sb);
	
	public abstract void resize(int width, int height);
	
	public abstract void dispose();
	
	public abstract void pause();
	
	public abstract void resume();
	
}
