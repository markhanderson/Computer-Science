/*
 * File: disallowcopy.h
 * --------------------
 * Stand-in for the CS106 disallowcopy.h. Placing
 * DISALLOW_COPYING(ClassName) in the private section of a class
 * declares, but never defines, the copy constructor and assignment
 * operator, so any attempt to copy an object fails at compile or
 * link time instead of silently sharing pointers.
 */

#ifndef _disallowcopy_h
#define _disallowcopy_h

#define DISALLOW_COPYING(Type) \
	Type(const Type &); \
	const Type &operator=(const Type &);

#endif
