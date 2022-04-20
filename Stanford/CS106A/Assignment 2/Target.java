/*
 * File: Target.java
 * Name: Julianne Crawford
 * Section Leader: Peter Maldonado
 * -----------------
 * Purpose: The Target graphics program draws an archery target
 * comprised of three circles (two red and one white) that form 
 * a bullseye. The outer circle has a radius of 72px, the middle
 * circle has a radius of 46.8px, and the inner circle has a 
 * radius of 21.6px. The target is centered in the window. 
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class Target extends GraphicsProgram {	
	public void run() {

		// make and add the outer red circle with radius = 72px
		drawCenteredCircle(Color.RED,72);

		// make and add the middle white circle with radius = 46.8px
		drawCenteredCircle(Color.WHITE, 46.8);

		// make and add the inner red circle with radius = 21.6px
		drawCenteredCircle(Color.RED,21.6);
	}

	/* Method: drawCenteredCircle
	 * The drawCenteredCircle method takes, as inputs, the fill color and 
	 * circle radius, and makes/adds a circle centered in the graphics 
	 * console window. 
	 */
	private void drawCenteredCircle(Color color, double radius) {
		// half the width of the screen.
		double centerX = getWidth()/2; 

		// half the height of the screen.
		double centerY = getHeight()/2; 

		// make and add a centered and filled circle 
		GOval outerOval = new GOval(centerX-radius,centerY-radius,2*radius,2*radius);
		outerOval.setFilled(true);
		outerOval.setColor(color);
		add(outerOval);
	}
}
