/*
 * File: ExtensionKarel.java
 * Name: Julianne Crawford
 * Section Leader: Peter Maldonado
 * --------------------------------
 * Purpose: My ExtensionKarel subclass turns Karel into 
 * "Justice Karel". Justice Karel makes sure that the 
 * muffins (represented by beepers) get allocated equally 
 * between the two bakeries in Karel's world: East (green) 
 * and West (cyan) bakery are located in their respective 
 * corners on 1st street. In order to achieve this 
 * allocation, the muffins are collected every morning and 
 * brought to a distribution facility on the west end of the 
 * northern-most street in Karel's world (marked by a blue square) 
 * The solution is general enough to work in any size world 
 * (as long as it is at least a 2x2 world) with any number of
 * muffins starting in each bakery as long as the sum of the 
 * muffins is even. 
 */

import stanford.karel.*;

public class ExtensionKarel extends SuperKarel {
	
	public void run() {
		moveAllMuffinsToFacility();
		distributeMuffinsEvenly();	
	}
	
	/* Method: moveAllMuffinsToFacility
	 * ----------------------
	 * Karel moves all of the muffins (beepers) from the east 
	 * and west bakeries to the distribution facility in the 
	 * northwest corner.  
	 * Precondition: Karel is facing east in the corner of 
	 * 1st avenue and 1st street. All Muffins (beepers) 
	 * are unevenly distributed between the two bakeries. 
	 * Postcondition: Karel is facing north in the northwest
	 * corner. All muffins (beepers) are in the distribution
	 * facility. 
	 */
	private void moveAllMuffinsToFacility() {
		moveWestBakeryMuffins();
		moveToEastBakery();
		moveEastBakeryMuffins();
	}
	
	/* Method: moveWestBakeryMuffins
	 * ----------------------
	 * Karel moves all of the West bakery muffins to the 
	 * distribution facility. 
	 * Precondition: Karel is facing east in the corner of 1st
	 * street and 1st avenue. 
	 * Postcondition: Karel is facing south in the corner of 1st
	 * street and 1st avenue. All muffins (beepers) from the 
	 * West bakery have been moved to the distribution facility. 
	 */
	private void moveWestBakeryMuffins() {
		turnLeft();
		while(beepersPresent()) {
			pickUpMuffin();
			moveAlongFirstAvenue();
			dropOffMuffin();
			returnToWestBakery();
		}	
	}
	
	/* Method: pickUpMuffin
	 * ----------------------
	 * Karel picks up a muffin (beeper) 
	 * Precondition: Karel is standing on a corner with a beeper
	 * Postcondition: The corner has one less beeper 
	 */
	private void pickUpMuffin() {
		pickBeeper();
	}
	
	/* Method: dropOffMuffin
	 * ----------------------
	 * Karel places a muffin (beeper) on the coner in which it stands
	 * Precondition: Karel has a beeper in its beeperbag
	 * Postcondition: Karel has one less beeper in its beeperbag 
	 */
	private void dropOffMuffin() {
		putBeeper();
	}
	
	/* Method: returnToWestBakery()
	 * ----------------------
	 * Karel returns to the West Bakery location from the distribution
	 * facility location. 
	 * Precondition: Karel is facing north and is located in the 
	 * northwest corner.
	 * Postcondition: Karel is facing north in the corner of 1st 
	 * street and 1st avenue. 
	 */
	private void returnToWestBakery() {
		turnAround();
		moveAlongFirstAvenue();
		turnAround();
	}
	
	/* Method: moveToEastBakery
	 * ----------------------
	 * Moves Karel from the West Bakery to the East Bakery 
	 * Precondition: Karel is facing east at the corner of 1st 
	 * avenue and 1st street. 
	 * Postcondition: Karel is facing east at the east-most 
	 * corner of 1st street. 
	 */
	private void moveToEastBakery() {
		turnRight();
		while(frontIsClear()) {
			move();
		}
	}
	
	/* Method: moveEastBakeryMuffins
	 * ----------------------
	 * Karel moves every muffin from the East bakery to the 
	 * distribution facility. 
	 * Precondition: Karel is facing East in the east-most 
	 * corner of 1st street.
	 * Postcondition: Karel is facing east in the east-most
	 * corner of 1st street. All muffins (beepers) from the 
	 * East bakery have been moved to the distribution facility.
	 */
	private void moveEastBakeryMuffins() {
		while(beepersPresent()) {
			pickUpMuffin();
			moveEastMuffinToFacility();
			dropOffMuffin();
			returnToEastBakery();
		}
	}
	
	/* Method: moveEastMuffinToFacility
	 * ----------------------
	 * Moves Karel from the East bakery to the distribution 
	 * facility to deliver the muffins (beepers) 
	 * Precondition: Karel is facing east in the east-most corner
	 * of 1st street. 
	 * Postcondition: Karel is facing north in the north-west corner.
	 */
	private void moveEastMuffinToFacility() {
		turnAround();
		moveAlongFirstStreet();
		turnRight();
		moveAlongFirstAvenue();
	}
	
	/* Method: moveAlongFirstStreet
	 * ----------------------
	 * Moves Karel horizontally until it reaches the nearest wall.
	 * Precondition: none
	 * Postcondition: Karel is facing the direction in which it 
	 * started, and depending on that direction is either in the 
	 * eastern-most or western-most corner of its current street. 
	 */
	private void moveAlongFirstStreet() {
		while(frontIsClear()) {
			move();
		}
	}
	
	/* Method: moveAlongFirstAvenue
	 * ----------------------
	 * Moves Karel vertically until it reaches the nearest wall. 
	 * Precondition: none
	 * Postcondition: Karel is facing the direction in which it
	 * started, and depending on that direction is either in the
	 * northern-most or southern-most corner of its current avenue.
	 */
	private void moveAlongFirstAvenue() {
		while(frontIsClear()) {
			move();
		}
	}
	
	/* Method: returnToEastBakery
	 * ----------------------
	 * Karel returns to the East bakery location from the distribution
	 * facility location. 
	 * Precondition: Karel is facing north and is located in the 
	 * northwest corner. 
	 * Postcondition: Karel is facing east in the eastern-most 
	 * corner of 1st street. 
	 */
	private void returnToEastBakery() {
		turnAround();
		moveAlongFirstAvenue();
		turnLeft();
		moveAlongFirstStreet();
	}
	
	/* Method: distributeMuffinsEvenly
	 * ----------------------
	 * Moves Karel back to the distribution facility. One at a time, 
	 * Karel moves the muffins (beepers) to each bakery, alternating
	 * between each muffin to ensure even distribution. 
	 * Precondition: Karel is facing east in the east-most corner of
	 * 1st street (East bakery). All muffins (beepers) are at the
	 * distribution facility. 
	 * Postcondition: The muffins (beepers) in the distribution
	 * facility have been distributed evenly between the East and 
	 * West bakeries.  
	 */
	private void distributeMuffinsEvenly() {
		returnToFacilityFromEast();
		distributeMuffins();
	}
	
	/* Method: returnToFacilityFromEast
	 * ----------------------
	 * Moves Karel from the East bakery to the distribution facility.
	 * Precondition: Karel is facing east in the east-most corner of
	 * 1st street (East bakery).
	 * Postcondition: Karel is facing north in the northwest corner
	 * (distribution facility). 
	 */
	private void returnToFacilityFromEast() {
		turnAround();
		moveAlongFirstStreet();
		turnRight();
		moveAlongFirstAvenue();
	}
	
	/* Method: distributeMuffins
	 * ----------------------
	 * One at a time, Karel moves the muffins (beepers) to each 
	 * bakery, alternating between each muffin to ensure even
	 * distribution. 
	 * Precondition: All of the muffins (beepers) are at the 
	 * distribution facility.  
	 * Postcondition: The muffins (beepers) in the distribution 
	 * facility have been distributed evenly between the East and 
	 * West bakeries. 
	 */
	private void distributeMuffins() {
		while(beepersPresent()) {
			moveMuffinToEastBakery();
			returnToFacilityFromEast();
			moveMuffinToWestBakery();
			returnToFacilityFromWest();
		}
	}
	
	/* Method: moveMuffinToEastBakery
	 * ----------------------
	 * Karel moves a single muffin from the distribution facility
	 * to the East bakery.
	 * Precondition: Karel is facing North in the northwest corner. 
	 * Postcondition: Karel is facing East in the east-most corner 
	 * of 1st street. Karel has delivered a single muffin (beeper)
	 *  from the distribution facility to the East bakery. 
	 */
	private void moveMuffinToEastBakery() {
		pickUpMuffin();
		returnToEastBakery();
		dropOffMuffin();
	}
	
	/* Method: moveMuffinToWestBakery
	 * ----------------------
	 * Karel moves a single muffin from the distribution facility
	 * to the West bakery.
	 * Precondition: Karel is facing North in the northwest corner. 
	 * Postcondition: Karel is facing south in the corner of 1st 
	 * street and 1st avenue. Karel has delivered a single muffin
	 * (beeper) from the distribution facility to the West bakery.
	 */
	private void moveMuffinToWestBakery() {
		pickUpMuffin();
		returnToWestBakery();
		dropOffMuffin();
	}
	
	/* Method: returnToFacilityFromWest
	 * ----------------------
	 * Moves Karel the West bakery to the distribution facility.
	 * Precondition: Karel is facing north in the corner of 1st 
	 * street and 1st avenue (West bakery)
	 * Postcondition: Karel is facing north in the northwest corner 
	 * (distribution facility). 
	 */
	private void returnToFacilityFromWest() {
		moveAlongFirstAvenue();
	}
	
}