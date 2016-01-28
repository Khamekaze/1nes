package com.almoatarknad.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class MuteButton {
	
	private float x, y, width, height;
	private Sprite muted, unmuted;
	private Rectangle hitBox;
	private boolean mutedBool = false;
	
	public MuteButton(float x, float y) {
		this.x = x;
		this.y = y;
		width = 60;
		height = width;
		muted = new Sprite(new Texture(Gdx.files.internal("gui/muted.png")));
		
		muted.setSize(width, height);
		muted.setPosition(x, y);
		unmuted = new Sprite(new Texture(Gdx.files.internal("gui/unmuted.png")));
		
		unmuted.setSize(width, height);
		unmuted.setPosition(x, y);
		
		hitBox = new Rectangle(x, y, width, height);
	}
	
	public void update() {
		
	}
	
	public void render(SpriteBatch sb) {
		if(!mutedBool) {
			unmuted.draw(sb);
		} else if(mutedBool) {
			muted.draw(sb);
		}
	}
	
	public void setMutedBool(boolean muted) {
		mutedBool = muted;
	}
	
	public Rectangle getHitBox() {
		return hitBox;
	}

}
