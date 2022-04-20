/* 
 * File: FacePamphletClient.java
 * Name: Julianne Crawford
 * Section Leader: Peter Maldonado
 * ------------------------------
 * Purpose: The purpose of FacePamphletClient is to interact with the
 * FacePamphletServer. The FacePamphletClient generates requests and 
 * sends them to the server based on the user's interactions with the 
 * canvas
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.*;
import javax.swing.*;

public class FacePamphletClient extends GraphicsProgram {

	/** Number of characters for each of the text input fields */
	public static final int TEXT_FIELD_SIZE = 15;

	/** Name of font used to display the application message at the
	 *  bottom of the display canvas */
	public static final String MESSAGE_FONT = "Dialog-18";

	/** Name of font used to display the name in a user's profile */
	public static final String PROFILE_NAME_FONT = "Dialog-24";

	/** The number of pixels in the vertical margin between the bottom 
	 *  of the canvas display area and the baseline for the message 
	 *  text that appears near the bottom of the display */
	public static final double BOTTOM_MESSAGE_MARGIN = 20;

	/** The number of pixels in the horizontal margin between the 
	 *  left side of the canvas display area and the Name, Image, and 
	 *  Status components that are display in the profile */	
	public static final double LEFT_MARGIN = 20;	

	/** The number of pixels in the vertical margin between the top 
	 *  of the canvas display area and the top (NOT the baseline) of 
	 *  the Name component that is displayed in the profile */	
	public static final double TOP_MARGIN = 20;	

	/** The address of the server that should be contacted when sending
	 * any Requests. */
	private static final String HOST = "http://localhost:8000/";

	/* Method: init
	 * -------------
	 * The init method is called before the window is created. In this case, 
	 * it initiates the interactors 
	 */
	public void init() {
		JLabel nameLabel = new JLabel("Name"); 
		add(nameLabel,NORTH);

		textField = new JTextField(TEXT_FIELD_SIZE); 
		add(textField,NORTH);

		JButton addButton = new JButton("Add"); 
		add(addButton,NORTH); 

		JButton deleteButton = new JButton("Delete"); 
		add(deleteButton,NORTH); 

		JButton lookupButton = new JButton("Lookup"); 
		add(lookupButton,NORTH); 
	}

	/* Method: run
	 * ------------
	 * The run method is called after the window is created 
	 */
	public void run() {
		addActionListeners(); 
	}

	/* Method: actionPerformed(e)
	 * --------------------------
	 * The actionPerformed method is called any time an interactor is used
	 * in the program screen.  
	 */
	public void actionPerformed(ActionEvent event) {
		if(event.getActionCommand().equals("Add")) {
			addProfile(); 
		} else if(event.getActionCommand().equals("Delete")) {
			deleteProfile(); 
		} else if(event.getActionCommand().equals("Lookup")) {
			lookUpProfile(); 
		}
	}

	/* Method: lookUpProfile
	 * ----------------------
	 * The lookUpProfile method attempts to display the profile corresponding
	 * to the given name in the FacePamphlet social network
	 */
	private void lookUpProfile() {
		String name = textField.getName(); 
		try {
			Request request = new Request("containsProfile"); 
			request.addParam("name",name);
			String response = SimpleClient.makeRequest(HOST, request); 
			if(response.equals("true")) {
				clear(); 
				displayProfile(name); 
				addBottomMessage("Displaying " + name); 
			} else if(response.equals("false")) {
				clear(); 
				addBottomMessage("A profile with the name " + name + " does not exist");
			}
		} catch(IOException e) {
			//addBottomMessage(e.getMessage());
		}
	}

	/* Method: displayProfile
	 * The displayProfile method displays the profile for the given name 
	 * Parameters: 
	 * name - the name associated with the profile to be displayed 
	 */
	private void displayProfile(String name) {
		addProfileNameLabel(name); 
	}

	/* Method: deleteProfile
	 * ----------------------
	 * The deleteProfile method attempts to delete the profile for the entered
	 * name from the FacePamphlet social network. If the profile is successfully 
	 * deleted, it displays a success message at the bottom of the canvas 
	 */
	private void deleteProfile() {
		String name = textField.getText(); 
		try{
			clear(); 
			Request request = new Request("deleteProfile");
			request.addParam("name",name); 
			SimpleClient.makeRequest(HOST,request); 
			addBottomMessage("Profile of " + name + " deleted"); 
		} catch (IOException e) {
			addBottomMessage(e.getMessage());
		}
	}

	/* Method: addProfile
	 * -------------------
	 * The addProfile method attempts to add a new profile to the 
	 * FacePamphlet social network. If the profile is successfully added, it 
	 * displays the corresponding profile name on the canvas and a success
	 * message at the bottom of the canvas
	 */
	private void addProfile() {
		String name = textField.getText(); 
		try {
			clear(); 
			Request request = new Request("addProfile"); 
			request.addParam("name", name);
			SimpleClient.makeRequest(HOST, request);
			displayProfile(name); 
			addBottomMessage("New profile created"); 
		} catch (IOException e) {
			addBottomMessage(e.getMessage());
		}
	}

	/* Method: addprofileNameLabel
	 * -------------------------
	 * The addProfileNameLabel adds a profile name label when a new profile
	 * is created
	 * Parameters: 
	 * name - the profile name to be added to the canvas 
	 */
	private void addProfileNameLabel(String name) {
		GLabel profileNameLabel = new GLabel(name); 
		profileNameLabel.setFont(PROFILE_NAME_FONT);
		profileNameLabel.setColor(Color.BLUE);
		add(profileNameLabel,LEFT_MARGIN,TOP_MARGIN + profileNameLabel.getAscent()); 
	}

	/* Method: addBottomMessage
	 * -------------------------
	 * The addBottomMessage adds a message at the bottom of the canvas 
	 * Parameters:
	 * message - message to be displayed at the bottom of the canvas
	 */
	private void addBottomMessage(String message) {
		GLabel bottomMessage = new GLabel(message); 
		bottomMessage.setFont(MESSAGE_FONT); 
		add(bottomMessage,getWidth()/2-bottomMessage.getWidth()/2,getHeight()-BOTTOM_MESSAGE_MARGIN); 
	}

	/** Private instance variables **/
	private JTextField textField = null; 
}
