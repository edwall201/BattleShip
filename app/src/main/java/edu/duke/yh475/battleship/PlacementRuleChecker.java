package edu.duke.yh475.battleship;

/**
 * Abstract class representing a rule for ship placement by chain of responsibility pattern
 */
public abstract class PlacementRuleChecker<T>{
  private final PlacementRuleChecker<T> next;

  /**
   * Constructor that links this rule to next
   * @param next is the next rule in the chain
   */
  public PlacementRuleChecker(PlacementRuleChecker<T> next){
    this.next = next;
  }

  /**
   * Subclass must implement this to define their specific rule.
   */
  protected abstract String checkMyRule(Ship<T> theShip, Board<T> theBoard);

  /**
   * Checks the placement against this rule and all sub rules in the chain
   * @pararm theShip is the ship being placed
   * @param theBoard is the board on which the ship is placed
   * @return null if the placement satisfies all the rule in the chain or return an error message
   */
  public String checkPlacement(Ship<T> theShip, Board<T> theBoard){
    String msg = checkMyRule(theShip, theBoard);
    if(msg != null){
        return msg;
      }
      if(next != null){
        return next.checkPlacement(theShip, theBoard);
      }
      return null;
  }
}
