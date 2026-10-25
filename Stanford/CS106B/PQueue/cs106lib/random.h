/*
 * File: random.h
 * --------------
 * Minimal stand-in for the CS106 random library.
 */

#ifndef _random_h
#define _random_h

#include <cstdlib>
#include <ctime>

/* Seeds the generator from the clock so each run differs. */
inline void Randomize()
{
	srand((unsigned) time(NULL));
}

/*
 * Returns a random integer in the range [low, high]. rand() alone only
 * goes up to 32767 on some platforms, so two calls are combined to
 * cover ranges in the millions used by the performance trials.
 */
inline int RandomInteger(int low, int high)
{
	unsigned long r = ((unsigned long) rand() << 15) ^ (unsigned long) rand();
	return low + (int) (r % ((unsigned long) (high - low) + 1));
}

inline double RandomReal(double low, double high)
{
	return low + (high - low) * (rand() / ((double) RAND_MAX + 1));
}

/* Returns true with probability p. */
inline bool RandomChance(double p)
{
	return RandomReal(0, 1) < p;
}

#endif
