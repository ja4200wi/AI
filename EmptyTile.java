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