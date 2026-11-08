/* Minimal stand-in for the 2007 CS106 Set (client comparison callback). */
#ifndef _set_h
#define _set_h
#include "genlib.h"
#include "cmpfn.h"
#include <set>
template <typename ElemType>
class Set {
	struct Less {
		int (*fn)(ElemType, ElemType);
		bool operator()(const ElemType &a, const ElemType &b) const { return fn(a, b) < 0; }
	};
	typedef std::set<ElemType, Less> Impl;
  public:
	Set(int (*cmpFn)(ElemType, ElemType) = OperatorCmp) : s(makeLess(cmpFn)) {}
	int size() const { return (int)s.size(); }
	bool isEmpty() const { return s.empty(); }
	void add(ElemType e) { s.insert(e); }
	void remove(ElemType e) { s.erase(e); }
	bool contains(ElemType e) { return s.count(e) > 0; }
	void clear() { s.clear(); }
	void unionWith(Set &other) { s.insert(other.s.begin(), other.s.end()); }
	bool isSubsetOf(Set &other) {
		for (typename Impl::iterator i = s.begin(); i != s.end(); ++i) if (!other.contains(*i)) return false;
		return true;
	}
	bool equals(Set &other) { return isSubsetOf(other) && other.isSubsetOf(*this); }
	class Iterator {
	  public:
		bool hasNext() { return it != end; }
		ElemType next() { if (!hasNext()) Error("Set iterator exhausted"); return *(it++); }
	  private:
		friend class Set;
		typename Impl::iterator it, end;
	};
	Iterator iterator() { Iterator i; i.it = s.begin(); i.end = s.end(); return i; }
  private:
	static Less makeLess(int (*f)(ElemType, ElemType)) { Less l; l.fn = f; return l; }
	Impl s;
};
#endif
