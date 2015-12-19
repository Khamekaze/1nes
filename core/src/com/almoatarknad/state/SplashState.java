package com.almoatarknad.state;

import com.almoatarknad.MainGame;
import com.almoatarknad.screen.ScreenManager;
import com.almoatarknad.screen.TitleScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SplashState {
	
	private Animation splashLogo;
	private TextureRegion[] splashFrames;
	private TextureRegion currentFrame;
	private float delay = 3f, logoDelay = 3f, elapsedTime = 0f;
	
	public SplashState() {
		splashFrames = new TextureRegion[39];
		for(int i = 0; i < 39; i++) {
			if(i < 10) {
				TextureRegion text = new TextureRegion(new Texture(Gdx.files.internal("splash/Splashscreen_0000" + i + ".png")));
				splashFrames[i] = text;
			} else if(i >= 10) {
				TextureRegion text = new TextureRegion(new Texture(Gdx.files.internal("splash/Splashscreen_000" + i + ".png")));
				splashFrames[i] = text;
			}
		}
		
		splashLogo = new Animation(0.05f, splashFrames);
		splashLogo.setPlayMode(PlayMode.NORMAL);
	}
	
	public void update() {
		if(delay > 0) {
			delay -= 0.1f;
			if(delay <= 0) {
				delay = 0;
			}
		}
		
		if(splashLogo.isAnimationFinished(elapsedTime) && logoDelay > 0) {
			logoDelay -= 0.1f;
			if(logoDelay <= 0) {
				logoDelay = 0;
			}
		}
		
		if(logoDelay == 0) {
			ScreenManager.setScreen(new TitleScreen());
		}
	}
	
	public void render(SpriteBatch sb) {
		sb.begin();
		if(delay == 0) {
			elapsedTime += Gdx.graphics.getDeltaTime();
			currentFrame = splashLogo.getKeyFrame(elapsedTime);
			sb.draw(currentFrame, MainGame.WIDTH / 2 - 125, MainGame.HEIGHT / 2 - 100, 250, 220);
		}
		sb.end();
	}

}
