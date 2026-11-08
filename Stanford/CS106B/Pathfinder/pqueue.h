/* File: pqueue.h
 * --------------
 * Defines the interface for the priority queue class template.
 *
 * A PQueue stores elements of any type and always dequeues the element of
 * MAXIMUM priority first. Priority is decided by a client-supplied
 * comparison callback (same convention as Set and cmpfn.h):
 *
 *     cmp(a, b) > 0   means a has higher priority than b
 *     cmp(a, b) < 0   means a has lower priority than b
 *     cmp(a, b) == 0  means they have equal priority
 *
 * If no callback is given, OperatorCmp is used, which compares with the
 * built-in relational operators (so a PQueue<int> dequeues largest first,
 * exactly like the integer PQueue from Assignment 6).
 *
 * To dequeue in MINIMUM order (e.g. shortest path first), the client simply
 * supplies a callback that reports the smaller item as the higher priority.
 */

#ifndef _pqueue_h
#define _pqueue_h

#include "genlib.h"
#include "cmpfn.h"

template <typename ElemType>
class PQueue
{
  public:

	/* Constructor: PQueue
	 * Usage: PQueue<int> pq;
	 *        PQueue<pathT> pq(ComparePaths);
	 * --------------------------------------
	 * Creates an empty priority queue that orders its elements using the
	 * given comparison function (OperatorCmp if none is given).
	 */
	PQueue(int (*cmpFn)(ElemType, ElemType) = OperatorCmp);

	/* Destructor: ~PQueue
	 * -------------------
	 * Frees all dynamically allocated storage owned by the queue.
	 */
	~PQueue();

	/* Member function: isEmpty / size
	 * -------------------------------
	 * Report whether the queue is empty / how many elements it holds.
	 */
	bool isEmpty();
	int size();

	/* Member function: enqueue
	 * Usage: pq.enqueue(elem);
	 * ------------------------
	 * Adds the element to the queue. Runs in O(log N).
	 */
	void enqueue(ElemType newElem);

	/* Member function: dequeueMax
	 * Usage: elem = pq.dequeueMax();
	 * ------------------------------
	 * Removes and returns the highest-priority element. It is an error to
	 * call this on an empty queue. Runs in O(log N).
	 */
	ElemType dequeueMax();

  private:
	/* Implementation: a binary max-heap stored in a dynamic array.
	 * The element at index i has children at 2i+1 and 2i+2 and its parent
	 * at (i-1)/2. Every parent has priority >= its children, so the max
	 * element is always at index 0.
	 */
	ElemType *elems;
	int numUsed;
	int numAllocated;
	int (*cmp)(ElemType, ElemType);

	void enlargeCapacity();
	void bubbleUp(int index);
	void trickleDown(int index);
	void swap(int i, int j);

	/* Copying is disallowed: a shallow copy would make two queues share
	 * (and both delete) the same array. Declared private, never defined.
	 */
	PQueue(const PQueue &other);
	PQueue &operator=(const PQueue &other);
};

/* Templates must be visible in full to every client, so the header pulls in
 * the implementation. NOTE: do NOT add pqueue.cpp to the compiled files of
 * your project.
 */
#include "pqueue.cpp"

#endif
