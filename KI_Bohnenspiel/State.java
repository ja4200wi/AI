
public class State {
	
	private int[] bohnenFeld = new int[12]; //index 0-5 rot; 6-11 blau
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
	
	public boolean isTerminal() {
		return this.terminal;
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
		return 0; // dummy, sollen wir da jetzt unsere nehmen?
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
		if(moveIsValid(place)) {
			bohnen = this.bohnenFeld[place];
			for(int i = 1; i <= bohnen; i++) {
				this.bohnenFeld[(place + i) % 12]++;
			}
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
	
	public static void main(String[] args) {
		State state = new State();
		System.out.println(state);
	}

}
