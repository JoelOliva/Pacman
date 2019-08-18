package com.pacman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.viewport.ExtendViewport;


/**
 * Created by Joel on 6/2/17.
 */
public class Level
{
	public static final int SPRITE_SIZE = 32;
	public static final int RIGHT_PANE_DEFAULT = 0;
	public static final int RIGHT_PANE = 1;
	public static final int TOP_PANE = 2;
	public static final int LEVEL_COUNT = 2;

	// Game states
	public static final int INTRO = 0;
	public static final int NORMAL = 1;
	public static final int PACMAN_DEATH = 2;
	public static final int GHOST_VULNARABLE = 3;
	public static final int GAME_END = 4;

	public static Maze maze;
	public static int[][] stage;
	public static int[] ghostEntrance;
	public static Array<Integer> bigDots;
	public static int items[];

	/**
	 * Since an empty space in the maze starts is represented as 0 and goes to N,
	 * an index in this array represents an empty space in the maze. True if the
	 * dot should still be drawn. For example, if Pacman steps over one of these dots
	 * which is still being drawn, we should set it to false in this array.
	 */
	public static boolean[] dots;

	public static float elapsedTime, bigDotTimer, chaseTimer, deathTimer, itemTimer, ghostDeathTimer;
	public static int level, dotsLeft, livesLeft, score, scoreboard, ghostScoreModifier, ghostVulnarableTimeFrame, gameState;
	public static int currentItem, itemPos, itemX, itemY;

	public static int[][] ghostsInitPos;
	public static Ghost currentDeadGhost;

	public static boolean activateItem;
	public static boolean itemsFinished;
	public static boolean deathSequence;
	public static boolean almostOver;
	public static boolean ghostDied;

	public static Color wallColor = Color.BLUE;

	// Characters/Actors
	public static Entity[] entities;
	public static Pacman pacman;
	public static Ghost[] ghosts;

	public static int stageWidth, stageHeight;
	public static int pacmanInitialHPos;
	public static int pacmanInitialVPos;

	public static ExtendViewport viewport;

	private Level() {}

	private static void loadLevel()
	{
		maze = new Maze("Mazes/level" + level + ".txt");

		FileHandle levelDataFile = Gdx.files.internal("Mazes/level" + level + "_data.txt");

		bigDots = new Array<>();

		StringProcessor levelData = new StringProcessor(levelDataFile.readString());

		for (int i = 0; i < 4; i++)
			bigDots.add(Integer.valueOf(levelData.next()));

		ghostEntrance = new int[4];
		for (int i = 0; i < ghostEntrance.length; i++)
			ghostEntrance[i] = Integer.valueOf(levelData.next());

		items = new int[8];
		for (int i = 0; i < items.length; i++)
			items[i] = Integer.valueOf(levelData.next());

		stage = maze.getMaze();
	}

	public static void nextLevel()
	{
		loadLevel();
		reset();

		stageWidth = SPRITE_SIZE * stage[0].length;
		stageHeight = SPRITE_SIZE * stage.length;
		viewport = new ExtendViewport(stageWidth, stageHeight);

		itemPos = maze.getItemPos();
		currentItem = findNextItem(currentItem);

		ghosts = new Ghost[4];
		int[] ghostMarkers = maze.getGhostGraphPos();
		ghostsInitPos = new int[4][2];
		int ghostIndex = 0;

		int pacmanStart = maze.getStart();
		pacmanInitialHPos = 0;
		pacmanInitialVPos = 0;

		int emptySpaceCount = 0;
		int stageLength = stage.length - 1;
		for (int row = stageLength; row >= 0; row--)
		{
			for (int col = 0; col < stage[0].length; col++)
			{
				if (stage[row][col] == pacmanStart)
				{
					pacmanInitialHPos = row;
					pacmanInitialVPos = col;
//					System.out.println(row + " " + col);
					emptySpaceCount++;
				}
				else if (stage[row][col] == ghostMarkers[0] || stage[row][col] == ghostMarkers[1] ||
						stage[row][col] == ghostMarkers[2] || stage[row][col] == ghostMarkers[3])
				{
					ghostsInitPos[ghostIndex][0] = row;
					ghostsInitPos[ghostIndex][1] = col;
					ghosts[ghostIndex++] = new Ghost(row, col, true);
					emptySpaceCount++;
				}
				else if (stage[row][col] == itemPos)
				{
					itemX = SPRITE_SIZE * col;
					itemY = SPRITE_SIZE * (stageLength - row);
//					System.out.println(itemPos);
					emptySpaceCount++;
				}
				else if (stage[row][col] != Maze.WALL)
					emptySpaceCount++;
			}
		}
		currentDeadGhost = ghosts[0]; // Default to an arbitrary ghost to avoid a null pointer exception

		// Exclude the gate in dotsLeft which take up 2 "maze" spaces, exclude another for the item position
		dotsLeft = emptySpaceCount - 8;

		// Activate all dots to be drawn
		dots = new boolean[emptySpaceCount];
		for (int dot = 0; dot < dots.length; dot++)
			dots[dot] = true;

		dots[itemPos] = false;

		// Remove the dot at the empty space inside the ghost chamber
		dots[ghostEntrance[2]] = false;

		// Remove all dots where a ghosts exists
		for (int i = 0; i < ghostsInitPos.length; i++)
			dots[ghostMarkers[i]] = false;

		pacman = new Pacman(pacmanInitialHPos, pacmanInitialVPos, false);

		int choice = Entity.random.nextInt(2);
		if (choice == 0)
			pacman.setDirection(Entity.LEFT);
		else
			pacman.setDirection(Entity.RIGHT);

		// The first three ghosts are going to be inside the chamber in the beginning,
		// so they should be able to get out.
		for (int i = 0; i < ghosts.length - 1; i++)
			ghosts[i].blockEntrance(false);

		entities = new Entity[5];
		entities[0] = pacman;
		entities[1] = ghosts[0];
		entities[2] = ghosts[1];
		entities[3] = ghosts[2];
		entities[4] = ghosts[3];

		Gdx.input.setInputProcessor(pacman);
		level++;
	}

	/**
	 * Finds the next item available
	 *
	 * @param currentItem	the current item index
	 * @return				the index of the current item or negative 1 if none left
	 */
	public static int findNextItem(int currentItem)
	{
		for (; currentItem < items.length; currentItem++)
		{
			if (items[currentItem] > 0)
				return currentItem;
		}
		return -1;
	}

	public static void reset()
	{
		elapsedTime = 0;
		bigDotTimer = 0;
		chaseTimer = 0;
		deathTimer = 0;
		itemTimer = 0;
		ghostDeathTimer = 0;
		scoreboard = 0;
		ghostScoreModifier = 200;
		gameState = NORMAL;
		activateItem = false;
		itemsFinished = false;
		almostOver = false;
		deathSequence = true;
		currentItem = 0;
	}

	public static void initVals()
	{
		level = 0;
		ghostVulnarableTimeFrame = 8;
		livesLeft = 3;
		score = 0;
	}
}
