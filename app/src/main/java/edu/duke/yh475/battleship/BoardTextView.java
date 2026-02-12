package edu.duke.yh475.battleship;

import java.util.function.Function;

/**
 * This class handles textual display of a board
 * It supports 2 wayts to display the board
 * one for the player's own board, an one for the empty's board
 */
public class BoardTextView {

  /**
   * The board to display
   */
  private final Board<Character> toDisplay;

  /**
   * Constructs a BoardView, given the board it will display
   * 
   * @param toDisplay is the board to display
   * @throws IllegalArgumentException if the board is larger than 10*26
   */
  public BoardTextView(Board<Character> toDisplay) {
    this.toDisplay = toDisplay;
    if (toDisplay.getWidth() > 10 || toDisplay.getHeight() > 26) {
      throw new IllegalArgumentException("Board must be no larger than 10x26, but is "
          + toDisplay.getWidth() + "x" + toDisplay.getHeight());
    }
  }

  /**
   * Generates a string representation of the board from the perspective of the
   * owner
   * The display includes a col number each row
   * 
   * @return a formatted string representing the complete of the board
   */
  protected String displayAnyBoard(Function<Coordinate, Character> getSquareFn) {
    StringBuilder ans = new StringBuilder();
    String header = makeHeader();
    ans.append(header);
    for (int i = 0; i < toDisplay.getHeight(); i++) {
      ans.append(makeRow(i, getSquareFn));
    }
    ans.append(header);
    return ans.toString();
  }

  /**
   * This is the header line , 0 | 1 | 2 | 3\n
   * 
   * @return the String that is the header for the given board
   */
  String makeHeader() {
    StringBuilder ans = new StringBuilder("  ");
    String sep = "";
    for (int i = 0; i < toDisplay.getWidth(); i++) {
      ans.append(sep);
      ans.append(i);
      sep = "|";
    }
    ans.append("\n");
    return ans.toString();
  }

  /**
   * This is the row line.
   * 
   * @return the String that is the row for the given board
   */
  String makeRow(int rowNum, Function<Coordinate, Character> getSquareFn) {
    StringBuilder ans = new StringBuilder();
    char letter = (char) ('A' + rowNum);
    ans.append(letter);
    ans.append(" ");
    String sep = "";
    for (int col = 0; col < toDisplay.getWidth(); col++) {
      ans.append(sep);
      Coordinate coord = new Coordinate(rowNum, col);
      Character cha = getSquareFn.apply(coord);
      if (cha == null) {
        ans.append(" ");
      } else {
        ans.append(cha);
      }
      sep = "|";
    }
    ans.append(" ");
    ans.append(letter);
    ans.append("\n");
    return ans.toString();
  }

  public Board<Character> getToDisplay() {
    return toDisplay;
  }

  /**
   * Generates a string representation of the board from the owner
   */
  public String displayMyOwnBoard() {
    return displayAnyBoard((c) -> toDisplay.whatIsAtForSelf(c));
  }

  /**
   * Generates a string representation of the board from the enemy
   */
  public String displayEnemyBoard() {
    return displayAnyBoard((c) -> toDisplay.whatIsAtForEnemy(c));
  }

}
