package com.almoatarknad.grid;

import java.util.Random;

import com.almoatarknad.MainGame;
import com.almoatarknad.block.Block;
import com.almoatarknad.screen.ScreenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Grid {
	
	private float x, y, oldX = 0, oldY = 0, newX = 0, newY = 0, moveX = 0, moveY = 0, oldGX = 0, oldGY = 0;
	private boolean gameOver = false, hasRestarted = false, selectedNew = false;
	private int blocksBlocked = 0;
	private int currentMaxTileValue = 0;
	private int score = 0;
	private int direction = -1;
	private int mergeDirection = -1;
	private int width, height;
	private Sprite gridSprite;
	private Array<Block> blocks;
	private Array<GridBlock> gridBlocks;
	private Block startBlock;
	private Block selectedBlock;
	private Random random;
	private Sprite selectIndicator;
	private boolean excecuteMove = false, merge = false, generated = false, moved = false, newRound = true, endRound = false;
	private Array<GridBlock> freeGridBlocks;
	private boolean animatedMerge = false, animationRun = false, mergeAnimation = false;
	private float animatedMergeTimer = 10.0f;
	private float elapsedTime = 0.0f;
	
	private float prevSelectedX = 0, prevSelectedY = 0;
	
	private Sprite logga = new Sprite(new Texture(Gdx.files.internal("Logga.png")));
	private Sprite bg = new Sprite(new Texture(Gdx.files.internal("backgound1.png")));
	
	private Animation gridAnimUp, gridAnimLeft, gridAnimRight, gridAnimDown, currentAnim;
	private TextureRegion[] upFrames, toLeftFrames, toRightFrames, downFrames;
	private TextureRegion currentFrame;
	
	ShapeRenderer renderer = new ShapeRenderer();
	float loggaX = MainGame.WIDTH / 2 - 115;
	float loggaY = MainGame.HEIGHT - 200;
	
	private Sound bell;
	private Sound swish;
	private Rectangle restartButton;
	
	public Grid(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		random = new Random();
		
		float startX = (float)random.nextInt(4);
		float startY = (float)random.nextInt(4);
		
		startBlock = new Block(startX * 96 + 15, startY * 96 + 15, 96, 96, 1);
		
		gridSprite = new Sprite(new Texture(Gdx.files.internal("grid/Gridonlycolor.png")));
		gridSprite.setPosition(x, y);
		gridSprite.setSize(width, height);
		
		blocks = new Array<Block>();
		blocks.add(startBlock);
		selectedBlock = null;
		startBlock = null;
		
		gridBlocks = new Array<GridBlock>();
		freeGridBlocks = new Array<GridBlock>();
		
		loadGrid();
		loadAnimations();
		gridAnimDown = new Animation(1f/40f, downFrames);
		gridAnimUp = new Animation(1f/40f, upFrames);
		gridAnimLeft = new Animation(1f/40f, toLeftFrames);
		gridAnimRight = new Animation(1f/40f, toRightFrames);
		currentAnim = null;
		
		selectIndicator = new Sprite(new Texture(Gdx.files.internal("grid/selectindicator.png")));
		
		logga.setSize(240, 196);
		logga.setPosition(loggaX, loggaY);
		
		restartButton = new Rectangle(logga.getX(), logga.getY(), logga.getWidth(), logga.getHeight());
		
		bg.setSize(MainGame.WIDTH, MainGame.HEIGHT);
		bg.setPosition(0, 0);
		
		bell = Gdx.audio.newSound(Gdx.files.internal("bell.ogg"));
		swish = Gdx.audio.newSound(Gdx.files.internal("swish.ogg"));
	}
	
	public void update() {
		if(!gameOver) {
			newRound();
			setSelectedBlock();
			if(!endRound && !newRound) {
				checkBounds();

				moveBlock();
				if(moved) {
					updateFreeBlocks();
				}
				checkIfOccupied();
				logga.setPosition(loggaX, loggaY);
				for(Block b : blocks) {
					b.update();
					if(!b.isMovable()) {
						animatedMerge = true;
						animationRun = true;
						elapsedTime = 0;
					}

					if(animatedMerge) {
						b.animate(direction);
					} else if(!animatedMerge && !animationRun) {
						direction = -1;

					}
				}

			}

			endRound();
		}
//		restart();
		
	}
	
	public void render(SpriteBatch sb) {
		bg.draw(sb);
		elapsedTime += Gdx.graphics.getDeltaTime();
		logga.draw(sb);
		
		if(!mergeAnimation) {
			if(mergeDirection == 0) {
				currentFrame = gridAnimUp.getKeyFrame(elapsedTime);
			} else if(mergeDirection == 1) {
				currentFrame = gridAnimRight.getKeyFrame(elapsedTime);
			} else if(mergeDirection == 2) {
				currentFrame = gridAnimDown.getKeyFrame(elapsedTime);
			} else if(mergeDirection == 3) {
				currentFrame = gridAnimLeft.getKeyFrame(elapsedTime);
			}
		} else if(mergeAnimation) {
			if(mergeDirection == 0) {
				currentFrame = gridAnimUp.getKeyFrame(elapsedTime);
			} else if(mergeDirection == 1) {
				currentFrame = gridAnimRight.getKeyFrame(elapsedTime);
			} else if(mergeDirection == 2) {
				currentFrame = gridAnimDown.getKeyFrame(elapsedTime);
			} else if(mergeDirection == 3) {
				currentFrame = gridAnimLeft.getKeyFrame(elapsedTime);
			}
		}
		
		sb.draw(currentFrame, x, y, width, height);
		
		for(GridBlock g : gridBlocks) {
			g.render(sb);
		}
		
		for(Block b : blocks) {
			b.render(sb);
		}
		
	}
	
	public void loadAnimations() {
		upFrames = new TextureRegion[22];
		toLeftFrames = new TextureRegion[22];
		toRightFrames = new TextureRegion[22];
		downFrames = new TextureRegion[22];
		for(int i = 0; i < 22; i++) {
			if(i < 10) {
				Texture texture = new Texture(Gdx.files.internal("grid/animation/up/Gridonlyfrombuttom00" + i + ".png" ));
				texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
				TextureRegion sprite = new TextureRegion(texture);
				upFrames[i] = sprite;
			} else {
				Texture texture = new Texture(Gdx.files.internal("grid/animation/up/Gridonlyfrombuttom0" + i + ".png" ));
				texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
				TextureRegion sprite = new TextureRegion(texture);
				upFrames[i] = sprite;
			}
		}
		
		for(int i = 0; i < 22; i++) {
			if(i < 10) {
				Texture texture = new Texture(Gdx.files.internal("grid/animation/left/Gridonlyfromright00" + i + ".png" ));
				texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
				TextureRegion sprite = new TextureRegion(texture);
				toLeftFrames[i] = sprite;
			} else {
				Texture texture = new Texture(Gdx.files.internal("grid/animation/left/Gridonlyfromright0" + i + ".png" ));
				texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
				TextureRegion sprite = new TextureRegion(texture);
				toLeftFrames[i] = sprite;
			}
		}
		
		for(int i = 0; i < 22; i++) {
			if(i < 10) {
				Texture texture = new Texture(Gdx.files.internal("grid/animation/right/Gridonlyfromleft00" + i + ".png" ));
				texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
				TextureRegion sprite = new TextureRegion(texture);
				toRightFrames[i] = sprite;
			} else {
				Texture texture = new Texture(Gdx.files.internal("grid/animation/right/Gridonlyfromleft0" + i + ".png" ));
				texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
				TextureRegion sprite = new TextureRegion(texture);
				toRightFrames[i] = sprite;
			}
		}
		
		for(int i = 0; i < 22; i++) {
			if(i < 10) {
				Texture texture = new Texture(Gdx.files.internal("grid/animation/down/Gridonlyfromtop00" + i + ".png" ));
				texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
				TextureRegion sprite = new TextureRegion(texture);
				downFrames[i] = sprite;
			} else {
				Texture texture = new Texture(Gdx.files.internal("grid/animation/down/Gridonlyfromtop0" + i + ".png" ));
				texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
				TextureRegion sprite = new TextureRegion(texture);
				downFrames[i] = sprite;
			}
		}
		
		currentFrame = new TextureRegion(new Texture(Gdx.files.internal("grid/animation/up/Gridonlyfrombuttom000.png")));
	}
	
	public void loadGrid() {
		GridBlock gridBlock;
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				gridBlock = new GridBlock(j * 96 + 15, i * 96 + 15);
				gridBlocks.add(gridBlock);
			}
		}
		
		for(GridBlock g : gridBlocks) {
			for(Block b : blocks) {
				if(b.getX() > g.getX() - 2 && b.getY() > g.getY() - 2 && b.getX() < g.getX() + 2 && b.getY() < g.getY() + 2
						&& b.isActive() && !g.isOccupied()) {
					g.setIsOccupied(true);
					g.setValue(b.getValue());
				}
			}
		}
		
		for(GridBlock g : gridBlocks) {
			if(!g.isOccupied()) {
				freeGridBlocks.add(g);
			}
		}
	}
	
	public void setSelectedBlock() {
		if(Gdx.input.justTouched()) {
		newRound = false;
		endRound = false;
		int selectedAmount = 0;
		for(Block b : blocks) {
			if(b.isSelected())
				selectedAmount++;
		}
		
		for(GridBlock g : gridBlocks) {
			for(Block b : blocks) {
				if(b.isMovable()) {
					if(Gdx.input.justTouched() && b.isActive() && ScreenManager.getCurrentScreen().inputManager.getIntersecting(b.getHitbox())) {
						if(selectedAmount == 1 && selectedBlock != null && selectedBlock.equals(b) && !selectedNew) {
							b.setSelected(false);
							endRound = true;
							newRound = false;
						}
						
						if(selectedAmount == 0) {
							b.setSelected(true);
							selectedBlock = b;
							selectedBlock.setSelected(true);
						}
						
						if(selectedAmount == 1 && selectedBlock != null && !b.equals(selectedBlock)) {
							if(g.isOccupied() && selectedBlock.isSelected() && g.isAvailable()) {
								merge(b, selectedBlock);
								endRound = true;
								newRound = false;
							}
						}
						
					} else if(Gdx.input.justTouched() && ScreenManager.getCurrentScreen().inputManager.getIntersecting(g.getHitBox()) && !g.isOccupied() && g.isAvailable()) {
						if(!moved && b.isSelected()) {
							if(g.getX() > b.getX() && g.getY() == b.getY()) {
								direction = b.right;
								b.setMovable(false);
							} else if(g.getX() < b.getX() && g.getY() == b.getY()) {
								direction = b.left;
								b.setMovable(false);
							} else if(g.getY() > b.getY() && g.getX() == b.getX()) {
								direction = b.up;
								b.setMovable(false);
							} else if(g.getY() < b.getY() && g.getX() == b.getX()) {
								direction = b.down;
								b.setMovable(false);
							}
						}
					}
				}
			}
		}
		}
	}
	
	public void moveBlock() {
		for(Block b : blocks) {
			if(!b.isMovable() && direction != -1) {
				move(direction, b);
				mergeDirection = direction;
				mergeAnimation = false;
				endRound = true;
				newRound = false;
			}
		}
	}
	
	public void createBlock() {
		Block block = null;
		float randomX = 0;
		float randomY = 0;
		int randomValue = 0;
		if(currentMaxTileValue < 16) {
			randomValue = 1;
		} else if(currentMaxTileValue >= 16 && currentMaxTileValue < 64) {
			randomValue = random.nextInt(2) + 1;
		} else if(currentMaxTileValue >= 64 && currentMaxTileValue < 256) {
			randomValue = random.nextInt(3) + 1;
			if(randomValue == 3) {
				randomValue = 4;
			}
		} else if(currentMaxTileValue >= 256 && currentMaxTileValue < 1024) {
			randomValue = random.nextInt(4) + 1;
			if(randomValue == 4) {
				randomValue = 8;
			} else if(randomValue == 3) {
				randomValue = 4;
			}
		} else if(currentMaxTileValue >= 1024 && currentMaxTileValue < 2048) {
			randomValue = random.nextInt(5) + 1;
			if(randomValue == 5) {
				randomValue = 16;
			} else if(randomValue == 4) {
				randomValue = 8;
			} else if(randomValue == 3) {
				randomValue = 4;
			}
		} else if(currentMaxTileValue >= 2048) {
			randomValue = random.nextInt(6) + 1;
			if(randomValue == 6) {
				randomValue = 32;
			} else if(randomValue == 5) {
				randomValue = 16;
			} else if(randomValue == 4) {
				randomValue = 8;
			} else if(randomValue == 3) {
				randomValue = 4;
			}
		}
		
		if(freeGridBlocks.size > 2) {
			int randomBlock = random.nextInt(freeGridBlocks.size);
			randomX = freeGridBlocks.get(randomBlock).getX();
			randomY = freeGridBlocks.get(randomBlock).getY();
			if(randomX != oldX && randomY != oldY) {
				block = new Block(randomX, randomY, 96, 96, randomValue);
				randomX = randomX / 96 + 1;
				randomY = randomY / 96 + 1;
			} else {
				block = new Block(oldX, oldY, 96, 96, randomValue);
			}
		} else {
			block = new Block(oldX, oldY, 96, 96, randomValue);
		}

		elapsedTime = 0;
		if(block != null) {
			blocks.add(block);
		}

		block = null;
		direction = -1;
		endRound = true;
		newRound = false;
		moved = false;
	}
	
	public void merge(Block a, Block b) {
		if(b.getValue() == a.getValue()) {
			if((b.getValue() + a.getValue()) > currentMaxTileValue) {
				currentMaxTileValue = b.getValue() + a.getValue();
			}
			score += b.getValue();
			score += a.getValue();
			mergeAnimation = true;
			if(b.getX() > a.getX() && b.getY() == a.getY()) {
				mergeDirection = 3;
			} else if(b.getX() < a.getX() && b.getY() == a.getY()) {
				mergeDirection = 1;
			} else if(b.getY() > a.getY() && b.getX() == a.getX()) {
				mergeDirection = 2;
			} else if(b.getY() < a.getY() && b.getX() == a.getX()) {
				mergeDirection = 0;
			}
			elapsedTime = 0;
			Block block = new Block(a.getX(), a.getY(), 96, 96, a.getValue() + b.getValue());
			block.setMovable(false);
			blocks.add(block);
			a.setActive(false);
			b.setActive(false);
			a.setSelected(false);
			b.setSelected(false);
			block = null;
			a = null;
			b = null;
			bell.play();
		}
	}
	
	public void move(int direction, Block b, GridBlock g) {
		if(!g.isOccupied()) {
			oldX = b.getX();
			oldY = b.getY();
			b.move(direction);
			b.setSelected(false);
			moved = true;
			swish.play();
		} else if(g.isOccupied()) {
			endRound = true;
			newRound = false;
		}
	}
	
	public void move(int direction, Block b) {
		oldX = b.getX();
		oldY = b.getY();
		b.move(direction);
		b.setSelected(false);
		mergeAnimation = true;
		moved = true;
		swish.play();
	}
	
	public void checkBounds() {
		for(GridBlock g : gridBlocks) {
			for(Block b : blocks) {
				if(b.isSelected()) {
					if(g.getX() > b.getX() && g.getX() < b.getX() + 100 && 
							g.getY() == b.getY()) {
						//RIGHT OF BLOCK
						if(!g.isOccupied() && g.getValue() == 0)
							g.setAvailable(true);
						else if(g.isOccupied() && g.getValue() != b.getValue()) {
							g.setAvailable(false);
						} else if(g.isOccupied() && g.getValue() == b.getValue()) {
							g.setAvailable(true);
						}
					}
					if(g.getX() < b.getX() && g.getX() > b.getX() - 100 && 
							g.getY() == b.getY()) {
						//LEFT OF BLOCK
						if(!g.isOccupied() && g.getValue() == 0)
							g.setAvailable(true);
						else if(g.isOccupied() && g.getValue() != b.getValue()) {
							g.setAvailable(false);
						} else if(g.isOccupied() && g.getValue() == b.getValue()) {
							g.setAvailable(true);
						}
					}
					if(g.getY() > b.getY() && g.getY() < b.getY() + 100 && g.getX() == b.getX()) {
						//ABOVE BLOCK
						if(!g.isOccupied() && g.getValue() == 0)
							g.setAvailable(true);
						else if(g.isOccupied() && g.getValue() != b.getValue()) {
							g.setAvailable(false);
						} else if(g.isOccupied() && g.getValue() == b.getValue()) {
							g.setAvailable(true);
						}
					}
					if(g.getY() < b.getY() && g.getY() > b.getY() - 100 && g.getX() == b.getX()) {
						if(!g.isOccupied() && g.getValue() == 0)
							g.setAvailable(true);
						else if(g.isOccupied() && g.getValue() != b.getValue()) {
							g.setAvailable(false);
						} else if(g.isOccupied() && g.getValue() == b.getValue()) {
							g.setAvailable(true);
						}
					}
				} else if(!b.isSelected() && b.isActive()) {
					if(g.getX() > b.getX() && g.getX() < b.getX() + 100 && 
							g.getY() == b.getY()) {
						//RIGHT OF BLOCK
						if(g.isOccupied() && g.getValue() != b.getValue())
							blocksBlocked += 1;
					}
					if(g.getX() < b.getX() && g.getX() > b.getX() - 100 && 
							g.getY() == b.getY()) {
						//LEFT OF BLOCK
						if(g.isOccupied() && g.getValue() != b.getValue())
							blocksBlocked += 1;
					}
					if(g.getY() > b.getY() && g.getY() < b.getY() + 100 && g.getX() == b.getX()) {
						//ABOVE BLOCK
						if(g.isOccupied() && g.getValue() != b.getValue())
							blocksBlocked += 1;
					}
					if(g.getY() < b.getY() && g.getY() > b.getY() - 100 && g.getX() == b.getX()) {
						//BELOW BLOCK
						if(g.isOccupied() && g.getValue() != b.getValue())
							blocksBlocked += 1;
					}
				}
				
			}
		}
		if(blocksBlocked >= 48) {
			gameOver = true;
		}
		blocksBlocked = 0;
	}
	
	public void checkIfOccupied() {
		for(GridBlock g : gridBlocks) {
			for(Block b : blocks) {
				if(b.getX() > g.getX() - 2 && b.getY() > g.getY() - 2 && b.getX() < g.getX() + 2 && b.getY() < g.getY() + 2
						&& b.isActive() && !g.isOccupied()) {
					g.setIsOccupied(true);
					g.setValue(b.getValue());
				}
			}
		}
	}
		
		public void updateFreeBlocks() {
			freeGridBlocks.clear();
			for(GridBlock g : gridBlocks) {
				if(!g.isOccupied()) {
					freeGridBlocks.add(g);
				}
			}
			createBlock();
		}
	
	public void endRound() {
		if(endRound) {
			for(Block b : blocks) {
					b.setSelected(false);
					if(selectedBlock != null) {
						selectedBlock.setSelected(false);
						selectedBlock = null;
					}
				}

			for(GridBlock g : gridBlocks) {
				g.setAvailable(false);
				g.setIsOccupied(false);
				g.setValue(0);
			}

			if(selectedBlock != null) {
				selectedBlock.setSelected(false);
				selectedBlock = null;
			}
			newRound = true;
			endRound = false;
		}
	}
	
	public void newRound() {
		
		if(hasRestarted == false) {
			hasRestarted = true;
		}
		if(newRound && !endRound) {
			freeGridBlocks.clear();
			for(GridBlock g : gridBlocks) {
				for(Block b : blocks) {
					if(b.getX() > g.getX() - 2 && b.getY() > g.getY() - 2 && b.getX() < g.getX() + 2 && b.getY() < g.getY() + 2
							&& b.isActive() && !g.isOccupied()) {
						g.setIsOccupied(true);
						g.setValue(b.getValue());
					}
				}
			}
			
			for(Block b : blocks) {
				b.setMovable(true);
			}
			
			for(GridBlock g : gridBlocks) {
				if(!g.isOccupied()) {
					freeGridBlocks.add(g);
				}
			}
		}
		newRound = false;
	}
	
	public int getScore() {
		return score;
	}
	
	public void restart() {
//		if(Gdx.input.justTouched() && ScreenManager.getCurrentScreen().inputManager.getIntersecting(restartButton) && hasRestarted) {
			float startX = (float)random.nextInt(4);
			float startY = (float)random.nextInt(4);
			gameOver = false;
			startBlock = new Block(startX * 96 + 15, startY * 96 + 15, 96, 96, 1);
			gridBlocks.clear();
			currentMaxTileValue = 0;
			score = 0;
			freeGridBlocks.clear();
			blocks = null;
			blocks = new Array<Block>();
			blocks.add(startBlock);
			startBlock = null;
			loadGrid();
			endRound = false;
			newRound = true;
			hasRestarted = false;
//		}
	}
	
	public boolean isGameOver() {
		return gameOver;
	}
	
	public boolean hasRestarted() {
		return hasRestarted;
	}
	
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}
}
