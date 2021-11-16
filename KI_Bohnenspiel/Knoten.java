package KI_Bohnenspiel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class Knoten {

	static Knoten bestNextMove;
	static int finalDepth = 9;
	Knoten predecessor;

	boolean isLeaf;
	State state;

	ArrayList<Knoten> children = new ArrayList<>();


	//returns the first knot of a tree with the given state parameter
	static Knoten getFirstKnot(State state) {
		Knoten first = new Knoten();
		first.state = state;
		first.isLeaf = true;
		first.predecessor = null;
		return first;
	}

	//appends and returns a child knot
	Knoten appendKnot(State state) {
		Knoten child = new Knoten();
		child.isLeaf = true;
		child.state = state;
		child.predecessor = this;
		if (this.state.isTerminal()) {
			this.isLeaf = true;
		}
		this.children.add(child);
		return child;
	}

	// tuns the states of getMove method in state into knots,
  // which are child knots of the initial state
	ArrayList<Knoten> getPossibleMovesAsKnots() {
		ArrayList<State> movesAsState = this.state.getPossibleMoves();
		Iterator<State> iterator = movesAsState.iterator();
		while (iterator.hasNext()) {
			this.appendKnot(iterator.next());
		}
		return this.children;
	}

	int max(int depth) {
		if (depth == 0 || this.state.calcTerminal()) {
			return this.state.heuristic007();
		}
		int maxWert = -100000000;
		ArrayList<Knoten> posMoves = getPossibleMovesAsKnots();
		Iterator<Knoten> iterator = posMoves.iterator();
		while (iterator.hasNext()) {
			Knoten k = iterator.next();
			int wert = k.min(depth - 1);
			if (wert > maxWert) {
				maxWert = wert;
				if (depth == finalDepth) {
					bestNextMove = k;
				}
			}
		}
		return maxWert;
	}

	int min(int depth) {
		if (depth == 0 || this.state.calcTerminal()) {
			return this.state.heuristic007();
		}
		int minWert = +10000;
		ArrayList<Knoten> posMoves = getPossibleMovesAsKnots();
		Iterator<Knoten> iterator = posMoves.iterator();
		while (iterator.hasNext()) {
			Knoten k = iterator.next();
			int wert = k.max(depth - 1);
			if (wert < minWert) {
				minWert = wert;
				if (depth == finalDepth) {
					bestNextMove = k;
				}
			}
		}
		return minWert;
	}

	//initiates the AI and returns the best next move
	static State letKIroll(State state) {
		bestNextMove = null;
		Knoten startingKnot = getFirstKnot(state);
		startingKnot.max(finalDepth);
		if (bestNextMove == null) {
			ArrayList<State> geht = state.getPossibleMoves();
			if(geht.size()>0) {
				return geht.get(0);
			}
			System.out.println("Its over...");
		}
		return bestNextMove.state;
	}

	//test method to play against an opponent that plays random moves
	static State someNextMove(State state) {
		ArrayList<State> posMoves = state.getPossibleMoves();
		if(posMoves.isEmpty()) {
			System.out.println("gibt nichts mehr");
			state.setTerminal();
			return state;
		}
		return posMoves.get((int) Math.random() * posMoves.size());
	}
}
