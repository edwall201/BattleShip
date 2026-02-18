package edu.duke.yh475.battleship;

import java.util.HashMap;
import java.util.Map;

public class SonarScanner<T> {
  private final Board<T> enemyBoard;

  /**
   * Constructs a SonarScanner with the given enemy board
   * 
   * @param enemyBoard the board to scan for ships
   */
  public SonarScanner(Board<T> enemyBoard) {
    this.enemyBoard = enemyBoard;
  }

  /**
   * Scans a diamond shaped area centered at the given coordinate and counts the
   * number of ship
   * Using Manhattan distance to determine the scan area
   * 
   * @param center the coordinate to center the scan on
   * @return a map that records how many ships are been scanned
   */
  public Map<String, Integer> scan(Coordinate center) {
    Map<String, Integer> counts = new HashMap<>();

    counts.put("Submarine", 0);
    counts.put("Destroyer", 0);
    counts.put("Battleship", 0);
    counts.put("Carrier", 0);

    int centerRow = center.getRow();
    int centerCol = center.getColumn();

    for (int r = -3; r <= 3; r++) {
      for (int c = -3; c <= 3; c++) {
        if (Math.abs(r) + Math.abs(c) <= 3) {
          int currR = centerRow + r;
          int currC = centerCol + c;

          if (isCoordinateInBounds(currR, currC)) {
            Coordinate coord = new Coordinate(currR, currC);
            Ship<T> ship = enemyBoard.getShipAt(coord);

            if (ship != null) {
              String name = ship.getName();
              counts.put(name, counts.getOrDefault(name, 0) + 1);
            }
          }
        }
      }
    }
    return counts;
  }

  /**
   * Checks if the given coordinate is in the bound of the board
   * 
   * @param r the row of the coordinate
   * @param c the column of the coordinate
   * @return true if the coordinate is in bounds, otherwise false
   */
  private boolean isCoordinateInBounds(int r, int c) {
    if (r < 0 || r >= enemyBoard.getHeight() || c < 0 || c >= enemyBoard.getWidth()) {
      return false;
    }
    return true;
  }
}
