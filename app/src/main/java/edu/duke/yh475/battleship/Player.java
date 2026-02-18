package edu.duke.yh475.battleship;

import java.io.IOException;

public interface Player {
  void doPlacementPhase() throws IOException;
  void playOneTurn(Board<Character> enemyBoard, BoardTextView enemyView, String myHeader, String enemyHeader) throws IOException;
  boolean isLost();  
  String getName();
  Board<Character> getBoard();
  BoardTextView getView();
}