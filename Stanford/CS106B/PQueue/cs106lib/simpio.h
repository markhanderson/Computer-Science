/*
 * File: simpio.h
 * --------------
 * Minimal stand-in for the CS106 simpio library: line-based
 * console input. End of input is treated as an empty line, so
 * the test program can be driven from a pipe.
 */

#ifndef _simpio_h
#define _simpio_h

#include "genlib.h"
#include <sstream>

inline string GetLine()
{
	string line;
	if (!getline(cin, line)) return "";
	return line;
}

inline int GetInteger()
{
	while (true) {
		if (!cin) return 0;		// end of input: behave as if the user typed 0
		string line = GetLine();
		istringstream stream(line);
		int value;
		char extra;
		if (stream >> value && !(stream >> extra)) return value;
		cout << "Illegal integer format. Try again: ";
	}
}

#endif
