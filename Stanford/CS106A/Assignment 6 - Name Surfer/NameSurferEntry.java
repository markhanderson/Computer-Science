/*
 * File: NameSurferEntry.java
 * Name: Julianne Crawford
 * Section Leader: Peter Maldonado
 * --------------------------
 * Purpose: The NameSurfer class represents a single entry in the 
 * names-data.txt database. Each NameSurferEntry contains a name and
 * a list giving the popularity rank of that name for each decade 
 * dating back to 1900.
 */

public class NameSurferEntry implements NameSurferConstants {

	/* given line of data from names-data.txt*/ 
	private String dataLine;
	
	/* Each dataLine has a babyName */ 
	private String babyName; 
	
	/* Each dataLine has baby name rank values for 11 decades*/ 
	private int[] rank = new int[NDECADES]; 

	/* Method: NameSurferEntry(dataLine)
	 * ---------------------------------
	 * The NameSurferEntry constructor creates a new NameSurferEntry from a data
	 * line as it appears in the names-data.txt file. Each line of a dataLine 
	 * begins with a name followed by 11 rank values for the 11 decades between 1900
	 * to 2000
	 * Parameters: 
	 * dataLine - a single String line from the names-data.txt file containing a baby
	 * name and rank values for the 11 decades between 1900 and 2000
	 */
	public NameSurferEntry(String dataLine) {
		this.dataLine = dataLine; 
		String[] dataLineSplit = this.dataLine.split(" "); 
		babyName = dataLineSplit[0]; 
		for(int i = 0; i < NDECADES; i++) {
			int rankInt = Integer.parseInt(dataLineSplit[i+1]); 
			rank[i] = rankInt;
		}
	}

	/* Method: getName()
	 * -----------------
	 * The getName method returns the name associated with a specific data line entry 
	 * Output:
	 * babyName - a baby name associated with a specific data line entry 
	 */
	public String getName() {
		return babyName;
	}

	/* Method: getRank(decade)
	 * ------------------------
	 * The getRank method returns the rank associated with an entry 
	 * for a specific decade. 
	 * Parameters: 
	 * decade - an integer value indicating how many decades have passed
	 * since the first year in the database (in this case, 1900)
	 * Output: 
	 * int - an integer value representing the rank of a baby name's 
	 * popularity for a specific decade. Integer values range from 0 to 
	 * 1000 with lower integers representing more popular baby names. 
	 */
	public int getRank(int decade) {
		if(decade >= NDECADES) {
			return -1; 
		} else {
			return rank[decade]; 
		}
	}

	/* Method: toString()
	 * -------------------
	 * The toString method returns the NameSurferEntry as an easy-to-read 
	 * String to the user 
	 * Output: 
	 * returnString - String showing the name and rank values for a specific 
	 * NameSurferEntry
	 */
	public String toString() {
		String returnString = ""; 
		returnString += babyName + " ["; 
		for(int i = 0; i < NDECADES-1; i++) {
			returnString += rank[i] + ", "; 
		}
		returnString += rank[NDECADES-1] + "]"; 
		return returnString;
	}
}

