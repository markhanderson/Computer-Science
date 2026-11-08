# Builds the assignment on Linux against the stand-in CS106 library.
STARTER = ..
CXXFLAGS = -std=c++98 -Wall -Wextra -g -fsanitize=address,undefined -I. -I$(STARTER)
all: pathfinder pqtest
pathfinder: $(STARTER)/pathfinder.cpp $(STARTER)/pqueue.h $(STARTER)/pqueue.cpp shim.cpp *.h
	g++ $(CXXFLAGS) -o $@ $(STARTER)/pathfinder.cpp shim.cpp
pqtest: $(STARTER)/pqtest.cpp $(STARTER)/pqueue.h $(STARTER)/pqueue.cpp shim.cpp *.h
	g++ $(CXXFLAGS) -o $@ $(STARTER)/pqtest.cpp shim.cpp
clean:
	rm -f pathfinder pqtest
