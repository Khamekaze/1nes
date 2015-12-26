package com.almoatarknad.texture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureManager {
	
	public static Texture b1 = new Texture(Gdx.files.internal("block/1.png"));
	public static Texture b2 = new Texture(Gdx.files.internal("block/2.png"));
	public static Texture b4 = new Texture(Gdx.files.internal("block/4.png"));
	public static Texture b8 = new Texture(Gdx.files.internal("block/8.png"));
	public static Texture b16 = new Texture(Gdx.files.internal("block/16.png"));
	public static Texture b32 = new Texture(Gdx.files.internal("block/32.png"));
	public static Texture b64 = new Texture(Gdx.files.internal("block/64.png"));
	public static Texture b128 = new Texture(Gdx.files.internal("block/128.png"));
	public static Texture b256 = new Texture(Gdx.files.internal("block/256.png"));
	public static Texture b512 = new Texture(Gdx.files.internal("block/512.png"));
	public static Texture b1024 = new Texture(Gdx.files.internal("block/1024.png"));
	public static Texture b2048 = new Texture(Gdx.files.internal("block/2048.png"));
	public static Texture b4096 = new Texture(Gdx.files.internal("block/4096.png"));
	public static Texture b8192 = new Texture(Gdx.files.internal("block/8192.png"));
	
	public static Sprite logga = new Sprite(new Texture("logga_med-ring-runt-final.png"));
	
	public static Animation availableBlock = new Animation(0.05f, new TextureRegion[] {
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

}
