package KI_Bohnenspiel;

import java.util.ArrayList;

public class State {

	private int[] bohnenFeld = new int[12]; // index 0-5 rot; 6-11 blau
	private int scoreBlue;
	private int scoreRed;
	private boolean turn; // true: its reds' turn; false: blue
	private boolean terminal;
	static private boolean iAmStarting = true;
	int lastMoveOnField;

	public State() {
		for (int i = 0; i < 12; i++) {
			this.bohnenFeld[i] = 6;
		}
		this.scoreBlue = 0;
		this.scoreRed = 0;
		this.turn = false;
	}

	public State(int[] state, boolean turn, int punkteRot, int punkteBlau) {
		for (int i = 0; i < state.length; i++) {
			this.bohnenFeld[i] = state[i];
		}
		this.scoreBlue = punkteBlau;
		this.scoreRed = punkteRot;
		this.turn = turn;
	}

	// Checks if the given int array only holds 0s. If yes, return true.
	public static boolean isEmpty(int[] pits) {
		boolean empty = true;
		for (int i = 0; i < pits.length; i++) {
			if (pits[i] != 0) {
				empty = false;
			}
		}
		return empty;
	}

	// Return only the pits of board which belong to red player (0-5)
	public int[] getRedPits() {
		int[] redPits = new int[6];
		for (int i = 0; i < 6; i++) {
			redPits[i] = this.getBohnenFeld()[i];
		}
		return redPits;
	}

	// Return only the pits of board which belong to blue player (6-11)
	public int[] getBluePits() {
		int[] redPits = new int[6];
		for (int i = 6; i < 12; i++) {
			redPits[i - 6] = this.getBohnenFeld()[i];
		}
		return redPits;
	}

	public int heuristic() {
		if (iAmStarting) {
			return this.scoreRed - this.scoreBlue;
		} else {
			return this.scoreBlue - this.scoreRed;
		}
	}

	// berechnet die Punkte die es für den neu erreichten Zustand gibt
	public int pointsForState(int lastPlace) {
		int last = bohnenFeld[lastPlace];
		int points = 0;
		if (last == 2 || last == 4 || last == 6) {
			points += last;
			for (int i = lastPlace - 1; i >= 0; i--) {
				int help = bohnenFeld[i];
				if (help == 2 || help == 4 || help == 6) {
					points += help;
				} else {
					return points;
				}
			}
		}
		return points;
	}

	public boolean isTerminal() {
		return this.terminal;
	}

	public void setTerminal() {
		this.terminal = true;
	}

	/*
	 * Method identifies, whether the given State checkTerminal is terminal or not.
	 * If the state is terminal return true.
	 */
	public boolean calcTerminal() {
		boolean result = false;
		int[] redPits = getRedPits();
		int[] bluePits = getBluePits();
		// If player who is at turn has only 0s it is terminal
		if (isEmpty(redPits) && this.turn) {
			result = true;
		}
		if (isEmpty(bluePits) && !this.turn) {
			result = true;
		}
		// If one player exceeds 36 points our AI shouldn't try looking for new possible
		// moves,because winner is already clear.
		if (this.scoreRed > 36 || this.scoreBlue > 36) {
			result = true;
		}
		return result;
	}

	public boolean equals(Object o) {
		State state = (State) o;
		if (this.turn != state.turn) {
			return false;
		}
		if (this.scoreBlue == state.scoreBlue && this.scoreRed == state.scoreRed) {
			for (int i = 0; i < 12; i++) {
				if (this.bohnenFeld[i] != state.bohnenFeld[i]) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public int hashCode() {
		int a = 0;
		for (int i : this.bohnenFeld) {
			a = 7 * a + i % 7;
		}
		if (this.turn) {
			return a;
		} else {
			return a * -1;
		}
	}

	// checks if the move that the AI wants to do is valid
	public boolean moveIsValid(int place) {
		if (this.turn) {
			// its reds' turn
			if (place < 0 || place > 5) {
				return false;
			}
			if (this.bohnenFeld[place] == 0) {
				return false;
			}
			return true;
		} else {
			// it's blues' turn
			if (place < 6 || place > 11) {
				return false;
			}
			if (this.bohnenFeld[place] == 0) {
				return false;
			}
			return true;
		}
	}

	public void makeAMove(int place) {
		int bohnen = 0;
		int startField = place;

		bohnen = this.bohnenFeld[place];
		this.bohnenFeld[place] = 0;
		for (int i = 1; i <= bohnen; i++) {
			this.bohnenFeld[(place + i) % 12]++;
		}

		int lastPlace = (place + bohnen) % 12; // wo wir uns danach befinden
		if (bohnenFeld[lastPlace] == 2 || bohnenFeld[lastPlace] == 4 || bohnenFeld[lastPlace] == 6) {
			do {
				if (startField < 6) {
					scoreRed += bohnenFeld[lastPlace];
				} else {
					scoreBlue += bohnenFeld[lastPlace];
				}
				bohnenFeld[lastPlace] = 0;
				lastPlace = (lastPlace == 0) ? lastPlace = 11 : --lastPlace;
			} while (bohnenFeld[lastPlace] == 2 || bohnenFeld[lastPlace] == 4 || bohnenFeld[lastPlace] == 6);
		}

		if (this.bohnenFeld[lastPlace % 12] == 2 || this.bohnenFeld[lastPlace % 12] == 4
				|| this.bohnenFeld[lastPlace % 12] == 6) {
			for (int i = (lastPlace % 12); i >= 0; i--) {
				if (this.bohnenFeld[i] == 2 || this.bohnenFeld[i] == 4 || this.bohnenFeld[i] == 6) {
					this.bohnenFeld[i] = 0;
				} else {
					i = -1;
				}
			}
			this.bohnenFeld[lastPlace % 12] = 0;
		}
		this.lastMoveOnField = place;

	}

	public int heuristic007() {
		// Initialisierung Variablen
		int value = 0; // Güte eines Zustands
		int meinScore = iAmStarting ? this.scoreRed : this.scoreBlue;
		int gegScore = !iAmStarting ? this.scoreRed : this.scoreBlue;
		int[] meinFeld = iAmStarting ? this.getRedPits() : this.getBluePits();
		int[] gegFeld = !iAmStarting ? this.getRedPits() : this.getBluePits();

		// Hier Parameter über die die Heuristik schnell angepasst werden kann
		int schatzKistenMulti = 10; // Multiplikator für bereits verteilte Punkte
		int beansInPossMulti = 1; // Multiplikator für Bohnen auf eigenen Feldern
		int gegNullFeldMulti = 1; // Multiplikator für 0 Felder des Gegners

		// Hier grundlegende Parameter
		int schatzKistenPunkte = (meinScore - gegScore) * schatzKistenMulti;
		int gegNullFeld = 0; // Felder die der Gegner nicht spielen kann
		int beansInPossesion = 0; // Bohnen die wir besitzen

		for (int i = 0; i < meinFeld.length; i++) {
			beansInPossesion += meinFeld[i];
		}
		for (int i = 0; i < gegFeld.length; i++) {
			if (gegFeld[i] == 0) {
				gegNullFeld++;
			}
		}
		beansInPossesion *= beansInPossMulti;
		gegNullFeld *= gegNullFeldMulti;

		value += beansInPossesion + gegNullFeld + schatzKistenPunkte;
		return value;
	}

	public String toString() {
		String output = "";
		for (int i = 11; i >= 6; i--) {
			if (i != 6) {
				output += this.bohnenFeld[i] + "; ";
			} else {
				output += this.bohnenFeld[i];
			}
		}

		output += "\n";
		for (int i = 0; i <= 5; i++) {
			if (i != 5) {
				output += this.bohnenFeld[i] + "; ";
			} else {
				output += this.bohnenFeld[i];
			}
		}

		return output;
	}

	/*
	 * diese methode gibt all die states zurück die der spieler, der gerade dran ist
	 * durch ein klicken erreichen kann.
	 */
	public ArrayList<State> getPossibleMoves() {
		ArrayList<State> possibleMoves = new ArrayList<>();

		int base = !turn ? 6 : 0;
		Main.printBoard(this.bohnenFeld);
		for (int x = 0; x < 6; x++) {
			if (this.bohnenFeld[(base + x)] != 0) {
				State clone = this.clone();
				clone.turn = !turn;
				clone.makeAMove(base + x);
				possibleMoves.add(clone);
			}
		}
		return possibleMoves;
	}

	public State clone() {
		State clone = new State();
		for (int i = 0; i < 12; i++) {
			clone.bohnenFeld[i] = this.bohnenFeld[i];
		}
		clone.scoreBlue = this.scoreBlue;
		clone.scoreRed = this.scoreRed;
		clone.turn = this.turn;
		clone.terminal = this.terminal;
		clone.lastMoveOnField = this.lastMoveOnField;
		return clone;
	}

	public int amountOfPosMoves() {
		int count = 0;
		int i = this.turn ? 0 : 5;
		for (int x = 0; x < 6; x++) {
			if (this.bohnenFeld[i + x] > 0) {
				count++;
			}
		}
		return count;
	}

	public int[] getBohnenFeld() {
		return this.bohnenFeld;
	}

	public boolean isTurn() {
		return turn;
	}

	String getGameStats() {
		return "Red: " + this.scoreRed + "\tBlue: " + this.scoreBlue;
	}

	public static void setiAmStarting(boolean iAmStarting) {
		State.iAmStarting = iAmStarting;
	}

	public int getScoreBlue() {
		return scoreBlue;
	}

	public int getScoreRed() {
		return scoreRed;
	}
}
