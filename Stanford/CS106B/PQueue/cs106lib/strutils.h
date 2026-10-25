/*
 * File: strutils.h
 * ----------------
 * Minimal stand-in for the CS106 strutils library (only the
 * function vector.h needs).
 */

#ifndef _strutils_h
#define _strutils_h

#include "genlib.h"
#include <sstream>

inline string IntegerToString(int n)
{
	ostringstream stream;
	stream << n;
	return stream.str();
}

#endif
