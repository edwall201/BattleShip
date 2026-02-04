package edu.duke.yh475.battleship;

public class SimpleShipDisplayInfo<T> implements ShipDisplayInfo<T> {
  private T myData;
  private T onHit;

  public SimpleShipDisplayInfo(T myData, T onHit){
    this.myData = myData;
    this.onHit = onHit;
  }

  @Override
  public T getInfo(Coordinate where, boolean hit) {
    return hit? onHit: myData;
  }

}
