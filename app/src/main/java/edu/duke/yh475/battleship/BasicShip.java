package edu.duke.yh475.battleship;

import java.util.HashMap;

/**
 * This abstract class provies a base implement for the ship interface
 * It manages the ships coordinate and hit status by hashmap
 * @param <T> the type of display information
 */

public abstract class BasicShip<T> implements Ship<T> {
  private HashMap<Coordinate, Boolean> myPieces;
  protected ShipDisplayInfo<T> myDisplayInfo;

  /**
   * Constructs a basicShip with the specificed coordinates and displayinformation
   * Initial all coordinate to not hit
   * @param where an interable of coordinate
   * @param myDisplayInfo the display information handler for this ship
   */
  public BasicShip(Iterable<Coordinate> where, ShipDisplayInfo<T> myDisplayInfo){
    this.myPieces = new HashMap<Coordinate, Boolean>();
    for(Coordinate c: where){
      this.myPieces.put(c, false);
    }
    this.myDisplayInfo = myDisplayInfo;
    
   }

  @Override
  public boolean occupiesCoordinates(Coordinate c) {
    return myPieces.containsKey(c);
  }

  @Override
  public boolean isSunk() {
    return false;
   }

  @Override
  public void recordHitAt(Coordinate where) {
   }

  @Override
  public boolean wasHitAt(Coordinate where) {
    return false;
  }

  @Override
  public T  getDisplayInfoAt(Coordinate where) {
    return myDisplayInfo.getInfo(where, false);
  }
  
}
