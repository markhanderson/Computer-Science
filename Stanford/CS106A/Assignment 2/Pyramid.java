/*
 * File: Pyramid.java
 * Name: Julianne Crawford 
 * Section Leader: Peter Maldonado
 * ------------------
 * Purpose: The Pyramid program draws a pyramid of bricks. 
 * The pyramid is centered at the base of the screen. The
 * number of bricks decreases each row until it reaches a
 * single brick at the top of the pyramid. The program 
 * can be altered to accommodate various brick widths, 
 * heights, and number of base bricks. 
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class Pyramid extends GraphicsProgram {

/** Width of each brick in pixels */
	private static final int BRICK_WIDTH = 30;

/** Height of each brick in pixels */
	private static final int BRICK_HEIGHT = 12;

/** Number of bricks in the base of the pyramid */
	private static final int BRICKS_IN_BASE = 14;
	
	public void run() {
		
		// Half the width of the screen 
		double centerX = getWidth()/2;
		
		// Height of the screen
		double ScreenBase = getHeight(); 
		
		// Initiate a brick row index 
		int rowIndex = 1; 
		
		// Build a brick pyramid 
		for(int row = BRICKS_IN_BASE; row > 0; row--) {
			int bricksInRow = row; 
			for(int bricks = 0; bricks < bricksInRow; bricks++) {
					// Brick x-coordinate
					double rectX = centerX-(bricksInRow/2.0)*BRICK_WIDTH+bricks*BRICK_WIDTH;
					// Brick y-coordinate
					double rectY = ScreenBase-BRICK_HEIGHT*rowIndex;
					// Make and add an unfilled rectangle 
					GRect brick = new GRect(rectX,rectY,BRICK_WIDTH,BRICK_HEIGHT);
					add(brick);
			}
			rowIndex++;  
		}
	}
}

