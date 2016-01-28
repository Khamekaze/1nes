package com.almoatarknad.grid;

import com.almoatarknad.screen.ScreenManager;
import com.almoatarknad.texture.TextureManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class GridBlock {
	
	private int width, height;
	private float x, y;
	private boolean occupied = false, available = false;
	private int value;
	private float elapsedTime = 0f;
	private TextureRegion currentFrame;
	
	public GridBlock(float x, float y) {
		width = 96;
		height = 96;
		this.x = x;
		this.y = y;
		value = 0;
	}
	
	public void update() {
		
	}
	
	public void render(SpriteBatch sb) {
		if(available && !occupied) {
			elapsedTime += Gdx.graphics.getDeltaTime();
			ScreenManager.getCurrentScreen().textureManager.getAvailableAnim().setPlayMode(PlayMode.LOOP_PINGPONG);
			currentFrame = ScreenManager.getCurrentScreen().textureManager.getAvailableAnim().getKeyFrame(elapsedTime);
			sb.draw(currentFrame, x + 10, y + 8, 84, 84);
		} else {
			elapsedTime = 0;
		}
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public boolean isOccupied() {
		return occupied;
	}
	
	public void setIsOccupied(boolean occupied) {
		this.occupied = occupied;
	}
	
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
	public boolean isAvailable() {
		return available;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public Vector2 getPosition() {
		return new Vector2(x, y);
	}
}
