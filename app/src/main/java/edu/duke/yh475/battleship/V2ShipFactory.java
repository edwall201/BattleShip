package edu.duke.yh475.battleship;

import java.util.LinkedHashSet;
import java.util.Set;

public class V2ShipFactory implements AbstractShipFactory<Character> {
  /**
   * To create a Rectangleship base on the provide placement
   *@param where the placement for the ship
   *@param w the width of the ship
   *@param h the height of the ship
   *@param name the descriptive name of the ship
   *@param letter the character used for display
   *@return A new RectangleShip
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

  protected Ship<Character> createCustomShip(String name, char letter, Set<Coordinate> coords){
    return new CustomShip<Character>(name, coords, new SimpleShipDisplayInfo<>(letter, '*'), new SimpleShipDisplayInfo<>(null, letter));
  }

  @Override
  public Ship<Character> makeBattleship(Placement where) {
    char direction = where.getOrientation();
    int row = where.getWhere().getRow();
    int col = where.getWhere().getColumn();
    Set<Coordinate> coords = new LinkedHashSet<>();
    int[][] off;
    if (direction == 'U') {
      off = new int[][]{{0, 1}, {1, 0}, {1, 1}, {1, 2}};
    } else if (direction == 'R') {
      off = new int[][]{{0, 0}, {1, 0}, {1, 1}, {2, 0}};
    } else if (direction == 'D') {
      off = new int[][]{{0, 0}, {0, 1}, {0, 2}, {1, 1}};
    } else if (direction     == 'L') {
      off = new int[][]{{0, 1}, {1, 0}, {1, 1}, {2, 1}};
    } else {
      throw new IllegalArgumentException("Battleship orientation must be U, R, D, or L");
    }
    
    for (int[] o : off) {
        coords.add(new Coordinate(row + o[0], col + o[1]));
    }
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
    char direction = where.getOrientation();
    int row = where.getWhere().getRow();
    int col = where.getWhere().getColumn();
    Set<Coordinate> coords = new LinkedHashSet<>();

    int[][] off;
    if (direction == 'U') {
      off = new int[][]{{0, 0}, {1, 0}, {2, 0}, {2, 1}, {3, 0}, {3, 1}, {4, 1}};
    } else if (direction == 'R') {
      off = new int[][]{{0, 2}, {0, 3}, {0, 4}, {1, 0}, {1, 1}, {1, 2}, {1, 3}};
    } else if (direction == 'D') {
      off = new int[][]{{0, 0}, {1, 0}, {1, 1}, {2, 1}, {2, 2}, {3, 2}, {4, 2}};
    } else if (direction == 'L') {
      off = new int[][]{{0, 1}, {0, 2}, {0, 3}, {0, 4}, {1, 0}, {1, 1}, {1, 2}};
    } else {
      throw new IllegalArgumentException("Carrier orientation must be U, R, D, or L");
    }

    for (int[] o : off) {
        coords.add(new Coordinate(row + o[0], col + o[1]));
    }
    return createCustomShip("Carrier", 'c', coords);
  }

}
