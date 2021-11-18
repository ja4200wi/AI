package Minesweeper.src;

import java.util.ArrayList;
import java.util.Iterator;
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
  private ArrayList<Integer> safeTiles;

  public LogicalMSAgent(MSField field) {
    super(field);
    MAXVAR = field.getNumOfCols() * field.getNumOfRows();
    numOfRows = this.field.getNumOfRows();
    numOfCols = this.field.getNumOfCols();
    KB = new ArrayList<int[]>();
  }

  public static void main(String[] args) {
    printTruthTable(8);
    MSField f = new MSField("Minesweeper/fields/" + UsageExample.fields[5]);
    LogicalMSAgent testAgent = new LogicalMSAgent(f);
    int count = 1;
    for(int i = 0;i < testAgent.numOfRows;i++){
      for(int j = 0;j < testAgent.numOfCols;j++){
        System.out.print(String.format("%2d",count++) + "\t");
      }
      System.out.println();
    }
    System.out.println(testAgent.findName(2,3));
    ArrayList<Integer> huso = testAgent.getNeighbours(2,3);
    Iterator<Integer> it = huso.iterator();
    while(it.hasNext()) {
      System.out.print(it.next() + " ");
    }
    testAgent.extendKB(2,1,1);
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

  /** Gibt für Feld an Position x,y den Namen zurück.
   * Oben links angefangen bei 1 und von links nach rechts hochzählend.
   */
  public int findName(int x,int y){
    return x + y * numOfCols + 1;
  }

  /** Nimmt x,y und gibt alle Nachbarn um das Feld als AraryList mit int zurück.
   * Die Zahl steht dabei für den Namen des Feldes. Angefangen oben links bei 1 und von links nach rechts dann inkrementierend.
   */
  private ArrayList<Integer> getNeighbours(int x, int y){
    ArrayList<Integer> neighbours = new ArrayList<Integer>();

    if(x == 0 && y == 0){
      // linke obere Ecke
      neighbours.add(this.findName(1,0));
      neighbours.add(this.findName(1,1));
      neighbours.add(this.findName(0,1));
    } else if(x == 0 && y == numOfRows){
      // linke untere Ecke
      neighbours.add(this.findName(0, y-1));
      neighbours.add(this.findName(1,y-1));
      neighbours.add(this.findName(1,y));
    } else if(x == numOfCols && y == 0){
      // rechte obere Ecke
      neighbours.add(this.findName(x-1,0));
      neighbours.add(this.findName(x-1,1));
      neighbours.add(this.findName(x,1));
    } else if(x == numOfCols && y == numOfRows){
      // recht untere Ecke
      neighbours.add(this.findName(x,y-1));
      neighbours.add(this.findName(x-1,y-1));
      neighbours.add(this.findName(x-1,y));
    } else if(x == 0){
      // linker Rand
      neighbours.add(this.findName(0,y-1));
      neighbours.add(this.findName(1,y-1));
      neighbours.add(this.findName(1,y));
      neighbours.add(this.findName(1,y+1));
      neighbours.add(this.findName(0,y+1));
    } else if(x == numOfCols){
      // rechter Rand
      neighbours.add(this.findName(x,y-1));
      neighbours.add(this.findName(x,y+1));
      neighbours.add(this.findName(x-1,y-1));
      neighbours.add(this.findName(x-1,y));
      neighbours.add(this.findName(x-1,y+1));
    } else if(y == 0){
      // oberer Rand
      neighbours.add(this.findName(x-1,y));
      neighbours.add(this.findName(x-1,y+1));
      neighbours.add(this.findName(x,y+1));
      neighbours.add(this.findName(x+1,y+1));
      neighbours.add(this.findName(x+1,y));
    } else if(y == numOfRows){
      // unterer Rand
      neighbours.add(this.findName(x-1,y));
      neighbours.add(this.findName(x-1,y-1));
      neighbours.add(this.findName(x,y-1));
      neighbours.add(this.findName(x+1,y-1));
      neighbours.add(this.findName(x+1,y));
    } else {
      // das Ding ist in der Mitte
      neighbours.add(this.findName(x-1,y-1));
      neighbours.add(this.findName(x,y-1));
      neighbours.add(this.findName(x+1,y-1));
      neighbours.add(this.findName(x-1,y));
      neighbours.add(this.findName(x+1,y));
      neighbours.add(this.findName(x-1,y+1));
      neighbours.add(this.findName(x,y+1));
      neighbours.add(this.findName(x+1,y+1));
    }
    return neighbours;
  }

  /** Gibt 2D-Feld von int zurück, welches alle Interpretationen für n Variablen als Tabelle darstellt.
   */
  private static int[][] printTruthTable(int n) {
    int rows = (int) Math.pow(2,n);
    int [][] truthTable = new int[rows][n];

    for (int i=0; i<rows; i++) {
      for (int j=0; j<n; j++) {
        truthTable [i][n-j-1] = (i/(int)Math.pow(2, j))%2;
      }
    }
    return truthTable;
  }

  /** Nimmt Wahrheitstabelle mit 1en und 0en und ersetzt in den Zeilen mit der passenden Variable
   * tt = truthTable; n = neighbours
   */
  private void addVarNames(int[][] tt,ArrayList<Integer> n){
    if(tt[0].length == n.size()) {
      for(int i = 0;i<tt.length;i++){
        for(int j = 0;j<tt[0].length;j++){
          if(tt[i][j]==0) {
            tt[i][j] = n.get(j);
          } else {
            tt[i][j] = n.get(j) * -1;
          }
        }
      }
    } else {

    }
  }

  /**
  Nimmt Wahrheitstabelle mit Variablen und schliesst die Zeilen aus, die Modelle sind.
  Gibt ArrayList mit Klauseln für KNF zurück.
   */
  private ArrayList<int[]> extractModels(int[][] tt,int feedback){
    ArrayList<int[]> KNF = new ArrayList<int[]>();
  for(int[] row : tt){
    int countNeg = 0;
    for(int i : row){
      if(i<0){countNeg++;}
    }
    if(countNeg!=feedback){KNF.add(row);}
  }
  return KNF;
  }

  /** Nimmt Feedback zu gedrücktem Feld x,y. Wandelt dies in Klauseln um, die in in die KB gespeist werden.
   */
  public void extendKB(int feedback,int x,int y) {
    ArrayList<Integer> neighbours = this.getNeighbours(x,y);
    int [][] truthTable = printTruthTable(neighbours.size());
    addVarNames(truthTable,neighbours);
    KB.addAll(extractModels(truthTable,feedback));
  }

  @Override
  public void activateDisplay() {this.displayActivated=true;}

  @Override
  public void deactivateDisplay() {this.displayActivated=false;}
}
