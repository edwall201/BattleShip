package edu.duke.yh475.battleship;

import java.util.LinkedHashMap;

/**
 * This abstract class provies a base implement for the ship interface
 * It manages the ships coordinate and hit status by hashmap
 * @param <T> the type of display information
 */
public abstract class BasicShip<T> implements Ship<T> {
  private LinkedHashMap<Coordinate, Boolean> myPieces;
  protected ShipDisplayInfo<T> myDisplayInfo;
  protected ShipDisplayInfo<T> enemyDisplayInfo;

  /**
   * Constructs a basicShip with the specificed coordinates and displayinformation
   * Initial all coordinate to not hit
   * @param where an interable of coordinate
   * @param myDisplayInfo the display information handler for this ship
   * @param enemyDisplayInfo the display information handler for the enemy ship
   */
  public BasicShip(Iterable<Coordinate> where, ShipDisplayInfo<T> myDisplayInfo, ShipDisplayInfo<T> enemyDisplayInfo){
    this.myPieces = new LinkedHashMap<Coordinate, Boolean>();
    for(Coordinate c: where){
      this.myPieces.put(c, false);
    }
    this.myDisplayInfo = myDisplayInfo;
    this.enemyDisplayInfo = enemyDisplayInfo;
    
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
  public T getDisplayInfoAt(Coordinate where, boolean myShip) {
    if(myShip){
      return myDisplayInfo.getInfo(where, wasHitAt(where));
    }
    return enemyDisplayInfo.getInfo(where, wasHitAt(where));
  }

  @Override
  public Iterable<Coordinate> getCoordinates(){
    return myPieces.keySet();
  }

  @Override
  public void recordHitAt(int index) {
    Coordinate c = getCoordinateByIndex(index);
    myPieces.put(c, true); 
  }

  @Override
  public int getNumPieces() {
      return myPieces.size(); 
  }

  @Override
  public boolean wasHitAt(int index) {
    Coordinate c = getCoordinateByIndex(index);
    return myPieces.get(c);
  }

  /**
   * Get the coordinate of this ship by the index
   * If the index is out of bound, throw IllegalArgumentException
   * @param index the index of the coordinate to get
   * @return the coordinate of this ship at the index
   * 
   */
  protected Coordinate getCoordinateByIndex(int index) {
    int i = 0;
    for (Coordinate c : myPieces.keySet()) {
      if (i == index) return c;
      i++;
    }
    throw new IllegalArgumentException("Index " + index + " is out of bounds.");
  }
}
