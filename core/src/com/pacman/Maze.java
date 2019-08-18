package com.pacman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

//import java.util.Scanner;

//import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;

/**
 * A maze contains information and utilities to simplify the processing of a maze file.
 * A maze file is any file that specifies height and width on the first line and contains
 * walls with no gaps and an inner field. For example:
 *
 * <pre>
 *     5 4
 *     XXXX
 *     XS X
 *     X  X
 *     X GX
 *     XXXX
 * </pre>
 *
 * Where S represents the starting position in the maze and G the ending position.
 *
 * @author Joel Oliva
 */
public class Maze
{
	public static final int WALL = -1;

	private int maze[][];			// the 2D representation of the maze, every non-negative value in the maze represents a valid path
	private StringProcessor input;			// to read from the file
	private int start;				// the starting position S in the 2D maze
	private int end;				// the ending position G in the 2D maze
	private int itemPos;			// the item position I int the 2D maze
	private int[] ghosts;
	private Graph mazeGraph;		// the graph representation of the maze
	private int mazeWidth;			// the width of the maze including walls
	private int mazeHeight;			// the height of the maze including walls

	/**
	 * Constructs a maze from a given file path.
	 *
	 * @param pathname			the path to the file
//	 * @throws IOException		if the file does not exist or cannot be opened for any other reason
	 * @throws RuntimeException	if the file does not match the format of a maze file
	 */
	public Maze(String pathname)
	{
//		try
//		{
//			input = new BufferedReader(new FileReader(pathname));
//		} catch (FileNotFoundException e)
//		{
//			e.printStackTrace();
//		}
		FileHandle mazeFile = Gdx.files.internal(pathname);
		input = new StringProcessor(mazeFile.readString());

		String[] size = input.readLine().split(" ");

		mazeWidth = 0;
		mazeHeight = 0;
		try
		{
			mazeHeight = Integer.parseInt(size[0]);
			mazeWidth = Integer.parseInt(size[1]);
		} catch(NumberFormatException e)
		{
			throw new RuntimeException("Illegal File Format");
		}

		maze = new int[mazeHeight][mazeWidth];
		ghosts = new int[4];

		// Default values for the maze should be -1
		for (int row = 0; row < mazeHeight; row++)
		{
			for (int col = 0; col < mazeWidth; col++)
			{
				maze[row][col] = WALL;
			}
		}

		generateMazeGraph();
	}

	/**
	 * The width of the maze.
	 *
	 * @return	the width of the maze
	 */
	public int getWidth() { return mazeWidth; }

	/**
	 * Returns the height of the maze.
	 *
	 * @return	the height of the maze
	 */
	public int getHeight() { return mazeHeight; }

	/**
	 * Returns the starting position in the maze.
	 *
	 * @return	the starting position in the maze
	 */
	public int getStart() { return start; }

	public int getItemPos() { return itemPos; }

	/**
	 * Returns the ending position in the maze.
	 *
	 * @return	the ending position in the maze
	 */
	public int getEnd() { return end; }

	/**
	 * Returns the initial ghosts position in the maze.
	 *
	 * @return	the initial ghosts position in the maze
	 */
	public int[] getGhostGraphPos() { return ghosts; }

	/**
	 * Returns the value at a position in the maze, while -1 marks the walls
	 * in the maze non-negative values mark paths in the maze as well as
	 * ID's for the graph representation of this maze.
	 *
	 * @param row	the row for the 2D maze
	 * @param col	the column for the 2D maze
	 * @return		the value at a position in the maze
	 */
	public int getMarker(int row, int col) { return maze[row][col]; }

	/**
	 * Returns a graph representation of this maze.
	 *
	 * @return	returns a graph representation of this maze
	 */
	public Graph getMazeGraph() { return mazeGraph; }

	public int[][] getMaze() { return maze; }

	/**
	 * A method for abstracting away the process of creating a graph out of the maze.
	 *
//	 * @throws IOException		if the file does not exist or cannot be opened for any other reason
	 * @throws RuntimeException	if the file does not match the format of a maze file
	 */
	private void generateMazeGraph()
	{
		mazeGraph = new Graph();

		// Make sure the first row abides by the maze format requirements
		String currentLine = input.readLine();
		for (int index = 0; index < currentLine.length(); index++)
		{
			if (currentLine.charAt(index) != 'X')
			{
				throw new RuntimeException("Illegal File Format");
			}
		}

		int ghostIndex = 0;
		int id = 0;
		int row = 1;
		while ((currentLine = input.readLine()) != null)
		{
			// If it's missing the "walls" then it is not a legal maze
			if (currentLine.charAt(0) != 'X' || currentLine.charAt(currentLine.length() - 1) != 'X')
			{
				throw new RuntimeException("Illegal File Format");
			}

			for (int col = 1, lineLength = currentLine.length() - 1; col < lineLength; col++)
			{
				switch (currentLine.charAt(col))
				{
					case ' ':
						maze[row][col] = id;
						mazeGraph.addNode(id++);
						break;
					case 'S':
						start = id++;
						maze[row][col] = start;
						mazeGraph.addNode(start);
						break;
					case 'G':
						end = id++;
						maze[row][col] = end;
						mazeGraph.addNode(end);
						break;
					case 'g':
						ghosts[ghostIndex] = id++;
						maze[row][col] = ghosts[ghostIndex];
						mazeGraph.addNode(ghosts[ghostIndex]);
						ghostIndex++;
						break;
					case 'I':
						itemPos = id++;
						maze[row][col] = itemPos;
						mazeGraph.addNode(itemPos);
						break;
				}
			}
			row++;

			// Make sure the last row abides by the maze format requirements
			if (row == mazeHeight)
			{
				for (int index = 0; index < currentLine.length(); index++)
				{
					if (currentLine.charAt(index) != 'X')
					{
						throw new RuntimeException("Illegal File Format");
					}
				}
				break;
			}
		}

		int rowLength = mazeHeight - 1;
		for (row = 1; row < rowLength; row++)
		{
			for (int col = 1, colLength = mazeWidth - 1; col < colLength; col++)
			{
				// Add edges. Every non-negative value in the maze represents a valid node ID
				int current = maze[row][col];
				if (current >= 0)
				{
					int north = maze[row - 1][col];
					int south = maze[row + 1][col];
					int east = maze[row][col + 1];
					int west = maze[row][col - 1];

					if (north >= 0)
						mazeGraph.addEdge(current, north);

					if (south >= 0)
						mazeGraph.addEdge(current, south);

					if (east >= 0)
						mazeGraph.addEdge(current, east);

					if (west >= 0)
						mazeGraph.addEdge(current, west);
				}
			}
		}
	}
}