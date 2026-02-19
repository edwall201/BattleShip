package edu.duke.yh475.battleship;

/** 
 * CustomShip is a extension of BasicShip that allows for custom not rectangular ships
 * By passing a coordinate set to define the occupied coordinates.
 */
public class CustomShip<T> extends BasicShip<T> {
  private final String name;

  /**
   * Constructs a custom ship
   * @param name the name of the ship
   * @param coordinates the set of coordinates occupied by the ship
   * @param myDisplayInfo the display info for this ship
   * @param enemyDisplayInfo the display info for this ship when viewed by the enemy
   */
  public CustomShip(String name, Iterable<Coordinate> coordinates, ShipDisplayInfo<T> myDisplayInfo, ShipDisplayInfo<T> enemyDisplayInfo) {
    super( coordinates, myDisplayInfo, enemyDisplayInfo);
    this.name = name;
  }

  /**
   * Desrcibe in Ship interface
   */
  @Override
  public String getName() {
    return name;
  }

}
