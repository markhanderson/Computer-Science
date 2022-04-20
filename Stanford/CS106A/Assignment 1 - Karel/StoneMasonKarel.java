
/*
 * File: StoneMasonKarel.java
 * Name: Julianne Crawford
 * Section Leader: Peter Maldonado
 * --------------------------
 * Purpose: The StoneMasonKarel subclass solves the
 * "repair the quad" problem by decomposing it into
 * three primary methods: checkColumn, returnToBase, and 
 * moveToNextColumn. Together, these methods serve to 
 * add stones (represented by beepers) to columns that are 
 * missing stones in the quad (represented by Karel's world).
 * The solution is general enough to handle any number of 
 * quad arch scenarios. 
 */

import stanford.karel.*;

public class StoneMasonKarel extends SuperKarel {

	public void run() {
		while(frontIsClear()) {
			checkColumn();
			returnToBase();
			moveToNextColumn();
		}
		checkColumn();
	}
	
	/* Method: checkColumn
	 * Moves Karel along the height of a given column to assess
	 * whether or not stones (beepers) are present at each corner. 
	 * Karel places a stone (beeper) at each corner that is missing
	 * a stone.
	 * Precondition: Karel is facing east at the base of a column.
	 * Postcondition: Karel is facing north at the top of a column 
	 * and has placed stones (beepers) at each corner of the column 
	 * that did not previously have a stone.
	 */
	private void checkColumn(){
		turnLeft();
		placeMissingStone();
		moveUpColumn();
		placeMissingStone();

	}
	
	/* Method: placeMissingStone
	 * Karel places a stone at the its current corner location if it 
	 * is missing a stone.
	 * Precondition: Karel is standing on a corner of a column 
	 * with no stone (beeper) present.
	 * Postcondition: Karel has placed stones (beepers) at each 
	 * corner of the column that did not previously have a stone. 
	 */
	private void placeMissingStone() {
		if(noBeepersPresent()) {
			putBeeper();
		}
	}
	
	/* Method: moveUpColumn
	 * Moves Karel along the height of a given column until it 
	 * reaches the top of the archway and places a stone in any
	 * corner of the column that is missing a stone
	 * Precondition: Karel is facing north at the base of a column
	 * Postcondition: Karel is facing north at the top of a column 
	 * and has placed stones (beepers) at each corner of the column 
	 * that did not previously have a stone.
	 */
	private void moveUpColumn() {
		while(frontIsClear()) {
			move();
			placeMissingStone();
		}
	}
	
	/* Method: returnToBase
	 * Moves Karel from the top of a given column back to the 
	 * column's base. 
	 * Precondition: Karel is facing north at the top of a column.
	 * Postcondition: Karel is facing south at the base of the column.
	 */
	private void returnToBase() {
		turnAround();
		while(frontIsClear()) {
			move();
		}
	}
	
	/* Method: moveToNextColumn
	 * Moves Karel from the base of one column to the base of the 
	 * next column to its right
	 * Precondition: Karel is facing south at the base of a column.
	 * Postcondition: Karel is facing east at the base of the next column.
	 */
	private void moveToNextColumn() {
		turnLeft();
		for(int i=0;i<4;i++) {
			move();
		}
	}
}