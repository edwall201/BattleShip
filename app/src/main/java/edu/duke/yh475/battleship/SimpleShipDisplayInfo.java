package edu.duke.yh475.battleship;

/**
 * To implement the shipdisplayInfo interface
 * @param <T> the type of display information
 */
public class SimpleShipDisplayInfo<T> implements ShipDisplayInfo<T> {
  private T myData;
  private T onHit;

  
  /**
   * Constructs the SimpleShipDisplayInfo
   * @param myData ship is not hit
   * @param onHit the ship is hit
   */
  public SimpleShipDisplayInfo(T myData, T onHit){
    this.myData = myData;
    this.onHit = onHit;
  }

  /**
   * Return the display data
   * @pararm where the coordinate
   * @param hit True if the coordinate has been hit
   */
  @Override
  public T getInfo(Coordinate where, boolean hit) {
    return hit? onHit: myData;
  }

}
