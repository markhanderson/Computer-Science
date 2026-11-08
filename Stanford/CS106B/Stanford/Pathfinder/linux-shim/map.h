/* Minimal stand-in for the 2007 CS106 Map (string keys). */
#ifndef _map_h
#define _map_h
#include "genlib.h"
#include <map>
template <typename ValueType>
class Map {
  public:
	int size() const { return (int)m.size(); }
	bool isEmpty() const { return m.empty(); }
	void put(string key, ValueType value) { m[key] = value; }
	ValueType get(string key) {
		typename std::map<string, ValueType>::iterator it = m.find(key);
		if (it == m.end()) Error("Map::get: no value for key " + key);
		return it->second;
	}
	bool containsKey(string key) { return m.count(key) > 0; }
	void remove(string key) { m.erase(key); }
	void clear() { m.clear(); }
	ValueType &operator[](string key) { return m[key]; }
	class Iterator {
	  public:
		bool hasNext() { return it != end; }
		string next() { if (!hasNext()) Error("Map iterator exhausted"); return (it++)->first; }
	  private:
		friend class Map;
		typename std::map<string, ValueType>::iterator it, end;
	};
	Iterator iterator() { Iterator i; i.it = m.begin(); i.end = m.end(); return i; }
  private:
	std::map<string, ValueType> m;
};
#endif
