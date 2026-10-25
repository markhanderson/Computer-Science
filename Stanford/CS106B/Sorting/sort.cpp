/*
 * File: sort.cpp
 * --------------
 * Part B of Assignment #5: a generic Sort template, implemented with
 * heapsort, plus two client comparison callbacks and test code.
 *
 * Heapsort works in two phases:
 *
 *   1. Heapify: rearrange the vector into a max-heap stored in the array,
 *      where the children of index i live at 2i+1 and 2i+2 and every
 *      element is >= its children. Built bottom-up (Floyd's method) by
 *      sifting down every internal node, starting from the last one. This
 *      phase is O(N).
 *
 *   2. Sort-down: the largest remaining element is always at index 0. Swap
 *      it with the last element of the heap, shrink the heap by one, and
 *      sift the new root down to restore the heap property. Each sift is
 *      O(log N), so this phase is O(N log N).
 *
 * The sorted region grows from the back of the vector, exactly like the
 * sorted region of selection sort; the heap is what makes "find the max of
 * the unsorted part" cost O(log N) instead of O(N).
 *
 * References:
 *   Wikipedia, "Heapsort", http://en.wikipedia.org/wiki/Heapsort
 *   R. W. Floyd, "Algorithm 245: Treesort 3", CACM 7(12), 1964 (bottom-up
 *     heap construction).
 *   Robert Sedgewick, "Algorithms in C++", 3rd ed., section 9.4.
 * The code below was written from the algorithm description, not copied.
 */

#include "genlib.h"
#include "vector.h"
#include "set.h"
#include "cmpfn.h"
#include "random.h"
#include <iostream>
#include <string>

/* Function: SiftDown
 * ------------------
 * Restores the max-heap property for the heap occupying v[0..heapSize-1],
 * assuming that only v[pos] may be smaller than its children. Rather than
 * swapping at every level, the element being sifted is held aside and each
 * larger child is moved up into the hole, which roughly halves the number
 * of element copies compared to repeated swaps.
 */
template <typename Type>
void SiftDown(Vector<Type> &v, int pos, int heapSize, int (cmpFn)(Type, Type))
{
    Type item = v[pos];
    while (true) {
        int child = 2 * pos + 1;
        if (child >= heapSize) break;
        if (child + 1 < heapSize && cmpFn(v[child + 1], v[child]) > 0) {
            child++;                        // pick the larger child
        }
        if (cmpFn(v[child], item) <= 0) break;
        v[pos] = v[child];                  // move child up into the hole
        pos = child;
    }
    v[pos] = item;
}

/* Function: Sort
 * Usage: Sort(v);  or  Sort(v, MyCmpFn);
 * --------------------------------------
 * Sorts v into increasing order according to cmpFn, which returns a
 * negative number, zero or a positive number to indicate that its first
 * argument is less than, equal to, or greater than its second. If no
 * comparison function is given, the built-in relational operators are
 * used via OperatorCmp.
 */
template <typename Type>
void Sort(Vector<Type> &v, int (cmpFn)(Type, Type) = OperatorCmp)
{
    int n = v.size();
    for (int i = n / 2 - 1; i >= 0; i--) {
        SiftDown(v, i, n, cmpFn);
    }
    for (int end = n - 1; end > 0; end--) {
        Type tmp = v[0];                    // move current max to its final spot
        v[0] = v[end];
        v[end] = tmp;
        SiftDown(v, 0, end, cmpFn);
    }
}

/* Function: LengthThenAlphaCmp
 * ----------------------------
 * Orders strings by increasing length, breaking ties alphabetically.
 */
int LengthThenAlphaCmp(string one, string two)
{
    if (one.length() != two.length()) {
        return (one.length() < two.length()) ? -1 : 1;
    }
    return OperatorCmp(one, two);
}

/* Function: SumOfSet
 * ------------------
 * Returns the sum of the elements in s.
 */
int SumOfSet(Set<int> &s)
{
    int sum = 0;
    Set<int>::Iterator itr = s.iterator();
    while (itr.hasNext()) {
        sum += itr.next();
    }
    return sum;
}

/* Function: SetSumCmp
 * -------------------
 * Orders sets of integers by increasing sum of their elements.
 */
int SetSumCmp(Set<int> one, Set<int> two)
{
    return OperatorCmp(SumOfSet(one), SumOfSet(two));
}


/* ---------------------------- Test code ---------------------------- */

/* Function: IsSorted
 * ------------------
 * Returns true if v is in non-decreasing order according to cmpFn.
 */
template <typename Type>
bool IsSorted(Vector<Type> &v, int (cmpFn)(Type, Type) = OperatorCmp)
{
    for (int i = 1; i < v.size(); i++) {
        if (cmpFn(v[i - 1], v[i]) > 0) return false;
    }
    return true;
}

/* Function: Report
 * ----------------
 * Prints whether a test passed and keeps a running count of failures.
 */
int numFailures = 0;

void Report(string testName, bool passed)
{
    cout << (passed ? "  PASS  " : "  FAIL  ") << testName << endl;
    if (!passed) numFailures++;
}

/* Function: PrintStrings
 * ----------------------
 * Prints a vector of strings on one line.
 */
void PrintStrings(Vector<string> &v)
{
    cout << "    ";
    for (int i = 0; i < v.size(); i++) cout << v[i] << " ";
    cout << endl;
}

/* Function: PrintSets
 * -------------------
 * Prints a vector of sets, each followed by its sum.
 */
void PrintSets(Vector< Set<int> > &v)
{
    cout << "    ";
    for (int i = 0; i < v.size(); i++) {
        cout << "{";
        Set<int>::Iterator itr = v[i].iterator();
        bool first = true;
        while (itr.hasNext()) {
            if (!first) cout << ",";
            cout << itr.next();
            first = false;
        }
        cout << "}=" << SumOfSet(v[i]) << " ";
    }
    cout << endl;
}

/* Function: ReverseIntCmp
 * -----------------------
 * Orders integers in decreasing order, to test a client callback on a
 * built-in type.
 */
int ReverseIntCmp(int one, int two)
{
    return OperatorCmp(two, one);
}

/* Function: TestIntegers
 * ----------------------
 * Tests the sort on small edge cases and on larger inputs in sorted,
 * reversed, random and all-duplicate arrangements. Each result is also
 * compared against a counting histogram to verify that no elements were
 * lost or duplicated.
 */
void TestIntegers()
{
    cout << "Integers:" << endl;
    for (int n = 0; n <= 5; n++) {
        Vector<int> v;
        for (int i = 0; i < n; i++) v.add(n - i);
        Sort(v);
        Report("reversed vector of size " + IntegerToString(n), IsSorted(v));
    }

    const int N = 10000;
    const int MaxVal = 100;
    string names[] = { "sorted", "reversed", "random", "all equal" };
    for (int kind = 0; kind < 4; kind++) {
        Vector<int> v;
        int counts[MaxVal] = {0};
        for (int i = 0; i < N; i++) {
            int val;
            switch (kind) {
                case 0:  val = i * MaxVal / N; break;
                case 1:  val = (N - 1 - i) * MaxVal / N; break;
                case 2:  val = RandomInteger(0, MaxVal - 1); break;
                default: val = 7; break;
            }
            v.add(val);
            counts[val]++;
        }
        Sort(v);
        for (int i = 0; i < v.size(); i++) counts[v[i]]--;
        bool sameElems = (v.size() == N);
        for (int i = 0; i < MaxVal; i++) {
            if (counts[i] != 0) sameElems = false;
        }
        Report(IntegerToString(N) + " " + names[kind] + " ints", IsSorted(v) && sameElems);
    }

    Vector<int> v;
    for (int i = 0; i < 1000; i++) v.add(RandomInteger(-500, 500));
    Sort(v, ReverseIntCmp);
    Report("1000 random ints, client callback (descending)", IsSorted(v, ReverseIntCmp));
}

/* Function: TestDoubles
 * ---------------------
 * Tests the default comparison on a second built-in type.
 */
void TestDoubles()
{
    cout << "Doubles:" << endl;
    Vector<double> v;
    for (int i = 0; i < 1000; i++) v.add(RandomReal(-1, 1));
    Sort(v);
    Report("1000 random doubles", IsSorted(v));
}

/* Function: TestStrings
 * ---------------------
 * Tests strings with both the default (alphabetical) ordering and the
 * length-then-alphabetical callback.
 */
void TestStrings()
{
    cout << "Strings:" << endl;
    string words[] = { "pear", "fig", "banana", "kiwi", "apple", "date",
                       "cherry", "plum", "a", "grape", "lime", "melon" };
    int numWords = sizeof(words) / sizeof(words[0]);

    Vector<string> v;
    for (int i = 0; i < numWords; i++) v.add(words[i]);
    Sort(v);
    PrintStrings(v);
    Report("alphabetical (default OperatorCmp)", IsSorted(v));

    Sort(v, LengthThenAlphaCmp);
    PrintStrings(v);
    bool expected = (v[0] == "a" && v[1] == "fig" && v[2] == "date" &&
                     v[3] == "kiwi" && v[7] == "apple" && v[numWords - 1] == "cherry");
    Report("length, ties alphabetical (LengthThenAlphaCmp)",
           IsSorted(v, LengthThenAlphaCmp) && expected);
}

/* Function: TestSets
 * ------------------
 * Tests sorting a vector of sets by their sums, including the example
 * from the handout ({1,2,3} comes before {2,8}) and the empty set.
 */
void TestSets()
{
    cout << "Sets of ints:" << endl;
    int data[][4] = { {2, 8, -1}, {1, 2, 3, -1}, {-1}, {10, -1}, {4, 5, -1},
                      {0, 1, -1}, {20, 1, 2, -1} };
    int numSets = sizeof(data) / sizeof(data[0]);

    Vector< Set<int> > v;
    for (int i = 0; i < numSets; i++) {
        Set<int> s;
        for (int j = 0; data[i][j] != -1; j++) s.add(data[i][j]);
        v.add(s);
    }
    Sort(v, SetSumCmp);
    PrintSets(v);
    Report("sets by increasing sum (SetSumCmp)", IsSorted(v, SetSumCmp));
}

int main()
{
    Randomize();
    TestIntegers();
    TestDoubles();
    TestStrings();
    TestSets();
    cout << endl << (numFailures == 0 ? "All tests passed." : "Some tests FAILED.") << endl;
    return 0;
}
