/* Stand-in for CS106 graphics/extgraph. Drawing is recorded to SVG files
 * (one per UpdateDisplay) and mouse clicks are read from stdin as "x y".
 */
#ifndef _extgraph_h
#define _extgraph_h
#include "genlib.h"
void InitGraphics();
void SetWindowTitle(string title);
double GetWindowWidth();
double GetWindowHeight();
void MovePen(double x, double y);
void DrawLine(double dx, double dy);
void DrawArc(double r, double start, double sweep);
void SetPenColor(string color);
void StartFilledRegion(double density);
void EndFilledRegion();
void SetFont(string font);
void SetPointSize(int size);
void DrawTextString(string text);
void DrawNamedPicture(string name);
void UpdateDisplay();
void WaitForMouseDown();
void WaitForMouseUp();
double GetMouseX();
double GetMouseY();
#endif
