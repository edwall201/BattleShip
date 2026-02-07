package edu.duke.yh475.battleship;


/**
 * A rule checker that ensures all parts of a ship are within the board boundaries
 */
public class InBoundsRuleChecker<T> extends PlacementRuleChecker<T>{

  /**
   * Construncts an InBoundsRuleChecker with a reference to next rules
   * @param next is the next rule in the chain;
   */
  public InBoundsRuleChecker(PlacementRuleChecker<T> next){
    super(next);
  }

  /**
   * Checks if the given ship is entirely within the boards width and heigh
   */
  @Override
  protected boolean checkMyRule(Ship<T> theShip, Board<T> theBoard){
    for(Coordinate c: theShip.getCoordinates()){
      if(c.getRow() < 0 || c.getRow() >= theBoard.getHeight()){
        return false;
      }
      if(c.getColumn() < 0 || c.getColumn() >= theBoard.getWidth()){
        return false;
      }
    }
    return true;
  }
}
