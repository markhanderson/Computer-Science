/*
 * File: sortdetective.cpp
 * -----------------------
 * Part A of Assignment #5 (Sort Detective).
 *
 * This program runs experiments on the five MysterySort routines exported
 * by mysterysort.lib. It gathers two kinds of evidence:
 *
 *   1. Timing: each sort is run on sorted, reverse-sorted and random input
 *      at several sizes. Doubling N should roughly double the time for an
 *      O(N) case, slightly more than double it for O(N log N), and
 *      quadruple it for O(N^2).
 *
 *   2. Mid-sort snapshots: each sort is stopped early (using the maxSecs
 *      argument) and the partially sorted data is printed, so we can see
 *      the pattern in which elements are being rearranged.
 *
 * Add mysterysort.lib to the project so the linker finds the mystery
 * functions.
 */

#include "genlib.h"
#include "vector.h"
#include "random.h"
#include "mysterysort.h"
#include <iostream>
#include <iomanip>
#include <ctime>

/* Type: sortFnT
 * -------------
 * The signature shared by all five mystery sorts.
 */
typedef void (*sortFnT)(Vector<int> &nums, double maxSecs);

enum inputT { Sorted, Reversed, Random };

const int NumSorts = 5;
sortFnT Sorts[NumSorts] = {
    MysterySort1, MysterySort2, MysterySort3, MysterySort4, MysterySort5
};

/* Minimum amount of CPU time to accumulate before reporting a timing. A
 * fast sort is repeated until at least this much time has gone by, so the
 * clock has ticked enough times to give a meaningful average.
 */
const double MinTotalSecs = 0.5;

/* Function: GetSeconds
 * --------------------
 * Returns the processor time used so far, in seconds.
 */
double GetSeconds()
{
    return double(clock()) / CLOCKS_PER_SEC;
}

/* Function: FillVector
 * --------------------
 * Fills v with n integers arranged as requested. Random values are drawn
 * from a range much larger than n, so there are few duplicates.
 */
void FillVector(Vector<int> &v, int n, inputT order)
{
    v.clear();
    for (int i = 0; i < n; i++) {
        switch (order) {
            case Sorted:   v.add(i); break;
            case Reversed: v.add(n - i); break;
            case Random:   v.add(RandomInteger(0, 10 * n)); break;
        }
    }
}

/* Function: IsSorted
 * ------------------
 * Returns true if v is in ascending order.
 */
bool IsSorted(Vector<int> &v)
{
    for (int i = 1; i < v.size(); i++) {
        if (v[i - 1] > v[i]) return false;
    }
    return true;
}

/* Function: TimeSort
 * ------------------
 * Returns the average time in seconds for one call to sortFn on the given
 * input. The sort works in place, so each repetition sorts a fresh copy of
 * the original. The cost of making those copies is measured separately and
 * subtracted out.
 */
double TimeSort(sortFnT sortFn, Vector<int> &original)
{
    int reps = 0;
    double start = GetSeconds(), elapsed;
    do {
        Vector<int> copy = original;
        sortFn(copy, -1);
        if (reps == 0 && !IsSorted(copy)) Error("Sort failed to sort!");
        reps++;
        elapsed = GetSeconds() - start;
    } while (elapsed < MinTotalSecs);

    double copyStart = GetSeconds();
    for (int i = 0; i < reps; i++) {
        Vector<int> copy = original;
    }
    double copyTime = GetSeconds() - copyStart;

    double perSort = (elapsed - copyTime) / reps;
    return (perSort > 0) ? perSort : 0;
}

/* Function: RunTimingTrials
 * -------------------------
 * Prints a table of times (in milliseconds) for every sort on every input
 * arrangement, doubling the size each row. The ratio column shows how much
 * the time grew relative to the previous row: ~2 means linear, a little
 * over 2 means N log N, ~4 means quadratic.
 */
void RunTimingTrials(inputT order, string orderName, int startN, int numSizes)
{
    cout << endl << "=== " << orderName << " input (time in ms, ratio to previous N) ===" << endl;
    cout << setw(8) << "N";
    for (int s = 0; s < NumSorts; s++) {
        cout << setw(13) << "Sort" << (s + 1) << setw(7) << "";
    }
    cout << endl;

    double prev[NumSorts] = {0};
    int n = startN;
    for (int row = 0; row < numSizes; row++, n *= 2) {
        Vector<int> original;
        FillVector(original, n, order);
        cout << setw(8) << n;
        for (int s = 0; s < NumSorts; s++) {
            double ms = TimeSort(Sorts[s], original) * 1000;
            cout << setw(12) << fixed << setprecision(3) << ms;
            if (row > 0 && prev[s] > 0) {
                cout << "  x" << setw(5) << setprecision(2) << ms / prev[s];
            } else {
                cout << setw(8) << "";
            }
            prev[s] = ms;
        }
        cout << endl;
    }
}

/* Function: PrintVector
 * ---------------------
 * Prints the contents of v, 20 values per line.
 */
void PrintVector(Vector<int> &v)
{
    for (int i = 0; i < v.size(); i++) {
        cout << setw(6) << v[i];
        if (i % 20 == 19 || i == v.size() - 1) cout << endl;
    }
}

/* Function: MakePermutation
 * -------------------------
 * Fills v with a random permutation of 0..n-1. Since every value appears
 * exactly once, a value equal to its index is in its final sorted position.
 */
void MakePermutation(Vector<int> &v, int n)
{
    v.clear();
    for (int i = 0; i < n; i++) v.add(i);
    for (int i = 0; i < n; i++) {
        int j = RandomInteger(i, n - 1);
        int tmp = v[i]; v[i] = v[j]; v[j] = tmp;
    }
}

/* Function: SnapshotSort
 * ----------------------
 * Stops a sort part-way through and prints the first and last 100 values
 * of the partially sorted data. The fast sorts can finish a big vector in
 * a fraction of a second, so the time limit is tried from longest to
 * shortest, and we report the longest one that leaves the data unsorted
 * (i.e. the snapshot showing the most progress).
 */
void SnapshotSort(int sortNum, int n)
{
    double limits[] = { 0.5, 0.2, 0.1, 0.05, 0.02, 0.01, 0.005, 0.002, 0.001 };
    int numLimits = sizeof(limits) / sizeof(limits[0]);
    Vector<int> v;
    double maxSecs = 0;
    for (int k = 0; k < numLimits; k++) {
        MakePermutation(v, n);
        maxSecs = limits[k];
        Sorts[sortNum - 1](v, maxSecs);
        if (!IsSorted(v)) break;
    }

    int inPlace = 0;
    for (int i = 0; i < n; i++) {
        if (v[i] == i) inPlace++;
    }
    cout << endl << "--- MysterySort" << sortNum << " stopped after " << maxSecs
         << " secs (N = " << n << ", " << inPlace << " values in final position";
    if (IsSorted(v)) cout << ", finished anyway";
    cout << ") ---" << endl;

    Vector<int> front, back;
    for (int i = 0; i < 100 && i < n; i++) front.add(v[i]);
    for (int i = (n > 100 ? n - 100 : 0); i < n; i++) back.add(v[i]);
    cout << "First 100:" << endl;
    PrintVector(front);
    cout << "Last 100:" << endl;
    PrintVector(back);
}

int main()
{
    Randomize();

    cout << "Sort Detective: timing trials" << endl;
    RunTimingTrials(Sorted, "Sorted", 1000, 5);
    RunTimingTrials(Reversed, "Reverse-sorted", 1000, 5);
    RunTimingTrials(Random, "Random", 1000, 5);

    cout << endl << "Sort Detective: mid-sort snapshots" << endl;
    for (int s = 1; s <= NumSorts; s++) {
        SnapshotSort(s, 100000);
    }
    return 0;
}
