package KI_Bohnenspiel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class Knoten {


  Knoten predecessor;

  boolean isLeaf;
  State state;


  ArrayList<Knoten> children = new ArrayList<>();


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
   if(this.state.isTerminal()){
     this.isLeaf = true;
   }
    this.children.add(child);
    return child;
  }




}
