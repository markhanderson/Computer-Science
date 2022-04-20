
/*
 * File: MidpointFindingKarel.java
 * Name: Julianne Crawford
 * Section Leader: Peter Maldonado
 * -------------------------------
 * Purpose: The MidpointFindingKarel subclass places a
 * Marker (represented by a beeper) at the midpoint corner
 * of 1st street in Karel's world. Specifically, the
 * problem is decomposed into four primary methods: 
 * placeStreetEndpointMarkers, narrowInOnStreetMidpoint,  
 * doubleMidpointMarker, and removeOtherMarkers. Together
 * these methods solve the algorithmic problem. If the
 * width of Karel's world is odd, Karel will place a 
 * marker at the center corner; however, if the width of 
 * Karel's world is even, Karel will place a marker in 
 * either of the two center corners. The solution is general
 * enough to work on any size world assuming that the world 
 * is at least as tall as it is wide. 
 */

import stanford.karel.*;

public class MidpointFindingKarel extends SuperKarel {

	public void run() {
		checkSpecialCases();
		if(noBeepersPresent()) { 
			placeStreetEndpointMarkers();
			narrowInOnStreetMidpoint();
			doubleMidpointMarker();
			removeOtherMarkers();
			returnToMidpoint();
		}
	}
	
	/* Method: checkSpecialCases
	 * Karel checks for the two special cases: if its world is 
	 * only 1 or 2 corners wide. 
	 * Precondition: Karel is facing east at the corner of 1st 
	 * avenue and 1st street.
	 * Postcondition: Karel is facing east at the corner of 1st
	 * avenue and 1st street and has checked if the world is 
	 * only 1 or 2 corners wide. 
	 */
	private void checkSpecialCases() {
		checkForOneCornerCase();
		if(noBeepersPresent()) {
			checkForTwoCornerCase();
		}
	}
	
	/* Method: checkForOneCornerCase
	 * Karel checks if the world is only 1 corner wide. If 
	 * this is the case, Karel places a beeper at the corner
	 * of 1st street and 1st avenue representing the midpoint
	 * of the street. 
	 * Precondition: Karel is facing east at the corner of 
	 * 1st avenue and 1st street.
	 * Postcondition: Karel is facing east at the corner of
	 * 1st avenue and 1st street and has placed a beeper 
	 * at the corner of 1st avenue and 1st street.
	 */
	private void checkForOneCornerCase() {
		if(frontIsBlocked())
			putBeeper();
	}
	
	/* Method: checkForTwoCornerCase
	 * Karel checks if the world is only 2 corners wide. If
	 * this is the case, Karel places a beeper at the corner
	 * of 1st street and 1st avenue representing the midpoint 
	 * of the street. 
	 * Precondition: Karel is facing east at the corner of 
	 * 1st avenue and 1st street.
	 * Postcondition: Karel is facing east at the corner of
	 * 1st avenue and 1st street and has placed a beeper 
	 * at the corner of 1st avenue and 1st street.
	 */
	private void checkForTwoCornerCase() {
		move();
		if(frontIsBlocked()) {
			turnAround();
			move();
			putBeeper();
			turnAround();
		}
		else {
			turnAround();
			move();
			turnAround();
		}		
	}
	
	/* Method: placeStreetEndpointMarkers
	 * Places markers (beepers) at the first and last 
	 * corners of 1st street in Karel's world 
	 * Precondition: Karel is facing east at the corner of 1st
	 * street and 1st avenue
	 * Postcondition: Karel is facing east at the final corner
	 * of 1st street and has placed beepers at this corner and
	 * the corner of 1st street and 1st avenue 
	 */
	private void placeStreetEndpointMarkers() {
		placeStartMarker();
		moveAlongStreet();
		placeEndMarker();
	}
	
	/* Method: placeStartMarker
	 * Places a marker (beeper) at the corner of 1st street and 
	 * 1st avenue 
	 * Precondition: Karel is facing east at the corner of 1st
	 * street and 1st avenue  
	 * Postcondition: beeper is placed at corner 
	 */
	private void placeStartMarker() {
		putBeeper();
	}
	
	/* Method: moveAlongStreet
	 * Moves Karel along 1st street from the first corner to the 
	 * last corner.
	 * Precondition: none 
	 * Postcondition: Karel is facing east at the final corner of 
	 * 1st street 
	 */
	private void moveAlongStreet() {
		while(frontIsClear()) {
			move(); 
		}
	}
	
	/* Method: placeEndMarker
	 * Places a marker (beeper) at the final corner of 1st street 
	 * Precondition: Karel is facing west at the final corner of
	 * 1st street
	 * Postcondition: beeper is placed at corner 
	 */
	private void placeEndMarker() {
		putBeeper();
		turnAround();
	}
	
	/* Method: narrowInOnStreetMidpoint
	 * Uses an algorithmic approach which moves Karel forward 
	 * two corners and checks whether or not a marker (beeper) 
	 * is present. If a marker is present, it indicates that the  
	 * previous corner was the street midpoint. Otherwise, Karel 
	 * keeps moving until it finds the next marker. Karel then 
	 * places a guide marker and repeats this process until it has
	 * completely narrowed in on the street midpoint. 
	 * Precondition: Karel is standing on a corner with a marker.
	 * Postcondition: Karel is standing on the midpoint corner of the
	 * street and has placed beepers along the way.  
	 */
	private void narrowInOnStreetMidpoint() {
		move();
		move();
		checkIfMidpoint();
	}
	
	/* Method: checkIfMidpoint
	 * checks if the corner is the street's midpoint by
	 * employing the algorithm described above
	 */
	private void checkIfMidpoint() {
		if(beepersPresent()) {
			placeMidpointMarker();
		}
		else {
			moveToNextMarker();
			placeGuideMarker();
			narrowInOnStreetMidpoint();
		}
	}
	
	/* Method: placeMidpointMarker
	 * places a marker at the midpoint corner 
	 * precondition: Karel is in the corner adjacent to the
	 * midpoint and facing the incorrect direction of movement.
	 * postcondition: Karel is in the midpoint corner and has
	 * placed a beeper. 
	 */
	private void placeMidpointMarker() {
		turnAround();
		move();
		putBeeper();
	}
	
	/* Method: moveToNextMarker
	 * Moves Karel to the next marker in its direction of movement 
	 * Precondition: none
	 * Postcondition: Karel is on a corner with a marker 
	 */
	private void moveToNextMarker() {
		while(noBeepersPresent()) {
			move();
		}
	}
	
	/* Method: placeGuideMarker
	 * Karel turns around and places a marker (beeper) on the 
	 * adjacent corner
	 */
	private void placeGuideMarker() {
		turnAround();
		move();
		putBeeper();
	}

	/* Method: doubleMidpointMarkers
	 * Karel places an additional marker (beeper) on the 
	 * midpoint corner 
	 * Precondition: one beeper present on midpoint corner
	 * Postcondition: two beepers present on midpoint corner
	 */
	private void doubleMidpointMarker() {
		putBeeper();
	}
	
	/* Method: removeOtherMarkers
	 * Removes a single marker from each corner of 1st street. 
	 * Ultimately, this leaves only a single beeper on the 
	 * midpoint corner of 1st street. 
	 * Precondition: at least 1 beeper is present on corner
	 * Postcondition: 1 less beeper is present on the corner
	 */
	private void removeOtherMarkers() {
		moveToEndMarker();
		pickUpMarkersAlongStreet();
	}
	
	/* Method: moveToEndMarker();
	 * Moves Karel to either end of 1st street 
	 * Precondition: none
	 * Postcondition: Karel is at either the east or west 
	 * end of 1st street. 
	 */
	private void moveToEndMarker() {
		while(frontIsClear()) {
			move();
		}
	}
	
	/* Method: pickUpMarkersAlongStreet
	 * Moves Karel along length of 1st street and picks up 
	 * single beeper at each corner. 
	 * Precondition: at least 1 beeper present on starting corner 
	 * Postcondition: single beeper present on midpoint corner
	 */
	private void pickUpMarkersAlongStreet() {
		turnAround();
		pickBeeper();
		while(frontIsClear()) {
			move();
			pickBeeper();
		}
	}
	
	/* Method: returnToMidpoint
	 * Returns Karel to the midpoint corner of 1st street 
	 * Precondition: none
	 * Postcondition; Karel is on the midpoint corner of 1st street
	 */
	private void returnToMidpoint() {
		turnAround();
		while(noBeepersPresent()) {
			move();
		}
	}
}
