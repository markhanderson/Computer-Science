/*
 * File: FindRange.java
 * Name: Julianne Crawford
 * Section Leader: Peter Maldonado
 * --------------------
 * Purpose: the FindRange program receives integer values from the user
 * until the user inputs the sentinel value (TARGET_VALUE). Once the
 * sentinel value is input by the user, the program outputs the 
 * minimum and maximum values received during its run. If the user 
 * only inputs a single value before entering the sentinel value, the
 * program considers this to be both the minimum and maximum value of
 * the run. If the user enters the sentinel value as the first input, 
 * the program outputs an error message telling the user that "no values
 * have been added". Note: the sentinel value can easily be changed from 
 * 0 to any other value. 
 */

import acm.program.*;

public class FindRange extends ConsoleProgram {

	/** Sentinel value */
	private static final int TARGET_VALUE = 0;

	public void run() {
		int num = readInt("Enter a number:");
		// display error message if target value is first number entered
		if(num == TARGET_VALUE) {
			println("no values have been added");
		} else {
			int min = num; 
			int max = num; 
			while(num != TARGET_VALUE) {
				// compare entered number to previous minimum
				if(num <= min) {
					min = num;
				} 
				// compare entered number to previous maximum
				else if(num >= max) {
					max = num; 
				}
				num = readInt("Enter a number:");
			}
			println("smallest: " + min); 
			println("largest: " + max);
		}
	}
}

