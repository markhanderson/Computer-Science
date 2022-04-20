/*
 * File: NameSurfer.java
 * Name: Julianne Crawford
 * Section Leader: Peter Maldonado
 * ---------------------
 * Purpose: The NameSurfer program graphs the popularity of US baby 
 * names from 1900 through 2000 for a given user input name. The 
 * user is able to enter multiple names, and they will all be graphed
 * together. The plot shows the name's rank (between 1 = most popular and 
 * 1000 = least popular) over the past 10 decades.
 */

import acm.program.*;
import java.awt.event.*;
import javax.swing.*;

public class NameSurfer extends Program implements NameSurferConstants {
	/* Method: init 
	 * -------------
	 * The init method initializes the program. It is responsible for 
	 * reading in the database and initializing the interactors
	 */
	public void init() {
		add(graph);

		nameSurferDataBase = new NameSurferDataBase(NAMES_DATA_FILE); 

		JLabel nameLabel = new JLabel("Name"); 
		add(nameLabel,NORTH); 

		nameInput.addActionListener(this);
		nameInput.setActionCommand("Graph");
		add(nameInput,NORTH); 

		JButton graphButton = new JButton("Graph"); 
		add(graphButton,NORTH); 

		JButton clearButton = new JButton("Clear"); 
		add(clearButton,NORTH); 

		addActionListeners(); 
	}

	/* Method: actionPerformed(e)
	 * --------------------------
	 * The actionPerformed method is called any time an interactor is used
	 * in the program screen.  
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Clear")) {
			graph.clear(); 
			graph.update(); 
		} else if(e.getActionCommand().equals("Graph")) {
			String babyName = nameInput.getText().toLowerCase();
			NameSurferEntry entry = nameSurferDataBase.findEntry(babyName); 
			graph.addEntry(entry);
			graph.update();
		}
	}

	/** Private instance variables **/ 
	/* Text field where the user enters a name */
	private JTextField nameInput = new JTextField(TEXT_FIELD_WIDTH); 

	/* database for the name file */
	private NameSurferDataBase nameSurferDataBase; 

	/* graph display */ 
	NameSurferGraph graph = new NameSurferGraph();

}
