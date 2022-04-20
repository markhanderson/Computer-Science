/*
 * File: Breakout.java
 * Name: Julianne Crawford
 * Section Leader: Peter Maldonado
 * --------------------------------
 * Purpose: This program creates the classic game, Hangman.
 * A random word is selected from an imported list of words. 
 * Players are given 7 guesses to decipher the word one letter 
 * at a time. Each time the player guesses incorrectly, a string
 * on Karel's parachute breaks. When all 7 strings break, Karel 
 * is "hung". The partially guessed word is displayed for the 
 * player to see as well as the letters the player has already 
 * guessed.  
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.io.*;
import java.util.ArrayList;
import java.util.*; 

public class Hangman extends ConsoleProgram {

	/***********************************************************
	 *              CONSTANTS                                  *
	 ***********************************************************/
	
	/* The number of guesses in one game of Hangman */
	private static final int N_GUESSES = 7;
	
	/* The width and the height to make the karel image */
	private static final int KAREL_SIZE = 150;
	
	/* The y-location to display karel */
	private static final int KAREL_Y = 230;
	
	/* The width and the height to make the parachute image */
	private static final int PARACHUTE_WIDTH = 300;
	private static final int PARACHUTE_HEIGHT = 130;
	
	/* The y-location to display the parachute */
	private static final int PARACHUTE_Y = 50;
	
	/* The y-location to display the partially guessed string */
	private static final int PARTIALLY_GUESSED_Y = 430;
	
	/* The y-location to display the incorrectly guessed letters */
	private static final int INCORRECT_GUESSES_Y = 460;
	
	/* The fonts of both labels */
	private static final String PARTIALLY_GUESSED_FONT = "Courier-36";
	private static final String INCORRECT_GUESSES_FONT = "Courier-26";
	
	/***********************************************************
	 *              Instance Variables                         *
	 ***********************************************************/
	
	private RandomGenerator rg = new RandomGenerator();
	private GCanvas canvas = new GCanvas();
	private int guessesLeft; 
	private String word = ""; 
	private String guessString = "";
	private GLine line = null; 
	private GImage karel = null; 
	private 	ArrayList<GLine> karelStrings = new ArrayList<GLine>(); 
	boolean toggle = true; 
	private ArrayList<String> wordList = new ArrayList<String>(); 
	private GLabel partiallyGuessed = null; 
	private GLabel incorrectlyGuessed = null; 
	private String incorrectGuess = ""; 

	/***********************************************************
	 *                    Methods                              *
	 ***********************************************************/
	public void init() {
		add(canvas); 
	}
	
	public void run() {
		word = getRandomWord();
		setUpGraphics(); 
		setUpConsole(word); 
		userGuesses(); 
	}
	
	/* Method: setUpGraphics
	 * The setUpGraphics method adds the background and images to the
	 * graphics console. 
	 */
	private void setUpGraphics() {
		GImage bg = new GImage("background.jpg"); 
		bg.setSize(canvas.getWidth(),canvas.getHeight());
		canvas.add(bg,0,0);
		
		karel = new GImage("karel.png"); 
		karel.setSize(KAREL_SIZE,KAREL_SIZE);
		canvas.add(karel,(canvas.getWidth()-karel.getWidth())/2,KAREL_Y);
		
		GImage parachute = new GImage("parachute.png"); 
		parachute.setSize(PARACHUTE_WIDTH,PARACHUTE_HEIGHT);
		canvas.add(parachute,(canvas.getWidth()-parachute.getWidth())/2,PARACHUTE_Y);
		
		double x1 = canvas.getWidth()/2;
		double y1 = KAREL_Y; 
		for(int i = 0; i <N_GUESSES; i++) {
			double x01 = canvas.getWidth()/2-parachute.getWidth()/2+i*(parachute.getWidth()/(N_GUESSES-1));
			double y01 = PARACHUTE_Y + parachute.getHeight(); 
			line = drawLine(x01,y01,x1,y1); 
			canvas.add(line); 
			karelStrings.add(line);
		}				
	}
	
	/* Method: drawLine
	 * The drawLine method draws a line between a starting and ending coordinate
	 * Parameters: 
	 * x0 - the line's starting x-coordinate
	 * y0 - the line's starting y-coordinate
	 * x1 - the line's ending x-coordinate
	 * y1 - the line's ending y-coordinate 
	 */
	private GLine drawLine(double x0, double y0, double x1, double y1) {
		GLine line = new GLine(x0,y0,x1,y1); 
		return(line); 
	}
	
	/* Method: setUpConsole
	 * ------------------
	 * The setUpConsole method sets up the window and randomly selects
	 * a word to use for the hangman game, showing the user how many 
	 * letters it contains. 
	 */
	private void setUpConsole(String word) {
		println("Welcome to Hangman"); 
		guessString = ""; 
		for(int i = 0; i < word.length(); i++) {
			guessString += "-"; 
		}
		println("Your word now looks like this: " + guessString); 
		
		partiallyGuessed = addLabel(guessString,PARTIALLY_GUESSED_FONT);
		canvas.add(partiallyGuessed); 
		partiallyGuessed.move(0,PARTIALLY_GUESSED_Y);
	}
	
	/* Method: userGuesses
	 * --------------------
	 * The userGuesses method reads user inputs, determines whether the guess
	 * is correct or incorrect, and keeps track of the number of remaining guesses.   
	 */
	private void userGuesses() {
		guessesLeft = N_GUESSES; 
		while(guessesLeft > 0) {
			println("You have " + guessesLeft + " guesses left." + '\n'); 
			String yourGuess = readLine("Your guess: ");
			if(yourGuess.length() > 1) {
				println("That is an invalid guess. Please guess a single letter"); 
			} else {
				guessString = checkLetter(yourGuess.toUpperCase(),guessString);
				canvas.remove(partiallyGuessed);
				partiallyGuessed = addLabel(guessString,PARTIALLY_GUESSED_FONT);
				canvas.add(partiallyGuessed); 
				partiallyGuessed.move(0,PARTIALLY_GUESSED_Y); 
				if(guessesLeft > 0) {
					println("Your word now looks like this: " + guessString); 
				}
			}
		}
	}
	
	/* Method: checkLetter
	 * -------------------
	 * The checkLetter method compares the user input to the letters in the 
	 * randomly generated word. If the guess is incorrect, the number of 
	 * guesses is decreased accordingly. 
	 */
	private String checkLetter(String yourGuess, String guessString) {
		char yourGuessCh = yourGuess.charAt(0); 
		String updatedGuessString = ""; 

		for(int i = 0; i < word.length(); i++) {
			char guessStringCh = guessString.charAt(i);
			char wordCh = word.charAt(i);  
			if(yourGuessCh == wordCh) {
				guessStringCh = yourGuessCh; 
			}
			updatedGuessString += guessStringCh; 
		}
		
		boolean correctCheck = guessString.equals(updatedGuessString);  
		if(correctCheck) {
			println("There are no " + yourGuessCh + "'s in the word.");			
			cutStrings(); 
			incorrectGuess +=  yourGuessCh; 
			guessesLeft -= 1; 
			if(incorrectlyGuessed != null) {
				canvas.remove(incorrectlyGuessed);
			}
			incorrectlyGuessed = addLabel(incorrectGuess,INCORRECT_GUESSES_FONT);
			canvas.add(incorrectlyGuessed); 
			incorrectlyGuessed.move(0,INCORRECT_GUESSES_Y);
			if(guessesLeft == 0) {
				flipKarel(); 
				println("You're completely hung."); 
				println("The word was: " + word); 
			}
		} else {
			println("That guess is correct");
			boolean winCheck = updatedGuessString.equals(word); 
			if(winCheck) {
				println("You win!");
				println("The word was: " + word); 
				guessesLeft = 0; 
			}
		}
		return (updatedGuessString);
	}

	/* Method: filpKarel
	 * The flipKarel method replaces the regular Karel image with an image
	 * of Karel faced upside down.
	 */
	private void flipKarel() {
		canvas.remove(karel);
		GImage karelFlipped = new GImage("karelFlipped.png"); 
		karelFlipped.setSize(KAREL_SIZE,KAREL_SIZE);
		canvas.add(karelFlipped,(canvas.getWidth()-karelFlipped.getWidth())/2,KAREL_Y);
	}

	/* Method: cutStrings
	 * If the player guesses an incorrect letter, the cutStrings method 
	 * removes the parachute strings holding up Karel one at a time until 
	 * karel is hung
	 */
	private void cutStrings() {
		GLine cutString = null; 
		if(toggle) {
			cutString = karelStrings.get(0);
			toggle = false; 
		} else {
			cutString = karelStrings.get(karelStrings.size()-1);
			toggle = true; 
		}
		karelStrings.remove(cutString); 
		canvas.remove(cutString);
	}

	/* Method: addLabel
	 * The addLabel method returns a message to the screen, centered in 
	 * the x-direction and at the top of the screen in the y-direction. 
	 */
	private GLabel addLabel(String message, String font) {
		GLabel label = new GLabel(message);
		label.setFont(font);
		double labelX = (canvas.getWidth()-label.getWidth())/2; 
		double labelY = 0;
		label.setLocation(labelX,labelY);
		return label; 
	}
	
	/* Method: getRandomWord
	 * -------------------------
	 * The getRandomWord method returns a word to use in the hangman game.
	 * It randomly selects this word from and imported list of words 
	 */
	private String getRandomWord() {
		try {
			Scanner HangmanLexicon = new Scanner(new File("HangmanLexicon.txt"));
			while(HangmanLexicon.hasNextLine()) {
				wordList.add(HangmanLexicon.nextLine()); 
			}
			HangmanLexicon.close();
		} catch (IOException e) {
			throw new ErrorException("getWord: Illegal index");
		}
		int index = rg.nextInt(0,wordList.size()); 
		String randomWord = wordList.get(index); 
		return randomWord; 
	}	
	
}
