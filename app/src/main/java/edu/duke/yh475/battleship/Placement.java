package edu.duke.yh475.battleship;

/**
 * A placement representation the combination of a starting coordinate and orientation
 */
public class Placement {

  private final Coordinate where;
  private final char orientation;

  /**
   * Gets the coordinates of the placement
   * @return the coordinate
   */
  public Coordinate getWhere() {
    return where;
  }

  /**
   * Get the orientation of the placement
   * @return the orientation
   */
  public char getOrientation() {
    return orientation;
  }

  /**
   * Constructs a Placement with the given Coordinate and orientation.
   *  @param where is the starting Coordinate of the ship
   * @param orientation is V or H.
   */
  public Placement(Coordinate where, char orientation) {
    this.where = where;
    this.orientation = Character.toUpperCase(orientation);
  }

  /**
   * Constructs a placement from a string description
   * This constructor leverages the coordinate constructor
   * @param input is a 3 character string, first 2 are coordinate the third is the  orientation
   * @throws IllegalArgumentException if the input lenth is not 3
   * @throws IllegalArgumentException if the orientation is not V or H or U or D or L or R
   * @throws IllegalArgumentException if the coordinate part of the string is invalid
   */
  public Placement(String input) {
    if (input.length() != 3) {
      throw new IllegalArgumentException("Placement description must be 3 characters but is " + input);
    }
    this.where = new Coordinate(input.substring(0, 2));
    char orient = Character.toUpperCase(input.charAt(2));

    if (!isValidOrientation(orient)) {
      throw new IllegalArgumentException("Orientation must be H, V, U, D, L, or R but is " + orient);
    }
    this.orientation = orient;
  }

  /**
   * Checks if the given character is a valid orientation
   * @param o is the chara to check
   * @return true if the orientation is valid or not
   */
  private boolean isValidOrientation(char o){
    return o == 'H' || o == 'V' || o == 'U' || o == 'D' || o == 'L' || o == 'R';
  }

  /**
   * Checks if this Placement is equal to another object.
   * @param o is the object to compare with.
   * @return true if they are equal or false
   */
  @Override
  public boolean equals(Object o) {
    if (o != null && o.getClass().equals(getClass())) {
      Placement p = (Placement) o;
      return where.equals(p.where) && orientation == p.orientation;
    }
    return false;
  }

  @Override
  public String toString() {
    return where.toString() + orientation;
  }

  /**
   * @return the hash code of this placement.
   */
  @Override
  public int hashCode() {
    return toString().hashCode();
  }

}
