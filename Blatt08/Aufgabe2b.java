package Blatt08;

import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

public class Aufgabe2b {

  public static void main(String[] args) {

    final int MAXVAR = 1000000;
    final int NBCLAUSES = 500000;
    ISolver solver = SolverFactory.newDefault();
    solver.newVar(MAXVAR);
    solver.setExpectedNumberOfClauses(NBCLAUSES);

    for (int i = 0; i < NBCLAUSES; i++) {
      int[] clause = {10};// get the clause from somewhere
      try {
        solver.addClause(new VecInt(clause));
      } catch (ContradictionException e) {
        e.printStackTrace();
      }
    }
    IProblem problem = solver;
    try {
      if (problem.isSatisfiable()) {
      } else {
      }
    } catch (TimeoutException e) {
      e.printStackTrace();
    }
  }
}
