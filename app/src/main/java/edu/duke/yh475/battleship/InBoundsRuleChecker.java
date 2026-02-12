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
   * @param theShip is the ship being checked
   * @param theBoard is the board on which the ship is being placed
   * @return null if the ship is within the board or an error message if any part of the ship is out of bounds
   */
  @Override
  protected String checkMyRule(Ship<T> theShip, Board<T> theBoard){
    for(Coordinate c: theShip.getCoordinates()){
      if(c.getRow() < 0){
        return "That placement is invalid: the ship goes off the top of the board.";
      }
      if(c.getRow() >= theBoard.getHeight()){
        return "That placement is invalid: the ship goes off the bottom of the board."; 
      }
      if(c.getColumn() < 0){
        return "That placement is invalid: the ship goes off the left of the board.";
      }
      if(c.getColumn() >= theBoard.getWidth()){
        return "That placement is invalid: the ship goes off the right of the board.";
      }
    }
    return null;
  }
}
