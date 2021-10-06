public class State {

  // Aufgabe 2a
  int[] bohnenFeld; //index 0-5 sind auf der grünen , index 6-11 auf roten Seite
  int scoreBlue;
  int scoreRed;


  // Aufgabe 2a
  public State(){
    this.bohnenFeld = new int[]{6,6,6,6,6,6,6,6,6,6,6,6};
    this.scoreBlue = 0;
    this.scoreRed = 0;
  }


  // Aufgabe 2b
  boolean equals(State state){
    for(int x = 0; x < state.bohnenFeld.length; x++){
        if(state.bohnenFeld[x] != this.bohnenFeld[x]){
          return false;
      }
    }
    return true;
  }


  public int hashCode(){
    return 0;
  }
}
