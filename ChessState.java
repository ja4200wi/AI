import java.util.Arrays;

public class ChessState {

  private Tile[][] board = new Tile[8][8];
  private int quality =0;

  public ChessState(){
    for(int x = 0;x<board.length;x++){
      for(int y = 0;y<board[x].length;y++){
        board[x][y] = new EmptyTile();
      }
    }
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
