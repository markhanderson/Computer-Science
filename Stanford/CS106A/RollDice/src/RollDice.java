/**
 * 
 */

/**
 * @author markh
 *
 */

import acm.program. *;
import acm.util.*;

public class RollDice extends ConsoleProgram {

	/**
	 * File: RollDice.java
	 * 
	 * This program simulates rolling some number of dice until the maximal value on all  the dice is rolled
	 * 
	 */
	
	
	public static void run() {
		int numDice = readInt("Number of dice: ");
		int maxRoll = numDice * NUM_SIDES;
		int numRolls = 0;
		while (true) {
			int roll = rollDice(numDice);
			numRolls++;
			if (roll == maxRoll) break;
			println("Rolled ' + roll);");
			
		}
		println("Rolled " + roll);
		
		// TODO Auto-generated method stub

	}
	private int rollDice(int numDice) {
		int total = 0:
		for (int i = 0; i < numDice; i++) {
			total += rgen.nextInt(1, NUM_SLIDES)
		}
	}
	return total;
}
