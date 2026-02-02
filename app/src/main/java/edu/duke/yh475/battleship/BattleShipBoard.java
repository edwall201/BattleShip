package edu.duke.yh475.battleship;

public class BattleShipBoard {
  private final int width;
  private final int height;

  public BattleShipBoard(int w, int h){
    this.width = w;
    this.height  =h;
  }
  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }

}
