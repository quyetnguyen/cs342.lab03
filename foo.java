import java.io.*;
import java.util.*;

public class foo
{
	public static void main (String[] args)
	{

		/* TESTS FOR PARSER */
		GeneBankParser gbParser = new GeneBankParser("test2.gbk", 7);
		
		String currLine = gbParser.nextSequence();

		while (!currLine.equals("EOF"))
		{
			System.out.println(currLine);

			//System.out.println(gbParser.longizeSequence(currLine));

			//System.out.println(gbParser.stringizeSequence(gbParser.longizeSequence(currLine)));

			currLine = gbParser.nextSequence();
		}

		

		/*try
		{
			RandomAccessFile test = new RandomAccessFile("test", "rw");
			test.writeChars("ahljelj");
			test.seek(0);
			System.out.println(test.readChar());
			System.out.println(test.readChar());

			
		} catch (FileNotFoundException e)
		{

		}
		catch (IOException e)
		{
			System.out.println("ERROR");
		}

*/

	}
}