package com.almoatarknad.texture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureManager {
	
	private Texture current;
	
	private Sprite selectedIndicator = new Sprite(new Texture(Gdx.files.internal("grid/selectindicator.png")));
	
	public Texture b1 = new Texture(Gdx.files.internal("block/1.png"));
	public Texture b2 = new Texture(Gdx.files.internal("block/2.png"));
	public Texture b4 = new Texture(Gdx.files.internal("block/4.png"));
	public Texture b8 = new Texture(Gdx.files.internal("block/8.png"));
	public Texture b16 = new Texture(Gdx.files.internal("block/16.png"));
	public Texture b32 = new Texture(Gdx.files.internal("block/32.png"));
	public Texture b64 = new Texture(Gdx.files.internal("block/64.png"));
	public Texture b128 = new Texture(Gdx.files.internal("block/128.png"));
	public Texture b256 = new Texture(Gdx.files.internal("block/256.png"));
	public Texture b512 = new Texture(Gdx.files.internal("block/512.png"));
	public Texture b1024 = new Texture(Gdx.files.internal("block/1024.png"));
	public Texture b2048 = new Texture(Gdx.files.internal("block/2048.png"));
	public Texture b4096 = new Texture(Gdx.files.internal("block/4096.png"));
	public Texture b8192 = new Texture(Gdx.files.internal("block/8192.png"));
	
	public Sprite logga = new Sprite(new Texture("logga_med-ring-runt-final.png"));
	
	public Animation availableBlock = new Animation(0.05f, new TextureRegion[] {
			new TextureRegion(new Texture(Gdx.files.internal("block/animation/loading_box_sequence0.png" ))),
			new TextureRegion(new Texture(Gdx.files.internal("block/animation/loading_box_sequence1.png" ))),
			new TextureRegion(new Texture(Gdx.files.internal("block/animation/loading_box_sequence2.png" ))),
			new TextureRegion(new Texture(Gdx.files.internal("block/animation/loading_box_sequence3.png" ))),
			new TextureRegion(new Texture(Gdx.files.internal("block/animation/loading_box_sequence4.png" ))),
			new TextureRegion(new Texture(Gdx.files.internal("block/animation/loading_box_sequence5.png" ))),
			new TextureRegion(new Texture(Gdx.files.internal("block/animation/loading_box_sequence6.png" ))),
			new TextureRegion(new Texture(Gdx.files.internal("block/animation/loading_box_sequence7.png" ))),
			new TextureRegion(new Texture(Gdx.files.internal("block/animation/loading_box_sequence8.png" ))),
			new TextureRegion(new Texture(Gdx.files.internal("block/animation/loading_box_sequence9.png" ))),
	});
	
	public TextureManager() {
		current = b1;
	}
	
	public Sprite getSelectedIndicator() {
		return selectedIndicator;
	}
	
	public Sprite getLogga() {
		return logga;
	}
	
	public Animation getAvailableAnim() {
		return availableBlock;
	}
	
	public Texture getBlockTexture(int val) {
		if(val == 1) {
			current = b1;
		} else if(val == 2) {
			current = b2;
		} else if(val == 4) {
			current = b4;
		} else if(val == 8) {
			current = b8;
		} else if(val == 16) {
			current = b16;
		} else if(val == 32) {
			current = b32;
		} else if(val == 64) {
			current = b64;
		} else if(val == 128) {
			current = b128;
		} else if(val == 256) {
			current = b256;
		} else if(val == 512) {
			current = b512;
		} else if(val == 1024) {
			current = b1024;
		} else if(val == 2048) {
			current = b2048;
		} else if(val == 4096) {
			current = b4096;
		} else if(val == 8192) {
			current = b8192;
		}
		
		return current;
	}
	
}
