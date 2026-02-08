package edu.duke.yh475.battleship;


/**
 * A rule checker that ensures the coordinate didn't be occupied
 */
public class NoCollisionRuleChecker<T> extends PlacementRuleChecker<T> {

  /**
   * Constructs a NoCollisionRuleChecker with a reference to next rules
   *@param is the next rule in teh chain
  */
  public NoCollisionRuleChecker(PlacementRuleChecker<T> next) {
    super(next);
  }

  /**
   * Checks if the given ship's coordinate has collosision with other ships on the board or not
   */

  @Override
  protected boolean checkMyRule(Ship<T> theShip, Board<T> theBoard) {
    for (Coordinate c : theShip.getCoordinates()) {
      if (theBoard.whatIsAt(c) != null) {
        return false;
      }
    }
    return true;
  }

}
