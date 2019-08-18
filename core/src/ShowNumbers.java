import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ShowNumbers
{
	public static void main(String[] args) throws IOException
	{
		if (args.length != 1)
		{
			System.out.println("Expected a filename");
			System.exit(0);
		}

		BufferedReader file = null;
		try
		{
			file = new BufferedReader(new FileReader(args[0]));
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		file.readLine();

		int count = 0;
		String currentLine;
		while ((currentLine = file.readLine()) != null)
		{
			for (int i = 0; i < currentLine.length(); i++)
			{
				if (currentLine.charAt(i) == ' ')
				{
					if (count < 10)
						System.out.print(" " + count + " ");
					else
						System.out.print(count + " ");
					count++;
				}
				else if (currentLine.charAt(i) == 'G')
				{
					System.out.print(count + " ");
					count++;
				}
				else if (currentLine.charAt(i) == 'g')
				{
					System.out.print(count + " ");
					count++;
				}
				else if (currentLine.charAt(i) == 'S')
				{
					System.out.print(count + " ");
					count++;
				}
				else if (currentLine.charAt(i) == 'I')
				{
					System.out.print(count + " ");
					count++;
				}
				else
					System.out.print(" X ");
			}
			System.out.println();
		}
		file.close();
	}
}
