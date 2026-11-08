/* pathfinder.cpp
 * ---------------
 * CS106B Assignment #7: Pathfinder
 *
 * Reads a graph (locations + bi-directional distance arcs) from a data file,
 * draws it over a background picture, and offers two classic algorithms:
 *
 *   - Dijkstra's algorithm: shortest path between two clicked locations.
 *   - Kruskal's algorithm:  minimal spanning tree connecting every location.
 *
 * Both algorithms are driven by the templatized PQueue (pqueue.h).
 */

#include "genlib.h"
#include "extgraph.h"
#include "simpio.h"
#include "map.h"
#include "set.h"
#include "vector.h"
#include "pqueue.h"
#include <iostream>
#include <fstream>
#include <cmath>

/* Constants
 * --------
 * A few program-wide constants concerning the graphical display.
 */
const double CircleRadius =.05;     	// the radius of a node
const int LabelFontSize = 9;          // for node name labels

const string NodeColor = "Blue";
const string ArcColor = "Dark Gray";
const string SelectedColor = "Green";  // node the user just clicked
const string PathColor = "Red";        // Dijkstra result
const string TreeColor = "Magenta";    // Kruskal result


/* Type: coordT
 * ------------
 * Just a simple struct to handle a pair of x,y coordinate values.
 */
struct coordT {
	double x, y;
};


/* Graph data structures
 * ---------------------
 * nodeT and arcT refer to each other, so arcT is forward-declared.
 *
 * Each line in the ARCS section of the data file becomes exactly ONE arcT.
 * Because arcs are bi-directional, that single arcT is stored in the arc
 * list of BOTH of its endpoints; OtherEnd() tells you where an arc leads
 * from a given node. Having one arcT per connection means Kruskal and the
 * drawing code see each connection exactly once, and there is exactly one
 * object to delete.
 */
struct arcT;

struct nodeT {
	string name;
	coordT loc;
	Vector<arcT *> arcs;       // every arc touching this node
};

struct arcT {
	nodeT *start, *end;
	double distance;
};

struct graphT {
	string imageFile;          // background picture (looked up in Pictures/)
	Map<nodeT *> nodes;        // all nodes, keyed by name (owns the nodeTs)
	Vector<arcT *> arcs;       // all arcs, each once (owns the arcTs)
};

/* A path under construction during Dijkstra's search. We keep the total
 * length and final node alongside the arcs so neither has to be recomputed
 * every time the path is compared or extended.
 */
struct pathT {
	Vector<arcT *> arcs;
	double length;
	nodeT *last;
};


/* ======================================================================
 * Starter-code drawing and event helpers
 * ====================================================================== */

/* Function: DrawFilledCircleWithLabel
 * Usage:  DrawFilledCircleWithLabel(center, "Green", "You are here");
 * -------------------------------------------------------------------
 * Uses facilities from extgraph to draw a circle filled with
 * color specified. The circle is centered at the given coord has the
 * specified radius.  A label is drawn to the right of the circle.
 * You can leave off the last argument if no label is desired.
 */
void DrawFilledCircleWithLabel(coordT center, string color, string label = "")
{
	MovePen(center.x + CircleRadius, center.y);
	SetPenColor(color);
	StartFilledRegion(1.0);
	DrawArc(CircleRadius, 0, 360);
	EndFilledRegion();
	if (!label.empty()) {
		MovePen(center.x + CircleRadius, center.y);
		SetFont("Helvetica");
		SetPointSize(LabelFontSize);
		DrawTextString(label);
	}
}

/* Function: DrawLineBetween
 * Usage:  DrawLineBetween(coord1, coord2, "Black");
 * -------------------------------------------------
 * Uses facilities from extgraph to draw a line of the
 * specified color between the two given coordinates.
 */
void DrawLineBetween(coordT start, coordT end, string color)
{
	SetPenColor(color);
	MovePen(start.x, start.y);
	DrawLine(end.x - start.x, end.y - start.y);
}


/* Function: GetMouseClick
 * Usage:  loc = GetMouseClick();
 * ------------------------------
 * Waits for the user to click somewhere on the graphics window
 * and returns the coordinate of where the click took place.
 */
coordT GetMouseClick()
{
	coordT where;
	WaitForMouseDown();
	WaitForMouseUp();
	where.x = GetMouseX();
	where.y = GetMouseY();
	return where;
}

/* Function: WithinDistance
 * Usage:  if (WithinDistance(click, pt))...
 * -----------------------------------------
 * Returns true if the Cartesian distance between the two coordinates
 * is <= the specified distance, false otherwise. If not specified,
 * the distance is assumed to be size of the node diameter on screen.
 */
bool WithinDistance(coordT pt1, coordT pt2, double maxDistance = CircleRadius*2)
{
	double dx = pt2.x - pt1.x;
	double dy = pt2.y - pt1.y;
	double distance = sqrt(dx*dx + dy*dy);
	return (distance <= maxDistance);
}


/* ======================================================================
 * Graph construction and destruction
 * ====================================================================== */

/* Function: FreeGraph
 * -------------------
 * Deletes every node and arc owned by the graph and leaves it empty.
 */
void FreeGraph(graphT &graph)
{
	for (int i = 0; i < graph.arcs.size(); i++) {
		delete graph.arcs[i];
	}
	Map<nodeT *>::Iterator itr = graph.nodes.iterator();
	while (itr.hasNext()) {
		delete graph.nodes.get(itr.next());
	}
	graph.arcs.clear();
	graph.nodes.clear();
	graph.imageFile = "";
}

/* Function: ReadGraph
 * -------------------
 * Populates an (empty) graph from an open data file stream. The format is
 *     <image file>  NODES  {<name> <x> <y>}*  ARCS  {<name1> <name2> <dist>}*
 * Extraction with >> skips all whitespace (including old Mac '\r' line
 * endings), which is exactly what we want here.
 */
void ReadGraph(graphT &graph, ifstream &in)
{
	string token;
	in >> graph.imageFile;
	in >> token;                               // "NODES"
	if (token != "NODES") Error("Malformed graph file: expected NODES");

	while (in >> token && token != "ARCS") {
		nodeT *node = new nodeT;
		node->name = token;
		in >> node->loc.x >> node->loc.y;
		graph.nodes.put(node->name, node);
	}

	string name1, name2;
	double distance;
	while (in >> name1 >> name2 >> distance) {
		if (!graph.nodes.containsKey(name1) || !graph.nodes.containsKey(name2)) {
			Error("Malformed graph file: arc " + name1 + "-" + name2 +
			      " refers to an unknown node");
		}
		arcT *arc = new arcT;
		arc->start = graph.nodes.get(name1);
		arc->end = graph.nodes.get(name2);
		arc->distance = distance;
		arc->start->arcs.add(arc);
		arc->end->arcs.add(arc);
		graph.arcs.add(arc);
	}
}

/* Function: LoadGraphFromUser
 * ---------------------------
 * Prompts for a data file name until one opens successfully, then frees the
 * old graph and reads the new one into its place. If the user just presses
 * return and a graph is already loaded, the current graph is kept.
 */
void LoadGraphFromUser(graphT &graph)
{
	while (true) {
		cout << "Enter the name of the graph data file";
		if (graph.nodes.size() > 0) cout << " (or RETURN to keep current)";
		cout << ": ";
		string filename = GetLine();
		if (filename.empty() && graph.nodes.size() > 0) return;

		ifstream in(filename.c_str());
		if (in.fail()) {
			cout << "Unable to open file named \"" << filename
			     << "\". Please try again." << endl;
			continue;
		}
		FreeGraph(graph);
		ReadGraph(graph, in);
		cout << "Loaded " << graph.nodes.size() << " locations and "
		     << graph.arcs.size() << " connections from " << filename << "." << endl;
		return;
	}
}

/* Function: OtherEnd
 * ------------------
 * Given an arc and one of its endpoints, returns the opposite endpoint.
 */
nodeT *OtherEnd(arcT *arc, nodeT *from)
{
	return (arc->start == from) ? arc->end : arc->start;
}


/* ======================================================================
 * Drawing
 * ====================================================================== */

void DrawNode(nodeT *node, string color)
{
	DrawFilledCircleWithLabel(node->loc, color, node->name);
}

void DrawGraphArc(arcT *arc, string color)
{
	DrawLineBetween(arc->start->loc, arc->end->loc, color);
}

/* Function: DrawGraph
 * -------------------
 * Wipes the window, draws the background picture, then overlays every arc
 * and then every node (nodes last so they sit on top of the lines).
 */
void DrawGraph(graphT &graph)
{
	// Clear any previous drawing by painting the window white.
	SetPenColor("White");
	MovePen(0, 0);
	StartFilledRegion(1.0);
	DrawLine(GetWindowWidth(), 0);
	DrawLine(0, GetWindowHeight());
	DrawLine(-GetWindowWidth(), 0);
	DrawLine(0, -GetWindowHeight());
	EndFilledRegion();

	MovePen(0, 0);
	DrawNamedPicture(graph.imageFile);

	for (int i = 0; i < graph.arcs.size(); i++) {
		DrawGraphArc(graph.arcs[i], ArcColor);
	}
	Map<nodeT *>::Iterator itr = graph.nodes.iterator();
	while (itr.hasNext()) {
		DrawNode(graph.nodes.get(itr.next()), NodeColor);
	}
	UpdateDisplay();
}

/* Function: ChooseNode
 * --------------------
 * Prompts the user, then waits until they click on (or very near) a node.
 * Clicks that miss every node are reported and ignored.
 */
nodeT *ChooseNode(graphT &graph, string prompt)
{
	cout << prompt << endl;
	while (true) {
		coordT click = GetMouseClick();
		Map<nodeT *>::Iterator itr = graph.nodes.iterator();
		while (itr.hasNext()) {
			nodeT *node = graph.nodes.get(itr.next());
			if (WithinDistance(click, node->loc)) {
				DrawNode(node, SelectedColor);
				UpdateDisplay();
				cout << "  You chose " << node->name << "." << endl;
				return node;
			}
		}
		cout << "  That click wasn't on a location. Please click on a node." << endl;
	}
}


/* ======================================================================
 * Dijkstra's shortest path
 * ====================================================================== */

/* Function: ComparePaths
 * ----------------------
 * PQueue callback. The PQueue dequeues the MAX priority first, and we want
 * the SHORTEST path first, so a shorter path must compare as "greater".
 */
int ComparePaths(pathT one, pathT two)
{
	if (one.length < two.length) return 1;
	if (one.length > two.length) return -1;
	return 0;
}

/* Function: FindShortestPath
 * --------------------------
 * Dijkstra's algorithm. Keeps a priority queue of partial paths ordered by
 * total distance. Each round dequeues the shortest path; if it ends at the
 * destination we're done (no shorter path can exist, since every path still
 * in the queue is at least as long and arc lengths are non-negative).
 * Otherwise it is extended along every arc leaving its last node.
 *
 *  - "finished" holds nodes whose shortest distance is already known; a
 *    path that reaches one of them again can be ignored.
 *  - "bestKnown" records the shortest distance seen so far to each node, so
 *    an extended path is discarded if we already know a better way there.
 *
 * Returns true and fills in result if a path exists.
 */
bool FindShortestPath(nodeT *start, nodeT *finish, pathT &result)
{
	PQueue<pathT> queue(ComparePaths);
	Set<nodeT *> finished;
	Map<double> bestKnown;

	pathT initial;
	initial.length = 0;
	initial.last = start;
	queue.enqueue(initial);
	bestKnown.put(start->name, 0);

	while (!queue.isEmpty()) {
		pathT path = queue.dequeueMax();
		nodeT *current = path.last;
		if (finished.contains(current)) continue;   // stale, longer entry
		if (current == finish) {
			result = path;
			return true;
		}
		finished.add(current);

		for (int i = 0; i < current->arcs.size(); i++) {
			arcT *arc = current->arcs[i];
			nodeT *next = OtherEnd(arc, current);
			if (finished.contains(next)) continue;

			double newLength = path.length + arc->distance;
			if (bestKnown.containsKey(next->name) &&
			    bestKnown.get(next->name) <= newLength) {
				continue;                            // already know better
			}
			bestKnown.put(next->name, newLength);

			pathT extended = path;                   // Vector deep-copies
			extended.arcs.add(arc);
			extended.length = newLength;
			extended.last = next;
			queue.enqueue(extended);
		}
	}
	return false;
}

/* Function: DoShortestPath
 * ------------------------
 * Menu action: user picks two nodes, the path is computed, highlighted on
 * screen and printed to the console.
 */
void DoShortestPath(graphT &graph)
{
	DrawGraph(graph);
	nodeT *start = ChooseNode(graph, "Click on starting location...");
	nodeT *finish = ChooseNode(graph, "Click on ending location...");

	pathT path;
	if (!FindShortestPath(start, finish, path)) {
		cout << "There is no path from " << start->name << " to "
		     << finish->name << "." << endl;
		return;
	}
	if (start == finish) {
		cout << "Start and end are the same location -- distance 0." << endl;
		return;
	}

	cout << "Shortest path from " << start->name << " to " << finish->name << ":" << endl;
	nodeT *at = start;
	for (int i = 0; i < path.arcs.size(); i++) {
		arcT *arc = path.arcs[i];
		nodeT *next = OtherEnd(arc, at);
		cout << "  " << at->name << " -> " << next->name
		     << " (" << arc->distance << ")" << endl;
		DrawGraphArc(arc, PathColor);
		at = next;
	}
	// Redraw nodes on the path so they sit on top of the highlighted lines.
	at = start;
	DrawNode(at, PathColor);
	for (int i = 0; i < path.arcs.size(); i++) {
		at = OtherEnd(path.arcs[i], at);
		DrawNode(at, PathColor);
	}
	UpdateDisplay();
	cout << "Total distance: " << path.length << endl;
}


/* ======================================================================
 * Kruskal's minimal spanning tree
 * ====================================================================== */

/* Function: CompareArcs
 * ---------------------
 * PQueue callback: a shorter arc is HIGHER priority (dequeued first).
 */
int CompareArcs(arcT *one, arcT *two)
{
	if (one->distance < two->distance) return 1;
	if (one->distance > two->distance) return -1;
	return 0;
}

/* Function: ComputeMinimalSpanningTree
 * ------------------------------------
 * Kruskal's algorithm. Every node starts in its own "connected set". Arcs
 * are considered shortest-first; an arc whose endpoints are already in the
 * same set would form a cycle and is skipped, otherwise it joins the tree
 * and the two sets are merged with unionWith.
 *
 * Implementation detail: connected maps each node's name to a POINTER to
 * the set it currently belongs to, so all members of a merged group share
 * one Set object. "Same set?" then becomes a cheap pointer comparison, and
 * on a merge we fold the smaller set into the larger and repoint only the
 * smaller set's members.
 *
 * Returns the arcs of the tree; also draws each one as it is accepted.
 */
Vector<arcT *> ComputeMinimalSpanningTree(graphT &graph)
{
	Vector<arcT *> tree;
	Map<Set<nodeT *> *> connected;
	Map<nodeT *>::Iterator itr = graph.nodes.iterator();
	while (itr.hasNext()) {
		nodeT *node = graph.nodes.get(itr.next());
		Set<nodeT *> *group = new Set<nodeT *>;
		group->add(node);
		connected.put(node->name, group);
	}

	PQueue<arcT *> queue(CompareArcs);
	for (int i = 0; i < graph.arcs.size(); i++) {
		queue.enqueue(graph.arcs[i]);
	}

	int numGroups = graph.nodes.size();
	cout << "Processing arcs in order of cost:" << endl;
	while (numGroups > 1 && !queue.isEmpty()) {
		arcT *arc = queue.dequeueMax();
		Set<nodeT *> *groupA = connected.get(arc->start->name);
		Set<nodeT *> *groupB = connected.get(arc->end->name);
		cout << "  " << arc->distance << ": " << arc->start->name
		     << " -> " << arc->end->name;
		if (groupA == groupB) {
			cout << " (not needed)" << endl;
			continue;
		}
		cout << endl;

		if (groupA->size() < groupB->size()) {      // fold smaller into larger
			Set<nodeT *> *tmp = groupA; groupA = groupB; groupB = tmp;
		}
		groupA->unionWith(*groupB);
		Set<nodeT *>::Iterator members = groupB->iterator();
		while (members.hasNext()) {
			connected.put(members.next()->name, groupA);
		}
		delete groupB;
		numGroups--;

		tree.add(arc);
		DrawGraphArc(arc, TreeColor);
		UpdateDisplay();
	}

	// Free the remaining group set(s). Normally one, but more if the graph
	// was not fully connected. Collect distinct pointers first so no set is
	// deleted twice.
	Set<Set<nodeT *> *> remaining;
	Map<Set<nodeT *> *>::Iterator gi = connected.iterator();
	while (gi.hasNext()) remaining.add(connected.get(gi.next()));
	Set<Set<nodeT *> *>::Iterator ri = remaining.iterator();
	while (ri.hasNext()) delete ri.next();

	if (numGroups > 1) {
		cout << "Note: the graph is not fully connected; this is a minimal "
		     << "spanning forest of " << numGroups << " pieces." << endl;
	}
	return tree;
}

void DoMinimalSpanningTree(graphT &graph)
{
	DrawGraph(graph);
	Vector<arcT *> tree = ComputeMinimalSpanningTree(graph);
	double total = 0;
	for (int i = 0; i < tree.size(); i++) {
		total += tree[i]->distance;
	}
	Map<nodeT *>::Iterator itr = graph.nodes.iterator();
	while (itr.hasNext()) {
		DrawNode(graph.nodes.get(itr.next()), NodeColor);
	}
	UpdateDisplay();
	cout << "Minimal spanning tree uses " << tree.size() << " arcs with total cost "
	     << total << "." << endl;
}


/* ======================================================================
 * Main program
 * ====================================================================== */

int GetMenuChoice()
{
	cout << endl << "Your options are:" << endl
	     << "    (1) Choose a new graph data file" << endl
	     << "    (2) Find shortest path using Dijkstra's algorithm" << endl
	     << "    (3) Compute the minimal spanning tree using Kruskal's algorithm" << endl
	     << "    (4) Quit" << endl;
	while (true) {
		cout << "Enter choice: ";
		int choice = GetInteger();
		if (choice >= 1 && choice <= 4) return choice;
		cout << "Please enter a number between 1 and 4." << endl;
	}
}

int main()
{
	InitGraphics();
	SetWindowTitle("CS106 Pathfinder");
    cout << "This masterful piece of work is a graph extravaganza!" << endl
        << "The main attractions include a lovely visual presentation of the graph" << endl
        << "along with an implementation of Dijkstra's shortest path algorithm and" << endl
        << "the computation of a minimal spanning tree.  Enjoy!" << endl << endl;

	graphT graph;
	LoadGraphFromUser(graph);
	DrawGraph(graph);

	while (true) {
		int choice = GetMenuChoice();
		if (choice == 4) break;
		switch (choice) {
			case 1: LoadGraphFromUser(graph); DrawGraph(graph); break;
			case 2: DoShortestPath(graph); break;
			case 3: DoMinimalSpanningTree(graph); break;
		}
	}

	FreeGraph(graph);
	cout << "Thanks for using Pathfinder!" << endl;
	return (0);
}
