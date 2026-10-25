/*
 * File: main.cpp
 * --------------
 * Driver for the priority queue assignment. Runs the correctness tests
 * and then the interactive performance trials for whichever pqueue
 * implementation is linked in.
 */

#include "genlib.h"
#include "pqueuetest.h"
#include "performance.h"
#include <iostream>

int main()
{
	PQueue pq;
	cout << "Testing the " << pq.implementationName() << " pqueue implementation." << endl;
	BasicPQueueTest();
	MorePQueueTest();
	PQueueSortTest();
	StressPQueueTest();
	PQueuePerformance();
	return 0;
}
