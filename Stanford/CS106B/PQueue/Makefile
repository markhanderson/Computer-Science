# Builds one executable per pqueue implementation:
#   make            builds pqvector, pqlist, pqchunk and pqheap
#   make pqchunk BLOCK=16    chunklist with a different MaxElemsPerBlock
#   make check      builds with AddressSanitizer and runs the tests on all four
#
# Each build compiles the shared client code together with one
# pq<version>.cpp, defining PQ_<VERSION> so pqueue.h declares the matching
# private section.

CXX      ?= g++
CXXFLAGS ?= -O2 -Wall -Wextra
CPPFLAGS += -Ics106lib
BLOCK    ?= 64

CLIENT  = main.cpp pqueuetest.cpp performance.cpp
HEADERS = pqueue.h pqueuetest.h performance.h vector.h $(wildcard cs106lib/*.h)
IMPLS   = pqvector pqlist pqchunk pqheap

all: $(IMPLS)

pqvector pqlist pqheap: %: %.cpp $(CLIENT) $(HEADERS)
	$(CXX) $(CXXFLAGS) $(CPPFLAGS) -DPQ_$(shell echo $* | sed 's/^pq//' | tr a-z A-Z) -o $@ $< $(CLIENT)

pqchunk: pqchunk.cpp $(CLIENT) $(HEADERS)
	$(CXX) $(CXXFLAGS) $(CPPFLAGS) -DPQ_CHUNK -DPQ_BLOCK_SIZE=$(BLOCK) -o $@ $< $(CLIENT)

# The tests stop at "Hit return" prompts and the performance menu; feeding
# empty lines answers the prompts and the final 0 quits the time trials.
check:
	@for impl in $(IMPLS); do \
		$(MAKE) -s -B $$impl CXXFLAGS="-O1 -g -fsanitize=address,undefined -Wall -Wextra" || exit 1; \
		printf '\n\n\n\n0\n' | ./$$impl > $$impl.check.txt 2>&1 || { cat $$impl.check.txt; exit 1; }; \
		grep -H "Are they sorted\|All stress" $$impl.check.txt; \
	done
	@rm -f *.check.txt
	@$(MAKE) -s -B all

clean:
	rm -f $(IMPLS) *.check.txt

.PHONY: all check clean
