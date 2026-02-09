package edu.duke.yh475.battleship;

import java.util.HashSet;

public class RectangleShip<T> extends BasicShip<T> {

  private final String name;
  /**
   * Constructs a RectangleShip
   * @param upperLeft The coordinate of the top-left corner of the ship
   * @param width The number of columns the ship occupies
   * @param height The number of rows the ship occupies
   * @param myDisplayInfo The logic used to determine how the ship is displayed
   */
  public RectangleShip(String name,Coordinate upperLeft, int width, int height, ShipDisplayInfo<T> myDisplayInfo) {
    super(makeCoords(upperLeft, width, height), myDisplayInfo);
    this.name = name;
  }

  /**
   * Constructs a RectangleShip data for no hit, onHit for hit
   * @param upperLeft The coordinate of the top-left corner of the ship
   * @param width The number of columns the ship occupies
   * @param height The number of rows the ship occupies
   * @param data The display info when a part of the ship is not hit
   * @param onHit The display info when a part of the ship is hit
   */
  public RectangleShip(String name, Coordinate upperLeft, int width, int height, T data, T onHit) {
    this(name, upperLeft, width, height, new SimpleShipDisplayInfo<T>(data, onHit));
  }

  /**
   * @param upperLeft The coordinate of the ship
   * @param data The display info when the ship is not hit
   * @param onHit The display info when the ship is hit
   */
  public RectangleShip(Coordinate upperLeft, T data, T onHit) {
    this("testship", upperLeft, 1, 1, data, onHit);
  }
  
  /**
   * Generates a set of coordinates representing a rectangle
   * @param upperleft The top-left starting point
   * @param width The number of columns
   * @param height The number of rows
   */
  static HashSet<Coordinate> makeCoords(Coordinate upperleft, int width, int height) {
    HashSet<Coordinate> coords = new HashSet<>();
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        coords.add(new Coordinate(upperleft.getRow() + row, upperleft.getColumn() + col));
      }
    }
    return coords;
  }

  @Override
  public String getName(){
    return name;
  }
}
