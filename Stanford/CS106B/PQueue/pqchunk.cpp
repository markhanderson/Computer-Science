/*
 * File: pqchunk.cpp
 * -----------------
 * This file implements the priority queue class as a "chunklist": a
 * singly-linked list of blocks, where each block holds a small sorted
 * array of up to MaxElemsPerBlock elements. All the elements, read
 * block by block and front to back within each block, are in decreasing
 * order, so the max is always the first element of the first block.
 *
 * Compared with the plain sorted linked list, searching for the insert
 * position can step over a whole block at a time, and the next pointer
 * is paid for once per block instead of once per element. Compared with
 * a sorted array, an insert never shifts more than MaxElemsPerBlock
 * elements.
 */

#include "pqueue.h"
#include "genlib.h"
#include <iostream>

#if PQ_BLOCK_SIZE < 2
#error "Splitting a block in half requires PQ_BLOCK_SIZE of at least 2"
#endif


/* Implementation notes: PQueue class
 * ----------------------------------
 * The private section for the pqchunk looks like this:

	static const int MaxElemsPerBlock = PQ_BLOCK_SIZE;	// 64 by default

	struct Block {
		int elems[MaxElemsPerBlock];	// sorted in decreasing order, slots [0, numUsed)
		int numUsed;
		Block *next;
	};
	Block *head;		// first block holds the largest elements; NULL when empty
	int numElems;		// total count over all blocks, so size() is O(1)

	Block *newBlock(Block *next);
	void splitBlock(Block *block);

 * Invariants: every block in the list holds at least one element (a block
 * is freed as soon as it empties), and the empty pqueue has head == NULL.
 * There is no dummy block, no tail pointer and no back pointers: dequeue
 * only touches the front, and enqueue walks forward from the head, so a
 * singly-linked list is all that's needed.
 */


PQueue::PQueue()
{
	head = NULL;
	numElems = 0;
}

PQueue::~PQueue()
{
	while (head != NULL) {
		Block *next = head->next;
		delete head;
		head = next;
	}
}

bool PQueue::isEmpty()
{
	return (head == NULL);
}

int PQueue::size()
{
	return numElems;
}


/* Implementation notes: enqueue
 * -----------------------------
 * Finding the block: walk forward until reaching a block whose smallest
 * (last) element is less than the new value, or the last block. The new
 * value belongs in that block, after any elements greater than or equal
 * to it. Only the last element of each block is examined, so the walk
 * costs one step per block, not one per element.
 *
 * Choosing between two blocks: a value that falls between two blocks
 * (smaller than everything in block A, larger than everything in the
 * following block B) could go at the end of A or at the front of B. The
 * walk always lands on B. If A has a free slot, we put the value there
 * instead: appending to A shifts nothing, and it uses up room that would
 * otherwise sit empty while B may be full and need splitting.
 *
 * A full target block is split in half first and the value goes into
 * whichever half it belongs in. Finally, insertion into the block is one
 * step of insertion sort: shift smaller elements right and drop the new
 * value into the gap.
 *
 * The empty pqueue isn't a special case beyond needing one empty block
 * to insert into: the walk stops immediately because there's no next
 * block, and the insertion loop handles numUsed == 0.
 */
void PQueue::enqueue(int newValue)
{
	if (head == NULL)
		head = newBlock(NULL);

	Block *prev = NULL, *cur = head;
	while (cur->next != NULL && cur->elems[cur->numUsed - 1] >= newValue) {
		prev = cur;
		cur = cur->next;
	}

	if (prev != NULL && prev->numUsed < MaxElemsPerBlock && newValue > cur->elems[0])
		cur = prev;		// goes on the boundary; prev has room at its end

	if (cur->numUsed == MaxElemsPerBlock) {
		splitBlock(cur);
		if (cur->elems[cur->numUsed - 1] >= newValue)
			cur = cur->next;	// belongs in the second half
	}

	int i;
	for (i = cur->numUsed; i > 0 && cur->elems[i - 1] < newValue; i--)
		cur->elems[i] = cur->elems[i - 1];
	cur->elems[i] = newValue;
	cur->numUsed++;
	numElems++;
}


/* Implementation notes: dequeueMax
 * --------------------------------
 * The max is the first element of the first block. The rest of the block
 * is shuffled down one slot so that every block keeps its elements in
 * slots [0, numUsed). That's at most MaxElemsPerBlock - 1 moves, the same
 * bound as an insert, and it keeps the block layout identical everywhere
 * (no per-block start index to maintain). A block that becomes empty is
 * unlinked and freed.
 */
int PQueue::dequeueMax()
{
	if (isEmpty())
		Error("Tried to dequeue max from an empty pqueue!");

	int maxValue = head->elems[0];
	head->numUsed--;
	for (int i = 0; i < head->numUsed; i++)
		head->elems[i] = head->elems[i + 1];

	if (head->numUsed == 0) {
		Block *toBeDeleted = head;
		head = head->next;
		delete toBeDeleted;
	}
	numElems--;
	return maxValue;
}


/* Implementation notes: bytesUsed
 * -------------------------------
 * The object itself plus every allocated block. Each block costs the
 * same whether it's full or nearly empty, so this also reflects how well
 * the blocks are packed.
 */
int PQueue::bytesUsed()
{
	int total = sizeof(*this);
	for (Block *cur = head; cur != NULL; cur = cur->next)
		total += sizeof(*cur);
	return total;
}


string PQueue::implementationName()
{
	return "sorted chunklist";
}


/*
 * Implementation notes: printDebuggingInfo
 * ----------------------------------------
 * Prints each block in order: its address, how full it is, its contents
 * and where its next pointer goes.
 */
void PQueue::printDebuggingInfo()
{
	int count = 0;

	cout << "------------------ START DEBUG INFO ------------------" << endl;
	cout << "Pqueue contains " << numElems << " entries in blocks of up to "
	     << MaxElemsPerBlock << endl;
	for (Block *cur = head; cur != NULL; cur = cur->next) {
		cout << "Block #" << count << " (at address " << cur << ") "
		     << cur->numUsed << "/" << MaxElemsPerBlock << " used: ";
		for (int i = 0; i < cur->numUsed; i++)
			cout << cur->elems[i] << " ";
		cout << " next = " << cur->next << endl;
		count++;
	}
	cout << "------------------ END DEBUG INFO ------------------" << endl;
}


/*
 * Private member function: newBlock
 * ---------------------------------
 * Allocates an empty block that links to the given next block.
 */
PQueue::Block *PQueue::newBlock(Block *next)
{
	Block *block = new Block;
	block->numUsed = 0;
	block->next = next;
	return block;
}


/*
 * Private member function: splitBlock
 * -----------------------------------
 * Splits a full block in two: the block keeps the first (larger) half of
 * its elements and a new block, linked in right after it, receives the
 * second half. Both halves end up with room to spare, so the next insert
 * into either one won't immediately force another split.
 */
void PQueue::splitBlock(Block *block)
{
	Block *second = newBlock(block->next);
	int keep = block->numUsed / 2;
	for (int i = keep; i < block->numUsed; i++)
		second->elems[second->numUsed++] = block->elems[i];
	block->numUsed = keep;
	block->next = second;
}
