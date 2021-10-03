import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

public class SpringerProblem {

  public static ChessState HillClimbing(ChessState startState) throws CloneNotSupportedException {
    ChessState current = new ChessState();
    int sidesteps = 100;
    while (true) {
      LinkedList<ChessState> neighbors = ChessState.getNeighbors(current);
      ChessState bestNeighbor = getBestState(neighbors);
      if (current.getQuality() >= bestNeighbor.getQuality()) {
        if(sidesteps>0){
          current = bestNeighbor;
          sidesteps--;
        } else {
          return current;
        }
      } else {
        current = bestNeighbor;
      }
    }
  }

  public static ChessState getBestState(LinkedList neighbors){
    ChessState maximum = new ChessState();
    int maximumQuality = maximum.getQuality();
    Iterator<ChessState> it = neighbors.iterator();
    while(it.hasNext()){
      ChessState potential = it.next();
      if(potential.getQuality()>maximumQuality){
        maximum = potential;
        maximumQuality = potential.getQuality();
      }
    }
    return maximum;
  }

  public static void main(String[] args) throws CloneNotSupportedException {
    ChessState test = new ChessState();
    test.calculateQuality();
    ChessState hopefullyBest = HillClimbing(test);
    System.out.println(hopefullyBest.getQuality());
    System.out.println(hopefullyBest);
    hopefullyBest.calculateQuality();
  }
}
