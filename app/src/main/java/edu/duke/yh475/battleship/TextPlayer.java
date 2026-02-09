package edu.duke.yh475.battleship;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;

/**
 * TextPlayer handles the textual interaction with a player
 * manage input display board and executing placement phase
 */
public class TextPlayer {
  final Board<Character> theBoard;
  final BoardTextView view;
  final BufferedReader inputReader;
  final PrintStream out;
  final AbstractShipFactory<Character> shipFactory;
  final String TextPlayer;

  /**
   * Construct a TextPlayer 
   * @param name is the name of the player
   * @param theBoard is the board where ships will be placed
   * @param inputSource is the source of user input
   * @param out is the output stream for messages
   * @param shipFactory is the factory used to create ship objects
   */
  public TextPlayer(String name, Board<Character> theBoard, BufferedReader inputSource, PrintStream out,
    AbstractShipFactory<Character> shipFactory) {
    this.TextPlayer = name;
    this.theBoard = theBoard;
    this.view = new BoardTextView(theBoard);
    this.inputReader = inputSource;
    this.out = out;
    this.shipFactory = new V1ShipFactory();
  }

  /**
   * Prompts the player for a aship placement and reads their response.
   * @param prompt is teh message to display to player
   * @param return a placement parsed from input
   * @throws IOException if there is an error reading
   */
  public Placement readPlacement(String prompt) throws IOException {
    out.println(prompt);
    String s = inputReader.readLine();
    return new Placement(s);
  }

  /**
   * A single ship placement and display the board
   * @throws IOException if input reading fails
   */
  public void doOnePlacement() throws IOException {
    String prompt = "Player " + TextPlayer + " where do you want to place a Destroyer?";
    Placement p = readPlacement(prompt);
    Ship<Character> s = shipFactory.makeDestroyer(p);
    theBoard.tryAddShip(s);
    out.print(view.displayMyOwnBoard());
  }

  /**
   * Displays the initial board, prints game instruction and handle ship placement
   * @throws IOException if there is an error during input output
   */
  public void doPlacementPhase() throws IOException {
    out.println(view.displayMyOwnBoard());
    String instruct = "Player " + TextPlayer
        + ": you are going to place the following ships (which are all rectangular). For each ship, type the coordinate of the upper left side of the ship, followed by either H (for horizontal) or V (for vertical).  For example M4H would place a ship horizontally starting at M4 and going to the right.  You have\n"
        + "\n" + "2 \"Submarines\" ships that are 1x2\n" + "3 \"Destroyers\" that are 1x3\n"
        + "3 \"Battleships\" that are 1x4\n" + "2 \"Carriers\" that are 1x6\n";

    out.println(instruct);
    doOnePlacement();
  }
}
