/* File: pqueue.cpp
 * ----------------
 * Implementation of the PQueue class template as a binary max-heap.
 * This file is #included from pqueue.h -- it is NOT compiled on its own.
 */

#ifdef _pqueue_h

const int InitialPQCapacity = 16;

template <typename ElemType>
PQueue<ElemType>::PQueue(int (*cmpFn)(ElemType, ElemType))
{
	cmp = cmpFn;
	numUsed = 0;
	numAllocated = InitialPQCapacity;
	elems = new ElemType[numAllocated];
}

template <typename ElemType>
PQueue<ElemType>::~PQueue()
{
	delete[] elems;
}

template <typename ElemType>
bool PQueue<ElemType>::isEmpty()
{
	return numUsed == 0;
}

template <typename ElemType>
int PQueue<ElemType>::size()
{
	return numUsed;
}

template <typename ElemType>
void PQueue<ElemType>::enqueue(ElemType newElem)
{
	if (numUsed == numAllocated) enlargeCapacity();
	elems[numUsed] = newElem;
	bubbleUp(numUsed);
	numUsed++;
}

template <typename ElemType>
ElemType PQueue<ElemType>::dequeueMax()
{
	if (isEmpty()) Error("Tried to dequeue max from an empty pqueue!");
	ElemType top = elems[0];
	numUsed--;
	if (numUsed > 0) {
		elems[0] = elems[numUsed];   // move last leaf to root...
		trickleDown(0);              // ...and sink it into place
	}
	return top;
}

/* Doubles the array, copying elements across (element-wise assignment so
 * that types with deep-copy semantics, e.g. Vector, are copied correctly).
 */
template <typename ElemType>
void PQueue<ElemType>::enlargeCapacity()
{
	int newCapacity = numAllocated * 2;
	ElemType *bigger = new ElemType[newCapacity];
	for (int i = 0; i < numUsed; i++) {
		bigger[i] = elems[i];
	}
	delete[] elems;
	elems = bigger;
	numAllocated = newCapacity;
}

/* Swap the element at index with its parent while it outranks the parent. */
template <typename ElemType>
void PQueue<ElemType>::bubbleUp(int index)
{
	while (index > 0) {
		int parent = (index - 1) / 2;
		if (cmp(elems[index], elems[parent]) <= 0) break;
		swap(index, parent);
		index = parent;
	}
}

/* Swap the element at index with its higher-priority child while that child
 * outranks it.
 */
template <typename ElemType>
void PQueue<ElemType>::trickleDown(int index)
{
	while (true) {
		int left = 2 * index + 1;
		int right = left + 1;
		int best = index;
		if (left < numUsed && cmp(elems[left], elems[best]) > 0) best = left;
		if (right < numUsed && cmp(elems[right], elems[best]) > 0) best = right;
		if (best == index) break;
		swap(index, best);
		index = best;
	}
}

template <typename ElemType>
void PQueue<ElemType>::swap(int i, int j)
{
	ElemType tmp = elems[i];
	elems[i] = elems[j];
	elems[j] = tmp;
}

#endif
