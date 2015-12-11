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
	
	private Sprite sprite, spriteSelected;
	private Sprite b1, b2, b4, b8, b16, b32, b64, b128, b256, b512, b1024, b2048, b4096, b8192;
	
	public Block(float x, float y, int width, int height, int value) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.value = value;
		sprite = new Sprite(TextureManager.b1);
		sprite.setPosition(x, y);
		sprite.setSize(width - 5, height - 5);
		spriteSelected = new Sprite(new Texture(Gdx.files.internal("grid/gridblockselected.png")));
		spriteSelected.setPosition(x, y);
		spriteSelected.setSize(width - 2, height - 2);
		hitBox = new Rectangle(x, y, width, height);
		hitBox.setPosition(x, y);
		oldX = x;
		oldY = y;
		loadSprites();
	}
	
	public void update() {
		if(active) {
			sprite.setPosition(x + 4, y + 4);
			spriteSelected.setPosition(x + 4, y + 4);
			hitBox.setPosition(x, y);
			updateSprite(x + 4, y + 4);
		} else if(!active) {
			sprite = null;
			hitBox = null;
			spriteSelected = null;
		}
		
	}
	
	public void render(SpriteBatch sb) {
		if(active) {
			if(!selected)
				sprite.draw(sb);
			else if(selected) {
				sprite.draw(sb);
				spriteSelected.setAlpha(0.3f);
				spriteSelected.draw(sb);
			}
		}
	}
	
	public void updateSprite(float x, float y) {
		if(value == 1) {
			sprite = b1;
			sprite.setPosition(x + 2, y + 2);
		} else if(value == 2) {
			sprite = b2;
			sprite.setPosition(x + 2, y + 2);
		} else if(value == 4) {
			sprite = b4;
			sprite.setPosition(x + 2, y + 2);
		} else if(value == 8) {
			sprite = b8;
			sprite.setPosition(x + 2, y + 2);
		} else if(value == 16) {
			sprite = b16;
			sprite.setPosition(x + 2, y + 2);
		} else if(value == 32) {
			sprite = b32;
			sprite.setPosition(x + 2, y + 2);
		} else if(value == 64) {
			sprite = b64;
			sprite.setPosition(x + 2, y + 2);
		} else if(value == 128) {
			sprite = b128;
			sprite.setPosition(x + 2, y + 2);
		} else if(value == 256) {
			sprite = b256;
			sprite.setPosition(x + 2, y + 2);
		} else if(value == 512) {
			sprite = b512;
			sprite.setPosition(x + 2, y + 2);
		} else if(value == 1024) {
			sprite = b1024;
			sprite.setPosition(x + 2, y + 2);
		} else if(value == 2048) {
			sprite = b2048;
			sprite.setPosition(x + 2, y + 2);
		} else if(value == 4096) {
			sprite = b4096;
			sprite.setPosition(x + 2, y + 2);
		} else if(value == 8192) {
			sprite = b8192;
			sprite.setPosition(x + 2, y + 2);
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
	
	public void loadSprites() {
		b1 = new Sprite(TextureManager.b1);
		b1.setSize(width - 5, height - 5);
		b1.setPosition(x + 2, y + 2);
		b2 = new Sprite(TextureManager.b2);
		b2.setSize(width - 5, height - 5);
		b2.setPosition(x + 2, y + 2);
		b4 = new Sprite(TextureManager.b4);
		b4.setSize(width - 5, height - 5);
		b4.setPosition(x + 2, y + 2);
		b8 = new Sprite(TextureManager.b8);
		b8.setSize(width - 5, height - 5);
		b8.setPosition(x + 2, y + 2);
		b16 = new Sprite(TextureManager.b16);
		b16.setSize(width - 5, height - 5);
		b16.setPosition(x + 2, y + 2);
		b32 = new Sprite(TextureManager.b32);
		b32.setSize(width - 5, height - 5);
		b32.setPosition(x + 2, y + 2);
		b64 = new Sprite(TextureManager.b64);
		b64.setSize(width - 5, height - 5);
		b64.setPosition(x + 2, y + 2);
		b128 = new Sprite(TextureManager.b128);
		b128.setSize(width - 5, height - 5);
		b128.setPosition(x + 2, y + 2);
		b256 = new Sprite(TextureManager.b256);
		b256.setSize(width - 5, height - 5);
		b256.setPosition(x + 2, y + 2);
		b512 = new Sprite(TextureManager.b512);
		b512.setSize(width - 5, height - 5);
		b512.setPosition(x + 2, y + 2);
		b1024 = new Sprite(TextureManager.b1024);
		b1024.setSize(width - 5, height - 5);
		b1024.setPosition(x + 2, y + 2);
		b2048 = new Sprite(TextureManager.b2048);
		b2048.setSize(width - 5, height - 5);
		b2048.setPosition(x + 2, y + 2);
		b4096 = new Sprite(TextureManager.b4096);
		b4096.setSize(width - 5, height - 5);
		b4096.setPosition(x + 2, y + 2);
		b8192 = new Sprite(TextureManager.b8192);
		b8192.setSize(width - 5, height - 5);
		b8192.setPosition(x + 2, y + 2);
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
