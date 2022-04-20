/*
 * File: CS106ATiles.java
 * Name: Julianne Crawford
 * Section Leader: Peter Maldonado
 * ----------------------
 * Purpose: The CS106ATiles program displays four tiles, each
 * containing the text "CS106A". The tiles are separated by a 
 * specified distance, but overall, the four tiles are centered 
 * in the graphics window and the text is centered in each 
 * rectangle. The amount of space between tiles, the tile width,
 * tile height, and text can all be easily adjusted in this program.
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class CS106ATiles extends GraphicsProgram {

	/** Amount of space (in pixels) between tiles */
	private static final int TILE_SPACE = 20;

	/** Width of each tile (in pixels) */
	private static final int TILE_WIDTH = 120; 

	/** Height of each tile (in pixels) */
	private static final int TILE_HEIGHT = 60; 

	/** Text displayed in tiles */ 
	private static final String TEXT = "CS106A"; 

	public void run() {	
		// place the Northwest tile
		double northwestX = -(TILE_SPACE/2+TILE_WIDTH/2); 
		double northwestY = -(TILE_SPACE/2+TILE_HEIGHT/2); 
		centerTextInRectangle(northwestX,northwestY); 

		// place the Northeast tile
		double northeastX = (TILE_SPACE/2+TILE_WIDTH/2); 
		double northeastY = -(TILE_SPACE/2+TILE_HEIGHT/2); 
		centerTextInRectangle(northeastX,northeastY); 

		// place the Southwest tile
		double southwestX = -(TILE_SPACE/2+TILE_WIDTH/2); 
		double southwestY = (TILE_SPACE/2+TILE_HEIGHT/2); 
		centerTextInRectangle(southwestX,southwestY); 

		// place the Southeast tile 
		double southeastX = (TILE_SPACE/2+TILE_WIDTH/2); 
		double southeastY = (TILE_SPACE/2+TILE_HEIGHT/2); 
		centerTextInRectangle(southeastX,southeastY); 
	}

	/* Method: centerTextInRectangle
	 * The centerTextInRectangle method takes as inputs the x and y coordinates
	 * (with respect to a centered rectangle) and places a rectangle with centered
	 * text at those coordinates. 
	 */
	private void centerTextInRectangle(double positionX, double positionY) {
		// half width of screen
		double centerX = getWidth()/2; 

		// half height of screen 
		double centerY = getHeight()/2; 

		// center rectangle x-coordinate
		double centerRectX = centerX-TILE_WIDTH/2; 

		// center rectangle y-coordinate
		double centerRectY = centerY-TILE_HEIGHT/2; 

		// make and add an unfilled rectangle based on coordinate inputs 
		GRect tile = new GRect(centerRectX+positionX,centerRectY+positionY, TILE_WIDTH,TILE_HEIGHT);
		add(tile); 

		// make and add a label 
		GLabel label = new GLabel(TEXT); 
		add(label); 

		// center label x-coordinate
		double centerLabelX = centerX-label.getWidth()/2;

		// center label y-coordinate 
		double centerLabelY = centerY+label.getAscent()/2;

		// re-position label based on inputs 
		label.setLocation(centerLabelX+positionX,centerLabelY+positionY); 
	}
}

