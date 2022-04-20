
/* File: GraphicsContest.java
 * Name: Julianne Crawford
 * --------------------------
 * Purpose: The GraphicsContest program, Kaleido, I wrote is an art platform that 
 * allows the user to draw "kaleidoscope" patterns. The program gives the user 
 * many different options in terms of line style (mirrored or rotated) line width, 
 * background color, and line color scheme. Just click and drag, and you'll start
 * making kaleidoscope art!
 * 
 * Future features: with more time, I would like to add a feature to allow the user 
 * to change the total number of lines, as well as a feature to allow the user to 
 * define their own color scheme. 
 */

import acm.program.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.*;
import acm.gui.TableLayout;
import javax.swing.*;
import javax.swing.ImageIcon;
import java.awt.event.ActionEvent;

public class GraphicsContest extends Program implements GraphicsContestConstants{

	/* Method: init 
	 * -------------
	 * The init method initializes the program. It is responsible for 
	 * initializing the interactors
	 */
	public void init() {
		addGraph(); 
		addSidebar();
		addActionListeners(); 
	}

	/* Method: run
	 * ------------
	 * After the init method initializes the program, the run method runs 
	 * the program that allows the user to draw patterns
	 */
	public void run() {
		graph.addEntry(userSettings);
		while(true) {
			graph.draw(); 
			pause(DELAY); 
		}
	}

	/* Method: addSidebar
	 * -------------------
	 * The addSidebar method sets up the sidebar with the user interactor
	 * buttons and labels
	 */
	private void addSidebar() {
		addLogo(); 
		addLineStyle(); 
		addLineWidth();
		addBackgroundColor(); 
		addColorScheme(); 
		addClear(); 		
	}

	/* Method: addGraph
	 * -----------------
	 * The addGraph method adds a GCanvas to the program where the user 
	 * can draw kaleidoscope patterns 
	 */
	private void addGraph() {
		add(graph); 
		setSize(PROGRAM_WIDTH,PROGRAM_HEIGHT);  
	}

	/* Method: addClear 
	 * --------------
	 * The clear method adds the "clear" button
	 */
	private void addClear() {
		JButton clear = new JButton("CLEAR"); 
		add(clear,EAST); 
	}

	/* Method: addColorScheme
	 * -----------------------
	 * The addColorScheme method adds the "Color Scheme" label and corresponding
	 * color scheme buttons 
	 */
	private void addColorScheme() {	
		colorSchemes = loadColorSchemes(); 

		JLabel colorSchemeLabel = addLabel("COLOR SCHEME", SIDEBAR_FONT); 
		add(colorSchemeLabel,EAST); 
		colorSchemeLabel.setBackground(LABEL_COLOR);
		colorSchemeLabel.setOpaque(true);
		colorSchemeLabel.setPreferredSize(new Dimension(0,LABEL_HEIGHT));

		JButton lastColorScheme = new JButton("<"); 
		JButton nextColorScheme = new JButton(">");
		colorSchemeButton = new JButton("");
		colorSchemeButton.setIcon(colorSchemes.get(colorSchemeCounter));
		colorSchemeButton.setBorderPainted(false);
		colorSchemeButton.setOpaque(true);

		JPanel colorSchemePanel = new JPanel(new TableLayout(1,3,SUB_BUTTON_SPACING,0));
		add(colorSchemePanel,EAST);
		colorSchemePanel.add(lastColorScheme); 
		colorSchemePanel.add(colorSchemeButton);
		colorSchemePanel.add(nextColorScheme);
		lastColorScheme.setPreferredSize(new Dimension(SUB_BUTTON_WIDTH,SUB_BUTTON_HEIGHT));
		nextColorScheme.setPreferredSize(new Dimension(SUB_BUTTON_WIDTH,SUB_BUTTON_HEIGHT));

		JLabel colorSchemeClearSpace = addLabel(" ",CLEAR_SPACE); 
		add(colorSchemeClearSpace,EAST); 
	}

	/* Method: loadColorSchemes
	 * ------------------------
	 * The loadColorSchemes method loads the colorScheme images to display on the 
	 * colorScheme JButton  
	 */
	private ArrayList<ImageIcon> loadColorSchemes(){
		ImageIcon colorScheme1 = new ImageIcon(getClass().getResource("/Resources/ColorScheme1.png")); 
		ImageIcon colorScheme2 = new ImageIcon(getClass().getResource("/Resources/ColorScheme2.png")); 
		ImageIcon colorScheme3 = new ImageIcon(getClass().getResource("/Resources/ColorScheme3.png")); 
		ImageIcon colorScheme4 = new ImageIcon(getClass().getResource("/Resources/ColorScheme4.png")); 
		ImageIcon colorScheme5 = new ImageIcon(getClass().getResource("/Resources/ColorScheme5.png")); 
		ImageIcon colorScheme6 = new ImageIcon(getClass().getResource("/Resources/ColorScheme6.png")); 
		ImageIcon colorScheme7 = new ImageIcon(getClass().getResource("/Resources/ColorScheme7.png")); 
		ImageIcon colorScheme8 = new ImageIcon(getClass().getResource("/Resources/ColorScheme8.png")); 
		ImageIcon colorScheme9 = new ImageIcon(getClass().getResource("/Resources/ColorScheme9.png")); 
		ImageIcon colorScheme10 = new ImageIcon(getClass().getResource("/Resources/ColorScheme10.png")); 

		colorSchemes.add(colorScheme1);
		colorSchemes.add(colorScheme2);
		colorSchemes.add(colorScheme3);
		colorSchemes.add(colorScheme4);
		colorSchemes.add(colorScheme5);
		colorSchemes.add(colorScheme6);
		colorSchemes.add(colorScheme7);
		colorSchemes.add(colorScheme8);
		colorSchemes.add(colorScheme9);
		colorSchemes.add(colorScheme10);

		return colorSchemes; 
	}

	/* Method: addBackgroundColor
	 * --------------------------
	 * The addBackgroundColor method adds the three background color option buttons 
	 */
	private void addBackgroundColor() {
		JLabel backgroundColorLabel = addLabel("BACKGROUND COLOR",SIDEBAR_FONT); 
		add(backgroundColorLabel,EAST);
		backgroundColorLabel.setBackground(LABEL_COLOR);
		backgroundColorLabel.setOpaque(true);
		backgroundColorLabel.setPreferredSize(new Dimension(0,LABEL_HEIGHT));

		JButton[] backgroundColorButtons = new JButton[NUM_BACKGROUND_COLORS]; 
		Color[] backgroundColors = new Color[] {Color.WHITE,Color.BLUE,Color.BLACK}; 

		whiteButton = new JButton("");
		backgroundColorButtons[0] = whiteButton;

		blueButton = new JButton(""); 
		backgroundColorButtons[1] = blueButton; 

		blackButton = new JButton("");
		backgroundColorButtons[2] = blackButton;

		JPanel backgroundColorPanel = new JPanel(new TableLayout(1,3,BACKGROUND_COLOR_SPACING,0)); 
		add(backgroundColorPanel,EAST); 		

		for(int i = 0; i < NUM_BACKGROUND_COLORS; i++) {
			backgroundColorButtons[i].setBackground(backgroundColors[i]); 
			backgroundColorButtons[i].setOpaque(true);
			backgroundColorButtons[i].setBorderPainted(false);
			backgroundColorButtons[i].setPreferredSize(new Dimension(BACKGROUND_COLOR_WIDTH,BACKGROUND_COLOR_HEIGHT)); 
			backgroundColorPanel.add(backgroundColorButtons[i]);
		}		

		JLabel penWidthClearSpace = addLabel(" ",CLEAR_SPACE); 
		add(penWidthClearSpace,EAST); 
	}

	/* Method: addLineWidth
	 * --------------------
	 * The addLineWidth method adds the "Line Width" label and corresponding 
	 * line width buttons 
	 */
	private void addLineWidth() {
		JLabel lineWidthLabel = addLabel("LINE WIDTH",SIDEBAR_FONT);
		add(lineWidthLabel,EAST); 
		lineWidthLabel.setBackground(LABEL_COLOR);
		lineWidthLabel.setOpaque(true);
		lineWidthLabel.setPreferredSize(new Dimension(0,LABEL_HEIGHT));

		minusLineWidth = new JButton("-"); 
		plusLineWidth = new JButton("+");
		lineWidthCounterLabel = addLabel("" + lineWidthCounter,SIDEBAR_FONT); 

		JPanel lineWidthPanel = new JPanel(new TableLayout(1,3,SUB_BUTTON_SPACING,0));
		add(lineWidthPanel,EAST);
		lineWidthPanel.add(minusLineWidth); 
		lineWidthPanel.add(lineWidthCounterLabel);
		lineWidthPanel.add(plusLineWidth);
		minusLineWidth.setPreferredSize(new Dimension(SUB_BUTTON_WIDTH,SUB_BUTTON_HEIGHT));
		plusLineWidth.setPreferredSize(new Dimension(SUB_BUTTON_WIDTH,SUB_BUTTON_HEIGHT));

		JLabel lineWidthClearSpace = addLabel(" ",CLEAR_SPACE); 
		add(lineWidthClearSpace,EAST); 
	}

	/* Method: addLineStyle
	 * --------------------
	 * The addLineStyle method adds the "Line Style" label and corresponding
	 * line style buttons 
	 */
	private void addLineStyle() {
		JLabel lineStyleLabel = addLabel("LINE STYLE",SIDEBAR_FONT);
		add(lineStyleLabel,EAST); 
		lineStyleLabel.setBackground(LABEL_COLOR);
		lineStyleLabel.setOpaque(true);
		lineStyleLabel.setPreferredSize(new Dimension(SIDEBAR_WIDTH,LABEL_HEIGHT));

		mirroredLines = new JButton("Mirrored"); 
		rotatedLines = new JButton("Rotated");

		JPanel lineStylePanel = new JPanel(new TableLayout(1,2,SUB_BUTTON_SPACING,0));
		add(lineStylePanel,EAST);
		lineStylePanel.add(mirroredLines); 
		lineStylePanel.add(rotatedLines); 

		JLabel lineStyleClearSpace = addLabel(" ",CLEAR_SPACE); 
		add(lineStyleClearSpace,EAST); 
	}

	/* Method: addLogo
	 * ----------------
	 * The addLogo method adds the Logo label 
	 */
	private void addLogo() {
		JLabel logoLabel = addLabel(LOGO,LOGO_FONT); 
		add(logoLabel,EAST); 

		JLabel logoClearSpace = addLabel(" ",CLEAR_SPACE); 
		add(logoClearSpace,EAST); 
	}

	/* Method: addLabel
	 * -----------------
	 * The addLabel method creates a JLabel based on an input text string
	 * and text font 
	 * Parameters: 
	 * text - the text to be displayed in the JLabel 
	 * font - the desired text font 
	 * Output: 
	 * label - JLabel with input text and text font 
	 */
	private JLabel addLabel(String text, Font font) {
		JLabel label = new JLabel(text);
		label.setFont(font);
		label.setHorizontalAlignment(JLabel.CENTER);
		return label; 
	}

	/* Method: actionPerformed(e)
	 * --------------------------
	 * The actionPerformed method is called any time an interactor is used
	 * in the program screen.  
	 */
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource(); 
		if(source == mirroredLines) {
			lineStyleCounter = 1; 
			userSettings = new GraphicsContestUserSettings(lineStyleCounter,lineWidthCounter,colorSchemeCounter,backgroundColor);
			graph.clearColorGradient();
			graph.addEntry(userSettings);
		} else if(source == rotatedLines) {
			lineStyleCounter = 2;
			userSettings = new GraphicsContestUserSettings(lineStyleCounter,lineWidthCounter,colorSchemeCounter,backgroundColor);
			graph.clearColorGradient();
			graph.addEntry(userSettings);
		} else if (source == minusLineWidth) {
			lineWidthCounter = userSettings.minusLineWidth(); 
			userSettings = new GraphicsContestUserSettings(lineStyleCounter,lineWidthCounter,colorSchemeCounter,backgroundColor);
			lineWidthCounterLabel.setText("" + lineWidthCounter);
			graph.clearColorGradient();
			graph.addEntry(userSettings);
		} else if(source == plusLineWidth) {
			lineWidthCounter = userSettings.plusLineWidth(); 
			userSettings = new GraphicsContestUserSettings(lineStyleCounter,lineWidthCounter,colorSchemeCounter,backgroundColor);
			lineWidthCounterLabel.setText("" + lineWidthCounter);
			graph.clearColorGradient();
			graph.addEntry(userSettings);
		} else if(source == whiteButton) {
			backgroundColor = Color.WHITE;
			userSettings = new GraphicsContestUserSettings(lineStyleCounter,lineWidthCounter,colorSchemeCounter,backgroundColor);
			graph.clearColorGradient();
			graph.addEntry(userSettings);
			graph.updateBackgroundColor();	
		} else if(source == blueButton) {
			backgroundColor = Color.BLUE;
			userSettings = new GraphicsContestUserSettings(lineStyleCounter,lineWidthCounter,colorSchemeCounter,backgroundColor);
			graph.clearColorGradient();
			graph.addEntry(userSettings);
			graph.updateBackgroundColor();	
		} else if(source == blackButton) {
			backgroundColor = Color.BLACK;
			userSettings = new GraphicsContestUserSettings(lineStyleCounter,lineWidthCounter,colorSchemeCounter,backgroundColor);
			graph.clearColorGradient();
			graph.addEntry(userSettings);
			graph.updateBackgroundColor();	 
		} else if(e.getActionCommand().equals("<")) {
			colorSchemeCounter = userSettings.minusColorScheme(); 
			graph.clearColorGradient();
			userSettings = new GraphicsContestUserSettings(lineStyleCounter,lineWidthCounter,colorSchemeCounter,backgroundColor);
			colorSchemeButton.setIcon(colorSchemes.get(colorSchemeCounter));
			graph.addEntry(userSettings);
		} else if(e.getActionCommand().equals(">")){
			colorSchemeCounter = userSettings.plusColorScheme(); 
			graph.clearColorGradient();
			userSettings = new GraphicsContestUserSettings(lineStyleCounter,lineWidthCounter,colorSchemeCounter,backgroundColor);
			colorSchemeButton.setIcon(colorSchemes.get(colorSchemeCounter));
			graph.addEntry(userSettings);
		} else if(e.getActionCommand().equals("CLEAR")){
			graph.clearColorGradient();
			graph.addEntry(userSettings);
			graph.clear();	
		} 
	}

	/** INSTANCE VARIABLES **/ 
	/* set initial values */ 
	private int lineStyleCounter = 1; 
	private int lineWidthCounter = 1; 
	private int colorSchemeCounter = 0; 
	private Color backgroundColor = Color.WHITE; 

	/* initiate interactors */ 
	private JButton mirroredLines; 
	private JButton rotatedLines; 
	private JButton minusLineWidth; 
	private JButton plusLineWidth; 
	private JButton whiteButton;
	private JButton blueButton; 
	private JButton blackButton;
	private JButton colorSchemeButton; 
	private JLabel lineWidthCounterLabel; 

	/* initiate color schemes */ 
	private ArrayList<ImageIcon> colorSchemes = new ArrayList<ImageIcon>();

	/* initiate new class */ 
	private GraphicsContestUserSettings userSettings = new GraphicsContestUserSettings(lineStyleCounter,lineWidthCounter,colorSchemeCounter,backgroundColor);
	private GraphicsContestGraph graph = new GraphicsContestGraph();

}