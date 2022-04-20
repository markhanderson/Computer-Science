/*
 * File: FacePamphletProfile.java
 * Name: Julianne Crawford
 * Section Leader: Peter Maldonado
 * ---------------------------------
 * The FacePamphletProfile class represents a single FacePamphlet profile. 
 * Each FacePamphletProfile contains a name with which to initiate the state
 * of a new profile. 
 */

import java.util.*;
import acm.graphics.*;

public class FacePamphletProfile {
	
	/* Constructor: FacePamphletProfile
	 * ---------------------------------
	 * The FacePamphletProfile constructor initiates the state of a 
	 * new profile with a given name
	 * Parameters: 
	 * name - name provided by the user to create a new profile for  
	 */
	public FacePamphletProfile(String name) {
		this.name = name; 
	}

	/* Method: getName
	 * ----------------
	 * The getName method returns the name associated with the profile
	 * Output: 
	 * name - the name associated with a given profile 
	 */
	public String getName() {
		return name;
	}

	/* Method: getImage
	 * -----------------
	 * The getImage method returns the image associated with the profile. 
	 * If the profile has no associated image, null is returned. 
	 * Output: 
	 * image - image associated with a given profile 
	 */
	public GImage getImage() {
		if(image == null) {
			return null; 
		} else {
			return image; 
		}
	}

	/* Method: setImage
	 * -----------------
	 * The setImage method sets the image associated with the profile
	 * to the provided image 
	 * Parameters: 
	 * image - image provided by user to set as the profile image for  
	 * the associated profile 
	 */
	public void setImage(GImage image) {
		this.image = image; 
	}

	/* Method: getStatus
	 * ------------------
	 * The getStatus method returns the status associated with the profile.
	 * If there is no associated status, the method returns the empty string
	 * Output: 
	 * status - the status associated with a given profile 
	 */
	public String getStatus() {
		return status; 
	}

	/* Method: setStatus
	 * -----------------
	 * The setStatus method sets the status associated with the profile 
	 * Parameters: 
	 * status - the desired profile status to be set 
	 */
	public void setStatus(String status) {
		this.status = status; 
	}

	/* Method: addFriend
	 * ------------------
	 * The addFriend method adds the named friend to the associated profile's 
	 * list of friends. The method returns "true" if the friend's name was 
	 * not already in the list of friends for the given profile. In this case,
	 * the method also adds the name to the list. Alternatively, the method 
	 * returns "false" if the given friend name was already in the list of 
	 * friends for the given profile. In this case, the given friend name is
	 * not added to the list of friends. 
	 * Parameters: 
	 * friend - A given friend name that the user wants to add to the associated
	 * profile
	 * Output: 
	 * boolean - true/false indicating whether or not the friend name was already
	 * in the associated profile's list of friends 
	 */
	public boolean addFriend(String friend) {
		if(!friendList.contains(friend)) {
			friendList.add(friend); 
			return true; 
		} else {
			return false; 
		}
	}

	/* Method: removeFriend 
	 * ---------------------
	 * The removeFriend method removes the named friend from the associated 
	 * profile's list of friends. The method returns "true" if the friend's 
	 * name was in the list of friends for this profile. In this case, the 
	 * method also removes the name from the list. Alternatively, the method 
	 * returns "false" if the given friend name was not in the list of friends 
	 * for this profile. In this case, the given friends name cannot be removed
	 * Parameters: 
	 * friend - A given friend name that the user would like to e removed from 
	 * the associated profile
	 * Output: 
	 * boolean - true/false indicating whether or not the friend name was already 
	 * in the associated profile's list of friends
	 */
	public boolean removeFriend(String friend) {
		if(friendList.contains(friend)) {
			int removeIndex = friendList.indexOf(friend);  
			friendList.remove(removeIndex); 
			return true; 
		} else {
			return false; 
		}
	}

	/* Method: getFriends
	 * -------------------
	 * The getFriends method returns an ArrayList of friends associated with a 
	 * given profile. 
	 * Output: 
	 * friendList - ArrayList of friends associated with a given profile
	 */
	public ArrayList<String> getFriends() {
		return friendList;
	}
	
	/** private instance variables **/
	private String name = ""; 
	private GImage image = null; 
	private String status = ""; 
	private ArrayList<String> friendList = new ArrayList<String>(); 
}
