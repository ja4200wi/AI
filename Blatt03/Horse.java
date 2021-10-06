public class Horse extends Tile {

  private boolean color;// true=black; false=white
  private String colorString;// to print color --> for info
  private int xPos; //Spaltenindex
  private int yPos; //Zeilenindex
  private boolean isPiece = true;

  //Constructor
  public Horse(boolean color){
    this.color = color;
    this.setColorString(color);
  }

  //Constructor
  public Horse(boolean color,int xPos,int yPos){
    this.color = color;
    this.setColorString(color);
    this.xPos = xPos;
    this.yPos = yPos;
  }

  //Sets colorString according to boolean value --> [1/true]=black [0/false]=white
  private void setColorString(boolean color) {
    if(color) {
      this.colorString = "black";
    } else {
      this.colorString = "white";
    }
  }
  public boolean getColor(){
    return this.color;
  }

  public boolean isPiece() {
    return isPiece;
  }

  public int getxPos() {
    return xPos;
  }

  public int getyPos() {
    return yPos;
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    Horse copy = new Horse(this.color,this.xPos,this.yPos);
    return copy;
  }

  @Override
  public String toString() {
    if (this.color) {
      return "B";
    } else {
      return "W";
    }
  }
}
