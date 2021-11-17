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

  public LogicalMSAgent(MSField field) {
    super(field);
    MAXVAR = field.getNumOfCols() * field.getNumOfRows();
  }

  @Override
  public boolean solve() {
    int numOfRows = this.field.getNumOfRows();
    int numOfCols = this.field.getNumOfCols();
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
      //Man hat entweder 3,5 oder 8 Nachbarn
      
    }
  }

  @Override
  public void activateDisplay() {this.displayActivated=true;}

  @Override
  public void deactivateDisplay() {this.displayActivated=false;}
}
