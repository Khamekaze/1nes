package com.almoatarknad.grid;

import java.util.Random;

import com.almoatarknad.MainGame;
import com.almoatarknad.block.Block;
import com.almoatarknad.input.MuteButton;
import com.almoatarknad.input.PauseButton;
import com.almoatarknad.screen.ScreenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Grid {
	
	private float x, y, oldX = 0, oldY = 0;
	private boolean gameOver = false, hasRestarted = false, muted = false;
	private int blocksBlocked = 0;
	private int currentMaxTileValue = 0;
	private int score = 0;
	private int direction = -1;
	private int mergeDirection = -1;
	private int width, height;
	private Array<Block> blocks;
	private Array<GridBlock> gridBlocks;
	private Block startBlock;
	private Block selectedBlock;
	private Random random;
	private boolean moved = false, newRound = true, endRound = false, paused = false;
	private Array<GridBlock> freeGridBlocks;
	private boolean animatedMerge = false, animationRun = false, mergeAnimation = false;
	private float elapsedTime = 0.0f;
	private int stageOfLoop = 0;
	private boolean checkBoundsBool = false, touched = false;
	
	private Sprite logga;
	private Sprite bg;
	private Sprite gridBG;
	
	private MuteButton muteButton;
	
	private Animation gridAnimUp, gridAnimLeft, gridAnimRight, gridAnimDown;
	private TextureRegion[] upFrames, toLeftFrames, toRightFrames, downFrames;
	private TextureRegion currentFrame;
	
	float loggaX = MainGame.WIDTH / 2 - 80;
	float loggaY = MainGame.HEIGHT - 200;
	
	private Sound bell;
	private Sound swish;
	private PauseButton pauseButton;
	
	public Grid(float x, float y, int width, int height, Preferences prefs) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		random = new Random();
		
		float startX = (float)random.nextInt(4);
		float startY = (float)random.nextInt(4);
		blocks = new Array<Block>();
		
		initSavedState(prefs);
		
		if(blocks.size == 0) {
			startBlock = new Block(startX * 96 + 15, startY * 96 + 15, 96, 96, 1);
			blocks.add(startBlock);
		}
		
		bg = new Sprite(new Texture(Gdx.files.internal("backgrounds/mountains.png")));
		gridBG = new Sprite(new Texture(Gdx.files.internal("gui/bgfade.png")));
		
		logga = new Sprite(new Texture(Gdx.files.internal("logga_med-ring-runt-final.png")));
		
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
		
		logga.setSize(160, 160);
		logga.setPosition(loggaX, loggaY);
		logga.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		bg.setSize(MainGame.WIDTH, MainGame.HEIGHT);
		bg.setPosition(0, 0);
		
		gridBG.setSize(width - 10, height - 10);
		gridBG.setPosition(x + 5, y + 5);
		gridBG.setAlpha(0.6f);
		
		bell = Gdx.audio.newSound(Gdx.files.internal("bell.ogg"));
		swish = Gdx.audio.newSound(Gdx.files.internal("swish.ogg"));
		
		pauseButton = new PauseButton(MainGame.WIDTH / 2 - 50, y + 390);
		muteButton = new MuteButton(10, MainGame.HEIGHT - 70);
		score = prefs.getInteger("score", 0);
	}
	
	public void update() {
		if(!Gdx.input.isTouched()) {
			touched = false;
		}
		if(!gameOver) {
			if(Gdx.input.justTouched() && ScreenManager.getCurrentScreen().inputManager.getMouseHitbox().overlaps(pauseButton.getHitbox())) {
				if(!paused)
					paused = true;
			}
			if(!paused) {
				if(stageOfLoop == 0) {
					newRound();
				}

				if(stageOfLoop == 1) {
					setSelectedBlock();
					if(checkBoundsBool) {
						checkBounds();
					}

				}


				if(stageOfLoop == 3) {
					moveBlock();
				}

				if(stageOfLoop == 4) {
					updateFreeBlocks();
				}

				if(stageOfLoop == 5) {
					createBlock();
				}
				if(stageOfLoop == 6) {
					checkIfOccupied();
				}

				if(stageOfLoop == 7) {
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
					stageOfLoop = 8;
				}
			}

			if(stageOfLoop == 8) {
				checkBounds();
				endRound();
			}
		}
		
		if(Gdx.input.justTouched() && ScreenManager.getCurrentScreen().inputManager.getIntersecting(muteButton.getHitBox())) {
			if(muted) {
				muted = false;
				muteButton.setMutedBool(muted);
			} else if(!muted) {
				muted = true;
				muteButton.setMutedBool(muted);
			}
		}
	}
	
	public void render(SpriteBatch sb) {
		bg.draw(sb);
		muteButton.render(sb);
		elapsedTime += Gdx.graphics.getDeltaTime();
		logga.draw(sb);
		gridBG.draw(sb);
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
		
		for(GridBlock g : gridBlocks) {
			g.render(sb);
		}
		
		for(Block b : blocks) {
			sb.draw(ScreenManager.getCurrentScreen().textureManager.getBlockTexture(b.getValue()), b.getX() + 5, b.getY() + 5, b.getWidth() - 5, b.getHeight() - 5);
			
			if(b.isSelected()) {
				b.render(sb);
			}
		} 
		
		sb.draw(currentFrame, x, y, width, height);
		
		pauseButton.render(sb);
		
	}
	
	public void loadAnimations() {
		upFrames = new TextureRegion[22];
		toLeftFrames = new TextureRegion[22];
		toRightFrames = new TextureRegion[22];
		downFrames = new TextureRegion[22];
		for(int i = 0; i < 22; i++) {
				Texture texture = new Texture(Gdx.files.internal("grid/animation/up/Newgridfrombottom" + i + ".png" ));
				texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
				TextureRegion sprite = new TextureRegion(texture);
				upFrames[i] = sprite;
		}
		
		for(int i = 0; i < 22; i++) {
				Texture texture = new Texture(Gdx.files.internal("grid/animation/left/Newgridfromright" + i + ".png" ));
				texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
				TextureRegion sprite = new TextureRegion(texture);
				toRightFrames[i] = sprite;
		}
		
		for(int i = 0; i < 22; i++) {
				Texture texture = new Texture(Gdx.files.internal("grid/animation/right/NewgridFromleft" + i + ".png" ));
				texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
				TextureRegion sprite = new TextureRegion(texture);
				toLeftFrames[i] = sprite;
		}
		
		for(int i = 0; i < 22; i++) {
				Texture texture = new Texture(Gdx.files.internal("grid/animation/down/Newgridfromtrop" + i + ".png" ));
				texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
				TextureRegion sprite = new TextureRegion(texture);
				downFrames[i] = sprite;
		}
		
		currentFrame = new TextureRegion(new Texture(Gdx.files.internal("grid/animation/up/Newgridfrombottom0.png")));
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
				if(b.getX() == g.getX() && b.getY() == g.getY()
						&& b.isActive() && !g.isOccupied()) {
					g.setIsOccupied(true);
					g.setValue(b.getValue());
				} else {
					freeGridBlocks.add(g);
				}
			}
		}
	}
	
	public void setSelectedBlock() {
		for(GridBlock g : gridBlocks) {
			for(Block b : blocks) {
				if(b.isMovable()) {
					if(Gdx.input.isTouched() && b.isActive() && !touched) {
						if(ScreenManager.getCurrentScreen().inputManager.getInputPos().x >= b.getX() && 
								ScreenManager.getCurrentScreen().inputManager.getInputPos().x <= b.getX() + 96 &&
								ScreenManager.getCurrentScreen().inputManager.getInputPos().y >= b.getY() && 
								ScreenManager.getCurrentScreen().inputManager.getInputPos().y <= b.getY() + 96)
						if(!b.isSelected() && selectedBlock == null) {
							b.setSelected(true);
							selectedBlock = b;
							selectedBlock.setSelected(true);
							checkBoundsBool = true;
							touched = true;
						}

					} else if(!Gdx.input.isTouched() && b.isSelected()) {
						b.setSelected(false);
						endRound = true;
						newRound = false;
						stageOfLoop = 7;
						checkBoundsBool = false;
						touched = false;
					} else if(selectedBlock != null && !g.isOccupied() && g.isAvailable()) {
						if(ScreenManager.getCurrentScreen().inputManager.getInputPos().x >= g.getX() &&
								ScreenManager.getCurrentScreen().inputManager.getInputPos().x <= g.getX() + 96 &&
								ScreenManager.getCurrentScreen().inputManager.getInputPos().y >= g.getY() &&
								ScreenManager.getCurrentScreen().inputManager.getInputPos().y <= g.getY() + 96) {
							if(!moved && b.isSelected()) {
								if(g.getX() > b.getX() && g.getY() == b.getY()) {
									direction = b.right;
									b.setMovable(false);
									stageOfLoop = 3;
								} else if(g.getX() < b.getX() && g.getY() == b.getY()) {
									direction = b.left;
									b.setMovable(false);
									stageOfLoop = 3;
								} else if(g.getY() > b.getY() && g.getX() == b.getX()) {
									direction = b.up;
									b.setMovable(false);
									stageOfLoop = 3;
								} else if(g.getY() < b.getY() && g.getX() == b.getX()) {
									direction = b.down;
									b.setMovable(false);
									stageOfLoop = 3;
								}
							}

						}
					} else if(selectedBlock != null && ScreenManager.getCurrentScreen().inputManager.getInputPos().x > selectedBlock.getX() ||
							selectedBlock != null && ScreenManager.getCurrentScreen().inputManager.getInputPos().x < selectedBlock.getX() ||
									selectedBlock != null && ScreenManager.getCurrentScreen().inputManager.getInputPos().y > selectedBlock.getY() ||
											selectedBlock != null && ScreenManager.getCurrentScreen().inputManager.getInputPos().y < selectedBlock.getY()) {
						//RIGHT
						if(ScreenManager.getCurrentScreen().inputManager.getInputPos().x > selectedBlock.getX() + selectedBlock.getWidth() &&
								ScreenManager.getCurrentScreen().inputManager.getInputPos().y >= selectedBlock.getY() &&
								ScreenManager.getCurrentScreen().inputManager.getInputPos().y <= selectedBlock.getY() + selectedBlock.getHeight()) {
							if(b.getX() == selectedBlock.getX() + selectedBlock.getWidth() && b.getY() == selectedBlock.getY()) {
								if(b.getX() == g.getX() && b.getY() == g.getY() && g.getValue() == selectedBlock.getValue()) {
									merge(b, selectedBlock);
									stageOfLoop = 3;
								}
								
							}
						//LEFT	
						} else if(ScreenManager.getCurrentScreen().inputManager.getInputPos().x < selectedBlock.getX() &&
								ScreenManager.getCurrentScreen().inputManager.getInputPos().y >= selectedBlock.getY() &&
								ScreenManager.getCurrentScreen().inputManager.getInputPos().y <= selectedBlock.getY() + selectedBlock.getHeight()) {
							if(b.getX() == selectedBlock.getX() - selectedBlock.getWidth() && b.getY() == selectedBlock.getY()) {
								if(b.getX() == g.getX() && b.getY() == g.getY() && g.getValue() == selectedBlock.getValue()) {
									merge(b, selectedBlock);
									stageOfLoop = 3;
								}
							}
						//UP	
						} else if(ScreenManager.getCurrentScreen().inputManager.getInputPos().y > selectedBlock.getY() + selectedBlock.getHeight() &&
								ScreenManager.getCurrentScreen().inputManager.getInputPos().x >= selectedBlock.getX() &&
								ScreenManager.getCurrentScreen().inputManager.getInputPos().x <= selectedBlock.getX() + selectedBlock.getWidth()) {
							if(b.getY() == selectedBlock.getY() + selectedBlock.getHeight() && b.getX() == selectedBlock.getX()) {
								if(b.getX() == g.getX() && b.getY() == g.getY() && g.getValue() == selectedBlock.getValue()) {
									merge(b, selectedBlock);
									stageOfLoop = 3;
								}
							}
						//DOWN	
						} else if(ScreenManager.getCurrentScreen().inputManager.getInputPos().y < selectedBlock.getY()&&
								ScreenManager.getCurrentScreen().inputManager.getInputPos().x >= selectedBlock.getX() &&
								ScreenManager.getCurrentScreen().inputManager.getInputPos().x <= selectedBlock.getX() + selectedBlock.getWidth()) {
							if(b.getY() == selectedBlock.getY() - selectedBlock.getHeight() && b.getX() == selectedBlock.getX()) {
								if(b.getX() == g.getX() && b.getY() == g.getY() && g.getValue() == selectedBlock.getValue()) {
									merge(b, selectedBlock);
									stageOfLoop = 3;
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
			}
		}
		stageOfLoop = 4;
	}
	
	public void createBlock() {
		if(moved) {
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
			moved = false;
		}
		stageOfLoop = 6;
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
			if(!muted)
				bell.play();
		}
	}
	
	public void move(int direction, Block b) {
		oldX = b.getX();
		oldY = b.getY();
		b.move(direction);
		b.setSelected(false);
		mergeAnimation = true;
		moved = true;
		if(!muted)
			swish.play();
	}
	
	public void checkBounds() {
		for(GridBlock g : gridBlocks) {
			for(Block b : blocks) {
				if(b.isSelected()) {
					if(g.getX() > b.getX() && g.getX() <= b.getX() + 96 && 
							g.getY() == b.getY()) {
						//RIGHT OF BLOCK
						if(!g.isOccupied() && g.getValue() == 0)
							g.setAvailable(true);
						else if(g.isOccupied() && g.getValue() != b.getValue()) {
							g.setAvailable(false);
						} else if(g.isOccupied() && g.getValue() == b.getValue()) {
							g.setAvailable(true);
						}
					} else if(g.getX() < b.getX() && g.getX() >= b.getX() - 96 && 
							g.getY() == b.getY()) {
						//LEFT OF BLOCK
						if(!g.isOccupied() && g.getValue() == 0)
							g.setAvailable(true);
						else if(g.isOccupied() && g.getValue() != b.getValue()) {
							g.setAvailable(false);
						} else if(g.isOccupied() && g.getValue() == b.getValue()) {
							g.setAvailable(true);
						}
					} else if(g.getY() > b.getY() && g.getY() <= b.getY() + 96 &&
							g.getX() == b.getX()) {
						//ABOVE BLOCK
						if(!g.isOccupied() && g.getValue() == 0)
							g.setAvailable(true);
						else if(g.isOccupied() && g.getValue() != b.getValue()) {
							g.setAvailable(false);
						} else if(g.isOccupied() && g.getValue() == b.getValue()) {
							g.setAvailable(true);
						}
					} else if(g.getY() < b.getY() && g.getY() >= b.getY() - 96 &&
							g.getX() == b.getX()) {
						if(!g.isOccupied() && g.getValue() == 0)
							g.setAvailable(true);
						else if(g.isOccupied() && g.getValue() != b.getValue()) {
							g.setAvailable(false);
						} else if(g.isOccupied() && g.getValue() == b.getValue()) {
							g.setAvailable(true);
						}
					} else if(g.getX() != b.getX() && g.getY() != g.getY()) {
						g.setAvailable(false);
					}
				} else if(!b.isSelected() && b.isActive()) {
					if(g.getX() > b.getX() && g.getX() <= b.getX() + 96 && 
							g.getY() == b.getY()) {
						//RIGHT OF BLOCK
						if(g.isOccupied() && g.getValue() != b.getValue())
							blocksBlocked += 1;
					}
					if(g.getX() < b.getX() && g.getX() >= b.getX() - 96 && 
							g.getY() == b.getY()) {
						//LEFT OF BLOCK
						if(g.isOccupied() && g.getValue() != b.getValue())
							blocksBlocked += 1;
					}
					if(g.getY() > b.getY() && g.getY() <= b.getY() + 96 &&
							g.getX() == b.getX()) {
						//ABOVE BLOCK
						if(g.isOccupied() && g.getValue() != b.getValue())
							blocksBlocked += 1;
					}
					if(g.getY() < b.getY() && g.getY() >= b.getY() - 96 &&
							g.getX() == b.getX()) {
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
		checkBoundsBool = false;
	}
	
	public void checkIfOccupied() {
		for(GridBlock g : gridBlocks) {
			for(Block b : blocks) {
				if(b.getX() == g.getX() && b.getY() == g.getY()) {
					g.setIsOccupied(true);
					g.setValue(b.getValue());
				}
			}
		}
		stageOfLoop = 7;
	}
		
	public void updateFreeBlocks() {
		freeGridBlocks.clear();
		for(GridBlock g : gridBlocks) {
			if(!g.isOccupied()) {
				freeGridBlocks.add(g);
			}
		}
		
		stageOfLoop = 5;
	}
	
	public void endRound() {
//		if(endRound) {
		
			for(Block b : blocks) {
					b.setSelected(false);
					b.update();
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
			cleanBlockArray();
			newRound = true;
			endRound = false;
			
//		}
		stageOfLoop = 0;
	}
	
	public void newRound() {
		if(hasRestarted == false) {
			hasRestarted = true;
		}
		if(newRound && !endRound) {
			freeGridBlocks.clear();
			
			checkIfOccupied();
			
			for(Block b : blocks) {
				b.setMovable(true);
				b.update();
			}
			
			for(GridBlock g : gridBlocks) {
				if(!g.isOccupied()) {
					freeGridBlocks.add(g);
				}
			}
			
		}
		newRound = false;
		stageOfLoop = 1;
	}
	
	public int getScore() {
		return score;
	}
	
	public void restart() {
		if(hasRestarted) {
			float startX = (float)random.nextInt(4);
			float startY = (float)random.nextInt(4);
			gameOver = false;
			startBlock = new Block(startX * 96 + 15, startY * 96 + 15, 96, 96, 1);
			currentMaxTileValue = 0;
			score = 0;
			freeGridBlocks.clear();
			blocks.clear();
			blocks.add(startBlock);
			for(GridBlock g : gridBlocks) {
				if(startBlock.getX() == g.getX() && startBlock.getY() == g.getY()) {
					g.setIsOccupied(true);
					g.setValue(startBlock.getValue());
				} else {
					g.setIsOccupied(false);
					g.setValue(0);
				}
			}
			startBlock = null;
			endRound = false;
			newRound = true;
			hasRestarted = false;
			stageOfLoop = 0;
		}
	}
	
	public void cleanBlockArray() {
		Array<Block> temp = new Array<Block>();
		for(Block b : blocks) {
			if(b.isActive()) {
				temp.add(b);
			}
		}
		blocks.clear();
		for(Block b : temp) {
			blocks.add(b);
		}
	}
	
	public void initSavedState(Preferences prefs) {
		Block b;
		currentMaxTileValue = 0;
		for(int i = 0; i < 32; i++) {
			if(prefs.getFloat("x" + i) != 0) {
				float x = prefs.getFloat("x" + i);
				float y = prefs.getFloat("y" + i);
				int val = (int) prefs.getFloat("val" + i);
				b = new Block(x, y, 96, 96, val);
				if(val > currentMaxTileValue)
					currentMaxTileValue = val;
				blocks.add(b);
				b = null;
			}
		}
	}
	
	public Array<Block> getBlocks() {
		return blocks;
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
	
	public boolean isPaused() {
		return paused;
	}
	
	public void setPaused(boolean paused) {
		this.paused = paused;
	}
}
