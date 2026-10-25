# CS106B Assignment 6: Priority Queue

A priority queue of integers, where larger values come out first, implemented four ways:

| File | Implementation | Author |
|---|---|---|
| `pqvector.cpp` | Unsorted vector | Starter code |
| `pqlist.cpp` | Sorted singly-linked list | Starter code |
| `pqchunk.cpp` | Sorted chunklist (a linked list of fixed-size sorted arrays) | New |
| `pqheap.cpp` | Binary max-heap in a dynamically grown array | New |

`writeup.txt` has the timing and memory results and the analysis of the tradeoffs.

## Building and running

```
make              # builds ./pqvector ./pqlist ./pqchunk ./pqheap
./pqheap          # runs the tests, then asks for time-trial sizes (0 quits)
make pqchunk BLOCK=16   # chunklist with a different MaxElemsPerBlock
make check        # all tests on all four, under AddressSanitizer/UBSan
```

## Changes to the starter project

- **`pqueue.h`**: the handout's workflow is to paste a different private section into the header each time you switch implementations. Instead, the header contains all four private sections, and `#if` picks one based on a `PQ_VECTOR`, `PQ_LIST`, `PQ_CHUNK` or `PQ_HEAP` flag that the Makefile sets. With no flag set, you get the heap.
- **`pqueuetest.cpp`**: the intentional dequeue-from-empty error was checked for every implementation and then commented out, as the test asks. A new `StressPQueueTest` checks 200,000-operation random runs against `std::priority_queue`. Those runs cover heavy duplicates, negative values, and long increasing or decreasing runs.
- **`main.cpp`**: the driver wasn't in the starter files I had, so this is a new one.
- **`cs106lib/`**: small stand-ins for the CS106 library headers (`genlib.h`, `simpio.h`, `random.h`, `strutils.h`, `disallowcopy.h`), so the project builds with a standard C++ compiler. To use the real CS106 libraries, drop this folder.

## Design notes

**Chunklist.** Each block holds a raw array of up to `MaxElemsPerBlock` elements (64 by default) in slots `[0, numUsed)`, sorted in decreasing order. The list is singly linked with no dummy block, and an empty pqueue has `head == NULL`. Every block in the list holds at least one element, because a block is freed the moment it empties.
- **Enqueue** walks forward, looking only at the last element of each block, until it reaches the first block that has an element smaller than the new value.
- **Values between two blocks** could go at the end of the previous block or the front of the next one. The value goes at the end of the previous block if that block has room. That fills space that would otherwise sit empty and avoids some splits.
- **A full block** is split in half before the insert.
- **Dequeue** removes the first element of the first block and shifts the rest of that block down one slot.

**Heap.** The heap lives in a raw `int` array numbered from 0, so the children of node `i` are `2i+1` and `2i+2`. The array doubles in size when it fills.
- `bubbleUp` and `heapify` hold the moving value aside and shift the nodes it passes over by one level each. The value is written once, into its final slot, instead of being swapped at every level.
