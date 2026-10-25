/*
 * File: genlib.h
 * --------------
 * Minimal stand-in for the CS106 genlib library, written so this
 * assignment builds with a standard C++ compiler. Provides the
 * string type, the std namespace and Error().
 */

#ifndef _genlib_h
#define _genlib_h

#include <string>
#include <iostream>
#include <cstdlib>
using namespace std;

/*
 * Function: Error
 * Usage: Error(msg);
 * ------------------
 * Prints the message to cerr and exits the program.
 */
inline void Error(string msg)
{
	cerr << endl << "Error: " << msg << endl;
	exit(1);
}

#endif
