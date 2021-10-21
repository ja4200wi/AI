package KI_Bohnenspiel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class Knoten {

  static final int C = 1;

  Knoten predecessor;

  boolean isLeaf;
  State state;
  boolean hasBeenPlayedOut = false;

  int winsA = 0;
  int winsB = 0;
  int tries = 0;

  ArrayList<Knoten> children = new ArrayList<>();
  boolean[] isPossible = new boolean[6];
  boolean[] wasMade = new boolean[6];


  static Knoten getFirstKnot(State state){
    Knoten first = new Knoten();
    first.state = state;
    first.isLeaf = true;
    first.predecessor = null;
    return first;
  }

  Knoten appendKnot(State state, int move){
    Knoten child = new Knoten();
    child.isLeaf = true;
    child.state = state;
    child.predecessor = this;
   if(this.children.size() == this.state.amountOfPosMoves()){
     this.isLeaf = true;
   }
    this.children.add(child);
    return child;
  }




}
