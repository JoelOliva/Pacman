package com.pacman;

/**
 * Created by Joel on 6/18/17.
 */
public class StringProcessor
{
	private String data;
	private int pos;

	public StringProcessor(String data)
	{
		this.data = data;
	}

	public String readLine()
	{
		if (pos == data.length())
			return null;

		StringBuilder line = new StringBuilder();
		char currentChar = data.charAt(pos);
		pos++;

		while (currentChar != 10)
		{
			line.append(currentChar);
			currentChar = data.charAt(pos);
			pos++;
		}

		if (line.charAt(line.length() - 1) == 10 || line.charAt(line.length() - 1) == ' ')
			line.deleteCharAt(line.length() - 1);

		return line.toString();
	}

	public String next()
	{
		if (pos == data.length())
			return null;

		StringBuilder word = new StringBuilder();
		char currentChar = data.charAt(pos);
		pos++;

		while (currentChar != 10 && currentChar != ' ')
		{
			word.append(currentChar);
			currentChar = data.charAt(pos);
			pos++;
		}

		return word.toString();
	}
}
