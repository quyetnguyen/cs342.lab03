import java.io.*;
import java.util.*;
import java.util.Scanner;

public class GeneBankParser

{
	int sequenceLength;
	Scanner gbkScanner;
	int lineIndex;
	String currLine;


	public GeneBankParser(String fileName, int sequenceLength)
	{
		this.sequenceLength = sequenceLength;
		this.currLine = "";

		try 
		{
			File gbkFile = new File(fileName);
			gbkScanner = new Scanner(new FileReader(gbkFile));
		}
		catch (FileNotFoundException e)
		{
			System.out.printf("Error, file: %s does not exist.\n", fileName);
		}

		try 
		{
			skipJunk();
			nextBlock();
		}
		catch (NoSuchElementException e)
		{
			System.out.printf("Error, file: %s contains no data.\n", fileName);
		}

	}

	private void skipJunk() throws NoSuchElementException
	{
		try{
			while( !gbkScanner.next().equals("ORIGIN"))
			{
			}
			//gbkScanner.nextLine();
		} catch (NoSuchElementException e){
			currLine = "EOF";
			return;
		}
	}

	public String stringizeSequence(Long longSequence)
	{
		String binaryString = Long.toBinaryString(longSequence);

		while (binaryString.length() != sequenceLength *2)
		{
			binaryString = "0" + binaryString;
		}


		StringBuilder stringSequence = new StringBuilder();

		for (int i = 0; i < binaryString.length()-1; i +=2)
		{
			String subSequence = binaryString.substring(i, i+2);

			if (subSequence.equals("00")) stringSequence.append("A");
			if (subSequence.equals("11")) stringSequence.append("T");
			if (subSequence.equals("01")) stringSequence.append("C");
			if (subSequence.equals("10")) stringSequence.append("G");
		}

		return stringSequence.toString();
	}

	public long longizeSequence(String stringSequence)
	{
		StringBuilder binarySequence = new StringBuilder();

		for (int i = 0; i < stringSequence.length(); i++)
		{
			switch (stringSequence.charAt(i))
			{
				case 'A':
					binarySequence.append("00");
					break;
				case 'T':
					binarySequence.append("11");
					break;
				case 'C':
					binarySequence.append("01");
					break;
				case 'G':
					binarySequence.append("10");
					break;	
			}
		}

		return Long.parseLong(binarySequence.toString(), 2);
	}

	public String nextSequence()
	{
		StringBuilder nextSequence = new StringBuilder();


		//if(lineIndex >= currLine.length()) nextBlock();
/*
		if (currLine.equals("//") || currLine.equals(""))
		{
			try 
			{
				skipJunk();
				nextBlock();
			}
			catch (NoSuchElementException e)
			{
				return "EOF";
			}
		}*/

				int tempIndex = lineIndex;


		while (nextSequence.length() < sequenceLength)
		{
		
			if((lineIndex + sequenceLength) > currLine.length()){
				skipJunk();
				
				if(currLine.equals("EOF")) return "EOF";
				
				nextBlock();
			}

			char tempChar = currLine.charAt(tempIndex);

			switch (tempChar)
			{
				case 'A':
				case 'a':
				case 'T':
				case 't':
				case 'C':
				case 'c':
				case 'G':
				case 'g':
					nextSequence.append(Character.toUpperCase(tempChar));
					tempIndex++;
					break;
				case 'N':
				case 'n':
					lineIndex++;
					nextSequence();
					break;
			}
		}
		lineIndex++;	
		return nextSequence.toString();
	}

	private void nextBlock() throws NoSuchElementException
	{
		lineIndex = 0;

		String tempLine = gbkScanner.nextLine();
		while(!tempLine.equals("//"))
		{
			currLine += tempLine;
			tempLine = gbkScanner.nextLine();
			System.out.println(tempLine);

		}
		currLine = currLine.replaceAll("[^a-zA-Z]","");
		System.out.println(currLine);
	}



	private void nextLine() throws NoSuchElementException
	{
			lineIndex = 0;

			currLine = gbkScanner.nextLine();

			if (currLine.equals("\\"))
			{
				skipJunk();
				currLine = gbkScanner.nextLine();
			}

			currLine = currLine.replaceAll("[^a-zA-Z]","");
	}
}