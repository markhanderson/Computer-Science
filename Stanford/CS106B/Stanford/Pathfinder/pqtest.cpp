/* pqtest.cpp
 * ----------
 * Exercises the PQueue template in isolation before using it in pathfinder.
 * Not part of the Pathfinder project build -- compile it on its own.
 */

#include "genlib.h"
#include "pqueue.h"
#include <iostream>
#include <cstdlib>
#include <string>

static int failures = 0;

static void Check(bool ok, string what)
{
	cout << (ok ? "  pass: " : "  FAIL: ") << what << endl;
	if (!ok) failures++;
}

/* Reverse ordering: smaller ints are higher priority (min-queue). */
int ReverseIntCmp(int a, int b) { return OperatorCmp(b, a); }

struct cityT {
	string name;
	double distance;
};

/* Closer city = higher priority. */
int CompareCities(cityT a, cityT b)
{
	if (a.distance < b.distance) return 1;
	if (a.distance > b.distance) return -1;
	return 0;
}

int main()
{
	cout << "PQueue<int>, default OperatorCmp (max first):" << endl;
	{
		PQueue<int> pq;
		Check(pq.isEmpty() && pq.size() == 0, "new queue is empty");
		int input[] = {5, 1, 9, 3, 9, -2, 7};
		for (int i = 0; i < 7; i++) pq.enqueue(input[i]);
		Check(pq.size() == 7, "size after 7 enqueues");
		int expected[] = {9, 9, 7, 5, 3, 1, -2};
		bool inOrder = true;
		for (int i = 0; i < 7; i++) if (pq.dequeueMax() != expected[i]) inOrder = false;
		Check(inOrder, "dequeues in descending order (dupes ok)");
		Check(pq.isEmpty(), "empty after dequeuing all");
	}

	cout << "PQueue<int>, reversed comparator (min first):" << endl;
	{
		PQueue<int> pq(ReverseIntCmp);
		for (int i = 10; i >= 1; i--) pq.enqueue(i);
		bool inOrder = true;
		for (int i = 1; i <= 10; i++) if (pq.dequeueMax() != i) inOrder = false;
		Check(inOrder, "dequeues 1..10 ascending");
	}

	cout << "PQueue<string>:" << endl;
	{
		PQueue<string> pq;
		pq.enqueue("banana"); pq.enqueue("cherry"); pq.enqueue("apple");
		Check(pq.dequeueMax() == "cherry", "max string first");
		Check(pq.dequeueMax() == "banana", "then next");
	}

	cout << "PQueue<cityT> with struct comparator:" << endl;
	{
		PQueue<cityT> pq(CompareCities);
		cityT a = {"Far", 900}, b = {"Near", 10}, c = {"Mid", 300};
		pq.enqueue(a); pq.enqueue(b); pq.enqueue(c);
		Check(pq.dequeueMax().name == "Near", "closest first");
		Check(pq.dequeueMax().name == "Mid", "then middle");
		Check(pq.dequeueMax().name == "Far", "then farthest");
	}

	cout << "Stress: 10000 random ints, interleaved enqueue/dequeue (forces growth):" << endl;
	{
		PQueue<int> pq;
		srand(106);
		for (int i = 0; i < 10000; i++) {
			pq.enqueue(rand() % 1000);
			if (i % 3 == 0) pq.dequeueMax();
		}
		bool sorted = true;
		int prev = pq.dequeueMax();
		while (!pq.isEmpty()) {
			int cur = pq.dequeueMax();
			if (cur > prev) sorted = false;
			prev = cur;
		}
		Check(sorted, "remaining elements come out non-increasing");
	}

	cout << endl << (failures == 0 ? "All PQueue tests passed." : "Some PQueue tests FAILED.") << endl;
	return failures == 0 ? 0 : 1;
}
