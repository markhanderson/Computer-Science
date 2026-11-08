"""Independent cross-check: runs ../linux-shim/pathfinder on every node pair
of a graph file and compares to a reference Dijkstra / Prim written in Python."""
import heapq, re, subprocess, sys, os

def load(path):
    toks = open(path).read().split()
    i = toks.index("NODES") + 1; nodes = {}
    while toks[i] != "ARCS":
        nodes[toks[i]] = (float(toks[i+1]), float(toks[i+2])); i += 3
    arcs = [(toks[j], toks[j+1], float(toks[j+2])) for j in range(i+1, len(toks), 3)]
    return nodes, arcs

def dijkstra(adj, s):
    dist = {s: 0}; pq = [(0, s)]
    while pq:
        d, u = heapq.heappop(pq)
        if d > dist[u]: continue
        for v, w in adj[u]:
            if d + w < dist.get(v, float("inf")):
                dist[v] = d + w; heapq.heappush(pq, (d + w, v))
    return dist

def prim_weight(nodes, adj):
    start = next(iter(nodes)); seen = {start}; pq = [(w, v) for v, w in adj[start]]; heapq.heapify(pq); total = 0
    while pq:
        w, v = heapq.heappop(pq)
        if v in seen: continue
        seen.add(v); total += w
        for x, wx in adj[v]: heapq.heappush(pq, (wx, x))
    return total

fname = sys.argv[1]
nodes, arcs = load(fname)
adj = {n: [] for n in nodes}
for a, b, w in arcs: adj[a].append((b, w)); adj[b].append((a, w))

names = sorted(nodes); pairs = [(a, b) for a in names for b in names if a != b]
script = [fname]
for a, b in pairs:
    script += ["2", "%g %g" % nodes[a], "%g %g" % nodes[b]]
script += ["3", "4"]
out = subprocess.run([os.path.join(os.path.dirname(__file__), "../linux-shim/pathfinder")],
                     input="\n".join(script) + "\n", capture_output=True, text=True).stdout

got = [float(x) for x in re.findall(r"Total distance: ([\d.]+)", out)]
bad = 0
for (a, b), g in zip(pairs, got):
    ref = dijkstra(adj, a)[b]
    if abs(ref - g) > 1e-6: bad += 1; print("MISMATCH", a, b, "got", g, "expected", ref)
# every printed path must be a real, contiguous route whose arcs sum to the total
for block in re.findall(r"Shortest path from (\w+) to (\w+):\n((?:  .*\n)+)Total distance: ([\d.]+)", out):
    s, t, body, tot = block
    hops = re.findall(r"(\w+) -> (\w+) \(([\d.]+)\)", body)
    ok = hops[0][0] == s and hops[-1][1] == t and all(hops[i][1] == hops[i+1][0] for i in range(len(hops)-1))
    ok = ok and abs(sum(float(h[2]) for h in hops) - float(tot)) < 1e-6
    if not ok: bad += 1; print("BAD PATH", s, t)
mst = float(re.search(r"total cost ([\d.]*\d)", out).group(1))
print("%s: %d/%d shortest paths match, MST %g (reference %g)%s" %
      (fname, len(pairs) - bad, len(pairs), mst, prim_weight(nodes, adj),
       "" if len(got) == len(pairs) and abs(mst - prim_weight(nodes, adj)) < 1e-6 and bad == 0 else "  <-- PROBLEM"))
