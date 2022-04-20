
/* File: GraphicsContestGraph.java
 * Name: Julianne Crawford
 * --------------------------------
 * Purpose: the GraphicsContestGraph class represents the canvas
 * on which the kaleidoscope patterns are drawn. The class is 
 * responsible for updating the graph whenever the user inputs
 * (line style, line width, background color, and color scheme)
 * change and as the user draws on the canvas itself. 
 */

import acm.graphics.*;
import java.util.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class GraphicsContestGraph extends GCanvas implements GraphicsContestConstants,MouseListener,MouseMotionListener{

	/* Constructor: GraphicsContestGraph
	 * ----------------------------------
	 * The GraphicsContestGraph constructor creates a new
	 * GraphicsContestGraph object to display the kaleidoscope
	 * patterns drawn by the user
	 */
	public GraphicsContestGraph() {

	}

	/* Method: addEntry 
	 * -----------------
	 * The addEntry method adds a new GraphicsContestUserSettings entry
	 * which includes line style, line width, color scheme, and 
	 * background color 
	 * Parameters: 
	 * entry - The GraphicsContestUserSettings input by the user in 
	 * the main GUI  
	 */
	public void addEntry(GraphicsContestUserSettings entry) {
		lineStyle = entry.getLineStyle(); 
		lineWidth = entry.getLineWidth();
		colorScheme = entry.getColorScheme(); 
		backgroundColor = entry.getBackgroundColor();
		setColorCombo(colorScheme[0],colorScheme[1],colorScheme[2],colorScheme[3],colorScheme[4]);
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	/* Method: updateBacgroundColor
	 * ---------------
	 * The updateBackgroundColor method updates the background color 
	 * of the graph display 
	 */
	public void updateBackgroundColor() {
		setBackground(backgroundColor); 
	}

	/* Method: clear
	 * --------------
	 * The clear method removes everything from the canvas 
	 */
	public void clear() {
		removeAll();
	}

	/* Method: draw
	 * -------------
	 * The draw method collects points based on the user's mouse location
	 * and draws lines between each point to create a continuous line. The 
	 * points are either mirrored or rotated based on the user preference 
	 * in order to create a "kaleidoscope" effect 
	 */
	public void draw() {
		if(drawToggle) {
			drawingPointsX.add(mouseX); 
			drawingPointsY.add(mouseY); 
			ArrayList<GLine> lineArray = new ArrayList<GLine>(); 

			if(drawingPointsX.size() > 2) {
				if(lineStyle == 1) {
					lineArray = mirroredLines(lineArray); 
				} else if(lineStyle == 2) {
					lineArray = rotatedLines(lineArray); 
				}
				
				counter++;
				if(colorCounter >= colorGradientArray.size()-2) {
					colorCounter = 0; 
				} else {
					colorCounter++; 
				}

				for(int i = 0; i < lineArray.size(); i++) {
					add(lineArray.get(i)); 
					lineArray.get(i).setColor((colorGradientArray.get(colorCounter)));
					lineArray.get(i).setLineWidth(lineWidth);
				}
			}
		}
	}

	/* Method: mirroredLines
	 * ----------------------
	 * The mirroredLines method takes in a lineArray and mirrors the points 
	 * around 360 degrees 
	 * Parameters
	 * lineArray - ArrayList of GLines which it continues to add to as the 
	 * user continues to drag their mouse
	 * Output: 
	 * lineArray - ArrayList containing GLines between the user's mouse 
	 * locations as they drag their mouse 
	 */
	private ArrayList<GLine> mirroredLines(ArrayList<GLine> lineArray){
		GLine line = new GLine(drawingPointsX.get(counter),drawingPointsY.get(counter),drawingPointsX.get(counter+1),drawingPointsY.get(counter+1)); 
		GLine lineReflection45 = new GLine(getHeight()-drawingPointsY.get(counter),-drawingPointsX.get(counter)+getWidth(),getHeight()-drawingPointsY.get(counter+1),-drawingPointsX.get(counter+1)+getWidth());
		for(double i = 2*ROTATION_ANGLE; i < TOTAL_DEGREES; i+= 2*ROTATION_ANGLE) {
			double[] rotatedCoordinates = rotatePoints(drawingPointsX.get(counter),drawingPointsY.get(counter),i); 
			double[] rotatedCoordinates2 = rotatePoints(drawingPointsX.get(counter+1),drawingPointsY.get(counter+1),i);
			GLine rotatedLine = new GLine(rotatedCoordinates[0],rotatedCoordinates[1],rotatedCoordinates2[0],rotatedCoordinates2[1]);
			double[] rotatedMirrorCoordinates = rotatePoints(getHeight()-drawingPointsY.get(counter),-drawingPointsX.get(counter)+getWidth(),i); 
			double[] rotatedMirrorCoordinates2 = rotatePoints(getHeight()-drawingPointsY.get(counter+1),-drawingPointsX.get(counter+1)+getWidth(),i);
			GLine rotatedMirrorLine = new GLine(rotatedMirrorCoordinates[0],rotatedMirrorCoordinates[1],rotatedMirrorCoordinates2[0],rotatedMirrorCoordinates2[1]);
			lineArray.add(line);
			lineArray.add(lineReflection45); 
			lineArray.add(rotatedLine); 
			lineArray.add(rotatedMirrorLine); 
		}
		return lineArray; 
	}
	
	/* Method: rotatedLines
	 * ----------------------
	 * The rotatedLines method takes in a lineArray and rotates the points
	 * around 360 degrees
	 * Parameters:
	 * lineArray - ArrayList of GLines which it continues to add to as the
	 * user continues to drag their mouse
	 * Output: 
	 * lineArray - ArrayList containing GLines between the user's mouse 
	 * locations as they drag their mouse
	 */
	private ArrayList<GLine> rotatedLines(ArrayList<GLine> lineArray){
		for(double i = ROTATION_ANGLE; i < TOTAL_DEGREES; i+= ROTATION_ANGLE) {
			GLine line = new GLine(drawingPointsX.get(counter),drawingPointsY.get(counter),drawingPointsX.get(counter+1),drawingPointsY.get(counter+1)); 
			double[] rotatedCoordinates = rotatePoints(drawingPointsX.get(counter),drawingPointsY.get(counter),i); 
			double[] rotatedCoordinates2 = rotatePoints(drawingPointsX.get(counter+1),drawingPointsY.get(counter+1),i); 
			GLine rotatedLine = new GLine(rotatedCoordinates[0],rotatedCoordinates[1],rotatedCoordinates2[0],rotatedCoordinates2[1]); 
			lineArray.add(line);
			lineArray.add(rotatedLine); 
		}
		return lineArray; 
	}

	/* Method: rotatePoints
	 * ---------------------
	 * The rotatePoints method takes in coordinates and rotates them by 
	 * a specified angle 
	 * Parameters: 
	 * pointX - x-coordinate based on the location of the user's mouse 
	 * pointY - y-coordinate based on the location of the uers's mouse 
	 * angle - specified angle of rotation 
	 * Output: 
	 * rotatedCoordinates - rotated x and y coordinates based on the 
	 * specified angle 
	 */
	private double[] rotatePoints(double pointX,double pointY,double angle) {
		double centerX = getWidth()/2; 
		double centerY = getHeight()/2; 
		double rotatedX = centerX+((pointX-centerX)*Math.cos(angle*DEGREES_TO_RADIANS)-(pointY-centerY)*Math.sin(angle*DEGREES_TO_RADIANS)); 
		double rotatedY = centerY+((pointX-centerX)*Math.sin(angle*DEGREES_TO_RADIANS)+(pointY-centerY)*Math.cos(angle*DEGREES_TO_RADIANS)); 
		double[] rotatedCoordinates = {rotatedX,rotatedY}; 
		return rotatedCoordinates; 
	}
	
	/* Method: setColorCombo
	 * ----------------------
	 * The setColorCombo method initiates a color combo based on the user's 
	 * colorScheme selection in the GUI 
	 * Parameters: 
	 * color1 - first rgb color in the colorScheme
	 * color2 - second rgb color in the colorScheme
	 * color3 - third rgb color in the colorScheme
	 * color4 - fourth rgb color in the colorScheme
	 * color5 - fifth rgb color in the colorScheme 
	 */
	private void setColorCombo(Color color1, Color color2, Color color3, Color color4, Color color5) {
		colorGradientArray = colorGradient(colorGradientArray,color1,color2);
		colorGradientArray = colorGradient(colorGradientArray,color2,color3); 
		colorGradientArray = colorGradient(colorGradientArray,color3,color4); 
		colorGradientArray = colorGradient(colorGradientArray,color4,color5);
	}

	/* Method: colorGradient
	 * ----------------------
	 * The colorGradient method creates a gradation of rgb colors between
	 * two input colors 
	 * Parameters: 
	 * colorGradientArray - ArrayList of rgb colors  
	 * color1 - first color (rgb) in the color gradient 
	 * color2 - second color (rgb) in the color gradient 
	 * Output: 
	 * colorGradientArray - ArrayList of rgb colors that create a gradient
	 * between color1 and color2 
	 */
	private ArrayList<Color> colorGradient(ArrayList<Color> colorGradientArray,Color color1, Color color2) {
		int redDiff = color2.getRed()-color1.getRed(); 
		int greenDiff = color2.getGreen()-color1.getGreen(); 
		int blueDiff = color2.getBlue()-color1.getBlue(); 

		maxDiff = Math.max(Math.max(Math.abs(redDiff), Math.abs(greenDiff)),Math.abs(blueDiff)); 
		int normRedDiff = Math.round(redDiff/maxDiff); 
		int normGreenDiff = Math.round(greenDiff/maxDiff); 
		int normBlueDiff = Math.round(blueDiff/maxDiff); 

		int redCounter = 0; 
		int greenCounter = 0; 
		int blueCounter = 0; 

		for(int i = 0; i < maxDiff; i++) {
			Color colorNew = new Color(color1.getRed()+redCounter,color1.getGreen()+greenCounter,color1.getBlue()+blueCounter); 
			colorGradientArray.add(colorNew); 

			if(redCounter <= RGB_MAX) {
				redCounter += normRedDiff; 
			}
			if(greenCounter <= RGB_MAX) {
				greenCounter += normGreenDiff; 
			}
			if(blueCounter <= RGB_MAX) {
				blueCounter += normBlueDiff; 
			}
		}
		return colorGradientArray; 
	}
	
	/* Method: clearColorGradient
	 * ---------------------------
	 * The clearColorGradient method clears the color gradient ArrayList 
	 */
	public void clearColorGradient() {
		colorGradientArray.clear(); 
	}

	/* Method: mousePressed
	 * -----------------------
	 * The mousePressed method is called any time the mouse is pressed 
	 * in the canvas 
	 */
	public void mousePressed(MouseEvent e) {
		mousePressedX = e.getX();
		mousePressedY = e.getY();
		drawingPointsX.add(mousePressedX);
		drawingPointsY.add(mousePressedY);
	}

	/* Method: mouseDragged
	 * -----------------------
	 *  The mouseDragged method is called any time the mouse is 
	 *  dragged in the canvas 
	 */
	public void mouseDragged(MouseEvent e) {
		drawToggle = true; 
		mouseX = e.getX();
		mouseY = e.getY();
	}

	/* Method: mouseReleased
	 * -----------------------
	 * The mouseReleased method is called any time the mouse is
	 * released in the canvas 
	 */
	public void mouseReleased(MouseEvent e) {
		drawToggle = false;
		drawingPointsX.clear(); 
		drawingPointsY.clear(); 
		counter = 0; 
		colorCounter = 0; 
		colorGradientArray.clear(); 
		setColorCombo(colorScheme[0],colorScheme[1],colorScheme[2],colorScheme[3],colorScheme[4]);
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {		
	} 
	
	/** INSTANCE VARIABLES **/ 
	private int lineStyle; 
	private int lineWidth; 
	private Color[] colorScheme; 
	private Color backgroundColor;
	private boolean drawToggle = false; 
	private double mouseX; 
	private double mousePressedX; 
	private double mouseY;
	private double mousePressedY; 
	private int counter = 0; 
	private int colorCounter = 0; 
	private ArrayList <Double> drawingPointsX = new ArrayList<Double>(); 
	private ArrayList <Double> drawingPointsY = new ArrayList<Double>();
	private static int maxDiff; 
	private static ArrayList<Color> colorGradientArray = new ArrayList<Color>();

}