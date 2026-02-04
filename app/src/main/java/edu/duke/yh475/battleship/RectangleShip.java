package edu.duke.yh475.battleship;

import java.util.HashSet;

public class RectangleShip<T> extends BasicShip<T> {

  public RectangleShip(Coordinate upperLeft, int width, int height, ShipDisplayInfo<T> myDisplayInfo) {
    super(makeCoords(upperLeft, width, height), myDisplayInfo);
  }
  
  public RectangleShip(Coordinate upperLeft, int width, int height, T data, T onHit) {
    this(upperLeft, width, height, new SimpleShipDisplayInfo<T>(data, onHit));
  }
  
  public RectangleShip(Coordinate upperLeft, T data, T onHit) {
    this(upperLeft, 1, 1, data, onHit);
  }
      
  static HashSet<Coordinate> makeCoords(Coordinate upperleft, int width, int height) {
    HashSet<Coordinate> coords = new HashSet<>();
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        coords.add(new Coordinate(upperleft.getRow() + row, upperleft.getColumn() + col));
      }
    }
    
    return coords;
  }

}
