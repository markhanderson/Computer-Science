/* Minimal stand-in for the 2007 CS106 Vector (same method names). */
#ifndef _vector_h
#define _vector_h
#include "genlib.h"
#include <vector>
template <typename ElemType>
class Vector {
  public:
	int size() const { return (int)v.size(); }
	bool isEmpty() const { return v.empty(); }
	void clear() { v.clear(); }
	ElemType getAt(int i) { check(i); return v[i]; }
	void setAt(int i, ElemType e) { check(i); v[i] = e; }
	void insertAt(int i, ElemType e) { if (i < 0 || i > size()) Error("Vector insertAt out of range"); v.insert(v.begin() + i, e); }
	void removeAt(int i) { check(i); v.erase(v.begin() + i); }
	void add(ElemType e) { v.push_back(e); }
	ElemType &operator[](int i) { check(i); return v[i]; }
  private:
	void check(int i) const { if (i < 0 || i >= size()) Error("Vector index out of range"); }
	std::vector<ElemType> v;
};
#endif
