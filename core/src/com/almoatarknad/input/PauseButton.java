package com.almoatarknad.input;

import com.almoatarknad.screen.ScreenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class PauseButton {
	
	private float x, y;
	private float height, width;
	private Sprite sprite;
	
	private Rectangle hitBox;
	
	public PauseButton(float x, float y) {
		sprite = new Sprite(new Texture(Gdx.files.internal("gui/pausesprite.png")));
		this.x = x;
		this.y = y;
		this.width = sprite.getWidth() / 2;
		this.height = sprite.getHeight() / 2;
		hitBox = new Rectangle(x, y, width, height);
		sprite.setSize(width, height);
		sprite.setPosition(x, y);
	}
	
	public void render(SpriteBatch sb) {
		sprite.setAlpha(0.7f);
		sprite.draw(sb);
	}
	
	public Rectangle getHitbox() {
		return hitBox;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}

}
