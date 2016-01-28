package com.almoatarknad.state;

import com.almoatarknad.MainGame;
import com.almoatarknad.input.MenuButton;
import com.almoatarknad.input.TitleButton;
import com.almoatarknad.screen.GameScreen;
import com.almoatarknad.screen.ScreenManager;
import com.almoatarknad.texture.TextureManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TitleState {
	
	private Sprite logga, loading, howToImg;
	private TitleButton play, howto;
	private MenuButton menuButton;
	private float cameraY = MainGame.HEIGHT / 2, cameraX = MainGame.WIDTH / 2;
	private boolean moveToHowTo = false, moveToMenu = false, moveToLoading = false;
	private float cameraSpeed = 30f, delay = 1f;
	private GameState game;
	
	public TitleState() {
		logga = ScreenManager.getCurrentScreen().textureManager.getLogga();
		logga.setSize(MainGame.WIDTH / 1.5f, MainGame.WIDTH / 1.5f);
		logga.setPosition(MainGame.WIDTH / 2 - logga.getWidth() / 2, MainGame.HEIGHT / 2);
		play = new TitleButton(MainGame.WIDTH / 2 - 175, MainGame.HEIGHT / 2 - 250, 150, 180, 0);
		howto = new TitleButton(MainGame.WIDTH / 2 + 25, MainGame.HEIGHT / 2 - 250, 150, 180, 1);
		menuButton = new MenuButton(MainGame.WIDTH / 2 - 125, 0 - MainGame.HEIGHT / 2 - 350);
		loading = new Sprite(new Texture(Gdx.files.internal("loading.png")));
		loading.setSize(MainGame.WIDTH, 250);
		loading.setPosition(MainGame.WIDTH  + 210, MainGame.HEIGHT / 2);
		howToImg = new Sprite(new Texture(Gdx.files.internal("title/howto.png")));
		howToImg.setSize(420, 780);
		howToImg.setPosition(0, 0 - MainGame.HEIGHT - 75);
	}
	
	public void update() {
		if(Gdx.input.justTouched() && ScreenManager.getCurrentScreen().inputManager.getMouseHitbox().overlaps(play.getHitBox())) {
			if(!moveToHowTo && !moveToMenu) {
				moveToLoading = true;
				moveToHowTo = false;
				moveToMenu = false;
			}
		} else if(Gdx.input.justTouched() && ScreenManager.getCurrentScreen().inputManager.getMouseHitbox().overlaps(howto.getHitBox())) {
			if(!moveToLoading && !moveToMenu) {
				moveToHowTo = true;
				moveToMenu = false;
				cameraSpeed = 30f;
			}
			
		} else if(Gdx.input.justTouched() && ScreenManager.getCurrentScreen().inputManager.getMouseHitbox().overlaps(menuButton.getHitbox())) {
			if(!moveToHowTo && !moveToLoading) {
				cameraSpeed = 30f;
				moveToHowTo = false;
				moveToMenu = true;
			}
		}
		
		if(moveToHowTo) {
			goToHowTo();
		} else if(moveToMenu) {
			goToMenu();
		} else if(moveToLoading) {
			goToLoadingScreen();
		}
		
		if(cameraX >= MainGame.WIDTH * 2) {
			delay -= 0.1f;
			if(delay <= 0)
				delay = 0;
			if(delay == 0) {
				if(game == null) {
					game = new GameState(this);
				}
				ScreenManager.setScreen(new GameScreen(this, game));
			}
		}
		
		ScreenManager.getCurrentScreen().camera.position.y = cameraY;
		ScreenManager.getCurrentScreen().camera.position.x = cameraX;
	}
	
	public void render(SpriteBatch sb) {
		sb.begin();
		Gdx.gl.glClearColor(0.85f, 0.85f, 0.85f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		logga.draw(sb);
		play.render(sb);
		howto.render(sb);
		menuButton.render(sb);
		loading.draw(sb);
		howToImg.draw(sb);
		sb.end();
	}
	
	public void goToHowTo() {
		if(cameraY > MainGame.HEIGHT / 2 - MainGame.HEIGHT) {
			cameraY -= cameraSpeed;
			cameraSpeed -= 0.65f;
			if(cameraSpeed < 4)
				cameraSpeed = 4;
			if(cameraY <= MainGame.HEIGHT / 2 - MainGame.HEIGHT) {
				cameraY = MainGame.HEIGHT / 2 - MainGame.HEIGHT;
				moveToHowTo = false;
				cameraSpeed = 30f;
			}
		}
	}
	
	public void goToMenu() {
		if(cameraY < MainGame.HEIGHT / 2) {
			cameraY += cameraSpeed;
			cameraSpeed -= 0.65f;
			if(cameraSpeed < 4)
				cameraSpeed = 4f;
			if(cameraY >= MainGame.HEIGHT / 2) {
				cameraY = MainGame.HEIGHT / 2;
				moveToMenu = false;
				cameraSpeed = 30f;
			}
		}
	}
	
	public void goToLoadingScreen() {
		if(cameraX < MainGame.WIDTH * 2) {
			cameraX += cameraSpeed;
			cameraSpeed -= 0.65f;
			if(cameraSpeed < 4)
				cameraSpeed = 4;
			if(cameraX >= MainGame.WIDTH * 2) {
				cameraX = MainGame.WIDTH * 2;
				cameraSpeed = 30f;
				moveToLoading = false;
			}
		}
	}
	
	public void reset() {
		moveToHowTo = false;
		moveToLoading = false;
		moveToMenu = false;
		cameraX = MainGame.WIDTH / 2;
		cameraY = MainGame.HEIGHT / 2;
		delay = 1f;
	}
}
