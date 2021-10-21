package KI_Bohnenspiel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class Knoten {


  static Knoten bestNextMove;
  static Knoten bestNextMoveLow;
  static int finalDepth = 2;
  static int finalDepthLow = 6;
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

  Knoten appendKnot(State state){
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

  ArrayList<Knoten> getPossibleMovesAsKnots(){
    ArrayList<State> movesAsState = this.state.getPossibleMoves();
    Iterator<State> iterator = movesAsState.iterator();
    while(iterator.hasNext()){
      this.appendKnot(iterator.next());
    }
    return this.children;
  }

  int max(int depth){
    if(depth == 0 || this.state.isTerminal()){
      return this.state.heuristic();
    }
    int maxWert = -100000000;
    ArrayList<Knoten> posMoves = getPossibleMovesAsKnots();
    Iterator<Knoten> iterator = posMoves.iterator();
    while(iterator.hasNext()){
      Knoten k = iterator.next();
      int wert = k.min(depth-1);
      if(wert > maxWert){
        maxWert = wert;
        if(depth == finalDepth){
          bestNextMove = k;
        }
      }
    }
    return maxWert;
  }

  int min(int depth){
    if(depth == 0 || this.state.isTerminal()){
      return this.state.heuristic();
    }
    int minWert = +100000000;
    ArrayList<Knoten> posMoves = getPossibleMovesAsKnots();
    Iterator<Knoten> iterator = posMoves.iterator();
    while(iterator.hasNext()){
      Knoten k = iterator.next();
      int wert = k.max(depth-1);
      if(wert < minWert){
        minWert = wert;
        if(depth == finalDepth){
          bestNextMove = k;
        }
      }
    }
    return minWert;
  }

  static State letKIroll(State state){
    bestNextMove = null;
    Knoten startingKnot = getFirstKnot(state);
    startingKnot.max(finalDepth);
    if(bestNextMove == null){
      System.out.println("Its over...");
    }
    return bestNextMove.state;
  }




  public static void main(String[] args){
    State state = new State();
    int moves = 0;
    while(!state.isTerminal() && moves < 60){
      moves++;
      state = letKIroll(state);
      System.out.println(state);
      state = someNextMove(state);
      System.out.println(state);
      System.out.println(state.getGameStats());
      System.out.println();

    }
  }

  static State someNextMove(State state){
    return state.getPossibleMoves().get(2);
  }



  //lowlights

  static State letKIrollLow(State state){
    bestNextMoveLow = null;
    Knoten startingKnot = getFirstKnot(state);
    startingKnot.maxLow(finalDepthLow);
    if(bestNextMoveLow == null){
      System.out.println("Its over...");
    }
    return bestNextMoveLow.state;
  }

  int maxLow(int depth){
    if(depth == 0 || this.state.isTerminal()){
      return this.state.heuristic();
    }
    int maxWert = 100000000;
    ArrayList<Knoten> posMoves = getPossibleMovesAsKnots();
    Iterator<Knoten> iterator = posMoves.iterator();
    while(iterator.hasNext()){
      Knoten k = iterator.next();
      int wert = k.minLow(depth-1);
      if(wert < maxWert){
        maxWert = wert;
        if(depth == finalDepthLow){
          bestNextMoveLow = k;
        }
      }
    }
    return maxWert;
  }

  int minLow(int depth){
    if(depth == 0 || this.state.isTerminal()){
      return this.state.heuristic();
    }
    int minWert = -100000000;
    ArrayList<Knoten> posMoves = getPossibleMovesAsKnots();
    Iterator<Knoten> iterator = posMoves.iterator();
    while(iterator.hasNext()){
      Knoten k = iterator.next();
      int wert = k.maxLow(depth-1);
      if(wert > minWert){
        minWert = wert;
        if(depth == finalDepthLow){
          bestNextMoveLow = k;
        }
      }
    }
    return minWert;
  }
}
