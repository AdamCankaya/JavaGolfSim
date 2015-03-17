/**********************************************************************************************
 * Program: Golf.java
 * Author: Adam Cankaya (adamcankaya@gmail.com)
 * Date: June 9, 2013
 * Description: A golf game between three players with random course pars and player scores.
 *				First the program generates two input files representing the course pars
 * 				and each player's score at each hole. The program then reads in two text files 
 * 				representing a list of par scores for an 18 hole (by default) golf course and 
 * 				scores for 3 players. The game calculates the course par total, each player's 
 * 				course par total, and prints a winner with the lowest total score. The over/under 
 * 				par score overall and for each individual player at each hole is also printed.
 * Input: Two text files. Course.txt has 18 lines, each with an int representing the hole's par.
 * 		  Scores.txt has 19 lines, the first line is an int representing the number of players
 * 		  (should be 3 by default), the other 18 lines should contain 3 ints each, separated by
 * 		  commas, representing the score for each player on each hole.
 * Output: A table of the course pars, player scores, over/under par score for each hole,
 * 		   and course & player totals. A winner is printed at the end. The results are then
 * 		   printed to a .out file.
 * Reference: Help with working out some bugs from the TA, Reed!
 * 
 * Extra Credit: 1) accepts two command line arguments for the two input file names
 * 				 2) prints results to a .out file with name of the two input files hyphenated
 * 				 3) generates random course pars & player scores and writes to the input files
 * 				 4) number of holes on the course can be arbitrarily set, default of 18
 * 				 5) number of holes is randomly generated each time program runs from 1-100 inclusive
 **********************************************************************************************/

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Golf {

	static int numOfHoles = (1 + (int)(Math.random() * (100 - 1) + 1));		// random number of holes mode!
	
	//static int numOfHoles = 18;					// number of holes on the course - default of 18
	static int[] coursePars = new int[numOfHoles];	// array of int for each hole's par score in an 18 hole course
	static int courseParTotal;						// total of all pars on the course
	static int numPlayers;							// number of players - default of 3
	static int[] playerScores;						// array of int for each player's score at each hole in an 18 hole course
	static int playerOneTotal;						// the total course score for player one
	static int playerTwoTotal;						// the total course score for player two
	static int playerThreeTotal;					// the total course score for player three
	static int winner;								// Number of winning player
	
	public static void main(String[] args) 
	{	
		RandomScores(args[0], args[1]);	//	generates random course par & player scores and writes to two input files based on command line args
		Course(args[0]);				//	1st command line arg = "course" - Sets each course hole's par in array, totals pars for course
		Player(args[1]);				//	2nd command line arg = "scores" - Sets score for each hole in arrays for each player, totals score for each player
		Scores();						//	Calculates player totals and prints table to screen for each hole's scores vs par
		Winner();						//  Compares player totals and declares a winner
		FileOut(args[0], args[1]);		//	Prints results table to a text file (course-scores.out)
	}
	
	public static void RandomScores(String course, String scores)
	{
		// first generate 18 random numbers from 1 to 10 inclusive and write as the course pars file
		try
		{
			PrintStream myFileWriter = new PrintStream(course);
			
			for(int i = 1; i <= numOfHoles; i++)
				myFileWriter.printf("%d\r\n", (1 + (int)(Math.random() * (10 - 1) + 1)));
			
			if(myFileWriter != null)	//close input to avoid potential memory leaks
			{
				myFileWriter.close();
			}
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Output file not found - please check your course and scores files.");
		}
		
		// now generate a random score from 1 to 10 inclusive for each player on each hole and write to scores file
		try
		{
			PrintStream myFileWriter = new PrintStream(scores);
			
			myFileWriter.print("3\r\n");	// prints 3 to first line of file for 3 players
			
			for(int i = 1; i <= numOfHoles; i++)
			{
				myFileWriter.printf("%d,", (1 + (int)(Math.random() * (10 - 1) + 1)));
				myFileWriter.printf("%d,", (1 + (int)(Math.random() * (10 - 1) + 1)));
				myFileWriter.printf("%d\r\n", (1 + (int)(Math.random() * (10 - 1) + 1)));
			}
			
			if(myFileWriter != null)	//close input to avoid potential memory leaks
			{
				myFileWriter.close();
			}
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Output file not found - please check your course and scores files.");
		}
	}
	
	public static void Course(String courseFileString)
	{
		File courseFile = new File(courseFileString);		// reads course pars file
		try
		{
			Scanner myFileReader = new Scanner(courseFile);
			
			for(int i = 0; i < numOfHoles; i++)					// loop to read in each hole's par score
				coursePars[i] = myFileReader.nextInt();
					
			if(myFileReader != null)						// close input to avoid potential memory leaks
				myFileReader.close();
		}
		catch(FileNotFoundException e)						// catch exception for a missing or misnamed course pars file
		{
			System.out.println("Course pars file not found - please make sure the file is named course.txt");
			System.exit(0);
		}
		catch(NumberFormatException e)						// catch exception for a non-integer entry in the course pars file
		{
			System.out.println("Non-integer scores read in the course pars file - please check your formatting.");
			System.exit(0);
		}
		catch(InputMismatchException e)						// catch exception for a non-integer entry in the course pars file
		{
			System.out.println("Non-integer scores read in the course pars file - please check your formatting.");
			System.exit(0);
		}
		catch(NoSuchElementException e)						// catch exception for a missing entry in the course pars file
		{
			System.out.println("There appears to be a missing score in the course pars file - please check your formatting.");
			System.exit(0);
		}
		
		for(int i = 0; i < numOfHoles; i++)
			courseParTotal = courseParTotal + coursePars[i];
	}

	public static void Player(String playerFileString)
	{
		File playerFile = new File(playerFileString);	// reads player scores file
		
		try
		{
			Scanner myFileReader = new Scanner(playerFile);
			
			numPlayers = myFileReader.nextInt();		// reads number of players on first line of file - should be 3
			myFileReader.nextLine();					// advances Scanner to next line
			
			if(numPlayers != 3)							// checks to make sure number of players properly set to 3
				System.out.println("There appears to be a number of players besides 3 in your player scores file - " +
						"please check your file formatting or otherwise be advised that this may affect the game calculations.");
			
			int[] playerScoresArray = new int[numPlayers*numOfHoles];	// array of ints for each player's score on each hole
		
			for(int i = 0; i < (numPlayers*numOfHoles); i = i+numPlayers)
			{
				String line = myFileReader.nextLine();
				
				String[] lineStr = line.split(",");				// splits each line into multiple strings and assigns to array
				int[] lineInt = new int[lineStr.length];		// new array of ints for each line's scores
			
				for(int j = 0; j < lineStr.length; j++)			// loop that parses each line String and assigns to array of ints
				{
					lineInt[j] = Integer.parseInt(lineStr[j]);
					playerScoresArray[i+j] = lineInt[j];
				}
			}
					
			if(myFileReader != null)		// close input to avoid potential memory leaks
				myFileReader.close();
			
			playerScores = playerScoresArray;	// assigns player scores array to global array variable for access in other classes
		}
		catch(FileNotFoundException e)			// catch exception for a missing or misnamed player scores file
		{
			System.out.println("Player scores file not found - please make sure the file is named scores.txt");
			System.exit(0);
		}
		catch(NumberFormatException e)			// catch exception for a non-integer entry in the player scores file
		{
			System.out.println("Non-integer scores read in the Player scores file - please check your formatting.");
			System.exit(0);
		}
		catch(InputMismatchException e)			// catch exception for a non-integer entry in the player scores file
		{
			System.out.println("Non-integer scores read in the Player scores file - please check your formatting.");
			System.exit(0);
		}
		catch(NoSuchElementException e)			// catch exception missing score(s) in the player scores file
		{
			System.out.println("There appears to be a missing score in the Player scores file - please check your formatting.");
			System.exit(0);
		}
	}

	public static void Scores()
	{
		int holeNum = 1;	// counter variable for the hole number being printed
		
		for(int i = 0; i < (numPlayers*numOfHoles); i++)	// checks for missing player score entries
			if(playerScores[i] == 0)
			{
				System.out.println("There appears to be a missing score - please double check your player scores file.");
				System.exit(0);
			}
		
		System.out.println("Hole # \t Par    Player 1 Score  Player 2 Score  Player 3 Score");
		
		for(int i = 0; i < (numPlayers*numOfHoles); i = i + numPlayers)			// sums and prints each player's total scores
		{
				if(i % 3 != 0)
					courseParTotal = courseParTotal + coursePars[holeNum-1];
				playerOneTotal = playerOneTotal + playerScores[i];
				playerTwoTotal = playerTwoTotal + playerScores[i+1];
				playerThreeTotal = playerThreeTotal + playerScores[i+2];
				System.out.printf("%d \t", holeNum);
				System.out.printf(" %d \t", coursePars[holeNum-1]);
				System.out.printf("%d (%+d)\t\t", playerScores[i], playerScores[i]-coursePars[holeNum-1]);
				System.out.printf("%d (%+d)\t\t", playerScores[i+1], playerScores[i+1]-coursePars[holeNum-1]);
				System.out.printf("%d (%+d)\n", playerScores[i+2], playerScores[i+2]-coursePars[holeNum-1]);
				holeNum++;
		}
		
		System.out.printf("TOTAL \t%d \t", courseParTotal);
		System.out.printf("%d (%+d) \t", playerOneTotal, playerOneTotal-courseParTotal);
		System.out.printf("%d (%+d) \t", playerTwoTotal, playerTwoTotal-courseParTotal);
		System.out.printf("%d (%+d)\n", playerThreeTotal, playerThreeTotal-courseParTotal);
		
		
	}

	public static void Winner()
	{	
		String overUnder = "under";
		
		if( (playerOneTotal < playerTwoTotal) && (playerOneTotal < playerThreeTotal) )	// checks if Player 1 is the winner
		{
			winner = 1;
			if(playerOneTotal > courseParTotal)
				overUnder = "over";
			System.out.printf("\nPlayer #%d is the winner with a score of %d %s par!", winner, Math.abs(playerOneTotal-courseParTotal), overUnder);
		}
		else if( (playerTwoTotal < playerOneTotal) && (playerTwoTotal < playerThreeTotal) )	// checks if Player 2 is the winner
		{
			winner = 2;
			if(playerTwoTotal > courseParTotal)
				overUnder = "over";
			System.out.printf("\nPlayer #%d is the winner with a score of %d %s par!", winner, Math.abs(playerTwoTotal-courseParTotal), overUnder);
		}
		else if( (playerThreeTotal < playerOneTotal) && (playerThreeTotal < playerTwoTotal) )	// checks if Player 3 is the winner
		{
			winner = 3;
			if(playerThreeTotal > courseParTotal)
				overUnder = "over";
			System.out.printf("\nPlayer #%d is the winner with a score of %d %s par!", winner, Math.abs(playerThreeTotal-courseParTotal), overUnder);
		}
		
		if( (playerOneTotal == playerTwoTotal) && (playerTwoTotal == playerThreeTotal) )	// checks if there is a draw between Player 1, 2 & 3 
		{
			if(playerOneTotal > courseParTotal)
				overUnder = "over";
			System.out.printf("\nIts a three way tie for first place with a score of %d %s par!", Math.abs(playerOneTotal-courseParTotal), overUnder);
		}
		else if(playerOneTotal == playerTwoTotal)	// checks if there is a draw between Player 1 & 2
		{
			if(playerOneTotal > courseParTotal)
				overUnder = "over";
			System.out.printf("\nPlayer one and two tied for first place with a score of %d %s par!", Math.abs(playerOneTotal-courseParTotal), overUnder);
		}
		else if(playerTwoTotal == playerThreeTotal)	// checks if there is a draw between Player 2 & 3
		{
			if(playerTwoTotal > courseParTotal)
				overUnder = "over";
			System.out.printf("\nPlayer two and three tied for first place with a score of %d %s par!", Math.abs(playerThreeTotal-courseParTotal), overUnder);
		}
		else if(playerOneTotal == playerThreeTotal)	// checks if there is a draw between Player 1 & 3
		{
			if(playerOneTotal > courseParTotal)
				overUnder = "over";
			System.out.printf("\nPlayer one and two tied for first place with a score of %d %s par!", Math.abs(playerThreeTotal-courseParTotal), overUnder);
		}
	}

	public static void FileOut(String course, String scores)
	{
		String outputFileName = course + "-" + scores + ".out";
		int holeNum = 1;
		playerOneTotal = 0;
		playerTwoTotal = 0;
		playerThreeTotal = 0;
		
		try
		{
			PrintStream myFileWriter = new PrintStream(outputFileName);
			
			myFileWriter.println("Hole # \t Par    Player 1 Score  Player 2 Score  Player 3 Score");
			
			for(int i = 0; i < (numPlayers*numOfHoles); i = i + numPlayers)			// sums and prints each player's total scores
			{
					if(i % 3 != 0)
						courseParTotal = courseParTotal + coursePars[holeNum-1];
					playerOneTotal = playerOneTotal + playerScores[i];
					playerTwoTotal = playerTwoTotal + playerScores[i+1];
					playerThreeTotal = playerThreeTotal + playerScores[i+2];
					myFileWriter.printf("%d \t", holeNum);
					myFileWriter.printf(" %d \t", coursePars[holeNum-1]);
					myFileWriter.printf("%d (%+d)\t\t", playerScores[i], playerScores[i]-coursePars[holeNum-1]);
					myFileWriter.printf("%d (%+d)\t\t", playerScores[i+1], playerScores[i+1]-coursePars[holeNum-1]);
					myFileWriter.printf("%d (%+d)\r\n", playerScores[i+2], playerScores[i+2]-coursePars[holeNum-1]);
					holeNum++;
			}
			
			myFileWriter.printf("TOTAL \t%d \t", courseParTotal);
			myFileWriter.printf("%d (%+d) \t", playerOneTotal, playerOneTotal-courseParTotal);
			myFileWriter.printf("%d (%+d) \t", playerTwoTotal, playerTwoTotal-courseParTotal);
			myFileWriter.printf("%d (%+d)\r\n", playerThreeTotal, playerThreeTotal-courseParTotal);
		
			if(myFileWriter != null)	//close input to avoid potential memory leaks
			{
				myFileWriter.close();
			}
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Output file not found - please check your course and scores files.");
		}
	}

}
