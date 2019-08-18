package com.pacman;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * @author Joel Oliva, Yi'an Yue
 */
public class StackNQueue
{
	private int[] backing_store = new int[1024]; // The backing store array
	private int first; // The first position
	private int last; // The last position
	private int size; // The size of the virtual array
	
	/**
	 * Will grow the backing store array if it runs out room.
	 */
	private void expand()
	{
		int[] new_backing_store = new int[backing_store.length * 2];
		
		int pos = first;
		for (int iter = 0; iter < size; iter++)
			new_backing_store[iter] = backing_store[bound(pos++)];
		
		last = size - 1;
		backing_store = new_backing_store;
	}

	/**
	 * Removes the the first element. This method is equivalent to remove_first.
	 *
	 * @return the removed element
	 */
	public int pop()
	{
		return remove_first();
	}

	/**
	 * Adds data at the beginning. This method is equivalent to add_first
	 *
	 * @param data the data to be added
	 */
	public void push(int data)
	{
		add_first(data);
	}

	/**
	 * Adds data at the end. This method is equivalent to add_last
	 *
	 * @param data the data to be added
	 */
	public void enqueue(int data)
	{
		add_last(data);
	}

	/**
	 * Removes the the first element. This method is equivalent to remove_first
	 *
	 * @return the removed element
	 */
	public int dequeue()
	{
		return remove_first();
	}

	/**
	 * Returns the first element. This method is equivalent to get_first
	 *
	 * @return the first element
	 */
	public int peek()
	{
		return get_first();
	}

	/**
	 * Inserts an element at beginning of the array.
	 */
	public void add_first(int data)
	{
		if (size == backing_store.length)
		{
			expand();
			first = bound(--first);
			backing_store[first] = data;
		}
		else if (size == 0)
		{
			backing_store[0] = data;
		}
		else
		{
			first = bound(--first);
			backing_store[first] = data;
		}

		size++;
	}

	/**
	 * Inserts an item at the end of the array.
	 */
	public void add_last(int data)
	{
		if (size == backing_store.length)
		{
			expand();
			backing_store[++last] = data;
		}
		else if (size == 0)
		{
			backing_store[0] = data; // add first
		}
		else
			backing_store[++last] = data; // add last
		
		size++;
	}

	/**
	 * Inserts an item after the specified index.
	 * 
	 * @param after	the position after of where to place data
	 * @param data	the item to place in the array
	 */
	public void add_middle(int after, int data)
	{
		if (size == backing_store.length)
		{
			expand();
			shift_insert(after, data); // add middle
		}
		else if(size == 0)
			backing_store[0] = data; // add first
		else if(after > size)
			backing_store[++last] = data; // add last
		else
			shift_insert(after, data); // add middle

		size++;
	}
	
	/**
	 * A helper method for add_middle(). Shifts elements starting from
	 * the last value up to the position where we need to insert data.
	 * 
	 * @param after	the position after of where to place data
	 * @param data	the item to place in the array
	 */
	private void shift_insert(int after, int data)
	{
			int pos = bound(first + after);
			for (int index = last; index > pos; index--)
				backing_store[bound(index + 1)] = backing_store[index];
			
			backing_store[++pos] = data;
			last++;
	}

	/**
	 * Removes all elements from the array
	 */
	public void clear()
	{
		first = 0;
		last = 0;
		size = 0;
	}

	/**
	 * Will determine if item exists in the array
	 * 
	 * @return true if item is in the array, false otherwise
	 */
	public boolean contains(int item)
	{
		int pos = first;
		for (int index = 0; index < size; index++)
			if(backing_store[bound(pos++)] == item)
				return true;

		return false;
	}

	/**
	 * Will determine if item exists in the array
	 * 
	 * @return true if item exists in the array, false otherwise
	 */
	public boolean contains_recursive(int item)
	{
		return check(item, first);
	}
	
	/**
	 * A helper method for contains_recursive(). Determines if item exists in the array.
	 * 
	 * @param item	the element in the array
	 * @param pos	the position in the array
	 * @return		true if item exists in the array, false otherwise
	 */
	private boolean check(int item, int pos)
	{
		if (backing_store[pos] == item)
			return true;
		if (pos == last)
			return false;

		return check(item, bound(pos + 1));
	}

	/**
	 * Returns the first element of the array
	 * 
	 * @return 							the first element in the array
	 * @throws NoSuchElementException	if this list is empty
	 */
	public int get_first() throws NoSuchElementException
	{
		if (size == 0)
			throw new NoSuchElementException();
		
		return backing_store[first];
	}

	/**
	 * Returns the last element of the array
	 * 
	 * @return 							the last element in the array
	 * @throws NoSuchElementException	if this list is empty
	 */
	public int get_last() throws NoSuchElementException
	{
		if (size == 0)
			throw new NoSuchElementException();

		return backing_store[last];
	}

	/**
	 * Removes the first element of the array and returns the value at that position
	 * 
	 * @return 							the value at the first element
	 * @throws NoSuchElementException	if this list is empty
	 */
	public int remove_first() throws NoSuchElementException
	{
		if (size == 0)
			throw new NoSuchElementException();

		Integer val = backing_store[first];
		first = bound(++first);
		size--;
		
		return val;
	}

	/**
	 * Removes the last element of the array and returns the value at that position
	 * 
	 * @return 							the value at the last element
	 * @throws NoSuchElementException	if this list is empty
	 */
	public int remove_last() throws NoSuchElementException
	{
		if (size == 0)
			throw new NoSuchElementException();

		Integer val = backing_store[last];
		last = bound(--last);
		size--;
		
		return val;
	}

	/**
	 * Returns the size of the array
	 * 
	 * @return the size of the array
	 */

	public int size()
	{
		return size;
	}

	/**
	 * Puts all the elements in the array in reverse order
	 */
	public void reverse()
	{
		if(size == 0)
			return;
		
		int right = first;
		int left = last;
		int temp = 0;
		int count = 0;
		while(count < size)
		{
			temp = backing_store[right];
			backing_store[right] = backing_store[left];
			backing_store[left] = temp;
			count = count + 2;
			right++;
			left--;
		}
	}

	/**
	 * This method for the array list is not required to implement.
	 * Instead this method returns the stored size of the array.
	 * 
	 * @return the size of the array
	 */

	public int compute_size_recursive()
	{
		return size;
	}

	/**
	 * Recursively builds and returns an ArrayList of the data in reverse order
	 * 
	 * @return an ArrayList of this data structure in reverse order
	 */
	public ArrayList<Integer> to_ArrayList_post_recurse()
	{
		ArrayList<Integer> list = new ArrayList<>();
		add_to_ArrayList(list, first);
		list.add(backing_store[first]);
		
		return list;
	}
	
	/**
	 * A helper method for to_ArrayList_post_recurse(), adds elements
	 * to the list passed in by to_ArrayList_post_recurse(). 
	 * 
	 * @param list	the ArrayList passed in by to_ArrayList_post_recurse()
	 * @param pos	the position in the virtual array
	 */
	private void add_to_ArrayList(ArrayList<Integer> list, int pos)
	{
		if (pos == last)
			return;
		
		pos = bound(++pos);
		add_to_ArrayList(list, pos);
		list.add(backing_store[pos]);
	}

	/**
	 * Builds and returns an ArrayList of the data in order
	 * 
	 * @return an ArrayList of this data structure
	 */
	public ArrayList<Integer> to_ArrayList()
	{
		ArrayList<Integer> list = new ArrayList<>();

		int pos = first;
		for (int index = 0; index < size; index++)
			list.add(backing_store[bound(pos++)]);
		
		return list;
	}
	
	/**
	 * Takes in a position and returns a bounded position determined
	 * by the backing store array length. Behaves like the mathematical mod.
	 * 
	 * e.g. -1 % array.length = array.length - 1
	 * 
	 * @param pos	the position in the virtual array
	 * @return		a wrapped position
	 */
	private int bound(int pos)
	{
		return (pos % backing_store.length + backing_store.length) % backing_store.length;
	}
	
	/**
	 * Return a string representation of the array list.
	 * e.g {2, 4, 5, 8}
	 * 
	 * @return return a string representation of the array list
	 */
	public String toString()
	{
		if (size == 0)
			return "{}";

		String array_list_str = "{" + backing_store[first];
		
		int pos = first + 1;
		for (int iter = 1; iter < size; iter++)
			array_list_str += ", " + backing_store[bound(pos++)];
		
		return array_list_str + "}";
	}
}
