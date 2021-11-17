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

  public void extendKB(int feedback,int x,int y) {
    if(feedback>=0){
      KB.add(new int[] {x + y * numOfCols + 1});
      //Man hat entweder 3,5 oder 8 Nachbarn
      int tile = x + y * numOfCols + 1;
      if(tile==1 || tile == (numOfCols * numOfRows) || tile == (numOfCols) || tile == (numOfCols * numOfRows - (numOfCols-1))) {
        // Eckfeld -> 3 Nachbarn
      } else if (x == 0 || y == 0 || x == numOfCols || y == numOfRows){
        //Randfeld -> 5 Nachbarn
      } else {
        //Mitte -> 8 Nachbarn
      }
    }
  }

  @Override
  public void activateDisplay() {this.displayActivated=true;}

  @Override
  public void deactivateDisplay() {this.displayActivated=false;}
}
