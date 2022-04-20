
/*
 * File: CollectNewspaperKarel.java
 * Name: Julianne Crawford
 * Section Leader: Peter Maldonado
 * --------------------------------
 * Purpose: The CollectNewspaperKarel subclass defines a "run"
 * method with three commands. These commands instruct Karel  
 * to walk to the door of its house, pick up the newspaper
 * (represented by a beeper), and then return to its initial
 * position in the upper west corner of the house. 
 */

import stanford.karel.*;

public class CollectNewspaperKarel extends SuperKarel {
	
	public void run() {
		moveToNewspaper();
		pickUpNewspaper();
		ReturnHome();
	}
	
	/* Method: moveToNewspaper
	 * Makes Karel move to the location of the newspaper outside of
	 * the doorway 
	 * Precondition: Karel is in the upper west corner of the house
	 * Postcondition: Karel is facing east, standing outside of the  
	 * doorway in the corner where the newspaper (beeper) is located.
	 */
	private void moveToNewspaper() {
		move();
		move();
		turnRight();
		move();
		turnLeft();
		move();
	}
	
	/* Method: pickUpNewspaper
	 * Makes Karel pick up the newspaper (beeper) 
	 * Precondition: Karel is standing outside of the doorway 
	 * (facing the same way in which she started) in the corner where
	 * the newspaper (beeper) is located
	 * Postcondition: Karel remains in the corner outside of the doorway, 
	 * but the corner no longer has a newspaper (beeper). Karel has
	 * picked it up.
	 */
	private void pickUpNewspaper() {
		pickBeeper();
	}
	
	/* Method: ReturnHome
	 * Makes Karel turn around and return to its initial position 
	 * Precondition: Karel is in the corner outside of the doorway. 
	 * Karel has the newspaper (beeper) in its beeperbag.
	 * Postcondition: Karel is standing in its initial position in 
	 * the upper west corner of the house. 
	 */
	private void ReturnHome() {
		turnAround();
		move();
		turnRight();
		move();
		turnLeft();
		move();
		move();
		turnAround();
	}
	
}
