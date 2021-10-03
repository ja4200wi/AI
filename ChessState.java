import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

public class ChessState implements Comparable {

  private Tile[][] board = new Tile[8][8];
  private int quality = 0;

  public ChessState(){
    for(int x = 0;x<board.length;x++){
      for(int y = 0;y<board[x].length;y++){
        board[x][y] = new EmptyTile();
      }
    }
    calculateQuality();
  }

  public ChessState(Tile[][] board){
    this.board = board;
    this.calculateQuality();
  }

  public void calculateQuality(){
    int result = 0;
    for(int x = 0;x<board.length;x++){
      for(int y = 0;y<board[x].length;y++){
        //Check if is horse
        if(board[x][y].isPiece()) {
          Horse horse = (Horse) board[x][y];
          //Add points for postion of horse
          result +=  x + y;
          //Check if black horse and increase quality by 3 if yes, by 2 if no
          if(horse.getColor()){
            result += 3;
          } else {
            result += 2;
          }
          //Add points for neighbors of different color
          result += getNeighborPoints(x,y);
        }
      }
    }
    result -= hits();
    this.quality = result;
  }


  public static LinkedList<ChessState> getNeighbors(ChessState current)
      throws CloneNotSupportedException {
    LinkedList<ChessState> neighbors = new LinkedList<>();
    Tile[][] currentBoard = current.getBoard();
    for(int x = 0;x<currentBoard.length;x++){
      for(int y = 0;y< currentBoard[x].length;y++){
        Horse blackHorse = new Horse(true,x,y);
        Horse whiteHorse = new Horse(false,x,y);
        EmptyTile empty = new EmptyTile();
        //If current checked tile is a horse add neighborstate with empty field and different color
        if(currentBoard[x][y].isPiece()){
          Horse horseToCheck = (Horse) currentBoard[x][y];
          Tile[][] newNeighbor = cloneBoard(currentBoard);
          newNeighbor[x][y] = empty;
          neighbors.add(new ChessState(newNeighbor));
          if(horseToCheck.getColor()){
            Tile[][] newNeighbor2 = cloneBoard(currentBoard);
            newNeighbor2[x][y] = new Horse(false,x,y);
            neighbors.add(new ChessState(newNeighbor2));
          } else {
            Tile[][] newNeighbor2 = cloneBoard(currentBoard);
            newNeighbor2[x][y] = new Horse(true,x,y);
            neighbors.add(new ChessState(newNeighbor2));
          }
        } else {
          Tile[][] newNeighbor = cloneBoard(currentBoard);
          Tile[][] newNeighbor2 = cloneBoard(currentBoard);
          newNeighbor[x][y] = new Horse(true,x,y);
          newNeighbor2[x][y] = new Horse(false,x,y);
          neighbors.add(new ChessState(newNeighbor));
          neighbors.add(new ChessState(newNeighbor2));
        }
      }
    }
    return neighbors;
  }

  public static Tile[][] cloneBoard(Tile[][] board) throws CloneNotSupportedException {
    Tile[][] copy = new Tile[board.length][board[0].length];
    for(int x = 0;x<board.length;x++){
      for(int y = 0;y<board[x].length;y++){
        if(board[x][y].isPiece()) {
          Horse horse = (Horse)board[x][y];
          copy[x][y] = (Horse) horse.clone();
        } else {
          copy[x][y] = new EmptyTile();
        }
      }
    }
    return copy;
  }

  public int getNeighborPoints(int x,int y){
    int result = 0;
    Horse horse = (Horse) board[x][y];
    boolean color = horse.getColor();
    //Check above
    if(y>0){
      if(checkNeighbor(x,y-1,color)){
        result++;
      }
    }
    //Check right
    if(x<7){
      if(checkNeighbor(x+1,y,color)){
        result++;
      }
    }
    //Check below
    if(y<7){
      if(checkNeighbor(x,y+1,color)){
        result++;
      }
    }
    //Check left
    if(x>0){
      if(checkNeighbor(x-1,y,color)){
        result++;
      }
    }
    return result;
  }

  public boolean checkNeighbor(int x,int y,boolean color){
    boolean result = false;
    if(board[x][y].isPiece()){
      Horse horse = (Horse) board[x][y];
      boolean neighborColor = horse.getColor();
      if(neighborColor != color){
        result = true;
      }
    }
    return result;
  }

   int hits(){
    int hits = 0;
    ArrayList<Horse> horses = new ArrayList<>();
    for(int x = 0; x < 8; x++){
      for(int y = 0; y < 8; y++){
        if(board[x][y].isPiece()){
          horses.add((Horse)board[x][y]);
        }
      }
    }
    Iterator<Horse> iterator = horses.iterator();
    while(iterator.hasNext()){
      Horse horse = iterator.next();
      hits += horseHits(horse.getxPos(),horse.getyPos());
    }
    return (hits /2)*3;
  }

   int horseHits(int x, int y){
    int count = 0;
    count += horseHitsInDirection(x,y,1,0);
    count += horseHitsInDirection(x,y,0,1);
    count += horseHitsInDirection(x,y,-1,0);
    count += horseHitsInDirection(x,y,0,-1);
    return count;
  }

   int horseHitsInDirection(int x, int y, int xDir, int yDir){
    int solv = 0;
    int x1 = x + 2 * xDir + 1* yDir;
    int y1 = y + 2 * yDir + 1* xDir;

    int x2 = x + 2 * xDir + -1* yDir;
     int y2 = y + 2 * yDir + -1* xDir;

     if (x1 < 8 && x1 >= 0 && y1 < 8 && y1 >= 0) {
       if(board[x1][y1].isPiece()){
         solv++;
       }
     }

     if (x2 < 8 && x2 >= 0 && y2 < 8 && y2 >= 0) {
       if(board[x2][y2].isPiece()){
         solv++;
       }
     }
    return solv;
  }

  public int getQuality() {
    return quality;
  }

  @Override
  public int compareTo(Object o) {
    ChessState object = (ChessState) o;
    if(this.quality<object.getQuality()){
      return 1;
    } else if (this.quality>object.getQuality()){
      return -1;
    } else {
      return 0;
    }
  }

  @Override
  public String toString() {
    String s = "";
    for(int x = 0;x<board.length;x++){
      for(int y = 0;y<board[x].length;y++){
        s+= board[x][y].toString() + " ";
      }
      s+="\n";
    }
    return s;
  }

  public Tile[][] getBoard() {
    return board;
  }
}
