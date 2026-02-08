package edu.duke.yh475.battleship;

/**
 * This interface represents an Abstract Factory patten for ship creation.
 */
public interface AbstractShipFactory<T> {
  /**
   * Make a submarine
   * 
   * @param where specifies the location and orientation of the ship to make
   * @return the ship ccreated for the submarine.
   */
  public Ship<T> makeSubmarine(Placement where);

  /**
   * Make a battleship
   * 
   * @param where specifies the location and orientation of the ship to make
   * @return the ship created for the battleship
   */
  public Ship<T> makeBattleship(Placement where);

  /**
   * Make a carrier
   * 
   * @param where specifies the location and orientation of the ship to make
   * @return the Ship created for the carrier
   */
  public Ship<T> makeCarrier(Placement where);

  /**
   * Make a destroy
   * 
   * @param where specifies the location and orientation of the ship to make
   * @return the ship created for the destroy
   */
  public Ship<T> makeDestroyer(Placement where);
}
