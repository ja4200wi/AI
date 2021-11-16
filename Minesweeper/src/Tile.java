package Minesweeper.src;

public class Tile {
  private int x;
  private int y;
  private boolean isBomb;
  private byte countNeighbors;

  public Tile(int x,int y,boolean isBomb,byte count){
    this.x = x;
    this.y = y;
    this.isBomb = isBomb;
    this.countNeighbors = count;
  }

  public boolean isBomb(){
    return isBomb;
  }

  public void setCountBombsNeighbor(byte countBombsNeighbor) {
    this.countNeighbors = countBombsNeighbor;
  }
}
