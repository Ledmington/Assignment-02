import sys

# steps for serial execution
# these are intended as the body of an infinite loop
steps = [
	"CF",
	"B1",
	"UP",
	"B2"
]

barriers = ["B1", "B2"]

indent = "\t"

# returns true if s1 appears in "steps" before s2
# if s1 == s2, returns false
def is_before(s1, s2):
	return steps.index(s1) < steps.index(s2)

def p(*args, **kwargs):
    print(indent+"".join(map(str,args)), **kwargs)

def generate_graph():
	serial_arcs = {}
	for i in range(len(steps)-1):
		serial_arcs[steps[i]] = steps[i+1]

	arcs = {}

	# preparing the queue for a BFS
	queue = [(steps[0], steps[0])]

	while len(queue) > 0:
		state = queue.pop(0)
		if state not in arcs:
			arcs[state] = set()

		for i,s in enumerate(state):
			if s not in serial_arcs:
				continue
			new_state = list(state)
			new_state[i] = serial_arcs[s]
			new_state = tuple(new_state)
			arcs[state].add(new_state)
			if new_state not in arcs:
				queue.append(new_state)

	# deleting impossible states due to barriers
	for barrier in barriers:
		for state in arcs.copy():
			s1 = state[0]
			s2 = state[1]
			if (is_before(s1, barrier) and is_before(barrier, s2)) or (is_before(s2, barrier) and is_before(barrier, s1)):
				del arcs[state]

	# deleting arcs to deleted nodes
	for start in arcs:
		for end in arcs[start].copy():
			if end not in arcs.keys():
				arcs[start].remove(end)
	
	tmp = {}
	for k,v in arcs.items():
		s = "".join(k)
		tmp[s] = ["".join(n) for n in v]

	return tmp

def main():
	if len(set(steps)) < len(steps):
		print("elements of steps must have unique IDs", file=sys.stderr)
		exit()

	if len(set(barriers)) < len(barriers):
		print("elements of barriers must have unique IDs", file=sys.stderr)
		exit()

	for b in barriers:
		if b not in steps:
			print("Barrier "+b+" not in steps", file=sys.stderr)
			exit()

	arcs = generate_graph()
	
	print("\\begin{tikzpicture}[node distance=1cm, >=stealth, auto, every place/.style={draw}]")

	p("% nodes")
	first_node = steps[0]+""+steps[0]
	p("\\node [place] ("+first_node+") {"+first_node+"};")
	p("\\coordinate[left of="+first_node+"] (left-"+first_node+");")
	p("\\coordinate[right of="+first_node+"] (right-"+first_node+");")

	for start,ends in arcs.items():
		for end in ends:
			p("\\node [place] ("+end+") [below =of left-"+start+"] {"+end+"};")
			p("\\coordinate[left of="+end+"] (left-"+end+");")
			p("\\coordinate[right of="+end+"] (right-"+end+");")

	p("\n% arcs")
	for start,ends in arcs.items():
		for end in ends:
			p("\\path[->] ("+start+") edge node {} ("+end+");")

	print("\\end{tikzpicture}")


if __name__ == "__main__":
	main()