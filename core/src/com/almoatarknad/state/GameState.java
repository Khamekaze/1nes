package com.almoatarknad.state;

import com.almoatarknad.MainGame;
import com.almoatarknad.grid.Grid;
import com.almoatarknad.input.RestartButton;
import com.almoatarknad.screen.ScreenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class GameState {
	
	private RestartButton restartButton;
	
	private Sprite redCounter, blueCounter, gameOverOverlay, backGroundFade;
	
	private Grid grid;
	
	private int score = 0;
	
	private FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/nulshockbd.ttf"));
	private FreeTypeFontParameter parameter = new FreeTypeFontParameter();
	private BitmapFont scoreFont, highscoreFont;
	private GlyphLayout layout = new GlyphLayout();
	
	private Preferences prefs = Gdx.app.getPreferences("highscore");
	private int highScore;
	
	private boolean hasFlushed = false;
	
	public GameState() {
		
		restartButton = new RestartButton();
		highScore = prefs.getInteger("highscore", 0);
		
		gameOverOverlay = new Sprite(new Texture(Gdx.files.internal("gui/gameoverlay.png")));
		gameOverOverlay.setPosition(0, MainGame.HEIGHT/ 2 - (gameOverOverlay.getHeight() / 2));
		restartButton.setPosition(MainGame.WIDTH / 2, gameOverOverlay.getY());
		
		backGroundFade = new Sprite(new Texture(Gdx.files.internal("gui/bgfade.png")));
		backGroundFade.setPosition(0, 0);
		backGroundFade.setSize(MainGame.WIDTH, MainGame.HEIGHT);
		backGroundFade.setAlpha(0.6f);
		
		grid = new Grid(15, 15, 390, 390);
		parameter.size = 20;
		parameter.shadowColor = new Color(0, 0, 0, 1f);
		parameter.shadowOffsetX = 1;
		parameter.shadowOffsetY = 1;
		score = grid.getScore();
		scoreFont = generator.generateFont(parameter);
		highscoreFont = generator.generateFont(parameter);
		redCounter = new Sprite(new Texture(Gdx.files.internal("gui/score/redscorecounter.png")));
		redCounter.setSize(120, 90);
		redCounter.setPosition(0, MainGame.HEIGHT - redCounter.getHeight() - 200);
		
		blueCounter = new Sprite(new Texture(Gdx.files.internal("gui/score/bluescorecounter.png")));
		blueCounter.setSize(120, 90);
		blueCounter.setPosition(MainGame.WIDTH - blueCounter.getWidth(), MainGame.HEIGHT - blueCounter.getHeight() - 200);
		
		scoreFont.setColor(Color.WHITE);
		scoreFont.getData().setScale(1.2f);
	}
	
	public void update() {
		grid.update();
		score = grid.getScore();
		if(score > highScore) {
			highScore = score;
		}
		
		if(grid.isGameOver() && !hasFlushed) {
			int currentHighScore = prefs.getInteger("highscore", 0);
			if(currentHighScore < highScore)
				prefs.putInteger("highscore", highScore);
			
			prefs.flush();
			
			System.out.println("GAME OVER");
			hasFlushed = true;
		} else if(!grid.isGameOver() && hasFlushed) {
			hasFlushed = false;
		}
		
		if(!grid.hasRestarted()) {
			int currentHighScore = prefs.getInteger("highscore", 0);
			if(currentHighScore < highScore) {
				prefs.putInteger("highscore", highScore);
			}
			prefs.flush();
		}
		
		if(Gdx.input.justTouched()
							 && ScreenManager.getCurrentScreen().inputManager.getIntersecting(restartButton.getHitbox())) {
			grid.restart();
		}
	}
	
	public void render(SpriteBatch sb) {
		grid.render(sb);
		layout.setText(scoreFont, String.valueOf(score));
		String scoreString = String.valueOf(score);
		redCounter.draw(sb);
		blueCounter.draw(sb);
		scoreFont.draw(sb, scoreString, redCounter.getX() + redCounter.getWidth() / 2 - layout.width / 2, redCounter.getY() + 40);
		layout.setText(highscoreFont, String.valueOf(highScore));
		String highScoreString = String.valueOf(highScore);
		highscoreFont.draw(sb, highScoreString, blueCounter.getX() + blueCounter.getWidth() / 2 - layout.width / 2, blueCounter.getY() + 40);
		if(grid.isGameOver()) {
			backGroundFade.draw(sb);
			gameOverOverlay.draw(sb);
			restartButton.render(sb);
		}
	}
	
	public Preferences getPrefs() {
		return prefs;
	}
}
