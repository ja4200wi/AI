import java.util.ArrayList;


public class State {
	
	private static int[] bohnenFeld = new int[12]; //index 0-5 rot; 6-11 blau
	private int scoreBlue;
	private int scoreRed;
	private boolean turn; // true: its reds' turn; false: blue
	private boolean terminal;
	
	public State() {
		for(int i = 0; i < 12; i++) {
			this.bohnenFeld[i] = 6;
		}
		this.scoreBlue = 0;
		this.scoreRed = 0;
		this.turn = false;
	}
	
	public State(int[] state, boolean turn, int punkteRot, int punkteBlau) {
		for(int i = 0; i < state.length; i++) {
			this.bohnenFeld[i] = state[i];
		}
		this.scoreBlue = punkteBlau;
		this.scoreRed = punkteRot;
		this.turn = turn;
	}
	
	public boolean isTerminal() {
		return this.terminal;
	}

	/*
	Method identifies, whether the given State checkTerminal is terminal or not.
	If the state is terminal return true.
	*/
	public boolean calcTerminal() {
	  boolean result = false;
	  int[] redPits = getRedPits(this);
	  int[] bluePits = getBluePits(this);
	  //If player who is at turn has only 0s it is terminal
	  if(isEmpty(redPits) && this.turn) result = true;
	  if(isEmpty(bluePits) && !this.turn) result = true;
	  //If one player exceeds 36 points our AI shouldn't try looking for new possible moves,because winner is already clear.
    if(this.scoreRed>36 || this.scoreBlue>36) result = true;
	return result;
	}
	
  //Checks if the given int array only holds 0s. If yes, return true.
	public static boolean isEmpty(int[] pits) {
	  boolean empty = true;
	  for(int i = 0;i<pits.length;i++){
	  if(pits[i] != 0) empty = false;
	  }
	  return empty;
	}

  //Return only the pits of board which belong to red player (0-5)
	public static int[] getRedPits(State s) {
	int[] redPits = new int[6];
	for(int i = 0;i<6;i++) {
	redPits[i] = s.getBohnenFeld()[i];
	}
	return redPits;
	}
	
	//Return only the pits of board which belong to blue player (6-11)
  	public static int[] getBluePits(State s) {
  	int[] redPits = new int[6];
  	for(int i = 6;i<12;i++) {
  	redPits[i-6] = s.getBohnenFeld()[i];
  	}
  	return redPits;
  	}
	
	public boolean equals(Object o) {
		State state = (State)o;
		if(this.turn != state.turn) {
			return false;
		}
		if(this.scoreBlue == state.scoreBlue && this.scoreRed == state.scoreRed) {
			for(int i = 0; i < 12; i++) {
				if(this.bohnenFeld[i] != state.bohnenFeld[i]) {
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
    if(this.turn){
      return a;
    }else{
      return a * -1;
    }
	}


	// checks if the move that the AI wants to do is valid
	public boolean moveIsValid(int place) {
		if(this.turn) {
			// its reds' turn
			if(place < 0 || place > 5) {
				// invalid da red nicht blauen place auswaehlen darf
				return false;
			}
			if(this.bohnenFeld[place] == 0) {
				//hier liegen keine Bohnen zum verteilen --> invalid
				return false;
			}
			return true;
		} else {
			// it's blues' turn
			if(place < 6 || place > 11) {
				return false;
			}
			if(this.bohnenFeld[place] == 0) {
				return false;
			}
			return true;
		}
	}
	
	public void makeAMove(int place) {
		int bohnen = 0;
		/*if(moveIsValid(place)) {
			bohnen = this.bohnenFeld[place];
			for(int i = 1; i <= bohnen; i++) {
				this.bohnenFeld[(place + i) % 12]++;
			}
		}*/
		
		bohnen = this.bohnenFeld[place];
		for(int i = 1; i <= bohnen; i++) {
			this.bohnenFeld[(place + i) % 12]++;
		}
		
		int lastPlace = place + bohnen; // wo wir uns danach befinden
		// TODO Punkte verteilen wenn bohnenFeld an lastPlace 2,4,6
		if(this.bohnenFeld[lastPlace % 12] == 2 || this.bohnenFeld[lastPlace % 12] == 4 || this.bohnenFeld[lastPlace % 12] == 6) {
			if(this.turn) {
				this.scoreRed += this.bohnenFeld[lastPlace % 12];
			} else {
				this.scoreBlue += this.bohnenFeld[lastPlace % 12];
			}
			this.bohnenFeld[lastPlace % 12] = 0;
			// setTerminality --> schauen ob generell dann Spiel vorbei und this.termianl setzen
		}
		this.bohnenFeld[lastPlace % 12] = 0;
		
	}

	// berechnet den Heursitikwert eines Zustandes 
	public static int heuristic(int lastPlace, boolean maxPlayer, String whoPlayed) {
		// maxPlayer true: red, maxPlayer false: blue
		int value = pointsForState(lastPlace);
		if(maxPlayer) {
			if(whoPlayed.equals("blue")) {
				return (-1) * value;
			}
			return value;
		} else {
			if(whoPlayed.equals("red")) {
				return (-1) * value;
			}
			return value;
		}
	}
	
	// berechnet die Punkte die es für den neu erreichten Zustand gibt
	public static int pointsForState(int lastPlace) {
		int last = bohnenFeld[lastPlace];
		int points = 0;
		if(last == 2 || last == 4 || last == 6) {
			points += last;
			for(int i = lastPlace-1; i >= 0; i--) {
				int help = bohnenFeld[i];
				if(help == 2 || help == 4 || help == 6) {
					points += help;
				} else {
					return points;
				}
			}
		}
		System.out.println("4 " + points);
		return points;
	}
	
	public String toString() {
		String output = "";
        for(int i = 11; i >= 6; i--) {
            if(i != 6) {
            	output += this.bohnenFeld[i] + "; ";
            } else {
            	output += this.bohnenFeld[i];
            }
        }

        output += "\n";
        for(int i = 0; i <= 5; i++) {
            if(i != 5) {
            	output += this.bohnenFeld[i] + "; ";
            } else {
            	output += this.bohnenFeld[i];
            }
        }

        return output;
	}
	
	/*
	diese methode gibt all die states zurück die der spieleer, 
	der gerade dran ist durch ein klicken erreichen kann.
	*/
	ArrayList<State> getPossibleMoves(){
	ArrayList<State> possibleMoves = new ArrayList<>();
		int base = 0;
	  for(int x = 0; x < 6; x++){
	  if(!this.turn){
	  base = 6;
	  }
	  State clone = (State) this.clone();
	  clone.makeAMove(base+x);
	  possibleMoves.add(clone);
	  }
	  return possibleMoves;
	}

  public State clone(){
  State clone = new State();
  	for(int i = 0; i < 12; i++) {
  			clone.bohnenFeld[i] = this.bohnenFeld[i];
  		}
  		clone.scoreBlue = this.scoreBlue;
  		clone.scoreRed = this.scoreRed;
  		clone.turn = this.turn;
  		clone.terminal = this.terminal;
  		return clone;
  }



	public static void main(String[] args) {
		State state = new State();
		
		for(State s: state.getPossibleMoves()){
		System.out.print(s);
		System.out.println("\n");
		}
		
		System.out.println(state);
		
    //int [] testTerminalFeld = {1,2,3,4,5,6,0,0,0,0,0,0};
		//State terminalState = new State(testTerminalFeld,true, 20, 50);
		//System.out.println(terminalState.calcTerminal());
		//System.out.println(terminalState);
		
		int [] testFeld = {1,2,3,4,5,6,2,0,0,0,0,0};
		State testState = new State(testFeld,true, 20, 50);
				System.out.println("is done? " + testState.calcTerminal());

		System.out.println("heuristic: " + heuristic(6, false, "red"));
	}


	public int[] getBohnenFeld(){
	return this.bohnenFeld;
	}
}