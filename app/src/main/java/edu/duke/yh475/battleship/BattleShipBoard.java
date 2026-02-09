package edu.duke.yh475.battleship;
import java.util.ArrayList;

/**
 * BattleShip represents the grid where ships are placed and tracked
 * It manages the ships and placement rules 
 * @param<T> the type of data stored on the board
 */
public class BattleShipBoard<T> implements Board<T> {
  private final int width;
  private final int height;
  final ArrayList<Ship<T>> myShips;
  private final PlacementRuleChecker<T> placementRuleChecker;
  
  /**
   * Constructs a BattleshipBoard with the specificed width and heights
   * @param w is the width of the newly constructed board
   * @param h is the heigh of the newly constructed board
   * @throws IllegalArgumentException if the width or height are less than or equal to zero
   */
  public BattleShipBoard(int w, int h, PlacementRuleChecker<T> placementRuleChecker) {
    if (w <= 0) {
      throw new IllegalArgumentException("BattleShipBoard's width must be positive but is " + w);
    }
    if (h <= 0) {
      throw new IllegalArgumentException("BattleShipBoard's height must be positive but is " + h);
    }
    this.width = w;
    this.height = h;
    this.myShips = new ArrayList<Ship<T>>();
    this.placementRuleChecker = placementRuleChecker;
  }

  public BattleShipBoard(int w, int h){
    this(w, h, new InBoundsRuleChecker<T>(new InBoundsRuleChecker<>(null)));
  }
  
  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public int getWidth() {
    return width;
  }

  /**
   * Add a ship to the board
   * @param toAdd is teh ship to be added
   * @return true
   */
  @Override
  public String tryAddShip(Ship<T> toAdd) {
    if(! placementRuleChecker.checkPlacement(toAdd, this)){
      return "This placement is invalid";
    }
    myShips.add(toAdd);
    return null;
  }

  /**
   * Check the board at a specific coordinate to see what is present
   * @param where is the coordinate to check
   * @return the display information of type T
   */
  @Override
  public T whatIsAt(Coordinate where) {
    for (Ship<T> s: myShips) {
      if (s.occupiesCoordinates(where)){
        return s.getDisplayInfoAt(where);
      }
    }
    return null;
  }


}
