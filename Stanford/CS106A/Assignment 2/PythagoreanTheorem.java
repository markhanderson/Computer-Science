/*
 * File: PythagoreanTheorem.java
 * Name: Julianne Crawford 
 * Section Leader: Peter Maldonado
 * -----------------------------
 * Purpose: the PythagoreanTheorem program receives as inputs from 
 * the user two numbers, a and b, which represent the lengths of
 * two legs of a right triangle. The program then uses the 
 * Pythagorean Theorem relationship to calculate the length of the 
 * 3rd leg (the hypotenuse), c. This value is output to the user. 
 */

import acm.program.*;

public class PythagoreanTheorem extends ConsoleProgram {
	public void run() {
		println("Enter positive values to compute the Pythagorean theorem");
		double a = readDouble("a: "); 
		double b = readDouble("b: ");
		double c = Math.sqrt(a*a+b*b);
		println("c = " + c);
	}
}
