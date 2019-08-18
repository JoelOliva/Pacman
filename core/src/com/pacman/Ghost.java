package com.pacman;

import java.util.ArrayDeque;

import static com.pacman.Level.ghostEntrance;
import static com.pacman.Level.stage;

/**
 * Created by Joel on 5/6/17.
 */
public class Ghost extends Entity
{
	public static final int[] possibleDirections = {LEFT, RIGHT, UP, DOWN};
	private ArrayDeque<Integer> directions;
	private boolean dead;
	private boolean isInsideGhostHouse;

	public Ghost(int vPos, int hPos, boolean isMoving)
	{
		super(vPos, hPos, isMoving);

		// If ghost below gate we know its inside the ghost house, only blinky starts outside
		if (stage[vPos][hPos] > ghostEntrance[1])
			isInsideGhostHouse = true;

		directions = new ArrayDeque<>(50);
		createPath();
	}

	@Override
	public void update()
	{
		if (directions.isEmpty())
			createPath();

		int direction = directions.remove();
		setDirection(direction);
		super.update();

		if (!dead && (stage[vPos + 1][hPos] == ghostEntrance[0] || stage[vPos + 1][hPos] == ghostEntrance[1]))
		{
			isInsideGhostHouse = false;
			blockEntrance(true);
			createPath();
		}
		else if (dead && directions.isEmpty())
		{
			speed = 150;
			isInsideGhostHouse = true;
			AssetsUtils.ghostFleeing.stop();
			dead = false;
			blockEntrance(true);
			createPath();
			blockEntrance(false);
		}
	}

	public void createPath()
	{
		directions.clear();
		int randIndex;
		do
		{
			randIndex = random.nextInt(4);
		} while (!isValid(possibleDirections[randIndex]));

		int currentHPos = hPos;
		int currentVPos = vPos;
		while (directions.size() != 50)
		{
			if (possibleDirections[randIndex] == LEFT)
			{
				if (isValid(possibleDirections[randIndex], currentHPos, currentVPos))
				{
					directions.add(possibleDirections[randIndex]);
					currentHPos--;

					boolean up = false;
					boolean down = false;
					if (isValid(UP, currentHPos, currentVPos))
						up = true;
					if (isValid(DOWN, currentHPos, currentVPos))
						down = true;

					if (up && down)
					{
						int choice = random.nextInt(2);
						if (choice == 0)
							randIndex = 2;
						else
							randIndex = 3;
					}
					else if (up)
						randIndex = 2;
					else if (down)
						randIndex = 3;
				}
				else
					randIndex = random.nextInt(4);
			}
			else if (possibleDirections[randIndex] == RIGHT)
			{
				if (isValid(possibleDirections[randIndex], currentHPos, currentVPos))
				{
					directions.add(possibleDirections[randIndex]);
					currentHPos++;

					boolean up = false;
					boolean down = false;
					if (isValid(UP, currentHPos, currentVPos))
						up = true;
					if (isValid(DOWN, currentHPos, currentVPos))
						down = true;

					if (up && down)
					{
						int choice = random.nextInt(2);
						if (choice == 0)
							randIndex = 2;
						else
							randIndex = 3;
					}
					else if (up)
						randIndex = 2;
					else if (down)
						randIndex = 3;
				}
				else
					randIndex = random.nextInt(4);
			}
			else if (possibleDirections[randIndex] == UP)
			{
				if (isValid(possibleDirections[randIndex], currentHPos, currentVPos))
				{
					directions.add(possibleDirections[randIndex]);
					currentVPos--;

					boolean left = false;
					boolean right = false;
					if (isValid(LEFT, currentHPos, currentVPos))
						left = true;
					if (isValid(RIGHT, currentHPos, currentVPos))
						right = true;

					if (left && right)
					{
						int choice = random.nextInt(2);
						if (choice == 0)
							randIndex = 0;
						else
							randIndex = 1;
					}
					else if (left)
						randIndex = 0;
					else if (right)
						randIndex = 1;
				}
				else
					randIndex = random.nextInt(4);
			}
			else if (possibleDirections[randIndex] == DOWN)
			{
				if (isValid(possibleDirections[randIndex], currentHPos, currentVPos))
				{
					directions.add(possibleDirections[randIndex]);
					currentVPos++;

					boolean left = false;
					boolean right = false;
					if (isValid(LEFT, currentHPos, currentVPos))
						left = true;
					if (isValid(RIGHT, currentHPos, currentVPos))
						right = true;

					if (left && right)
					{
						int choice = random.nextInt(2);
						if (choice == 0)
							randIndex = 0;
						else
							randIndex = 1;
					}
					else if (left)
						randIndex = 0;
					else if (right)
						randIndex = 1;
				}
				else
					randIndex = random.nextInt(4);
			}
		}
	}

	public void setPath(StackNQueue path)
	{
		directions.clear();

//		System.out.println(path);
		int currentHPos = hPos;
		int currentVPos = vPos;
		while (path.size() != 0)
		{
			int pos = path.dequeue();
			if (stage[currentVPos][currentHPos] - 1 == pos)
			{
				directions.add(LEFT);
				currentHPos--;
			}
			else if (stage[currentVPos][currentHPos] + 1 == pos)
			{
				directions.add(RIGHT);
				currentHPos++;
			}
			else if (stage[currentVPos][currentHPos] > pos)
			{
				directions.add(UP);
				currentVPos--;
			}
			else
			{
				directions.add(DOWN);
				currentVPos++;
			}
		}
	}

	public boolean isValid(int direction, int hPos, int vPos)
	{
		switch (direction)
		{
			case LEFT:
				if (Level.stage[vPos][hPos - 1] != Maze.WALL)
				{
					if (entranceBlocked() && (Level.stage[vPos][hPos - 1] == Level.ghostEntrance[0] || Level.stage[vPos][hPos - 1] == Level.ghostEntrance[1]))
						return false;
					else
						return true;
				}
				break;
			case RIGHT:
				if (Level.stage[vPos][hPos + 1] != Maze.WALL)
				{
					if (entranceBlocked() && (Level.stage[vPos][hPos + 1] == Level.ghostEntrance[0] || Level.stage[vPos][hPos + 1] == Level.ghostEntrance[1]))
						return false;
					else
						return true;
				}
				break;
			case UP:
				if (Level.stage[vPos - 1][hPos] != Maze.WALL)
				{
					if (entranceBlocked() && (Level.stage[vPos - 1][hPos] == Level.ghostEntrance[0] || Level.stage[vPos - 1][hPos] == Level.ghostEntrance[1]))
						return false;
					else
						return true;
				}
				break;
			case DOWN:
				if (Level.stage[vPos + 1][hPos] != Maze.WALL)
				{
					if (entranceBlocked() && (Level.stage[vPos + 1][hPos] == Level.ghostEntrance[0] || Level.stage[vPos + 1][hPos] == Level.ghostEntrance[1]))
						return false;
					else
						return true;
				}
		}
		return false;
	}

	public ArrayDeque<Integer> getPath() { return directions; }

	public void dead(boolean state) { dead = true; }

	public boolean isDead() { return dead; }

	public boolean isInsideGhostHouse() { return isInsideGhostHouse; }
}
