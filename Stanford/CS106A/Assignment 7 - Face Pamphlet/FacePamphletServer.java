/*
 * File: FacePamphletServer.java
 * Name: Julianne Crawford
 * Section Leader: Peter Maldonado
 * ------------------------------
 * Purpose: The FacePamphletServer implements a basic social network 
 * management server. This server stores all of the data and contains 
 * the logic for keeping track of profiles and getting and setting 
 * profile properties. Each time the FacePamphletServer receives a request
 * from the client, it updates its internal data and sends back a 
 * string response. 
 */

import java.util.ArrayList;
import java.util.HashMap;
import acm.graphics.GImage;
import acm.program.*;

public class FacePamphletServer extends ConsoleProgram 
implements SimpleServerListener {

	/* The internet port to listen to requests on */
	private static final int PORT = 8000; 

	/* Method: run
	 * ------------
	 * The run method starts the server running so that when a program 
	 * sends a request to this computer, the method requestMade is called
	 */
	public void run() {
		println("Starting server on port " + PORT);
		server.start();
	}

	/* Method: requestMade
	 * --------------------
	 * The requestMade method is called whenever a request is sent to this computer
	 * Parameters: 
	 * request - request sent to the computer
	 * Output: 
	 * String - result of the request sent to the computer 
	 */
	public String requestMade(Request request) {
		String cmd = request.getCommand();
		if(cmd.equals("ping")) {
			return("Hello, internet"); 
		} else if(cmd.equals("addProfile")){
			return(addProfile(request)); 
		} else if(cmd.equals("containsProfile")){
			return(containsProfile(request)); 
		} else if(cmd.equals("deleteProfile")) {
			return(deleteProfile(request)); 
		} else if(cmd.equals("setStatus")) {
			return(setStatus(request)); 
		} else if(cmd.equals("getStatus")) {
			return(getStatus(request)); 
		} else if(cmd.equals("setImage")) {
			return(setImage(request)); 
		} else if(cmd.equals("getImage")) {
			return(getImage(request)); 
		} else if(cmd.equals("addFriend")) {
			return(addFriend(request)); 
		} else if(cmd.equals("getFriends")) {
			return(getFriends(request)); 
		} else {
			return "Error: Unknown command " + cmd + ".";
		}
	}

	/* Method: getFriends
	 * ------------------
	 * The getFriends method returns the list of friends for the user with
	 * the given name
	 * Parameters: 
	 * request - getFriends request sent to the computer 
	 * Output: 
	 * String - list of friends if profile exists; "Error: " if the profile 
	 * does not exist 
	 */
	private String getFriends(Request request) {
		String name = request.getParam("name"); 
		if(profileHashMap.get(name)!= null) {
			return(profileHashMap.get(name).getFriends().toString());
		} else {
			return "Error: Database does not contain " + name;
		}
	}

	/* Method: addFriend
	 * ------------------
	 * The addFriend method makes the user with name name1 friends with user
	 * with the name nam2, and vice versa
	 * Parameters:
	 * request - addFriend request sent to the computer 
	 * Output: 
	 * String - "success" if friend is successfully added; "Error: " if either 
	 * user does not exist, if they are already friends, or if they are the same
	 * person 
	 */
	private String addFriend(Request request) {
		String name1 = request.getParam("name1"); 
		String name2 = request.getParam("name2"); 
		boolean condition1 = profileHashMap.get(name1) != null; 
		boolean condition2 = profileHashMap.get(name2) != null; 
		boolean condition3 = !name1.equals(name2); 
		boolean condition4 = !profileHashMap.get(name1).getFriends().contains(name2);
		if(condition1 && condition2 && condition3 && condition4){
			profileHashMap.get(name1).addFriend(name2); 
			profileHashMap.get(name2).addFriend(name1); 
			return "success"; 
		} else {
			return "Error: This request cannot be executed"; 
		}
	}

	/* Method: getImage
	 * -----------------
	 * The getImage method returns the profile image of the user with the
	 * given name, or the empty string if the user exists but does not have
	 * an image
	 * Parameters: 
	 * request - getImage request sent to the computer 
	 * Output: 
	 * String - returns the profile image of the user (as a String) if the 
	 * user with the give name exists, or the empty String if the user exists but
	 * does not have an image, or "Error: " if the profile does not exist 
	 */
	private String getImage(Request request) {
		String name = request.getParam("name"); 
		if(profileHashMap.get(name)!= null) {
			GImage image = profileHashMap.get(name).getImage(); 
			String imageString = SimpleServer.imageToString(image); 
			return(imageString); 
		} else {
			return "Error: Database does not contain " + name; 
		}
	}

	/* Method: setImage
	 * -----------------
	 * The setImage method sets the image for the user with the given name. 
	 * Parameters: 
	 * request - setImage request sent to the computer 
	 * Output: 
	 * String - "success" if the profile exists; "Error: " if the profile does
	 * not exist
	 */
	private String setImage(Request request) {
		String name = request.getParam("name"); 
		String imageString = request.getParam("imageString"); 
		GImage image = SimpleServer.stringToImage(imageString); 
		if(profileHashMap.get(name)!= null) {
			profileHashMap.get(name).setImage(image);
			return "success"; 
		} else {
			return "Error: Database does not contain " + name; 
		}
	}

	/* Method: getStatus
	 * -----------------
	 * The getStatus method returns the status of the user with the given 
	 * name, or the empty string if the user exists but does not have a status
	 * Parameters: 
	 * request - getStatus request sent to the computer
	 * Output: 
	 * String - the status of the user with the given name, or "" if the user 
	 * exists but does not have a status
	 */
	private String getStatus(Request request) {
		String name = request.getParam("name"); 
		if(profileHashMap.get(name)!= null) {
			return(profileHashMap.get(name).getStatus()) ;
		} else {
			return("Error: Database does not contain " + name);
		}
	}

	/* Method: setStatus
	 * ------------------
	 * The setStatus method sets the status of the user with the given name
	 * Parameters: 
	 * request - setStatus request sent to the computer
	 * Output: 
	 * String - "success" if profile exists; "Error: " if profile does not exist 
	 */
	private String setStatus(Request request) {
		String name = request.getParam("name"); 
		String status = request.getParam("status");
		if(profileHashMap.get(name)!= null) {
			profileHashMap.get(name).setStatus(status);
			return "success"; 
		} else {
			return "Error: Database does not contain " + name;
		}
	}

	/* Method: addProfile 
	 * -------------------
	 * The addProfile method creates a new profile with the given name.  
	 * Parameters: 
	 * request - addProfile request sent to the computer
	 * Output: 
	 * String - "success" if profile does not already exist; "Error: " if profile 
	 * already exists 
	 */
	private String addProfile(Request request) {
		String name = request.getParam("name"); 
		if(profileHashMap == null) {
			profile = new FacePamphletProfile(name); 
			profileHashMap.put(name,profile);
			return "success"; 
		} else {
			if(!profileHashMap.containsKey(name)) {
				profile = new FacePamphletProfile(name);
				profileHashMap.put(name,profile); 
				return "success"; 
			} else {
				return "Error: Database already contains " + name; 
			}
		}			
	}

	/* Method: containsProfile
	 * ------------------------
	 * The containsProfile method checks whether or not a given name exists.
	 * Parameters: 
	 * request - containsProfile request sent to the computer 
	 * Output:
	 * String - "true" if a profile with the given name exists; "false" if a 
	 * profile with the given name does not exist 
	 */
	private String containsProfile(Request request) {
		String name = request.getParam("name"); 
		if(profileHashMap.containsKey(name)) {
			return "true"; 
		} else {
			return "false"; 
		}
	}

	/* Method: deleteProfile
	 * ----------------------
	 * The deleteProfile method removes the profile with the given name from 
	 * the database.
	 * Parameters: 
	 * request - deleteProfile request sent to the computer 
	 * Output: 
	 * String - "success" if the profile exists; "Error: " if the profile
	 * does not exist. 
	 */
	private String deleteProfile(Request request) {
		String name = request.getParam("name");
		if(profileHashMap.containsKey(name)) {
			ArrayList<String> friendList = profileHashMap.get(name).getFriends();
			for(int i = 0; i < friendList.size(); i++) {
				profileHashMap.get(friendList.get(i)).removeFriend(name); 
			}
			profileHashMap.remove(name); 
			return "success"; 
		} else {
			return "Error: Database does not contain " + name;
		}
	}

	/** Private instance variables **/
	/* The server object. All you need to do is start it */
	private SimpleServer server = new SimpleServer(this, PORT);

	/* HashMap to store all of the created profiles with their associated name */
	private HashMap<String,FacePamphletProfile> profileHashMap = new HashMap<String,FacePamphletProfile>();

	/* FacePamphletProfile */
	private FacePamphletProfile profile; 

}
