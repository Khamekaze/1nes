package com.almoatarknad.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class UnpauseButton {
	
	private float x, y;
	private float height, width;
	private Sprite sprite;
	
	private Rectangle hitBox;
	
	public UnpauseButton(float x, float y) {
		sprite = new Sprite(new Texture(Gdx.files.internal("gui/unpause.png")));
		this.x = x;
		this.y = y;
		this.width = sprite.getWidth() / 2;
		this.height = sprite.getHeight() / 2;
		hitBox = new Rectangle(x, y, width, height);
		sprite.setSize(width, height);
		sprite.setPosition(x, y);
	}
	
	public void render(SpriteBatch sb) {
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
	
	public void setPosition(float x, float y) {
		this.x = x - (width / 2);
		this.y = y - (height / 2);
		sprite.setPosition(x - (width / 2), y - (height / 2));
		hitBox.setPosition(x - (width / 2), y - (height / 2));
	}



}
