package edu.duke.yh475.battleship;

/**
 * Represent a game board that can put ships and do actions
 */
public interface Board<T>{
  /**
   * @return the board's width
   */
  public int getWidth();

  /**
   * @return the board's height
   */
  public int getHeight();

  /**
   * Attemp to add the given ship
   * @param toAdd the ship to the board
   * @return null if the placement succeed, otherwise a error
   */
  public String  tryAddShip(Ship<T> toAdd);

  /**
   * Return the marker at the coordinate
   * @param where the coordinate what to check
   * @return the value at the coordinate for player, otherwise null
   */
  public T whatIsAtForSelf(Coordinate where);

  /**
   * Record attack at the given coordinate
   * @param c the coordinate being fired at
   * @return the ship that occupies the coordinate if hit
   */
  public Ship<T> fireAt(Coordinate c);

  /**
   * Return the enemy board on this coordinate
   * @param where the coordinate to check
   * @return the value at the coordinate for enemy view
   */
  public T whatIsAtForEnemy(Coordinate where);

  /**
   * Checking the player is lost or not
   * @return true if the player is lost, otherwise false
   */
  public boolean isLost();

  /**
   * Return the ship which located at the given coordinate
   * @param where the coordinate to check
   * @return the hsip occupying the coordinate
   */
  public Ship<T> getShipAt(Coordinate where);

  /**
   * Do some ship removement
   * @param ship the ship to remove from the board
   */
  public void removeShip(Ship<T> ship);
}

