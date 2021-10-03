import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class ChessState {

  private Tile[][] board = new Tile[8][8];
  private int quality = 0;

  public ChessState(){
    for(int x = 0;x<board.length;x++){
      for(int y = 0;y<board[x].length;y++){
        board[x][y] = new EmptyTile();
      }
    }
    board[1][1] = new Horse(true, 1,1);
    board[5][5] = new Horse(true, 5,5);
    board[6][4] = new Horse(true, 6,4);
    board[4][6] = new Horse(true, 7,3);
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
    System.out.println("Hit Cost: "+ (hits));
    return (hits /2)*3;
  }

   int horseHits(int x, int y){
    int count = 0;
    count += horseHitsInDirection(x,y,1,1);
    count += horseHitsInDirection(x,y,-1,-1);
    count += horseHitsInDirection(x,y,-1,1);
    count += horseHitsInDirection(x,y,1,-1);
    System.out.println(count);
    return count;
  }

   int horseHitsInDirection(int x, int y, int xDir, int yDir){
    int x1 = x +xDir;
    int y1 = y+ yDir;
    while(x1 < 8 && x1 >= 0 && y1 < 8 && y1 >= 0){
      if(board[x1][y1].isPiece()){
        return 1;
      }
      x1 += xDir;
      y1 += yDir;
    }
    return 0;
  }

  public int getQuality() {
    return quality;
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

  public class EmptyTile extends Tile{
    private boolean isPiece = false;

    public EmptyTile(){

    }
    public boolean isPiece() {
      return isPiece;
    }

    @Override
    public String toString() {
      return "-";
    }
  }

}
