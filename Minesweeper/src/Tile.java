package Minesweeper.src;

public class Tile {
  private int name;
  private boolean isBomb;
  private byte countNeighbors;

  public Tile(int name,boolean isBomb,byte count){
    this.name = name;
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
