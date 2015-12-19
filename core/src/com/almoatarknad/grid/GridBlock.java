package com.almoatarknad.grid;

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
	private Sprite sprite;
	private Rectangle hitBox;
	private int value;
	private float elapsedTime = 0f;
	
	private Animation indicator;
	private TextureRegion[] indicatorFrames;
	private TextureRegion currentFrame;
	
	public GridBlock(float x, float y) {
		width = 96;
		height = 96;
		this.x = x;
		this.y = y;
		indicatorFrames = new TextureRegion[10];
		loadAnimation();
		indicator = new Animation(1f/15f, indicatorFrames);
		indicator.setPlayMode(PlayMode.LOOP_PINGPONG);
		sprite = new Sprite(new Texture(Gdx.files.internal("grid/selectindicator.png")));
		sprite.setPosition(x + 6, y + 6);
		sprite.setSize(92, 92);
		hitBox = new Rectangle(x, y, width, height);
		value = 0;
	}
	
	public void update() {
		
	}
	
	public void render(SpriteBatch sb) {
		elapsedTime += Gdx.graphics.getDeltaTime();
		if(available) {
			currentFrame = indicator.getKeyFrame(elapsedTime);
			Sprite temp = new Sprite(currentFrame.getTexture());
			temp.setSize(84, 84);
			temp.setPosition(x + 10, y + 8);
			temp.setAlpha(0.5f);
			sprite = temp;
			sprite.draw(sb);
		}
	}
	
	public void loadAnimation() {
		for(int i = 0; i < 10; i++) {
			Texture texture = new Texture(Gdx.files.internal("block/animation/loading_box_sequence" + i + ".png" ));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			TextureRegion sprite = new TextureRegion(texture);
			indicatorFrames[i] = sprite;
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
	
	public Rectangle getHitBox() {
		return hitBox;
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
