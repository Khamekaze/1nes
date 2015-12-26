package com.almoatarknad.block;

import com.almoatarknad.texture.TextureManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Block {
	
	public static int up = 0, right = 1, down = 2, left = 3;
	
	private float x, y, oldX, oldY;
	private int width, height, value;
	private boolean selected = false, hasMoved = false;
	private Rectangle hitBox;
	private boolean active = true, movable = true;
	private float animationTimer = 2.0f;
	
	private Sprite spriteSelected;
	
	public Block(float x, float y, int width, int height, int value) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.value = value;
		spriteSelected = new Sprite(new Texture(Gdx.files.internal("grid/selectindicator.png")));
		spriteSelected.setPosition(x, y);
		spriteSelected.setSize(width - 2, height - 2);
		hitBox = new Rectangle(x, y, width, height);
		hitBox.setPosition(x, y);
		oldX = x;
		oldY = y;
	}
	
	public void update() {
		if(active) {
			spriteSelected.setPosition(x + 5, y + 5);
			hitBox.setPosition(x, y);
		} else if(!active) {
			hitBox = null;
			spriteSelected = null;
		}
		
	}
	
	public void render(SpriteBatch sb) {
		if(active) {
			if(selected) {
				spriteSelected.setAlpha(0.5f);
				spriteSelected.draw(sb);
			}
		}
	}
	
	public void animate(int direction) {
		if(!movable) {
			if(direction == right) {
				x += animationTimer;
				if(x > oldX + 3) {
					x = oldX;
					movable = true;
				}
			} else if(direction == left) {
				x -= animationTimer;
				if(x < oldX - 3) {
					x = oldX;
					movable = true;
				}
			} else if(direction == up) {
				y += animationTimer;
				if(y > oldY + 3) {
					y = oldY;
					movable = true;
				}
			} else if(direction == down) {
				y -= animationTimer;
				if(y < oldY - 3) {
					y = oldY;
					movable = true;
				}
			}
		}
	}
	
	public void move(int direction) {
			if(direction == up) {
				y += height;
				oldX = x;
				oldY = y;
			} else if(direction == right) {
				x += width;
				oldX = x;
				oldY = y;
			} else if(direction == down) {
				y -= height;
				oldX = x;
				oldY = y;
			} else if(direction == left) {
				x -= width;
				oldX = x;
				oldY = y;
			} else {
				oldX = x;
				oldY = y;
			}
		selected = false;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public Rectangle getHitbox() {
		return hitBox;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setMovable(boolean movable) {
		this.movable = movable;
	}
	
	public boolean isMovable() {
		return movable;
	}
	
	public Vector2 getPosition() {
		return new Vector2(x, y);
	}

}
