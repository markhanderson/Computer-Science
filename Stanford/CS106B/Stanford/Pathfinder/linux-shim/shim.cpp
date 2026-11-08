/* Implementation of the Linux test stand-ins. Not part of the assignment. */
#include "genlib.h"
#include "simpio.h"
#include "extgraph.h"
#include <cstdio>
#include <cstdlib>
#include <cmath>
#include <fstream>
#include <sstream>
#include <vector>
#include <map>
#include <climits>
#include <unistd.h>

void Error(string msg) { cerr << "Error: " << msg << endl; exit(1); }

string GetLine() {
	string line;
	if (!getline(cin, line)) { cout << "\n[shim] end of input, exiting" << endl; exit(0); }
	if (!line.empty() && line[line.size()-1] == '\r') line.erase(line.size()-1);
	cout << line << endl;              // echo, so transcripts read naturally
	return line;
}
int GetInteger() {
	while (true) {
		string s = GetLine();
		istringstream in(s); int n; char extra;
		if ((in >> n) && !(in >> extra)) return n;
		cout << "Illegal integer format. Try again." << endl << "Retry: ";
	}
}
double GetReal() {
	while (true) {
		string s = GetLine();
		istringstream in(s); double d; char extra;
		if ((in >> d) && !(in >> extra)) return d;
		cout << "Illegal numeric format. Try again." << endl << "Retry: ";
	}
}

/* ---------- graphics recorded as SVG ---------- */
static const double W = 7.6, H = 4.6, PX = 100;  // inches, px per inch
static double penX = 0, penY = 0, mouseX = 0, mouseY = 0;
static string color = "black";
static bool filling = false;
static vector<pair<double,double> > poly;
static vector<string> elems;
static int snapshot = 0;

static string svgColor(string c) {
	static map<string,string> m;
	if (m.empty()) {
		m["Black"]="#000"; m["Dark Gray"]="#444"; m["Gray"]="#888"; m["Light Gray"]="#ccc";
		m["White"]="#fff"; m["Red"]="#e00"; m["Yellow"]="#ee0"; m["Green"]="#0a0";
		m["Cyan"]="#0cc"; m["Blue"]="#00d"; m["Magenta"]="#d0d";
	}
	return m.count(c) ? m[c] : "#000";
}
static double sx(double x) { return x * PX; }
static double sy(double y) { return (H - y) * PX; }

void InitGraphics() { elems.clear(); penX = penY = 0; }
void SetWindowTitle(string) {}
double GetWindowWidth() { return W; }
double GetWindowHeight() { return H; }
void MovePen(double x, double y) { penX = x; penY = y; if (filling) poly.push_back(make_pair(x,y)); }
void SetPenColor(string c) { color = svgColor(c); }
void SetFont(string) {}
void SetPointSize(int) {}
void DrawLine(double dx, double dy) {
	if (filling) { if (poly.empty()) poly.push_back(make_pair(penX,penY)); poly.push_back(make_pair(penX+dx,penY+dy)); }
	else {
		char b[256];
		snprintf(b, sizeof b, "<line x1='%.1f' y1='%.1f' x2='%.1f' y2='%.1f' stroke='%s' stroke-width='1.5'/>",
		         sx(penX), sy(penY), sx(penX+dx), sy(penY+dy), color.c_str());
		elems.push_back(b);
	}
	penX += dx; penY += dy;
}
void DrawArc(double r, double start, double sweep) {
	// CS106 convention: the current point lies on the circle at angle `start`.
	double a = start * M_PI / 180, cx = penX - r*cos(a), cy = penY - r*sin(a);
	char b[256];
	snprintf(b, sizeof b, "<circle cx='%.1f' cy='%.1f' r='%.1f' %s/>", sx(cx), sy(cy), r*PX,
	         filling ? ("fill='" + color + "'").c_str() : ("fill='none' stroke='" + color + "'").c_str());
	elems.push_back(b);
	double e = (start + sweep) * M_PI / 180;
	penX = cx + r*cos(e); penY = cy + r*sin(e);
}
void StartFilledRegion(double) { filling = true; poly.clear(); }
void EndFilledRegion() {
	filling = false;
	if (poly.size() > 2) {
		// A white rectangle covering the window is a "clear": drop old elements.
		if (color == "#fff") elems.clear();
		string pts;
		for (size_t i = 0; i < poly.size(); i++) { char b[64]; snprintf(b, sizeof b, "%.1f,%.1f ", sx(poly[i].first), sy(poly[i].second)); pts += b; }
		elems.push_back("<polygon points='" + pts + "' fill='" + color + "'/>");
	}
	poly.clear();
}
static string esc(string s) {
	string o; for (size_t i=0;i<s.size();i++){ char c=s[i]; if(c=='<')o+="&lt;"; else if(c=='&')o+="&amp;"; else o+=c; } return o;
}
void DrawTextString(string text) {
	char b[128];
	snprintf(b, sizeof b, "<text x='%.1f' y='%.1f' font-family='Helvetica' font-size='9pt' fill='%s'>", sx(penX)+2, sy(penY)+4, color.c_str());
	elems.push_back(string(b) + esc(text) + "</text>");
	penX += text.size() * 0.07;
}
static bool imageSize(string path, int &w, int &h) {
	ifstream in(path.c_str(), ios::binary); if (!in) return false;
	vector<unsigned char> d((istreambuf_iterator<char>(in)), istreambuf_iterator<char>());
	if (d.size() > 26 && d[0]=='B' && d[1]=='M') {
		w = d[18] | d[19]<<8 | d[20]<<16 | d[21]<<24; h = d[22] | d[23]<<8 | d[24]<<16 | d[25]<<24; if (h<0) h=-h; return true;
	}
	for (size_t i = 2; i + 9 < d.size(); ) {          // JPEG: find SOF marker
		if (d[i] != 0xFF) { i++; continue; }
		int m = d[i+1], len = d[i+2]<<8 | d[i+3];
		if (m >= 0xC0 && m <= 0xC3) { h = d[i+5]<<8 | d[i+6]; w = d[i+7]<<8 | d[i+8]; return true; }
		i += 2 + len;
	}
	return false;
}
void DrawNamedPicture(string name) {
	string path = "Pictures/" + name;
	int w, h;
	if (!imageSize(path, w, h)) Error("DrawNamedPicture: can't find picture " + path);
	char cwd[PATH_MAX]; if (!getcwd(cwd, sizeof cwd)) cwd[0] = 0;
	double wi = w / 72.0, hi = h / 72.0;  // pictures are 72 dpi
	char b[PATH_MAX + 200];
	snprintf(b, sizeof b, "<image href='file://%s/%s' x='%.1f' y='%.1f' width='%.1f' height='%.1f' preserveAspectRatio='none'/>",
	         cwd, path.c_str(), sx(penX), sy(penY + hi), wi*PX, hi*PX);
	elems.push_back(b);
}
void UpdateDisplay() {
	const char *dir = getenv("SHIM_SVG_DIR");
	if (!dir) return;
	char name[PATH_MAX]; snprintf(name, sizeof name, "%s/snapshot_%02d.svg", dir, ++snapshot);
	ofstream out(name);
	out << "<svg xmlns='http://www.w3.org/2000/svg' width='" << W*PX << "' height='" << H*PX << "'>\n";
	for (size_t i = 0; i < elems.size(); i++) out << elems[i] << "\n";
	out << "</svg>\n";
}
void WaitForMouseDown() {
	string s = GetLine();
	istringstream in(s);
	if (!(in >> mouseX >> mouseY)) Error("[shim] expected a mouse click as 'x y', got: " + s);
}
void WaitForMouseUp() {}
double GetMouseX() { return mouseX; }
double GetMouseY() { return mouseY; }
