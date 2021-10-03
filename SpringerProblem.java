public class SpringerProblem {

  /*Hill-Climbing(Searchproblem problem)
  State current = createRandomState(problem)
while (true):
  Set neighbors = getNeighbors(current)
  State bestNeighbor = getBestState(neighbors)
if (valueOf(current) <= valueOf(bestNeighbor)):
      return current
else:
  current = bestNeighbor*/

  public static void main(String[] args) {
    ChessState test = new ChessState();
    test.calculateQuality();
    System.out.println(test.toString());
  }
}
