package com.almoatarknad.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class TitleButton {
	protected Sprite sprite;
	private float x, y;
	private int width, height, type;
	private Rectangle hitBox;
	
	public TitleButton(float x, float y, int width, int height, int type) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.type = type;
		if(type == 0) {
			sprite = new Sprite(new Texture(Gdx.files.internal("title/play_knapp.png")));
		} else if(type == 1) {
			sprite = new Sprite(new Texture(Gdx.files.internal("title/howto_knapp.png")));
		}
		
		sprite.setSize(width, height);
		sprite.setPosition(x, y);
		sprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.MipMapLinearLinear);
		hitBox = new Rectangle(x, y, width, height);
	}
	
	public void update() {
		hitBox.setPosition(x, y);
	}
	
	public void render(SpriteBatch sb) {
		sprite.draw(sb);
	}
	
	public Rectangle getHitBox() {
		return hitBox;
	}

}
