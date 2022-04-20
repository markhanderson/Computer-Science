/*
 * File: Hailstone.java
 * Name: Julianne Crawford
 * Section Leader: Peter Maldonado
 * --------------------
 * Purpose: The Hailstone program receives as an input from 
 * the user a positive integer value. Through a series of steps
 * the original integer value becomes one. Specifically, any 
 * time the program encounters an even number, it divides the 
 * number by two, and any time the program encounters an odd 
 * number, it multiplies the number by three and adds one. This 
 * process continues until the number equals one. Once this 
 * process ends, the program also outputs the total number of 
 * steps taken to reach one. 
 */

import acm.program.*;

public class Hailstone extends ConsoleProgram {
	public void run() {
		int n = readInt("Enter a positive number: ");
		// initiates a step counter
		int steps = 0; 
		while(n != 1) {
			// checks if n is even or odd
			if(n%2 == 0) {
				// if n is even 
				int even_num = n; 
				n = n/2; 
				println(even_num + " is even so I take half: " + n);
				// adds a count to the step counter
				steps = steps + 1; 
			} else {
				// if n is odd
				int odd_num = n; 
				n = 3*n+1;
				println(odd_num + " is odd, so I make 3n+1: " + n);
				// adds a count to the step counter
				steps = steps + 1; 
			}
		}
		println("The process took " + steps + " steps to reach 1");
	}
}

