package edu.duke.yh475.battleship;

public class Coordinate {
  private final int row;
  private final int column;

  public Coordinate(int r, int c) {
    this.row = r;
    this.column = c;
  }

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
