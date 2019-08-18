package com.pacman;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.badlogic.gdx.utils.Align;


import static com.pacman.Level.*;
import static com.pacman.AssetsUtils.*;

/**
 * Created by Joel on 4/14/17.
 */
public class GameScreen implements Screen
{
	private Game game;
	private ShapeRenderer shapes;
	private SpriteBatch batch;
	private boolean won;

	private int[] entityDirection;

	private Color dotColor;
	private Color gateColor;

	public GameScreen(Game game)
	{
		this.game = game;
	}

	/**
	 * Called when this screen becomes the current screen for a {@link Game}.
	 */
	@Override
	public void show()
	{
		AssetsUtils.initSetup();
		Level.initVals();
		Level.nextLevel();

		shapes = new ShapeRenderer();
		batch = new SpriteBatch();

		dotColor = new Color(1, 0.71f, 0.60f, 1);
		gateColor = new Color(1, 0.72f, 0.86f, 1);

		entityDirection = new int[5];

		gameState = INTRO;
		intro.play();
	}

	/**
	 * Called when the screen should render itself.
	 *
	 * @param delta The time in seconds since the last render.
	 */
	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		elapsedTime += delta;
		bigDotTimer += delta;
		chaseTimer += delta;
		deathTimer += delta;
		itemTimer += delta;

		shapes.setProjectionMatrix(viewport.getCamera().combined);

		shapes.begin(ShapeRenderer.ShapeType.Filled);

		shapes.setColor(Color.BLACK);
		shapes.rect(0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());

		shapes.setColor(wallColor);
		int stageLength = stage.length - 1;
		for (int row = stageLength; row >= 0; row--)
		{
			for (int col = 0; col < stage[0].length; col++)
			{
				if (stage[row][col] == Maze.WALL)
				{
					// stageLength - row is necessary to draw correctly, because of how
					// LIBGdx has the y the bottom left, it would draw the map upside down
					if (col == 0)
					{
						shapes.setColor(wallColor);
						shapes.rect(SPRITE_SIZE * col, SPRITE_SIZE * (stageLength - row), SPRITE_SIZE, SPRITE_SIZE);
						shapes.setColor(Color.BLACK);
						if (row > 0 && stage[row - 1][col] == Maze.WALL || row != stageLength && stage[row + 1][col] == Maze.WALL)
							shapes.rect(SPRITE_SIZE * col + 5, SPRITE_SIZE * (stageLength - row),  SPRITE_SIZE - 10, SPRITE_SIZE);
						else
							shapes.rect(SPRITE_SIZE * col + 10, SPRITE_SIZE * (stageLength - row) + 5,  SPRITE_SIZE - 10, SPRITE_SIZE - 10);
					}
					else if (stage[row][col - 1] != Maze.WALL)
					{
						shapes.setColor(wallColor);
						shapes.rect(SPRITE_SIZE * col, SPRITE_SIZE * (stageLength - row), SPRITE_SIZE, SPRITE_SIZE);
						shapes.setColor(Color.BLACK);
						if (row > 0 && stage[row - 1][col] == Maze.WALL || row != stageLength && stage[row + 1][col] == Maze.WALL)
							shapes.rect(SPRITE_SIZE * col + 5, SPRITE_SIZE * (stageLength - row),  SPRITE_SIZE - 10, SPRITE_SIZE);
						else
							shapes.rect(SPRITE_SIZE * col + 10, SPRITE_SIZE * (stageLength - row) + 5,  SPRITE_SIZE - 10, SPRITE_SIZE - 10);
					}
					else
					{
						shapes.setColor(wallColor);
						shapes.rect(SPRITE_SIZE * col, SPRITE_SIZE * (stageLength - row), SPRITE_SIZE, SPRITE_SIZE);
						shapes.setColor(Color.BLACK);
						if (row > 0 && stage[row - 1][col] == Maze.WALL || row != stageLength && stage[row + 1][col] == Maze.WALL)
							shapes.rect(SPRITE_SIZE * col + 5, SPRITE_SIZE * (stageLength - row),  SPRITE_SIZE - 10, SPRITE_SIZE);
						else
							shapes.rect(SPRITE_SIZE * col, SPRITE_SIZE * (stageLength - row) + 5,  SPRITE_SIZE, SPRITE_SIZE - 10);
					}
				}
				else if (stage[row][col] >= 0 && dots[stage[row][col]] == true)
				{
					shapes.setColor(dotColor);
					// Big pac-pellets
					if (Level.bigDots.contains(stage[row][col], false))
					{
						if (bigDotTimer < 0.25)
							shapes.circle(col * SPRITE_SIZE + SPRITE_SIZE / 2, (stageLength - row) * SPRITE_SIZE + SPRITE_SIZE / 2, 10);
						else if (bigDotTimer < 0.50) {}
						else
							bigDotTimer = 0;
					}
					// The ghost entrance gate
					else if (stage[row][col] == ghostEntrance[0] || stage[row][col] == ghostEntrance[1])
					{
						shapes.setColor(gateColor);
						shapes.rect(col * SPRITE_SIZE, (stageLength - row) * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE / 4);
					}
					// Small pac-pellets
					else
						shapes.circle(col * SPRITE_SIZE + SPRITE_SIZE / 2, (stageLength - row) * SPRITE_SIZE + SPRITE_SIZE / 2, 3);
				}
			}
		}
		shapes.end();

		if (itemTimer > 30 && !itemsFinished && !activateItem)
			activateItem = true;

		if (gameState == NORMAL)
		{
			updateStage(delta);
			animateEntities();

			if (chaseTimer > 50)
				chaseTimer = 0;
			else if (chaseTimer > 25)
				chasePacman();
		}
		else if (gameState == GHOST_VULNARABLE)
		{
			if (elapsedTime > ghostDeathTimer)
			{
				if (ghostDied)
				{
					ghostFleeing.stop();
					ghostFleeing.loop();
					ghostDied = false;
				}
				updateStage(delta);
				animateEntities();
			}

			if (elapsedTime > ghostVulnarableTimeFrame)
			{
				gameState = NORMAL;
				powerUpMode.stop();
				playSiren();
				ghostScoreModifier = 200;
			}
		}
		else if (gameState == PACMAN_DEATH)
		{
			if (deathTimer < 0.5)
				animateGhosts();
			else if (deathTimer < 2)
			{
				if (deathSequence)
				{
					elapsedTime = 0;
					pacmanDeathSound.play();
					deathSequence = false;
				}
				entityCurrentTexture[0] = pacmanDeath.getKeyFrame(elapsedTime, false);
			}
			else
			{
				gameState = INTRO;
				livesLeft--;
				elapsedTime = 2;
				entityCurrentTexture[0] = sprites[40];
				resetActors();
			}
		}
		else if (gameState == GAME_END)
		{
			if (elapsedTime > 1.5)
			{
				if (!won && livesLeft != -2)
				{
					nextLevel();
					gameState = INTRO;
					resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				}
				else if (!won)
					game.setScreen(new GameOverScreen(game, false));
				else
					game.setScreen(new GameOverScreen(game, true));
			}
			else if (bigDotTimer < 0.25)
				wallColor = Color.WHITE;
			else if (bigDotTimer < 0.5)
				wallColor = Color.BLUE;
			else
				bigDotTimer = 0;
		}

		// Check for win
		if (dotsLeft == 0)
		{
			if (level == LEVEL_COUNT)
				won = true;

			gameState = GAME_END;
			elapsedTime = 0;
			stopSiren();
			ghostFleeing.stop();
			powerUpMode.stop();
			dotsLeft = -1;
			bigDotTimer = 0;
		}

		// Check for game over
		if (livesLeft == -1)
		{
			gameState = GAME_END;
			won = false;
			elapsedTime = 0;
			stopSiren();
			ghostFleeing.stop();
			powerUpMode.stop();
			dotsLeft = -1;
			livesLeft = -2;
			bigDotTimer = 0;
		}

		batch.setProjectionMatrix(viewport.getCamera().combined);
		batch.begin();

		if (scoreboard == RIGHT_PANE_DEFAULT)
		{
			font.draw(batch, "SCORE", stageWidth + 20, stageHeight - 50);
			font.draw(batch, String.valueOf(score), stageWidth + 20, viewport.getWorldHeight() - 55, 0, Align.center, false);
			for (int pos = 0; pos < livesLeft; pos++)
				batch.draw(entityDefaultFrame[0][0], stageWidth + SPRITE_SIZE * pos, 0, SPRITE_SIZE, SPRITE_SIZE);
			int pos = (int) (viewport.getWorldHeight() / 2 / SPRITE_SIZE);
			for (int i = 0; i < items.length; i++)
				for (int j = 0; j < items[i]; j++)
				{
					batch.draw(itemTexture[i], viewport.getWorldWidth() + 20, pos * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
					pos--;
				}
		}
		else if (scoreboard == RIGHT_PANE)
		{
			font.draw(batch, "SCORE", viewport.getMinWorldWidth() + 5, stageHeight - 50);
			font.draw(batch, String.valueOf(score), stageWidth + 20, viewport.getWorldHeight() - 70);
			for (int pos = 0; pos < livesLeft; pos++)
				batch.draw(entityDefaultFrame[0][0], stageWidth + SPRITE_SIZE * pos, 0, SPRITE_SIZE, SPRITE_SIZE);
			int pos = (int) (viewport.getWorldHeight() / 2 / SPRITE_SIZE);
			for (int i = 0; i < items.length; i++)
				for (int j = 0; j < items[i]; j++)
				{
					batch.draw(itemTexture[i], stageWidth, pos * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
					pos--;
				}
		}
		else
		{
			font.setColor(Color.WHITE);
			font.draw(batch, "SCORE", stageWidth / 2 - 50, viewport.getWorldHeight() - 25);
			font.draw(batch, String.valueOf(score), stageWidth / 2, viewport.getWorldHeight() - 50, 0, Align.center, false);
			for (int pos = 0; pos < livesLeft; pos++)
				batch.draw(entityDefaultFrame[0][0], SPRITE_SIZE * pos, viewport.getMinWorldHeight(), SPRITE_SIZE, SPRITE_SIZE);
			int pos = (int) (viewport.getWorldWidth() / SPRITE_SIZE) - 1;
			for (int i = 0; i < items.length; i++)
				for (int j = 0; j < items[i]; j++)
				{
					batch.draw(itemTexture[i], SPRITE_SIZE * pos, viewport.getMinWorldHeight(), SPRITE_SIZE, SPRITE_SIZE);
					pos--;
				}
		}

		if (gameState == NORMAL || gameState == GHOST_VULNARABLE || gameState == GAME_END)
		{
			for (int i = 0; i < entities.length; i++)
				batch.draw(entityCurrentTexture[i], entities[i].x, stageLength * SPRITE_SIZE - entities[i].y, SPRITE_SIZE, SPRITE_SIZE);

			if (activateItem && !itemsFinished)
				batch.draw(itemTexture[currentItem], itemX, itemY, SPRITE_SIZE, SPRITE_SIZE);
		}
		else if (gameState == PACMAN_DEATH)
		{
			if (deathTimer < 0.5)
				for (int i = 0; i < entities.length; i++)
					batch.draw(entityCurrentTexture[i], entities[i].x, stageLength * SPRITE_SIZE - entities[i].y, SPRITE_SIZE, SPRITE_SIZE);
			else
				batch.draw(entityCurrentTexture[0], pacman.x, stageLength * SPRITE_SIZE - pacman.y, SPRITE_SIZE, SPRITE_SIZE);
		}
		else if (gameState == INTRO)
		{
			font.setColor(Color.YELLOW);
			if (elapsedTime > 2)
			{
				entityCurrentTexture[0] = sprites[40];
				if (elapsedTime > 4)
				{
					gameState = NORMAL;
					elapsedTime = 0;

					playSiren();
				}
				else if (level == 1 && livesLeft == 3)
					livesLeft--;
				else
					for (int i = 0; i < entities.length; i++)
						batch.draw(entityCurrentTexture[i], entities[i].x, stageLength * SPRITE_SIZE - entities[i].y, SPRITE_SIZE, SPRITE_SIZE);
				font.draw(batch, "READY!", itemX - SPRITE_SIZE / 2, itemY + font.getLineHeight());
			}
			font.draw(batch, "READY!", itemX - SPRITE_SIZE / 2, itemY + font.getLineHeight());
		}

		batch.end();
	}

	public void animateEntities()
	{
		// Flip between entity key frames to produce animation if entity is moving
		for (int i = 0; i < entityCurrentTexture.length; i++)
		{
			if (entityDirection[i] == Entity.LEFT)
			{
				if (entities[i] instanceof Ghost)
				{
					if (ghosts[i - 1].isDead())
						entityCurrentTexture[i] = sprites[65];
					else if (gameState == GHOST_VULNARABLE && !ghosts[i - 1].isInsideGhostHouse())
						entityCurrentTexture[i] = entityFrames[5][0].getKeyFrame(elapsedTime, true);
					else
						entityCurrentTexture[i] = entityFrames[i][0].getKeyFrame(elapsedTime, true);
				}
				else
				{
					if (entities[i].isMoving())
						entityCurrentTexture[i] = entityFrames[i][0].getKeyFrame(elapsedTime, true);
					else
						entityCurrentTexture[i] = entityDefaultFrame[i][0];
				}
			}
			else if (entityDirection[i] == Entity.RIGHT)
			{
				if (entities[i] instanceof Ghost)
				{
					if (ghosts[i - 1].isDead())
						entityCurrentTexture[i] = sprites[64];
					else if (gameState == GHOST_VULNARABLE && !ghosts[i - 1].isInsideGhostHouse())
						entityCurrentTexture[i] = entityFrames[5][0].getKeyFrame(elapsedTime, true);
					else
						entityCurrentTexture[i] = entityFrames[i][1].getKeyFrame(elapsedTime, true);
				}
				else
				{
					if (entities[i].isMoving())
						entityCurrentTexture[i] = entityFrames[i][1].getKeyFrame(elapsedTime, true);
					else
						entityCurrentTexture[i] = entityDefaultFrame[i][1];
				}
			}
			else if (entityDirection[i] == Entity.UP)
			{
				if (entities[i] instanceof Ghost)
				{
					if (ghosts[i - 1].isDead())
						entityCurrentTexture[i] = sprites[66];
					else if (gameState == GHOST_VULNARABLE && !ghosts[i - 1].isInsideGhostHouse())
						entityCurrentTexture[i] = entityFrames[5][0].getKeyFrame(elapsedTime, true);
					else
						entityCurrentTexture[i] = entityFrames[i][2].getKeyFrame(elapsedTime, true);
				}
				else
				{
					if (entities[i].isMoving())
						entityCurrentTexture[i] = entityFrames[i][2].getKeyFrame(elapsedTime, true);
					else
						entityCurrentTexture[i] = entityDefaultFrame[i][2];
				}
			}
			else
			{
				if (entities[i] instanceof Ghost)
				{
					if (ghosts[i - 1].isDead())
						entityCurrentTexture[i] = sprites[67];
					else if (gameState == GHOST_VULNARABLE && !ghosts[i - 1].isInsideGhostHouse())
						entityCurrentTexture[i] = entityFrames[5][0].getKeyFrame(elapsedTime, true);
					else
						entityCurrentTexture[i] = entityFrames[i][3].getKeyFrame(elapsedTime, true);
				}
				else
				{
					if (entities[i].isMoving())
						entityCurrentTexture[i] = entityFrames[i][3].getKeyFrame(elapsedTime, true);
					else
						entityCurrentTexture[i] = entityDefaultFrame[i][3];
				}
			}

			// Set the texture to be the score after having just killed a ghost
			if (entities[i] == currentDeadGhost && currentDeadGhost.isDead() && ghostDeathTimer > elapsedTime)
				entityCurrentTexture[i] = scoreTextures[log(ghostScoreModifier / 100) - 2];
		}
	}

	public void animateGhosts()
	{
		// Flip between entity key frames to produce animation if entity is moving
		for (int i = 1; i < entityCurrentTexture.length; i++)
		{
			if (!ghosts[i - 1].isDead())
			{
				if (entityDirection[i] == Entity.LEFT)
				{
					if (entities[i].isMoving())
						entityCurrentTexture[i] = entityFrames[i][0].getKeyFrame(elapsedTime, true);
					else
						entityCurrentTexture[i] = entityDefaultFrame[i][0];
				}
				else if (entityDirection[i] == Entity.RIGHT)
				{
					if (entities[i].isMoving())
						entityCurrentTexture[i] = entityFrames[i][1].getKeyFrame(elapsedTime, true);
					else
						entityCurrentTexture[i] = entityDefaultFrame[i][1];
				}
				else if (entityDirection[i] == Entity.UP)
				{
					if (entities[i].isMoving())
						entityCurrentTexture[i] = entityFrames[i][2].getKeyFrame(elapsedTime, true);
					else
						entityCurrentTexture[i] = entityDefaultFrame[i][2];
				}
				else
				{
					if (entities[i].isMoving())
						entityCurrentTexture[i] = entityFrames[i][3].getKeyFrame(elapsedTime, true);
					else
						entityCurrentTexture[i] = entityDefaultFrame[i][3];
				}
			}
		}
	}

	public void updateStage(float delta)
	{
		for (int i = 0; i < entities.length; i++)
		{
			// Handle ghost collision with Pacman
			Ghost ghost = collisionWithGhosts();
			if (i == 0 && ghost != null)
			{
				if (gameState == NORMAL)
				{
					gameState = PACMAN_DEATH;
					deathTimer = 0;
					elapsedTime = 0;
				}
				else
				{
					currentDeadGhost = ghost;
					ghostVulnarableTimeFrame += 1;
					ghostDeathTimer = elapsedTime + 1;

					score += ghostScoreModifier;
					if (ghostScoreModifier < 3200)
						ghostScoreModifier *= 2;

					ghostDeath.play();
					StackNQueue pathToChamber = maze.getMazeGraph().findShortestPath(ghost.getPosition(), ghostEntrance[3]);
					ghost.dead(true);
					ghost.blockEntrance(false);
					ghost.setPath(pathToChamber);
					ghost.speed = 400;
					ghostDied = true;
				}
				stopSiren();
			}
			if (entities[i].isMoving())
			{
				if (!entities[i].isUpdating())
				{
					if (i == 0) // If the current entity is Pacman, handle dots/items
					{
						int pacmanPos = stage[pacman.vPos][pacman.hPos];
						if (Level.bigDots.contains(pacmanPos, false))
						{
							if (dotsLeft != 0)
							{
								gameState = GHOST_VULNARABLE;
								powerUpMode.stop(); // If already playing
								powerUpMode.loop();
							}
							elapsedTime = 0;
							ghostDeathTimer = -1;
							ghostVulnarableTimeFrame = 10;
							Level.bigDots.removeValue(pacmanPos, false);
							dots[pacmanPos] = false;
							dotsLeft--;
							score += 50;
							chomp.play();

							stopSiren();

//							System.out.println(dotsLeft == 10);
							if (dotsLeft == 10)
								almostOver = true;
						}
						else if (dots[pacmanPos] == true)
						{
							dots[pacmanPos] = false;
							dotsLeft--;
							score += 10;
							chomp.play();

//							System.out.println(dotsLeft == 10);
							if (dotsLeft == 10)
							{
								almostOver = true;
								sirenLow.stop();

								if (gameState == NORMAL)
									sirenHigh.loop();
							}
						}
						else if (pacmanPos == itemPos && !itemsFinished && activateItem)
						{
							itemCollected.play();
							switch (currentItem)
							{
								case 0:
									score += 100; // Cherry
									break;
								case 1:
									score += 300; // Strawberry
									break;
								case 2:
									score += 500; // Orange
									break;
								case 3:
									score += 700; // Apple
									break;
								case 4:
									score += 1000; // Melon
									break;
								case 5:
									score += 2000; // Galaxian Boss
									break;
								case 6:
									score += 3000; // Bell
									break;
								case 7:
									score += 5000; // Key
									break;
							}

							items[currentItem]--;
							if (items[currentItem] == 0)
							{
								currentItem = findNextItem(currentItem);
								if (currentItem == -1)
									itemsFinished = true;
							}
							activateItem = false;
							itemTimer = 0;
						}
						switch (score)
						{
							case 10000:
								livesLeft++;
								extraLife.play();
								break;
							case 100000:
								livesLeft++;
								extraLife.play();
								break;
						}
					}
					entities[i].update();
					entityDirection[i] = entities[i].getDirection();
					entities[i].updating(true);
				}

				entities[i].updateWorldPos(entityDirection[i], delta);
			}
		}
	}

	public Ghost collisionWithGhosts()
	{
		for (Ghost ghost : ghosts)
		{
			if (!ghost.isDead())
			{
				int collisionSize = SPRITE_SIZE / 4 * 2;
				if (rectCollision(pacman.x + SPRITE_SIZE / 4, pacman.y - SPRITE_SIZE / 4, collisionSize, collisionSize,
						ghost.x + SPRITE_SIZE / 4, ghost.y - SPRITE_SIZE / 4, collisionSize, collisionSize))
				{
					return ghost;
				}
			}
		}
		return null;
	}

	public int log(int x)
	{
		return (int) (Math.log(x) / Math.log(2));
	}

	public boolean rectCollision(float x1, float y1, int w1, int h1, float x2, float y2, int w2, int h2)
	{
		return x1 < x2 + w2 &&
				x2 < x1 + w1 &&
				y1 < y2 + h2 &&
				y2 < y1 + h1;
	}

	/**
	 * Gives Blinky a new path from his current position to Pacman's position
	 */
	public void chasePacman()
	{
		ghosts[3].setPath(maze.getMazeGraph().findShortestPath(stage[ghosts[3].vPos][ghosts[3].hPos], stage[pacman.vPos][pacman.hPos]));
	}


	public void resetActors()
	{
		pacman.setMoving(false);
		pacman.hPos = pacmanInitialVPos;
		pacman.vPos = pacmanInitialHPos;
		pacman.x = pacman.hPos * SPRITE_SIZE;
		pacman.y = pacman.vPos * SPRITE_SIZE;
//		System.out.println("Pacman column: " + pacman.hPos + "Pacman row: " + pacman.vPos);

		for (int i = 0; i < ghosts.length; i++)
		{
			ghosts[i].hPos = ghostsInitPos[i][1];
			ghosts[i].vPos = ghostsInitPos[i][0];
			ghosts[i].x = ghosts[i].hPos * SPRITE_SIZE;
			ghosts[i].y = ghosts[i].vPos * SPRITE_SIZE;
			ghosts[i].blockEntrance(false);
			ghosts[i].createPath();
		}
		deathSequence = true;
	}

	public void playSiren()
	{
		if (almostOver)
			sirenHigh.loop();
		else
			sirenLow.loop();
	}

	public void stopSiren()
	{
		sirenHigh.stop();
		sirenLow.stop();
	}

//	public float toWorldCoord(float pos, float offset)
//	{
//		return pos * SPRITE_SIZE + offset;
//	}


	/**
	 * @param width
	 * @param height
	 * @see ApplicationListener#resize(int, int)
	 */
	@Override
	public void resize(int width, int height)
	{
		viewport.update(width, height, true);

		float offsetX = viewport.getWorldWidth() - viewport.getMinWorldWidth();
		float offsetY = viewport.getWorldHeight() - viewport.getMinWorldHeight();
		if (offsetX > 100)
			scoreboard = RIGHT_PANE;
		else if (offsetY > 100)
			scoreboard = TOP_PANE;
		else
			scoreboard = RIGHT_PANE_DEFAULT;

//		if (scoreboard == RIGHT_PANE_DEFAULT)
//			viewport.update(width, height, true);
	}

	/**
	 * @see ApplicationListener#pause()
	 */
	@Override
	public void pause()
	{

	}

	/**
	 * @see ApplicationListener#resume()
	 */
	@Override
	public void resume()
	{

	}

	/**
	 * Called when this screen is no longer the current screen for a {@link Game}.
	 */
	@Override
	public void hide()
	{

	}

	/**
	 * Called when this screen should release all resources.
	 */
	@Override
	public void dispose()
	{
		shapes.dispose();
		batch.dispose();
		spritesheet.dispose();
		font.dispose();
		font32.dispose();
		intro.dispose();
		chomp.dispose();
		itemCollected.dispose();
		extraLife.dispose();
		pacmanDeathSound.dispose();
		ghostDeath.dispose();
		sirenLow.dispose();
		sirenHigh.dispose();
		ghostFleeing.dispose();
		powerUpMode.dispose();
		playAgain.dispose();
		playAgainBright.dispose();
	}
}
