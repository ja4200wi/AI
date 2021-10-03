public class EmptyTile extends Tile{
  private boolean isPiece = false;

  public EmptyTile(){

  }
  public boolean isPiece() {
    return isPiece;
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    return new EmptyTile();
  }

  @Override
  public String toString() {
    return "-";
  }
}