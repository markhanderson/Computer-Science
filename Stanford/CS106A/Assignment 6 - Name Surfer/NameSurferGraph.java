/*
 * File: NameSurferGraph.java
 * Name: Julianne Crawford
 * Section Leader: Peter Maldonado
 * ---------------------------
 * Purpose: the NameSurferGraph class represents the canvas on 
 * which the graph of names and ranks are drawn. The class is 
 * responsible for updating the graphs whenever the list of 
 * entries changes or the window changes size.
 */

import acm.graphics.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;

public class NameSurferGraph extends GCanvas

implements NameSurferConstants, ComponentListener {

	/* The number of colors in the rotating color pattern */
	private static final int COLOR_PATTERN = 4;

	/* Method: NameSurferGraph
	 * ------------------------
	 * Constructor: The NameSurferGraph constructor creates a new 
	 * NameSurferGraph object to display the baby-name-ranking data
	 */
	public NameSurferGraph() {		
		addComponentListener(this);
	}

	/* Method: clear
	 * -------------
	 * The clear method clears the list of names and ranks to effectively
	 * delete all of the displayed entries on the graph 
	 */
	public void clear() {
		namesStore.clear(); 
		ranksStore.clear(); 
	}

	/* Method: addEntry(entry)
	 * -----------------------
	 * The addEntry method adds a new NameSurferEntry to the list of entries
	 * on the graph display. 
	 * Parameters: 
	 * entry - the NameSurferEntry name input by the user in the GUI textfield 
	 */
	public void addEntry(NameSurferEntry entry) {
		if(entry != null) {
			namesStore.add(entry.getName());
			int[] ranks = new int[NDECADES]; 
			for(int i = 0; i < NDECADES; i++) {
				ranks[i] = entry.getRank(i); 
			}
			ranksStore.add(ranks);
		}
	}

	/* Method: update()
	 * ----------------
	 * The update method updates the graph display image by deleting all the 
	 * graphical objects from the canvas and re-drawing the display based on 
	 * the list of user name entries. 
	 */
	public void update() {
		removeAll();
		drawMarginLines();
		drawDecades(); 
		drawAllEntryLines(); 
	}

	/* Method: drawAllEntryLines
	 * -------------------------
	 * the drawAllEntryLines method takes the stored list of NameSurferEntryLine 
	 * values and plots them individually on the graph 
	 */
	private void drawAllEntryLines() {
		ArrayList<GLine[]> allLines = new ArrayList<GLine[]>(); 
		for(int store = 0; store < ranksStore.size(); store++) {
			GLine[] entryLine = drawSingleEntryLine(store);
			allLines.add(entryLine); 
			for(int i = 0; i < NDECADES-1; i++) {
				add(allLines.get(store)[i]); 
				Color color = lineColor(store); 
				allLines.get(store)[i].setColor(color);
			}
		}
	}

	/* Method: drawSingleEntryLine
	 * --------------------------------
	 * The drawSingleEntryLine method assigns the coordinates to plot a single 
	 * NameSurferEntryLine value and draws a line between all sets of 
	 * coordinates. Furthermore, it plots a label displaying the baby name and
	 * its respective popularity in the given decade
	 * Parameters: 
	 * store - the integer index value of the NameSurferEntryLine 
	 */
	private GLine[] drawSingleEntryLine(int store) {
		GLine[] entryLine = new GLine[NDECADES]; 
		GLabel[] entryLineLabel = new GLabel[NDECADES];
		Color color = lineColor(store);

		for(int i = 0; i < NDECADES; i++) {
			if(i == NDECADES-1) {
				double[] lastLabelPoint = getPoint(i,store); 
				String lastLabel = getLabel(i,store); 
				entryLineLabel[i] = new GLabel(lastLabel,lastLabelPoint[0],lastLabelPoint[1]);
				entryLineLabel[i].setColor(color);
				add(entryLineLabel[i]); 
			} else {
				double[] startPoint = getPoint(i,store); 
				double[] endPoint = getPoint(i+1,store); 
				String label = getLabel(i,store); 
				entryLine[i] = new GLine(startPoint[0],startPoint[1],endPoint[0],endPoint[1]); 
				entryLineLabel[i] = new GLabel(label,startPoint[0],startPoint[1]);
				entryLineLabel[i].setColor(color);
				add(entryLineLabel[i]);  
			}
		}
		return(entryLine); 
	}

	/* Method: getLabel
	 * ----------------
	 * The getLabel method returns the name label for the single entry line 
	 * Parameters: 
	 * index - the decade index
	 * store - the integer index value of the NameSurferEntryLine 
	 * Output: 
	 * label - name label for the corresponding decade 
	 */
	private String getLabel(int index, int store) {
		String nameCase = nameCase(namesStore.get(store)); 
		String label = nameCase + " *"; 
		if(ranksStore.get(store)[index] != 0) { 
			label = nameCase + " " + ranksStore.get(store)[index]; 
		}
		return(label); 
	}
	
	/* Method: getPoint
	 * -----------------
	 * The getPoint method returns the coordinates of a point to plot on the graph
	 * Parameters: 
	 * index - the decade index
	 * store - the integer index value of the NameSurferEntryLine
	 * Output: 
	 * point - the x and y coordinates to plot on the graph for the corresponding decade 
	 */
	private double[] getPoint(int index, int store) {
		double decadeLineSpacing = getWidth()/NDECADES; 
		double xCoordinate = decadeLineSpacing*index; 
		double yNormalization = (getHeight()-2*GRAPH_MARGIN_SIZE)/MAX_RANK;
		double yCoordinate = getHeight()-GRAPH_MARGIN_SIZE;
		if(ranksStore.get(store)[index] != 0) {
			yCoordinate =GRAPH_MARGIN_SIZE + ranksStore.get(store)[index]*yNormalization; 
		} 
		double[] point = {xCoordinate,yCoordinate}; 
		return point; 
	}
	
	/* Method: nameCase
	 * -----------------
	 * The nameCase method takes in the user-entered baby name and 
	 * changes its case for display
	 * Parameters: 
	 * name - the user-entered baby name 
	 */
	private String nameCase(String name) {
		String nameCase = ""; 
		for(int i = 0; i < name.length(); i++) {
			char ch = name.charAt(i); 
			if(i == 0) {
				ch = Character.toUpperCase(ch); 
			} 
			nameCase += ch; 
		}
		return nameCase; 
	}
	
	/* Method: lineColor 
	 * -----------------
	 * The lineColor method assigns each line in the stored list of NameSurferEntryLine
	 * values a different color in a 4-color pattern so that they are easier to 
	 * distinguish on the graph
	 * Parameters: 
	 * store - the integer index value of the NameSurferEntryLine 
	 */
	private Color lineColor(int store) {
		Color color = Color.RED; 
		double colorCounter = Math.floor(store/COLOR_PATTERN);
		if (store <= 0 + COLOR_PATTERN*colorCounter) {
			color = Color.BLACK;
		} else if (store > 0 + COLOR_PATTERN*colorCounter && store <= 1 + COLOR_PATTERN*colorCounter) {
			color = Color.RED; 
		} else if (store > 1 + COLOR_PATTERN*colorCounter && store <= 2 + COLOR_PATTERN*colorCounter) {
			color = Color.BLUE;
		} else if (store > 2 + COLOR_PATTERN*colorCounter && store <= 3 + COLOR_PATTERN*colorCounter) {
			color = Color.MAGENTA;
		}
		return color; 
	}

	/* Method: createMarginLines
	 * --------------------------
	 * The createMarginLines method draws the top and bottom horizontal margin lines
	 * in the graph window
	 */
	private void drawMarginLines() {
		GLine topMarginLine = new GLine(0,GRAPH_MARGIN_SIZE,getWidth(),GRAPH_MARGIN_SIZE); 
		add(topMarginLine); 
		GLine bottomMarginLine = new GLine(0,getHeight()-GRAPH_MARGIN_SIZE,getWidth(),getHeight()-GRAPH_MARGIN_SIZE);
		add(bottomMarginLine); 
	}

	/* Method: drawDecades
	 * -------------------
	 * The drawDecades method draws the evenly-spaced vertical lines representing 
	 * the decades. Furthermore, this method adds labels at the base of the vertical
	 * lines to show which respective decade they represent between 1900 and 2000
	 */
	private void drawDecades() {
		double decadeLineSpacing = getWidth()/NDECADES; 
		for(int i = 0; i < NDECADES; i++) {
			GLine decadeLine = new GLine(decadeLineSpacing*i,0,decadeLineSpacing*i,getHeight()); 
			add(decadeLine); 
			int decade = START_DECADE + DECADE*i; 
			String decadeString = "" + decade; 
			GLabel decadeLabel = new GLabel(decadeString,decadeLineSpacing*i,getHeight()-DECADE_LABEL_MARGIN_SIZE); 
			add(decadeLabel); 
		}
	}

	/* Implementation of the ComponentListener interface */
	public void componentHidden(ComponentEvent e) { }
	public void componentMoved(ComponentEvent e) { }
	public void componentResized(ComponentEvent e) { update(); }
	public void componentShown(ComponentEvent e) { }

	/** Private instance variables **/ 
	/* Associated baby name from the specific NameSurferEntry */ 
	private ArrayList<String> namesStore = new ArrayList<String>();

	/* Associated ranks from the specific NameSurferEntry */ 
	private ArrayList<int[]> ranksStore = new ArrayList<int[]>(); 

}
