package edu.duke.yh475.battleship;

import java.util.HashMap;

public abstract class BasicShip<T> implements Ship<T> {
  private HashMap<Coordinate, Boolean> myPieces;
  protected ShipDisplayInfo<T> myDisplayInfo;

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
