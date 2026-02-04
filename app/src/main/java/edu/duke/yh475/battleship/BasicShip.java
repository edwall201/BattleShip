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

  /**
   * Check if the coordinate in this ship or not
   * @param c The coordinate
   * @thorws IllegalArgumentException if the coordinate is not part of this ship
   */
  protected void checkCoordinateInThisShip(Coordinate c){
    if(!myPieces.containsKey(c)){
        throw new IllegalArgumentException("Coordinate" + c + "not in this ship");
    }
 }

  @Override
  public boolean occupiesCoordinates(Coordinate c) {
    return myPieces.containsKey(c);
  }

  @Override
  public boolean isSunk() {
    for(Boolean b : myPieces.values()){
      if(! b) return false;
    }
    return true;
   }

  @Override
  public void recordHitAt(Coordinate where) {
    checkCoordinateInThisShip(where);
    myPieces.put(where, true);
   }

  @Override
  public boolean wasHitAt(Coordinate where) {
    checkCoordinateInThisShip(where);
    return myPieces.get(where);
  }

  @Override
  public T  getDisplayInfoAt(Coordinate where) {
    return myDisplayInfo.getInfo(where, wasHitAt(where));
  }
  
}
