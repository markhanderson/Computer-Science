
/* File: GraphicsContestUserSettings.java
 * Name: Julianne Crawford
 * ---------------------------------------
 * Purpose: The GraphicsContestUserSettings class represents a single 
 * instance of the user inputs. Each GraphicsContestUserSettings 
 * contains the user input line style, line width, color scheme, and
 * background color
 */

import java.awt.Color;

public class GraphicsContestUserSettings implements GraphicsContestConstants {

	/* Constructor: GraphicsContestUserSettings
	 * -----------------------------------------
	 *  The GraphicsContestUserSettings constructor creates a new 
	 *  GraphicsContestUserSettings with the line style, line width, 
	 *  color scheme, and background colors specified by the user
	 */
	public GraphicsContestUserSettings(int lineStyleCounter,int lineWidthCounter,int colorSchemeCounter, Color backgroundColor) {
		this.lineStyleCounter = lineStyleCounter; 
		this.lineWidthCounter = lineWidthCounter; 
		this.colorSchemeCounter = colorSchemeCounter; 
		this.backgroundColor = backgroundColor; 
	}
	
	/* Method: getLineStyle
	 * -------------
	 * The getLineStyle method returns the lineStyleCounter which 
	 * indicates whether or not the lines will be mirrored 
	 * (lineStyleCounter = 1) or rotated (lineStyleCounter = 2)
	 * Output: 
	 * lineStyleCounter - either 1 or 2 depending on the line style
	 */
	public int getLineStyle() {
		return(lineStyleCounter);
	}
	
	/* Method: getLineWidth
	 * -------------
	 * The getLineWidth returns the line width 
	 * Output: 
	 * lineWidthCounter - line width 
	 */
	public int getLineWidth() {
		return(lineWidthCounter); 
	}
	
	/* Method: getColorScheme
	 * -----------------------
	 * the getColorScheme method returns an array with 5 rgb colors 
	 * representing the color scheme 
	 * Output: 
	 * Color[] - Color array with 5 rgb colors representing the color scheme 
	 */
	public Color[] getColorScheme() {
		if(colorSchemeCounter == 0) {
			Color[] colorScheme1 = {new Color(255,0,0),new Color(255,150,0),new Color(255,255,0),new Color(0,255,0),new Color(0,0,255)}; 
			return(colorScheme1); 
		} else if(colorSchemeCounter == 1) {
			Color[] colorScheme2 = {new Color(68,114,196),new Color(0,253,255),new Color(0,145,147),new Color(115,253,214),new Color(218,239,255)}; 
			return(colorScheme2); 
		} else if(colorSchemeCounter == 2) {
			Color[] colorScheme3 = {new Color(255,255,0),new Color(159,0,94),new Color(255,94,1),new Color(192,255,2),new Color(1,63,191)}; 
			return(colorScheme3); 
		} else if(colorSchemeCounter == 3){
			Color[] colorScheme4 = {new Color(127,255,2),new Color(255,192,0),new Color(222,41,196),new Color(122,129,255),new Color(87,6,254)};
			return(colorScheme4); 
		} else if(colorSchemeCounter == 4) {
			Color[] colorScheme5 = {new Color(255,255,255),new Color(115,254,255),new Color(37,36,224),new Color(148,55,255),new Color(255,64,255)}; 
			return(colorScheme5); 
		} else if(colorSchemeCounter == 5) {
			Color[] colorScheme6 = {new Color(242,242,242),new Color(166,166,166),new Color(127,127,127),new Color(64,64,64),new Color(13,13,13)}; 
			return(colorScheme6); 
		} else if(colorSchemeCounter == 6) {
			Color[] colorScheme7 = {new Color(255,47,146),new Color(255,64,255),new Color(0,154,132),new Color(44,193,161),new Color(115,253,214)}; 
			return(colorScheme7); 
		} else if(colorSchemeCounter == 7) {
			Color[] colorScheme8 = {new Color(192,0,0),new Color(255,0,0),new Color(255,147,0),new Color(255,192,0),new Color(255,255,0)}; 
			return(colorScheme8); 
		} else if(colorSchemeCounter == 8) {
			Color[] colorScheme9 = {new Color(255,213,230),new Color(212,255,235),new Color(238,203,255),new Color(255,255,163),new Color(219,221,255)}; 
			return(colorScheme9); 
		} else if(colorSchemeCounter == 9) {
			Color[] colorScheme10 = {new Color(56,87,35),new Color(0,145,147),new Color(0,176,80),new Color(115,251,121),new Color(214,251,198)}; 
			return(colorScheme10); 
		} else {
			return null; 
		}
	}
	
	/* Method: getBackgroundColor
	 * --------------------------
	 * The getBackgroundColor method returns the background color 
	 * Output: 
	 * backgroundColor - background color to assign to the canvas. 
	 * Either black, white, or blue 
	 */
	public Color getBackgroundColor() {
		return(backgroundColor); 
	}
	
	/* Method: minusLineWidth
	 * -----------------------
	 * The minusLineWidth method decreases the lineWidthCounter 
	 * Output: 
	 * lineWidthCounter - lineWidthCounter decreased by 1 
	 */
	public int minusLineWidth() {
		if(lineWidthCounter > LINE_WIDTH_MIN) {
			lineWidthCounter--; 
		} 
		return lineWidthCounter;
	}
	
	/* Method: plusLineWidth
	 * ----------------------
	 * The plusLineWidth method increases the lineWidthCounter
	 * Output: 
	 * lineWidthCounter - lineWidthCounter increased by 1
	 */
	public int plusLineWidth() {
		if(lineWidthCounter < LINE_WIDTH_MAX) {
			lineWidthCounter++; 
		} 
		return lineWidthCounter; 
	}
	
	/* Method: minusColorScheme
	 * -------------------------
	 * The minusColorScheme method decreases the colorSchemeCounter
	 * Output: 
	 * colorSchemeCounter - colorSchemeCounter decreased by 1
	 */
	public int minusColorScheme() {
		if(colorSchemeCounter > 0) {
			colorSchemeCounter--; 
		} 
		return colorSchemeCounter; 
	}
	
	/* Method: plusColorScheme
	 * ------------------------
	 * The plusColorScheme method increases the colorSchemeCounter
	 * Output: 
	 * colorSchemeCounter - colorSchemeCounter increases by 1
	 */
	public int plusColorScheme() {
		if(colorSchemeCounter < COLOR_SCHEMES_MAX) {
			colorSchemeCounter++; 
		} 
		return colorSchemeCounter; 
	}
	
	/** Instance Variables **/ 
	private int lineStyleCounter; 
	private int lineWidthCounter; 
	private int colorSchemeCounter; 
	private Color backgroundColor; 
}