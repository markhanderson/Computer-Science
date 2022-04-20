/*
 * File: NameSurferDataBase.java
 * Name: Julianne Crawford
 * Section Leader: Peter Maldonado
 * -----------------------------
 * Purpose: The NameSurferDataBase class keeps track of the complete 
 * database of names from an imported file. The method 
 * findEntry makes it possible to look up a specific name 
 * and get back the corresponding NameSurferEntry which includes
 * the name and the names rankings over the past 10 decades. 
 * The class is case-insensitive. 
 */

import acm.util.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class NameSurferDataBase implements NameSurferConstants {

	/* Constructor: NameSurferDataBase(filename) 
	 * ------------------------------------------
	 * The NameSurferDataBase method creates a new database and initializes it 
	 * using the data in the specified filed. 
	 * Parameters: 
	 * filename - the name of the file you want to create a database from 
	 */
	public NameSurferDataBase(String filename) {
		this.filename = filename; 
		try {
			Scanner sc = new Scanner(new File(filename)); 
			while(sc.hasNextLine()) {
				String dataLine = sc.nextLine(); 
				String[] dataLineSplit = dataLine.split(" ");

				int[] ranksArray = new int[NDECADES]; 
				for(int i = 0; i < NDECADES; i++) {
					int rankInt = Integer.parseInt(dataLineSplit[i+1]); 
					ranksArray[i] = rankInt;
				}

				dataLinesHashMap.put(dataLineSplit[0].toLowerCase(),ranksArray); 
			}
			sc.close(); 
		} catch(IOException e) {
			e.printStackTrace(); 
		}
	}

	/* Method: findEntry(name)
	 * -----------------------
	 * The findEntry method returns the NameSurferEntry associated with an input 
	 * name. If the name does not appear in the database, the findEntry method 
	 * returns null. 
	 * Parameters: 
	 * name - user input baby name (into the textfield interactor)  
	 */
	public NameSurferEntry findEntry(String name) {
		if(dataLinesHashMap.containsKey(name)) {
			String nameSurferString = name; 
			for(int i = 0; i < NDECADES; i++) {
				int rankValue = dataLinesHashMap.get(name)[i]; 
				String rankValueString = "" + rankValue;
				nameSurferString += " " + rankValueString; 
			}
			NameSurferEntry nameSurferEntry = new NameSurferEntry(nameSurferString); 
			return nameSurferEntry; 
		} else {
			return null;
		}
	}

	/** Private instance variables **/
	/* name of file used to create a database */ 
	private String filename; 

	/* HashMap to store all of the name and rank combinations */ 
	private HashMap<String,int[]> dataLinesHashMap = new HashMap<String,int[]>(); 
}

