package Minesweeper.src;

import java.util.ArrayList;
import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;

public class LogicalMSAgent extends MSAgent {

  private boolean displayActivated = false;
  private boolean firstDecision = true;
  private ArrayList<int[]> KB;
  final int MAXVAR;
  final int NBCLAUSES = 500000;
  private int numOfRows;
  private int numOfCols;

  public LogicalMSAgent(MSField field) {
    super(field);
    MAXVAR = field.getNumOfCols() * field.getNumOfRows();
    numOfRows = this.field.getNumOfRows();
    numOfCols = this.field.getNumOfCols();
  }

  @Override
  public boolean solve() {
    int x, y, feedback;
    do {
      if (displayActivated) {
        System.out.println(field);
      }
      if (firstDecision) {
        x = 0;
        y = 0;
        firstDecision = false;
      } else {
        chooseTile();
        x = 0;
        y = 0;
      }

      if (displayActivated)
        System.out.println("Uncovering (" + x + "," + y + ")");
      feedback = field.uncover(x, y);
      extendKB(feedback,x,y);

    } while (feedback >= 0 && !field.solved());

    if (field.solved()) {
      if (displayActivated) {
        System.out.println("Solved the field");
      }
      return true;
    } else {
      if (displayActivated) {
        System.out.println("BOOM!");
      }
      return false;
    }
  }

  public void chooseTile(){
    ISolver solver = SolverFactory.newDefault();
    solver.newVar(MAXVAR);
    solver.setExpectedNumberOfClauses(NBCLAUSES);
    try {
      for (int i = 0;i<NBCLAUSES;i++){
        int[] clause = KB.get(i);// get the clause from somewhere
            solver.addClause(new VecInt(clause));
      }
      IProblem problem = solver;
      if (problem.isSatisfiable()) {

      } else {

      }
    } catch (Exception e){

    }
  }

  private ArrayList<Integer> getNeighbours(int x, int y){
    ArrayList<Integer> neighbours = new ArrayList<Integer>();

    if(x == 0 && y == 0){
      // linke obere Ecke
      neighbours.add(Tile.findName(1,0));
      neighbours.add(Tile.findName(1,1));
      neighbours.add(Tile.findName(0,1));
    } else if(x == 0 && y == numOfRows){
      // linke untere Ecke
      neighbours.add(Tile.findName(0, y-1));
      neighbours.add(Tile.findName(1,y-1));
      neighbours.add(Tile.findName(1,y));
    } else if(x == numOfCols && y == 0){
      // rechte obere Ecke
      neighbours.add(Tile.findName(x-1,0));
      neighbours.add(Tile.findName(x-1,1));
      neighbours.add(Tile.findName(x,1));
    } else if(x == numOfCols && y == numOfRows){
      // recht untere Ecke
      neighbours.add(Tile.findName(x,y-1));
      neighbours.add(Tile.findName(x-1,y-1));
      neighbours.add(Tile.findName(x-1,y));
    } else if(x == 0){
      // linker Rand
      neighbours.add(Tile.findName(0,y-1));
      neighbours.add(Tile.findName(1,y-1));
      neighbours.add(Tile.findName(1,y));
      neighbours.add(Tile.findName(1,y+1));
      neighbours.add(Tile.findName(0,y+1));
    } else if(x == numOfCols){
      // rechter Rand
      neighbours.add(Tile.findName(x,y-1));
      neighbours.add(Tile.findName(x,y+1));
      neighbours.add(Tile.findName(x-1,y-1));
      neighbours.add(Tile.findName(x-1,y));
      neighbours.add(Tile.findName(x-1,y+1));
    } else if(y == 0){
      // oberer Rand
      neighbours.add(Tile.findName(x-1,y));
      neighbours.add(Tile.findName(x-1,y+1));
      neighbours.add(Tile.findName(x,y+1));
      neighbours.add(Tile.findName(x+1,y+1));
      neighbours.add(Tile.findName(x+1,y));
    } else if(y == numOfRows){
      // unterer Rand
      neighbours.add(Tile.findName(x-1,y));
      neighbours.add(Tile.findName(x-1,y-1));
      neighbours.add(Tile.findName(x,y-1));
      neighbours.add(Tile.findName(x+1,y-1));
      neighbours.add(Tile.findName(x+1,y));
    } else {
      // das Ding ist in der Mitte
      neighbours.add(Tile.findName(x-1,y-1));
      neighbours.add(Tile.findName(x,y-1));
      neighbours.add(Tile.findName(x+1,y-1));
      neighbours.add(Tile.findName(x-1,y));
      neighbours.add(Tile.findName(x+1,y));
      neighbours.add(Tile.findName(x-1,y+1));
      neighbours.add(Tile.findName(x,y+1));
      neighbours.add(Tile.findName(x+1,y+1));
    }
    return neighbours;
  }

  public void extendKB(int feedback,int x,int y) {
    int neighbours = 0;
    if(feedback>=0){
      KB.add(new int[] {x + y * numOfCols + 1});
      //Man hat entweder 3,5 oder 8 Nachbarn
      int tile = x + y * numOfCols + 1;
      if(tile==1 || tile == (numOfCols * numOfRows) || tile == (numOfCols) || tile == (numOfCols * numOfRows - (numOfCols-1))) {
        // Eckfeld -> 3 Nachbarn
        neighbours = 3;
        ArrayList<Integer> allNeighbours = getNeighbours(x,y);
        // 0 1 2 3 Minen drum herum

      } else if (x == 0 || y == 0 || x == numOfCols || y == numOfRows){
        //Randfeld -> 5 Nachbarn
        neighbours = 5;
        ArrayList<Integer> allNeighbours = getNeighbours(x,y);
      } else {
        //Mitte -> 8 Nachbarn
        neighbours = 8;
        ArrayList<Integer> allNeighbours = getNeighbours(x,y);
      }
    }
  }

  @Override
  public void activateDisplay() {this.displayActivated=true;}

  @Override
  public void deactivateDisplay() {this.displayActivated=false;}
}
