package edu.duke.yh475.battleship;
import java.util.ArrayList;
import java.util.HashSet;

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
  HashSet<Coordinate> enemyMisses;
  final T missInfo;
  
  /**
   * Constructs a BattleshipBoard with the specificed width and heights
   * @param w is the width of the newly constructed board
   * @param h is the heigh of the newly constructed board
   * @throws IllegalArgumentException if the width or height are less than or equal to zero
   */
  public BattleShipBoard(int w, int h, PlacementRuleChecker<T> placementRuleChecker, T missInfo) {
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
    this.enemyMisses = new HashSet<>();
    this.missInfo = missInfo;
  }

  public BattleShipBoard(int w, int h){
    this(w, h, new InBoundsRuleChecker<T>(new NoCollisionRuleChecker<>(null)), (T)(Character)'X');
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
    String msg = placementRuleChecker.checkPlacement(toAdd, this);
    if(msg != null){
      return msg;
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
  public T whatIsAtForSelf(Coordinate where) {
    return whatIsAt(where, true);
  }

  protected T whatIsAt(Coordinate where, boolean isSelf){
    for (Ship<T> s: myShips) {
      if (s.occupiesCoordinates(where)){
        return s.getDisplayInfoAt(where, isSelf);
      }
    }
    if (!isSelf && enemyMisses.contains(where)) {
        return missInfo;
    }
    return null;
  }

  /**
   * Check the board at a specific coordinate to see what is present for enemy view
   * @param where is the coordinate to check
   * @return the display information of type T for enemy view
   */
  @Override
  public T whatIsAtForEnemy(Coordinate where){
    return whatIsAt(where, false);
  }

  /**
   * Fire at a specific coordinate and return the ship that is hit
   * @param c is the coordinate to fire at
   * @return the ship that is hit, or null if no ship is hit
   */
  @Override
  public Ship<T> fireAt(Coordinate where){
    for(Ship<T> s: myShips){
      if(s.occupiesCoordinates(where)){
        s.recordHitAt(where);
        return s;
      }
    }
    enemyMisses.add(where);
    return null;
  }


}
