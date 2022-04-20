/*
 * File: DrawCenteredRect.java
 * Name: Julianne Crawford
 * Section Leader: Peter Maldonado
 * ----------------------
 * Purpose: the DrawCenterRect program draws a blue, filled 
 * rectangle with width = 350px and height = 270px at the 
 * center of the screen. The program can be altered to draw
 * any sized blue-filled rectangle at the center of the 
 * screen. 
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class DrawCenteredRect extends GraphicsProgram {

	/** Width of rectangle in pixels */
	private static final int WIDTH = 350;

	/** Height of rectangle in pixels */
	private static final int HEIGHT = 270;

	public void run() {
		// half the height of the screen.
		double centerY = getHeight()/2; 

		// half the width of the screen.
		double centerX = getWidth()/2; 

		// input x-coordinate of rectangle 
		double rectX = centerX-WIDTH/2; 

		// input y-coordinate of rectangle 
		double rectY = centerY-HEIGHT/2; 

		// make and add a blue rectangle to the center of the screen 
		GRect rect = new GRect(rectX,rectY,WIDTH,HEIGHT);
		rect.setFilled(true);
		rect.setColor(Color.BLUE);
		add(rect);
	}
}

