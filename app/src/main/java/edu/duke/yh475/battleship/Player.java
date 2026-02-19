package edu.duke.yh475.battleship;

import java.io.IOException;

/**
 * This interface defines the methods that a plyare must implement in the game.
 */
public interface Player {
  /**
   * Execute the ship placement
   * @throws IOException if an input or output error occurs
   */
  void doPlacementPhase() throws IOException;

  /**
   * Implement the turn of this player
   * @param enemyBoard the enemy's board
   * @param enemyView the text view used to display enemy's board
   * @param myHeader the header label for display my board
   * @param enemyHeader the header label for enemy's board
   * @throws IOException if an input or output error occurs
   */
  void playOneTurn(Board<Character> enemyBoard, BoardTextView enemyView, String myHeader, String enemyHeader) throws IOException;
  
  /**
   * Checking player is lost or not
   * @return True if the player is lost, otherwise false
   */
  boolean isLost();  

  /**
   * Name getter
   * @return name
   */
  String getName();

  /**
   * Board getter
   * @return board
   */
  Board<Character> getBoard();

  /**
   * View getter
   * @return View
   */
  BoardTextView getView();
}