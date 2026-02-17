package edu.duke.yh475.battleship;

import java.util.LinkedHashSet;
import java.util.Set;

public class V2ShipFactory implements AbstractShipFactory<Character> {
  /**
   * To create a Rectangleship base on the provide placement
   * 
   * @param where  the placement for the ship
   * @param w      the width of the ship
   * @param h      the height of the ship
   * @param name   the descriptive name of the ship
   * @param letter the character used for display
   * @return A new RectangleShip
   */
  protected Ship<Character> createShip(Placement where, int w, int h, char letter, String name) {
    char orientation = where.getOrientation();
    if (orientation != 'H' && orientation != 'V') {
      throw new IllegalArgumentException("Standard ships must be H or V, but got " + orientation);
    }
    int new_w = 0;
    int new_h = 0;
    if (orientation == 'V') {
      new_w = w;
      new_h = h;
    } else {
      new_w = h;
      new_h = w;
    }
    return new RectangleShip<Character>(name, where.getWhere(), new_w, new_h, letter, '*');
  }

  protected Ship<Character> createCustomShip(String name, char letter, Set<Coordinate> coords) {
    return new CustomShip<Character>(name, coords, new SimpleShipDisplayInfo<>(letter, '*'),
        new SimpleShipDisplayInfo<>(null, letter));
  }

  @Override
  public Ship<Character> makeBattleship(Placement where) {
    int[][] upOffsets = { { 0, 1 }, { 1, 0 }, { 1, 1 }, { 1, 2 } };
    Set<Coordinate> coords = getRotatedCoords(where, upOffsets, 1, 2);
    return createCustomShip("Battleship", 'b', coords);
  }

  @Override
  public Ship<Character> makeDestroyer(Placement where) {
    return createShip(where, 1, 3, 'd', "Destroyer");
  }

  @Override
  public Ship<Character> makeSubmarine(Placement where) {
    return createShip(where, 1, 2, 's', "Submarine");
  }

  @Override
  public Ship<Character> makeCarrier(Placement where) {
    int[][] upOffsets = { { 0, 0 }, { 1, 0 }, { 2, 0 }, { 3, 0 }, { 2, 1 }, { 3, 1 }, { 4, 1 } };
    Set<Coordinate> coords = getRotatedCoords(where, upOffsets, 4, 1);
    return createCustomShip("Carrier", 'c', coords);
  }

  /**
   * To orient the offset based on the orientation of the placement
   * @param where  the placement for the ship
   * @param upOffsets the offsets for the ship when it is facing up
   * @param maxR the maximum row index of the offsets
   * @param maxC the maximum column index of the offsets
   * @return the set of coordinates for the ship based on the orientation of the placement
   * @throws IllegalArgumentException if the orientation is invalid
   */
  private Set<Coordinate> getRotatedCoords(Placement where, int[][] upOffsets, int maxR, int maxC) {
    char dir = where.getOrientation();
    int baseRow = where.getWhere().getRow();
    int baseCol = where.getWhere().getColumn();
    Set<Coordinate> coords = new LinkedHashSet<>();

    for (int[] off : upOffsets) {
      int r = off[0];
      int c = off[1];
      int newR, newC;

      if (dir == 'U') {
        newR = r;
        newC = c;
      } else if (dir == 'R') {
        newR = c;
        newC = maxR - r;
      } else if (dir == 'D') {
        newR = maxR - r;
        newC = maxC - c;
      } else if (dir == 'L') {
        newR = maxC - c;
        newC = r;
      } else {
        throw new IllegalArgumentException("Invalid orientation: " + dir);
      }
      coords.add(new Coordinate(baseRow + newR, baseCol + newC));
    }
    return coords;
  }

}
