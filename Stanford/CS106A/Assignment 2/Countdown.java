/*
 * File: Countdown.java
 * Name: Julianne Crawford
 * Section Leader: Peter Maldonado
 * ----------------------
 * Purpose: this program creates a count-down call for
 * a spaceship that is about to launch. Specifically, it
 * counts down from 10 to 1 and subsequently indicates
 * liftoff. The program can be altered to count down from
 * any start time.  
 */

import acm.program.*;

public class Countdown extends ConsoleProgram {

	/** Countdown starting value */
	private static final int START = 10;

	// Initiate countdown  
	public void run() {
		for(int countDown = START; countDown > 0;countDown--) {
			println(countDown);
		}
		println("Liftoff");
	}
}

