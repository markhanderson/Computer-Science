/*
 * File: pqheap.cpp
 * ----------------
 * This file implements the priority queue class as a binary max-heap
 * stored in a dynamically-allocated array. The heap is a complete binary
 * tree where each node is >= its children, so the max is always at the
 * root. Both enqueue and dequeueMax move a single value along one path
 * between the root and the bottom, so each is O(log N).
 */

#include "pqueue.h"
#include "genlib.h"
#include <iostream>


/* Implementation notes: PQueue class
 * ----------------------------------
 * The private section for the pqheap looks like this:

	int *elems;			// heap in array form, root at index 0
	int numAllocated;	// capacity of elems
	int numUsed;		// number of elements in the heap

	void enlargeCapacity();
	void bubbleUp(int index);
	void heapify(int index);

 * The nodes are numbered level by level starting from 0, so the root is
 * elems[0], the children of node i are 2i+1 and 2i+2, and the parent of
 * node i is (i-1)/2. Numbering from 0 wastes no slot at the front of the
 * array; the price is the +1/-1 in the index arithmetic.
 *
 * The array is a raw C++ array rather than a Vector so each access avoids
 * Vector's bounds check. It doubles in size whenever it fills, so there is
 * no upper bound on the heap size and the amortized cost of growing is
 * O(1) per enqueue.
 */

const int InitialCapacity = 10;

static int Parent(int index)     { return (index - 1) / 2; }
static int LeftChild(int index)  { return 2 * index + 1; }
static int RightChild(int index) { return 2 * index + 2; }


PQueue::PQueue()
{
	elems = new int[InitialCapacity];
	numAllocated = InitialCapacity;
	numUsed = 0;
}

PQueue::~PQueue()
{
	delete[] elems;
}

bool PQueue::isEmpty()
{
	return (numUsed == 0);
}

int PQueue::size()
{
	return numUsed;
}


/* Implementation notes: enqueue
 * -----------------------------
 * The completeness property dictates where the new node goes: the next
 * free slot at the end of the array. The new value then bubbles up,
 * swapping with its parent until it reaches a parent at least as large
 * or the root.
 */
void PQueue::enqueue(int newValue)
{
	if (numUsed == numAllocated)
		enlargeCapacity();
	elems[numUsed] = newValue;
	bubbleUp(numUsed);
	numUsed++;
}


/* Implementation notes: dequeueMax
 * --------------------------------
 * The max is at the root. To keep the tree complete, the last node is the
 * one that has to go, so its value is moved into the root and the last
 * slot is released. Both subtrees of the root are still heaps; only the
 * root value may be out of place, so it is trickled down by heapify.
 */
int PQueue::dequeueMax()
{
	if (isEmpty())
		Error("Tried to dequeue max from an empty pqueue!");

	int maxValue = elems[0];
	numUsed--;
	elems[0] = elems[numUsed];
	heapify(0);
	return maxValue;
}


/* Implementation notes: bytesUsed
 * -------------------------------
 * The object plus the whole allocated array, including unused capacity,
 * since that memory is reserved for this pqueue either way.
 */
int PQueue::bytesUsed()
{
	return sizeof(*this) + numAllocated * sizeof(int);
}


string PQueue::implementationName()
{
	return "heap";
}


/*
 * Implementation notes: printDebuggingInfo
 * ----------------------------------------
 * Prints the array one tree level per line, so the shape of the heap is
 * easy to check by eye: each line holds twice as many nodes as the line
 * above it, and every node should be <= the node above it.
 */
void PQueue::printDebuggingInfo()
{
	cout << "------------------ START DEBUG INFO ------------------" << endl;
	cout << "Pqueue contains " << numUsed << " entries (capacity "
	     << numAllocated << ")" << endl;
	int levelEnd = 1;
	for (int i = 0; i < numUsed; i++) {
		cout << elems[i] << " ";
		if (i + 1 == levelEnd || i + 1 == numUsed) {
			cout << endl;
			levelEnd = 2 * levelEnd + 1;
		}
	}
	cout << "------------------ END DEBUG INFO ------------------" << endl;
}


/*
 * Private member function: enlargeCapacity
 * ----------------------------------------
 * Doubles the capacity of the array, copying over the existing elements.
 */
void PQueue::enlargeCapacity()
{
	numAllocated *= 2;
	int *bigger = new int[numAllocated];
	for (int i = 0; i < numUsed; i++)
		bigger[i] = elems[i];
	delete[] elems;
	elems = bigger;
}


/*
 * Private member function: bubbleUp
 * ---------------------------------
 * Moves the value at index up toward the root until its parent is at
 * least as large. Rather than swapping at every level, the moving value
 * is held aside and each smaller parent is shifted down one level into
 * the hole; the value is written once, into its final slot.
 */
void PQueue::bubbleUp(int index)
{
	int value = elems[index];
	while (index > 0 && elems[Parent(index)] < value) {
		elems[index] = elems[Parent(index)];
		index = Parent(index);
	}
	elems[index] = value;
}


/*
 * Private member function: heapify
 * --------------------------------
 * Trickles the value at index down until it is >= both its children or
 * it reaches the bottom. At each level it trades places with the larger
 * child, which keeps that child >= its new sibling. As in bubbleUp, the
 * moving value is held aside and written once at the end.
 */
void PQueue::heapify(int index)
{
	int value = elems[index];
	while (LeftChild(index) < numUsed) {
		int larger = LeftChild(index);
		if (RightChild(index) < numUsed && elems[RightChild(index)] > elems[larger])
			larger = RightChild(index);
		if (elems[larger] <= value) break;
		elems[index] = elems[larger];
		index = larger;
	}
	elems[index] = value;
}
