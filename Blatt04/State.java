package Blatt04;

public class State {

	// Aufgabe 2a
	int[] bohnenFeld; // index 0-5 sind auf der grünen , index 6-11 auf roten Seite
	int scoreBlue;
	int scoreRed;
	boolean turn; // false = Spieler1; true = Spieler2

	// Aufgabe 2a
	public State(int[] i) {
		this.bohnenFeld = i;
		this.scoreBlue = 0;
		this.scoreRed = 0;
		this.turn = false;
	}
	
	/*public State() {
		this.bohnenFeld = new int[] { 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6 };
		this.scoreBlue = 0;
		this.scoreRed = 0;
	}*/

	// Aufgabe 2b
	boolean equals(State state) {
    if(state.turn!=this.turn){
      return false;
    }
		for (int x = 0; x < state.bohnenFeld.length; x++) {
			if (state.bohnenFeld[x] != this.bohnenFeld[x]) {
				return false;
			}
		}
		return true;
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
	

	
	public static void main(String[] args) {
		int[] a = new int[] { 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6 };
		State s = new State(a);
		System.out.println(s.hashCode());
		
		//test:
		int[] a1 = new int[] { 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6 };
		
		int[] a2 = new int[] { 1, 6, 2, 6, 6, 14, 6, 6, 6, 6, 6, 6 };
		int[] a3 = new int[] { 6, 11, 6, 6, 6, 6, 6, 9, 6, 6, 6, 6 };
		int[] a4 = new int[] { 6, 6, 6, 2, 3, 2, 2, 7, 6, 6, 6, 6 };
		int[] a5 = new int[] { 6, 11, 6, 6, 6, 6, 6, 9, 6, 6, 6, 6 };
		
		System.out.println("a1 equals: " + s.equals(new State(a1)));

	}
}
