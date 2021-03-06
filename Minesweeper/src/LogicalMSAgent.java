package Minesweeper.src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;

public class LogicalMSAgent extends MSAgent {

  private Random rand = new Random();

  private boolean displayActivated = false;
  private boolean firstDecision = true;
  private ArrayList<int[]> KB;
  final int MAXVAR;
  private int numOfRows;
  private int numOfCols;
  private ArrayList<Integer> safeTiles;
  private ArrayList<Integer> clickedTiles;

  public LogicalMSAgent(MSField field) {
    super(field);
    MAXVAR = field.getNumOfCols() * field.getNumOfRows();
    numOfRows = this.field.getNumOfRows();
    numOfCols = this.field.getNumOfCols();
    KB = new ArrayList<>();
    clickedTiles = new ArrayList<>();
    safeTiles = new ArrayList<>();
  }

  public static void main(String[] args) {
    printTruthTable(8);
    MSField f = new MSField("Minesweeper/fields/" + UsageExample.fields[9]);
    LogicalMSAgent testAgent = new LogicalMSAgent(f);
    testAgent.solve();

    int count = 1;
    for(int i = 0;i < testAgent.numOfRows;i++){
      for(int j = 0;j < testAgent.numOfCols;j++){
        System.out.print(String.format("%2d",count++) + "\t");
      }
      System.out.println();
    }

  }

  @Override
  public boolean solve() {
    int x, y, feedback;

    int steps = 0;
    do {
      steps++;
      if (displayActivated) {
        System.out.println(field);
      }
      if (firstDecision) {
        x = 0;
        y = 0;
        firstDecision = false;
        clickedTiles.add(findName(x,y));
      } else {
        int[] xy = chooseTile();
        x = xy[0];
        y = xy[1];
        clickedTiles.add(findName(x,y));
        if(safeTiles.indexOf(findName(x,y)) > 0){
          safeTiles.remove(safeTiles.indexOf(findName(x, y)));
        }
      }

      if (displayActivated) {
        System.out.println("Uncovering (" + x + "," + y + ")");
      }


      feedback = field.uncover(x, y);
      extendKB(feedback,x,y);
    } while (feedback >= 0 && !field.solved());

    if (field.solved()) {
     // if (displayActivated) {
        System.out.println("Solved the field");
     // }
      return true;
    } else {
      if (displayActivated) {
        System.out.println("BOOM!");
     }
      return false;
    }
  }

  /** Gibt x,y zur??ck f??r Position/Feld, das gedr??ckt werden soll
   */
  public int[] chooseTile(){
    int chosen;
    if(safeTiles.size()>0) {
      chosen = safeTiles.get(safeTiles.size()-1);
    } else {
      HashSet<Integer> explore = new HashSet<Integer>();
      /*for(int i : clickedTiles){
        int[] xy = findPos(i);
        explore.addAll(this.getNeighbours(xy[0],xy[1]));
      }*/
      for(int i = 1;i<=(numOfCols*numOfRows);i++) {
        explore.add(i);
      }
      explore.removeAll(safeTiles); // eigtl unn??tig, weil safeTiles muss kleiner 1 sein
      explore.removeAll(clickedTiles);
      findSafeTiles(explore); // f??gt neue safeTiles der Liste hinzu
      if(safeTiles.size() == 0){
      //  System.out.println("Random Pick");
        int t =  findName(rand.nextInt(numOfCols), rand.nextInt(numOfRows));
        while(clickedTiles.contains(t)){
          t =  findName(rand.nextInt(numOfCols), rand.nextInt(numOfRows));
        }
        chosen = t;
      }else {
        chosen = safeTiles.get(safeTiles.size() - 1);
      }
    }
    if(safeTiles.size() > 0) {
      safeTiles.remove(safeTiles.size() - 1);
    }
    clickedTiles.add(chosen);
    return findPos(chosen);
  }

  /** Benutzt SAT4J und KB um sichere Felder zu entdecken.
   * Alle Felder, die ??bergeben werden, werden auf Erf??llbarkeit gepr??ft.
   * Annahme: Feld i ist frei, dann gilt: KB U !i ist unerf??llbar.
   */
  public void findSafeTiles(HashSet<Integer> explore) {
    ISolver solver = SolverFactory.newDefault();
    solver.newVar(MAXVAR);
    solver.setExpectedNumberOfClauses(KB.size()+1);
    try {
      for(int j : explore) {
        for (int i = 0;i<KB.size();i++){
          int[] clause = KB.get(i); // get the clause from somewhere
          try {
            solver.addClause(new VecInt(clause));
          }catch(Exception x){
          }
          solver.addClause(new VecInt(new int[]{j})); // j dachte ich zumindest m??sste negativ sein
        }
        IProblem problem = solver;
        if (problem.isSatisfiable()) {
          // Variable konnte nicht bewiesen werden, dass sie keine Bombe sein muss.
          solver.reset();
        } else {
          safeTiles.add(j);
          solver.reset();
        }}

    } catch (Exception ex){
      System.out.println("SAT4J Problem occurred" + "\n" + ex.getMessage());
      ex.printStackTrace();
    }
  }

  /** Gibt f??r Feld an Position x,y den Namen zur??ck.
   * Oben links angefangen bei 1 und von links nach rechts hochz??hlend.
   */
  public int findName(int x,int y){
    return x + y * numOfCols + 1;
  }

  /** Gegenst??ck zu findName. Aus name werden x und y berechnet.
   */
  public int[] findPos(int name) {
    int y = (name-1) / numOfCols;
    int x = name % numOfCols - 1;
    if(x==-1) {x=numOfCols-1;}
    return new int[]{x,y};
  }

  /** Nimmt x,y und gibt alle Nachbarn um das Feld als AraryList mit int zur??ck.
   * Die Zahl steht dabei f??r den Namen des Feldes. Angefangen oben links bei 1 und von links nach rechts dann inkrementierend.
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
    removeClickedNeigbours(neighbours);
    return neighbours;
  }


  private ArrayList<Integer> removeClickedNeigbours(ArrayList<Integer> neighbours){
    neighbours.removeAll(this.clickedTiles);
    return neighbours;
  }

  /** Gibt 2D-Feld von int zur??ck, welches alle Interpretationen f??r n Variablen als Tabelle darstellt.
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
   Gibt ArrayList mit Klauseln f??r KNF zur??ck.
   */
  private ArrayList<int[]> extractModels(int[][] tt,int feedback){
    ArrayList<int[]> KNF = new ArrayList<>();
    for(int[] row : tt){
      int countNeg = 0;
      for(int i : row){
        if(i<0){countNeg++;}
      }
      if(countNeg!=feedback){KNF.add(row);}
    }
    return KNF;
  }

  /** Nimmt Feedback zu gedr??cktem Feld x,y. Wandelt dies in Klauseln um, die in in die KB gespeist werden.
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