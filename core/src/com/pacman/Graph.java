package com.pacman;

import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * A Graph implements the concepts found in graph theory. A Graph has nodes/vertices
 * and edges/lines which can be used to model various data and problems.
 *
 * @author Joel Oliva
 */
public class Graph
{
	private ArrayList<Node> nodes;
	private StackNQueue path;

	private class Node
	{
		private int id;
		private boolean visited;
		private Node back;
		private ArrayList<Node> neighbors;

		public Node(int id)
		{
			this.id = id;
			neighbors = new ArrayList<>();
		}
	}

	/**
	 * Constructs a new Graph.
	 */
	public Graph()
	{
		nodes = new ArrayList<>();
		path = new StackNQueue();
	}

	/**
	 * Add a new node to this graph with an associated id
	 *
	 * @param id	the id of the node
	 */
	public void addNode(int id)
	{
		nodes.add(new Node(id));
	}

	/**
	 * Connects a node with another in the graph
	 *
	 * @param id1	the id of the node that gets a new adjacent node
	 * @param id2	the id of the other node
	 */
	public void addEdge(int id1, int id2)
	{
		nodes.get(id1).neighbors.add(nodes.get(id2));
	}

	/**
	 * Finds the shortest path from one node in the graph to another. The shortest path is
	 * measured by the number of steps it takes to get to the other node.
	 *
	 * @param startingNodeId	the starting position
	 * @param endingNodeId		the ending position
	 * @return					a list of id's representing the shortest path or an empty set if no path exists
	 */
	public StackNQueue findShortestPath(int startingNodeId, int endingNodeId)
	{
		ArrayDeque<Node> queue = new ArrayDeque<>();
		queue.addLast(nodes.get(startingNodeId));
		nodes.get(startingNodeId).visited = true;

		// a breath first search approach
		while (!queue.isEmpty())
		{
			Node current = queue.removeFirst();

			if (current.id == endingNodeId) { break; }

			for (Node adjacentNode : current.neighbors)
			{
				if (!adjacentNode.visited)
				{
					adjacentNode.visited = true;
					adjacentNode.back = current;
					queue.addLast(adjacentNode);
				}
			}
		}

		// build the path if at all possible
		path.clear();
		Node next = nodes.get(endingNodeId).back;
		while (next != null)
		{
			path.push(next.id);
			next = next.back;
		}

		if (path.size() > 0)
		{
			path.pop();
		}

		for (Node node : nodes)
		{
			node.visited = false;
			node.back = null;
		}

		return path;
	}
}
