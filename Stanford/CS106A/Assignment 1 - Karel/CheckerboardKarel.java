/*
 * File: CheckerboardKarel.java
 * Name: Julianne Crawford
 * Section Leader: Peter Maldonado
 * ----------------------------
 * Purpose: The CheckerboardKarel subclass creates a checkerboard 
 * pattern in Karel's world using beepers. The problem is decomposed
 * into two primary methods: checkerFirstColumn and checkerRows. 
 * Together these methods solve the algorithmic problem. Karel moves 
 * down the first column, and at each corner moves across the corresponding
 * row. If the row is odd, Karel assigns an "odd checker sequence", and 
 * if the row is even, Karel assigns an "even checker sequence". The 
 * solution is general enough to work on any size world. 
 */

import stanford.karel.*;

public class CheckerboardKarel extends SuperKarel {

	public void run() {
		checkerFirstColumn();
		checkerRows();
	}
	
	/* Method: checkerFirstColumn
	 * -------------------------	 
	 * Assigns an alternating checker pattern to the first column of 
	 * Karel's world
	 * Precondition: Karel is facing east at the corner of 1st street 
	 * and 1st avenue 
	 * Postcondition: Karel is facing north at the top of 1st avenue 
	 * and has placed alternating beepers to create a checker pattern
	 * along 1st avenue 
	 */
	private void checkerFirstColumn() {
		turnLeft();
		checkerOddColumns();
	}
	
	/* Method: checkerOddColumns
	 * -------------------------
	 * Karel places beepers to create a checker pattern along the length 
	 * of the odd number avenues (columns) 
	 * Precondition: Karel is facing north at the base of an odd column (avenue)
	 * Postcondition: Karel is facing north at the top of an odd column (avenue) 
	 * and has put beepers in alternating corners along the length of the column.
	 */
	private void checkerOddColumns() {
		putBeeper();
		oddCheckerSequence();
	}
	
	/* Method: checkerRows
	 * -------------------------
	 * Karel moves down the first column (avenue), and at each corner 
	 * determines whether the row (street) is odd or even. If the row 
	 * is odd, Karel assigns an odd checker sequence, and if the row
	 * is even, Karel assigns an even checker sequence
	 * Precondition: Karel is facing north at the top of 1st avenue 
	 * Postcondition: Karel is facing east at the south-east corner 
	 * of Karel's world and has placed beepers along its way to  
	 * create a checkered pattern in its world. 
	 */
	private void checkerRows(){
		turnAround();
		while(frontIsClear()) {
			turnLeft();
			determineOddOrEvenRow();
			returnToFirstColumn();
			moveDownColumn();
		}
		turnLeft();
		determineOddOrEvenRow();
	}

	/* Method: determineOddOrEvenRow
	 * -------------------------
	 * As Karel moves down the first column (avenue), it checks whether a 
	 * beeper is present in order to determine whether it is at the start 
	 * of an odd or even row. Depending on this determination, Karel assigns
	 * an odd or even checker sequence to the row. 
	 */
	private void determineOddOrEvenRow() {
		if(beepersPresent()) {
			oddCheckerSequence();
		}
		else {
			evenCheckerSequence();
		}
	}
	
	/* Method: oddCheckerSequence
	 * -------------------------
	 * A sequence Karel follows for placing beepers to create a checker pattern 
	 * for odd rows and columns 
	 */
	private void oddCheckerSequence() {
		while(frontIsClear()) {
			move();
			if(frontIsClear()) {
				move();
				putBeeper();
			}
		}
	}
	
	/* Method: evenCheckerSequence
	 * -------------------------
	 * A sequence Karel follows for placing beepers to create a checker pattern 
	 * for even rows and columns 
	 */
	private void evenCheckerSequence() {
		while(frontIsClear()) {
			move();
			putBeeper();
			if(frontIsClear()) {
				move();
			}
		}
	}
	
	/* Method: returnToFirstColumn
	 * -------------------------
	 * Karel turns around and returns to the first column (1st avenue) 
	 * Precondition: Karel is facing east and in a square in the last column 
	 * Postcondition: Karel is facing south and in a square in the first 
	 * column (1st avenue) 
	 */
	private void returnToFirstColumn() {
		turnAround();
		moveAcrossRow();
		turnLeft();
	}
	
	/* Method: moveAcrossRow
	 * -------------------------
	 * Moves Karel along a row (street) 
	 * Precondition: none
	 * Postcondition: Karel is facing the same direction as it started and is at 
	 * the opposite end of the row (street) from which it started 
	 */
	private void moveAcrossRow() {
		while(frontIsClear()) {
			move();
		}
	}
	
	/* Method: moveDownColumn
	 * -------------------------
	 * Moves Karel along a column (avenue) 
	 * Precondition: none
	 * Postcondition: Karel is facing the same direction as it started and is at 
	 * the opposite end of the column (avenue) from which it started 
	 */
	private void moveDownColumn() {
		if(frontIsClear()) {
			move();
		}
	}

}

