package com.almoatarknad.state;

import com.almoatarknad.MainGame;
import com.almoatarknad.grid.Grid;
import com.almoatarknad.input.MenuButton;
import com.almoatarknad.input.RestartButton;
import com.almoatarknad.input.UnpauseButton;
import com.almoatarknad.screen.ScreenManager;
import com.almoatarknad.screen.TitleScreen;
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
	private MenuButton menuButton;
	private UnpauseButton unpauseButton;
	
	private TitleState title;
	
	private Sprite redCounter, blueCounter, gameOverOverlay, backGroundFade;
	
	private Grid grid;
	
	private int score = 0;
	
	private FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/nulshockbd.ttf"));
	private FreeTypeFontParameter parameter = new FreeTypeFontParameter();
	private BitmapFont scoreFont, highscoreFont;
	private GlyphLayout layout = new GlyphLayout();
	
	private Preferences prefs = Gdx.app.getPreferences("highscore");
	private int highScore;
	
	private boolean hasFlushed = false, showingAd = false;
	
	public GameState(TitleState title) {
		this.title = title;
		title.reset();
		restartButton = new RestartButton(MainGame.WIDTH / 2 - 78, MainGame.HEIGHT / 2 - 100);
		menuButton = new MenuButton(MainGame.WIDTH / 2 - 125, MainGame.HEIGHT / 2);
		unpauseButton = new UnpauseButton(MainGame.WIDTH / 2 - 78, MainGame.HEIGHT / 2 + 100);
		highScore = prefs.getInteger("highscore");
		
		gameOverOverlay = new Sprite(new Texture(Gdx.files.internal("gui/gameoverlay.png")));
		gameOverOverlay.setPosition(0, MainGame.HEIGHT/ 2 - (gameOverOverlay.getHeight() / 2));
		
		backGroundFade = new Sprite(new Texture(Gdx.files.internal("gui/bgfade.png")));
		backGroundFade.setPosition(0, 0);
		backGroundFade.setSize(MainGame.WIDTH, MainGame.HEIGHT);
		backGroundFade.setAlpha(0.6f);
		grid = new Grid(15, 15, 390, 390, prefs);
		
		parameter.size = 20;
		parameter.shadowColor = new Color(0, 0, 0, 1f);
		parameter.shadowOffsetX = 1;
		parameter.shadowOffsetY = 1;
		score = grid.getScore();
		scoreFont = generator.generateFont(parameter);
		highscoreFont = generator.generateFont(parameter);
		redCounter = new Sprite(new Texture(Gdx.files.internal("gui/score/scorecounter.png")));
		redCounter.setSize(120, 26.4f);
		redCounter.setPosition(18, MainGame.HEIGHT - redCounter.getHeight() - 260);
		
		blueCounter = new Sprite(new Texture(Gdx.files.internal("gui/score/highscorecounter.png")));
		blueCounter.setSize(120, 26.4f);
		blueCounter.setPosition(MainGame.WIDTH - blueCounter.getWidth() - 18, MainGame.HEIGHT - blueCounter.getHeight() - 260);
		
		scoreFont.setColor(Color.WHITE);
		scoreFont.setFixedWidthGlyphs("0123456789");
		highscoreFont.setFixedWidthGlyphs("0123456789");
		grid.setPaused(false);
	}
	
	public void update() {
		if(!grid.isPaused())
			grid.update();
		score = grid.getScore();
		if(score > highScore) {
			highScore = score;
		}
		
		if(grid.isGameOver() && !hasFlushed) {
			System.out.println("GAME OVER");
			saveState();
			hasFlushed = true;
		} else if(!grid.isGameOver() && hasFlushed) {
			hasFlushed = false;
		}
		
		if(!grid.hasRestarted()) {
			saveState();
		}
		
		if(Gdx.input.justTouched()
							 && ScreenManager.getCurrentScreen().inputManager.getIntersecting(restartButton.getHitbox())) {
			if(grid.isGameOver()) {
				saveState();
				grid.restart();
				System.out.println("RESTART");
			}
		}
		
		if(Gdx.input.justTouched() && grid.isPaused()) {
			if(ScreenManager.getCurrentScreen().inputManager.getMouseHitbox().overlaps(menuButton.getHitbox())) {
				saveState();
				if(hasFlushed) {
					grid.newRound();
					grid.endRound();
					grid.setPaused(false);
					ScreenManager.setScreen(new TitleScreen(title));
				}
			} else if(ScreenManager.getCurrentScreen().inputManager.getMouseHitbox().overlaps(unpauseButton.getHitbox())) {
				grid.setPaused(false);
//				saveState();
			} else if(ScreenManager.getCurrentScreen().inputManager.getMouseHitbox().overlaps(restartButton.getHitbox())) {
//				saveState();
				grid.restart();
				grid.setPaused(false);
			}
		}
		
		handleAds();
	}
	
	public void render(SpriteBatch sb) {
		grid.render(sb);
//		layout.setText(scoreFont, String.valueOf(score));
		
		String scoreString = String.valueOf(score);
		redCounter.draw(sb);
		blueCounter.draw(sb);
		scoreCounter(sb);
		layout.setText(highscoreFont, String.valueOf(highScore));
		String highScoreString = String.valueOf(highScore);
		highScoreCounter(sb);
		if(grid.isGameOver()) {
			backGroundFade.draw(sb);
			gameOverOverlay.draw(sb);
			restartButton.render(sb);
		}
		
		if(grid.isPaused()) {
			backGroundFade.draw(sb);
			menuButton.render(sb);
			restartButton.render(sb);
			unpauseButton.render(sb);
		}
	}
	
	public void scoreCounter(SpriteBatch sb) {
		if(score < 10) {
			scoreFont.draw(sb, String.valueOf(score), redCounter.getX() + 2, redCounter.getY() + 20);
		} else if(score >= 10 && score < 100) {
			int secondNumber = score % 10;
			int newAmount = score - secondNumber;
			newAmount /= 10;
			int firstNumber = newAmount;
			scoreFont.draw(sb, String.valueOf(firstNumber), redCounter.getX() + 2, redCounter.getY() + 20);
			scoreFont.draw(sb, String.valueOf(secondNumber), redCounter.getX() + 25, redCounter.getY() + 20);
		} else if(score >= 100 && score < 1000) {
			int thirdNumber = score % 10;
			int newAmount = score - thirdNumber;
			newAmount /= 10;
			int secondNumber = newAmount % 10;
			newAmount /= 10;
			int firstNumber = newAmount;
			scoreFont.draw(sb, String.valueOf(firstNumber), redCounter.getX() + 2, redCounter.getY() + 20);
			scoreFont.draw(sb, String.valueOf(secondNumber), redCounter.getX() + 27, redCounter.getY() + 20);
			scoreFont.draw(sb, String.valueOf(thirdNumber), redCounter.getX() + 51, redCounter.getY() + 20);
		} else if(score >= 1000 && score < 10000) {
			int fourthNumber = score % 10;
			int newAmount = score - fourthNumber;
			newAmount /= 10;
			int thirdNumber = newAmount % 10;
			newAmount /= 10;
			int secondNumber = newAmount % 10;
			newAmount /= 10;
			int firstNumber = newAmount;
			scoreFont.draw(sb, String.valueOf(firstNumber), redCounter.getX() + 2, redCounter.getY() + 20);
			scoreFont.draw(sb, String.valueOf(secondNumber), redCounter.getX() + 27, redCounter.getY() + 20);
			scoreFont.draw(sb, String.valueOf(thirdNumber), redCounter.getX() + 51, redCounter.getY() + 20);
			scoreFont.draw(sb, String.valueOf(fourthNumber), redCounter.getX() + 75, redCounter.getY() + 20);
		} else if(score >= 10000 && score < 100000) {
			int fifthNumber = score % 10;
			int newAmount = score - fifthNumber;
			newAmount /= 10;
			int fourthNumber = newAmount % 10;
			newAmount /= 10;
			int thirdNumber = newAmount % 10;
			newAmount /= 10;
			int secondNumber = newAmount % 10;
			newAmount /= 10;
			int firstNumber = newAmount;
			scoreFont.draw(sb, String.valueOf(firstNumber), redCounter.getX() + 2, redCounter.getY() + 20);
			scoreFont.draw(sb, String.valueOf(secondNumber), redCounter.getX() + 27, redCounter.getY() + 20);
			scoreFont.draw(sb, String.valueOf(thirdNumber), redCounter.getX() + 51, redCounter.getY() + 20);
			scoreFont.draw(sb, String.valueOf(fourthNumber), redCounter.getX() + 75, redCounter.getY() + 20);
			scoreFont.draw(sb, String.valueOf(fifthNumber), redCounter.getX() + 99, redCounter.getY() + 20);
		}
	}
	
	public void highScoreCounter(SpriteBatch sb) {
		if(highScore < 10) {
			highscoreFont.draw(sb, String.valueOf(highScore), blueCounter.getX() + blueCounter.getWidth() - 19, blueCounter.getY() + 20);
		} else if(highScore >= 10 && highScore < 100) {
			int secondNumber = highScore % 10;
			int newAmount = highScore - secondNumber;
			newAmount /= 10;
			int firstNumber = newAmount;
			highscoreFont.draw(sb, String.valueOf(secondNumber), blueCounter.getX() + blueCounter.getWidth() - 19, blueCounter.getY() + 20);
			highscoreFont.draw(sb, String.valueOf(firstNumber), blueCounter.getX() + blueCounter.getWidth() - 45, blueCounter.getY() + 20);
		} else if(highScore >= 100 && highScore < 1000) {
			int thirdNumber = highScore % 10;
			int newAmount = highScore - thirdNumber;
			newAmount /= 10;
			int secondNumber = newAmount % 10;
			newAmount /= 10;
			int firstNumber = newAmount;
			highscoreFont.draw(sb, String.valueOf(thirdNumber), blueCounter.getX() + blueCounter.getWidth() - 19, blueCounter.getY() + 20);
			highscoreFont.draw(sb, String.valueOf(secondNumber), blueCounter.getX() + blueCounter.getWidth() - 45, blueCounter.getY() + 20);
			highscoreFont.draw(sb, String.valueOf(firstNumber), blueCounter.getX() + blueCounter.getWidth() - 70, blueCounter.getY() + 20);
		} else if(highScore >= 1000 && highScore < 10000) {
			int fourthNumber = highScore % 10;
			int newAmount = highScore - fourthNumber;
			newAmount /= 10;
			int thirdNumber = newAmount % 10;
			newAmount /= 10;
			int secondNumber = newAmount % 10;
			newAmount /= 10;
			int firstNumber = newAmount;
			highscoreFont.draw(sb, String.valueOf(fourthNumber), blueCounter.getX() + blueCounter.getWidth() - 19, blueCounter.getY() + 20);
			highscoreFont.draw(sb, String.valueOf(thirdNumber), blueCounter.getX() + blueCounter.getWidth() - 45, blueCounter.getY() + 20);
			highscoreFont.draw(sb, String.valueOf(secondNumber), blueCounter.getX() + blueCounter.getWidth() - 70, blueCounter.getY() + 20);
			highscoreFont.draw(sb, String.valueOf(firstNumber), blueCounter.getX() + blueCounter.getWidth() - 95, blueCounter.getY() + 20);
		} else if(highScore >= 10000 && highScore < 100000) {
			int fifthNumber = highScore % 10;
			int newAmount = highScore - fifthNumber;
			newAmount /= 10;
			int fourthNumber = newAmount % 10;
			newAmount /= 10;
			int thirdNumber = newAmount % 10;
			newAmount /= 10;
			int secondNumber = newAmount % 10;
			newAmount /= 10;
			int firstNumber = newAmount;
			highscoreFont.draw(sb, String.valueOf(fifthNumber), blueCounter.getX() + blueCounter.getWidth() - 19, blueCounter.getY() + 20);
			highscoreFont.draw(sb, String.valueOf(fourthNumber), blueCounter.getX() + blueCounter.getWidth() - 45, blueCounter.getY() + 20);
			highscoreFont.draw(sb, String.valueOf(thirdNumber), blueCounter.getX() + blueCounter.getWidth() - 70, blueCounter.getY() + 20);
			highscoreFont.draw(sb, String.valueOf(secondNumber), blueCounter.getX() + blueCounter.getWidth() - 95, blueCounter.getY() + 20);
			highscoreFont.draw(sb, String.valueOf(firstNumber), blueCounter.getX() + blueCounter.getWidth() - 120, blueCounter.getY() + 20);
		}
	}
	
	public void saveState() {
		hasFlushed = false;
		int currentHighScore = prefs.getInteger("highscore");
		System.out.println("CURRENT HIGH: " + currentHighScore);
		prefs.clear();
		prefs.flush();
		for(int i = 0; i < grid.getBlocks().size; i++) {
			prefs.putFloat("x" + i, grid.getBlocks().get(i).getX());
			prefs.putFloat("y" + i, grid.getBlocks().get(i).getY());
			prefs.putFloat("val" + i, grid.getBlocks().get(i).getValue());
			System.out.println("NUM: " + i);
		}
		
		prefs.putInteger("score", grid.getScore());
		
		if(currentHighScore < score) {
			prefs.putInteger("highscore", score);
			System.out.println("NEW HIGH SCORE");
		} else {
			prefs.putInteger("highscore", currentHighScore);
		}
		prefs.flush();
		hasFlushed = true;
		currentHighScore = prefs.getInteger("highscore");
		System.out.println("CURRENT HIGH: " + currentHighScore);
	}
	
	public void handleAds() {
		if(grid.isPaused() && !showingAd) {
//			MainGame.actionResolver.showOrLoadInterstital();
//			MainGame.handler.showAds(true);
			if(MainGame.adsController.isWifiConnected())
				MainGame.adsController.showBannerAd();
			showingAd = true;
		} else if(grid.isGameOver() && !showingAd) {
//			MainGame.actionResolver.showOrLoadInterstital();
//			MainGame.handler.showAds(true);
			if(MainGame.adsController.isWifiConnected())
				MainGame.adsController.showBannerAd();
			showingAd = true;
		} else if(!grid.isPaused() && !grid.isGameOver()) {
//			MainGame.handler.showAds(false);
			MainGame.adsController.hideBannerAd();
			showingAd = false;
		}
	}
	
	public Preferences getPrefs() {
		return prefs;
	}
}
