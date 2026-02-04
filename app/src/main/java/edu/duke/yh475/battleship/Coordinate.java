package edu.duke.yh475.battleship;

/**
 * This class represents a loction with 2D board
 * Coordinates consist of a row and a col
 */
public class Coordinate {
  private final int row;
  private final int column;
  /**
   * Construct a coordinate
   * @param r the row index
   * @param c the col index
   */
  public Coordinate(int r, int c) {
    this.row = r;
    this.column = c;
  }

  /**
   *Construct a coordinate by parsing a string
   * @param input A string with length2, first char is A-Z, and the second is 0-9
   * @throw IllegalArgumentException if the string is  not format correct
   */
  public Coordinate(String input) {
    if (input.length() != 2) {
      throw new IllegalArgumentException("Coordinate must be 2 characters");
    }
    char rowLetter = Character.toUpperCase(input.charAt(0));
    char cloumnnum = input.charAt(1);
    if (rowLetter < 'A' || rowLetter > 'Z') {
      throw new IllegalArgumentException("Coordinate row must be A-Z, but is " + rowLetter);
    }
    if (cloumnnum < '0' || cloumnnum > '9') {
      throw new IllegalArgumentException("Coordinate column must be 0-9, but is " + cloumnnum);
    }
    this.row = rowLetter - 'A';
    this.column = cloumnnum - '0'; 
 }

  public int getRow() {
    return row;
  }

  public int getColumn() {
    return column;
  }

  /**
   * Compares this corrdinate with another object
   * @param o the object to compare
   * @return true if they are the same row and col
   */
  @Override
  public boolean equals(Object o) {
    if (o != null && o.getClass().equals(getClass())) {
      Coordinate c = (Coordinate) o;
      return row == c.row && column == c.column;
    }
    return false;
  }

  @Override
  public String toString() {
    return "(" + row + ", " + column + ")";
  }

  @Override
  public int hashCode() {
    return toString().hashCode();
  }
}
