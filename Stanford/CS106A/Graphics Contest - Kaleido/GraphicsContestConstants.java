
/* File: GraphicsContestConstants.java
 * Name: Julianne Crawford 
 * ------------------------------------
 * The GraphicsContestConstants file declares several constants
 * that are shared by the different modules in the GraphicsContest
 * application. Any class that implements this interface can use
 * the constants.
 */

import java.awt.Color;
import java.awt.Font;

public interface GraphicsContestConstants {
	/** Dimensions of the canvas, in pixels **/
	public static final int PROGRAM_WIDTH = 800; 
	public static final int PROGRAM_HEIGHT = 578; 
	
	/** EAST Sidebar dimension, in pixels **/ 
	public static final int SIDEBAR_WIDTH = 200;

	/** Logo specifications **/ 
	public static final String LOGO = "K A L E I D O";
	public static final Font LOGO_FONT = new Font ("Spectre", Font.PLAIN,22); 
	
	/** Labels font, and dimensions, in pixels **/ 
	public static final Font SIDEBAR_FONT = new Font ("Spectre", Font.PLAIN,12);
	public static final int LABEL_HEIGHT = 25;  
	public static final Color LABEL_COLOR = new Color(214,214,214); 
	
	/** Sub-button color and dimensions, in pixels **/ 
	public static final int SUB_BUTTON_WIDTH = 30; 
	public static final int SUB_BUTTON_HEIGHT = 20;
	public static final int SUB_BUTTON_SPACING = 15; 
	public static final int NUM_SUB_BUTTONS = 4;

	/** Background color button dimensions, in pixels **/ 
	public static final int BACKGROUND_COLOR_WIDTH = 30; 
	public static final int BACKGROUND_COLOR_HEIGHT = 30;
	public static final int BACKGROUND_COLOR_SPACING = 5; 
	public static final int NUM_BACKGROUND_COLORS = 3;
	
	/** Clear spaces **/ 
	public static final Font CLEAR_SPACE = new Font ("Spectre", Font.PLAIN,25);

	/** Number of user options **/
	public static final int NUM_LINES_MAX = 16; 
	public static final int NUM_STYLES = 2; 
	public static final int LINE_WIDTH_MAX = 10; 
	public static final int LINE_WIDTH_MIN = 1; 
	public static final int COLOR_SCHEMES_MAX = 9; 
	
	/** draw animation delay, in milliseconds **/ 
	public static int DELAY = 2; 
	
	/** mathematical PI **/ 
	public static double PI = 3.14159; 
	
	/** degrees to radians conversion **/
	public static double DEGREES_TO_RADIANS = PI/180; 

	/** rotated lines rotation angle **/ 
	public static double ROTATION_ANGLE = 45; 
	public static int TOTAL_DEGREES = 360; 
	
	/** maximum RGB value **/
	public static int RGB_MAX = 255; 
}
