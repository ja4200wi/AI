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
      bestNeighbor = pickRandomBest(neighbors, bestNeighbor.getQuality());
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

  public static ChessState pickRandomBest(LinkedList<ChessState> list,int highestQuality){
    ChessState randomBest = null;
    LinkedList<ChessState> bestOnes = new LinkedList<>();
    Iterator<ChessState> it = list.iterator();
    while(it.hasNext()) {
      ChessState now = it.next();
      if (now.getQuality() == highestQuality) {
        bestOnes.add(now);
      }
    }
      int amount = bestOnes.size();
      int random = (int)(Math.random() * amount);
      for(int i = 0;i<=random;i++){
        Iterator<ChessState> it2 = bestOnes.iterator();
        randomBest = it2.next();
      }
    return randomBest;
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
