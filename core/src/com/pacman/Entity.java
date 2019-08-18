package com.pacman;

import com.badlogic.gdx.Input;

import java.util.Random;

import static com.pacman.Level.SPRITE_SIZE;

/**
 * Created by Joel on 4/18/17.
 */
public abstract class Entity
{
	public static final Random random = new Random();
	public static final int LEFT = Input.Keys.LEFT;
	public static final int RIGHT = Input.Keys.RIGHT;
	public static final int UP = Input.Keys.UP;
	public static final int DOWN = Input.Keys.DOWN;

	public float x, y, speed;
	public int vPos;
	public int hPos;
	private int direction, prevDirection;
	private boolean isMoving;
	private boolean updating;
	private boolean blockEntrance;

	public Entity(int vPos, int hPos, boolean isMoving)
	{
		this.vPos = vPos;
		this.hPos = hPos;
		this.isMoving = isMoving;

		x = hPos * SPRITE_SIZE;
		y = vPos * SPRITE_SIZE;
		speed = 150;
		blockEntrance = true;
	}

	public void update()
	{
		if (direction != prevDirection)
		{
			if (!update(prevDirection))
				update(direction);
			else
				direction = prevDirection;
		}
		else
			if (!update(direction))
				isMoving = false;
	}

	/**
	 * Updates the entities position based on a given direction.
	 *
	 * @param direction	left, right, up, down
	 * @return			true if was able to move in the given direction, false otherwise
	 */
	public boolean update(int direction)
	{
		switch (direction)
		{
			case LEFT:
				if (Level.stage[vPos][hPos - 1] != Maze.WALL)
				{
					if (blockEntrance && (Level.stage[vPos][hPos - 1] == Level.ghostEntrance[0] || Level.stage[vPos][hPos - 1] == Level.ghostEntrance[1]))
						return false;
					else
						hPos -= 1;
				}
				else return false;
				break;
			case RIGHT:
				if (Level.stage[vPos][hPos + 1] != Maze.WALL)
				{
					if (blockEntrance && (Level.stage[vPos][hPos + 1] == Level.ghostEntrance[0] || Level.stage[vPos][hPos + 1] == Level.ghostEntrance[1]))
						return false;
					else
						hPos += 1;
				}
				else return false;
				break;
			case UP:
				if (Level.stage[vPos - 1][hPos] != Maze.WALL)
				{
					if (blockEntrance && (Level.stage[vPos - 1][hPos] == Level.ghostEntrance[0] || Level.stage[vPos - 1][hPos] == Level.ghostEntrance[1]))
						return false;
					else
						vPos -= 1;
				}
				else return false;
				break;
			case DOWN:
				if (Level.stage[vPos + 1][hPos] != Maze.WALL)
				{
					if (blockEntrance && (Level.stage[vPos + 1][hPos] == Level.ghostEntrance[0] || Level.stage[vPos + 1][hPos] == Level.ghostEntrance[1]))
						return false;
					else
						vPos += 1;
				}
				else return false;
		}
		return true;
	}

	public void updateWorldPos(int direction, float delta)
	{
		if (direction == LEFT)
		{
			x -= speed * delta;
			if (x <= hPos * SPRITE_SIZE)
			{
				x = hPos * SPRITE_SIZE;
				updating(false);
			}
		}
		else if (direction == RIGHT)
		{
			x += speed * delta;
			if (x >= hPos * SPRITE_SIZE)
			{
				x = hPos * SPRITE_SIZE;
				updating(false);
			}
		}
		else if (direction == UP)
		{
			y -= speed * delta;
			if (y <= vPos * SPRITE_SIZE)
			{
				y = vPos * SPRITE_SIZE;
				updating(false);
			}
		}
		else if (direction == DOWN)
		{
			y += speed * delta;
			if (y >= vPos * SPRITE_SIZE)
			{
				y = vPos * SPRITE_SIZE;
				updating(false);
			}
		}
	}

	public void setDirection(int direction)
	{
		if (direction == LEFT || direction == RIGHT || direction == UP || direction == DOWN)
		{
			if (isValid(direction))
			{
				this.direction = direction;
				prevDirection = direction;
				isMoving = true;
			}
			else
			{
//				System.out.println("not valid: " + direction);
				prevDirection = direction;
			}
		}
	}

	public boolean isValid(int direction)
	{
		switch (direction)
		{
			case LEFT:
				if (Level.stage[vPos][hPos - 1] != Maze.WALL)
				{
					if (blockEntrance && (Level.stage[vPos][hPos - 1] == Level.ghostEntrance[0] || Level.stage[vPos][hPos - 1] == Level.ghostEntrance[1]))
						return false;
					else
						return true;
				}
				break;
			case RIGHT:
				if (Level.stage[vPos][hPos + 1] != Maze.WALL)
				{
					if (blockEntrance && (Level.stage[vPos][hPos + 1] == Level.ghostEntrance[0] || Level.stage[vPos][hPos + 1] == Level.ghostEntrance[1]))
						return false;
					else
						return true;
				}
				break;
			case UP:
				if (Level.stage[vPos - 1][hPos] != Maze.WALL)
				{
					if (blockEntrance && (Level.stage[vPos - 1][hPos] == Level.ghostEntrance[0] || Level.stage[vPos - 1][hPos] == Level.ghostEntrance[1]))
						return false;
					else
						return true;
				}
				break;
			case DOWN:
				if (Level.stage[vPos + 1][hPos] != Maze.WALL)
				{
					if (blockEntrance && (Level.stage[vPos + 1][hPos] == Level.ghostEntrance[0] || Level.stage[vPos + 1][hPos] == Level.ghostEntrance[1]))
						return false;
					else
						return true;
				}
		}
		return false;
	}

	public int getPosition()
	{
		return Level.stage[vPos][hPos];
	}

	public int getDirection() { return direction; }

	public void blockEntrance(boolean state) { blockEntrance = state; }

	public boolean entranceBlocked() { return blockEntrance; }

	public boolean isMoving() { return isMoving; }

	public boolean isUpdating() { return updating; }

	public void updating(boolean state) { updating = state; }

	public void setMoving(boolean state) { isMoving = state; }
}
