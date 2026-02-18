package edu.duke.yh475.battleship;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * TextPlayer handles the textual interaction with a player
 * manage input display board and executing placement phase
 */
public class TextPlayer implements Player {
  final Board<Character> theBoard;
  final BoardTextView view;
  final BufferedReader inputReader;
  final PrintStream out;
  final AbstractShipFactory<Character> shipFactory;
  final String name;
  final ArrayList<String> shipsToPlace;
  final HashMap<String, Function<Placement, Ship<Character>>> shipCreationFns;
  private int moveCount = 3;
  private int sonarCount = 3;

  /**
   * Construct a TextPlayer
   * 
   * @param name        is the name of the player
   * @param theBoard    is the board where ships will be placed
   * @param inputSource is the source of user input
   * @param out         is the output stream for messages
   * @param shipFactory is the factory used to create ship objects
   */
  public TextPlayer(String name, Board<Character> theBoard, BufferedReader inputSource, PrintStream out,
      AbstractShipFactory<Character> shipFactory) {
    this.name = name;
    this.theBoard = theBoard;
    this.view = new BoardTextView(theBoard);
    this.inputReader = inputSource;
    this.out = out;
    this.shipFactory = shipFactory;
    this.shipsToPlace = new ArrayList<>();
    this.shipCreationFns = new HashMap<>();

    setupShipCreationMap();
    setupShipCreatoinList();
  }

  @Override
  public String getName() {
      return this.name;
  }

  @Override
  public boolean isLost() {
      return theBoard.isLost();
  }

  @Override
  public Board<Character> getBoard() {
    return this.theBoard;
  }

  @Override
  public BoardTextView getView() {
    return this.view;
  }

  /**
   * Prompts the player for a aship placement and reads their response.
   * 
   * @param prompt is teh message to display to player
   * @param return a placement parsed from input
   * @throws EOFException if there is an error reading
   */
  public Placement readPlacement(String prompt) throws IOException {
    out.println(prompt);
    String s = inputReader.readLine();
    if (s == null) {
      throw new EOFException("Player " + name + " failed to enter a placement");
    }
    return new Placement(s);
  }

  /**
   * A single ship placement and display the board
   * 
   * @param shipName is the name of the ship to place
   * @param createFn is the function to create the ship based on placement
   * @throws IOException if input reading fails
   */
  public void doOnePlacement(String shipName, Function<Placement, Ship<Character>> createFn) throws IOException {
    while (true) {
      try {
        String prompt = "Player " + name + " where do you want to place a " + shipName + "?";
        Placement p = readPlacement(prompt);
        Ship<Character> s = createFn.apply(p);
        String msg = theBoard.tryAddShip(s);
        if (msg == null) {
          out.print(view.displayMyOwnBoard());
          return;
        }
        out.println(msg);
      } catch (IllegalArgumentException e) {
        out.println("That placement is invalid: it does not have the correct format.");
      }
    }
  }

  /**
   * Displays the initial board, prints game instruction and handle ship placement
   * 
   * @throws IOException if there is an error during input output
   */
  public void doPlacementPhase() throws IOException {
    out.println(view.displayMyOwnBoard());
    String instruct = "Player " + name + ": you are going to place the following ships.\n"
        + "For each ship, type the coordinate of the upper left side of the ship's bounding box, \n"
        + "followed by the orientation.\n"
        + " - For Submarines and Destroyers, use H (horizontal) or V (vertical).\n"
        + " - For Battleships and Carriers, use U (up), R (right), D (down), or L (left).\n\n"
        + "You have:\n"
        + " 2 \"Submarines\" that are 1x2 rectangles (represented by \"s\")\n"
        + " 3 \"Destroyers\" that are 1x3 rectangles (represented by \"d\")\n"
        + " 3 \"Battleships\" that are now shaped as shown below \n"
        + "       b            b          bbb          b\n"
        + "      bbb    OR     bb   OR     b    OR    bb\n"
        + "                    b                       b\n"
        + "      Up          Right       Down        Left\n\n"
        + " 2 \"Carriers\" that are now shaped as shown below\n"
        + "      c                        c\n"
        + "      c            cccc        cc          ccc\n"
        + "      cc     OR     ccc  OR    cc    OR   cccc\n"
        + "      cc                       c\n"
        + "       c                       c\n"
        + "      Up          Right       Down        Left\n";
    out.println(instruct);

    for (String shipName : shipsToPlace) {
      Function<Placement, Ship<Character>> createFn = shipCreationFns.get(shipName);
      doOnePlacement(shipName, createFn);
    }
  }

  /**
   * Handles the ship placement phase for the player, prompting then to place each
   * ship
   */
  protected void setupShipCreationMap() {
    shipCreationFns.put("Submarine", p -> shipFactory.makeSubmarine(p));
    shipCreationFns.put("Destroyer", p -> shipFactory.makeDestroyer(p));
    shipCreationFns.put("Battleship", p -> shipFactory.makeBattleship(p));
    shipCreationFns.put("Carrier", p -> shipFactory.makeCarrier(p));
  }

  /**
   * Handles the ship placement phase for the player, prompting then to place each
   * ship
   */
  protected void setupShipCreatoinList() {
    shipsToPlace.addAll(Collections.nCopies(2, "Submarine"));
    shipsToPlace.addAll(Collections.nCopies(3, "Destroyer"));
    shipsToPlace.addAll(Collections.nCopies(3, "Battleship"));
    shipsToPlace.addAll(Collections.nCopies(2, "Carrier"));
  }

  /**
   * Prompts the player for a coordinate and reads their response.
   * 
   * @param prompt is the message to display to player
   * @return a coordinate parsed from input
   * @throws IOException if there is an error input
   */
  protected Coordinate readCoordinate(String prompt) throws IOException {
    while (true) {
      try {
        out.println(prompt);
        String input = inputReader.readLine();
        if (input == null) {
          throw new EOFException("Input ended unexpectedly");
        }
        return new Coordinate(input);
      } catch (IllegalArgumentException e) {
        out.println(e.getMessage() + " Please try again.");
      }
    }
  }

  /**
   * Implement the firing action, prompting the player for a coordinate to fire
   * Display the result of the fire action
   * 
   * @param enemyBoard is the board of the enemy player
   * @throws IOException if there is an error during input
   */
  protected void doFire(Board<Character> enemyBoard) throws IOException {
    while(true){
      try{
        Coordinate coord = readCoordinate("Player " + name + "'s turn: input a coordinate to fire at");
        Ship<Character> hitRecord = enemyBoard.fireAt(coord);
        if (hitRecord != null) {
          out.println("You hit a " + hitRecord.getName() + "!");
        } else {
          out.println("You missed!\n");
        }
        break;
      } catch (IllegalArgumentException e) {
        out.println(e.getMessage() + " Please try again.");
      }
    }
    
  }

  /**
   * Prompts the player for an action to take and reads their response
   * 
   * @param promt is the message to display
   * @return the action parsed from the user input
   * @throws IOException if there is an error
   */
  protected String readAction(String prompt) throws IOException {
    while (true) {
      out.println(prompt);
      String input = inputReader.readLine();
      if (input == null) {
        throw new EOFException("Input ended unexpectedly");
      }
      String action = input.toUpperCase().trim();

      if (action.equals("F") || action.equals("M") || action.equals("S")) {
        return action;
      }

      out.println("That action is invalid: please enter F, M, or S.");
    }
  }

  /**
   * Manages a single turn of attacking the enemy board
   * Base on the player's choice, fire, move, or sonar.
   * 
   * @param enemyBoard  is the board of the enemy player
   * @param enemyView   is the view of the enemy board
   * @param myHeader    is the header to display above the player's own board
   * @param enemyHeader is the header to display above the enemy board
   * @throws IOException if there is an error during input output
   */
  public void playOneTurn(Board<Character> enemyBoard, BoardTextView enemyView, String myHeader, String enemyHeader)
      throws IOException {
    out.println("---------------------------------------------------------------------------");
    out.println(view.displayMyBoardWithEnemyNextToIt(enemyView, myHeader, enemyHeader));
    if (moveCount <= 0 && sonarCount <= 0) {
      doFire(enemyBoard);
      return;
    }
    while (true) {
      StringBuilder prompt = new StringBuilder("Possible actions for Player " + name + ":\n");
      prompt.append("\n F Fire at a square");
      if (moveCount > 0) {
        prompt.append("\n M Move a ship to another square (").append(moveCount).append(" remaining)");
      }
      if (sonarCount > 0) {
        prompt.append("\n S Sonar scan (").append(sonarCount).append(" remaining)");
      }
      prompt.append("\n\nPlayer ").append(name).append(", what would you like to do?");

      String action = readAction(prompt.toString());

      if (action.equals("F")) {
        doFire(enemyBoard);
        break;
      } else if (action.equals("M")) {
        if (moveCount <= 0) {
          out.println("That action is invalid: no move actions remaining.");
          continue;
        }
        if (doMove())
          break;
      }
      if (action.equals("S")) {
        if (sonarCount <= 0) {
          out.println("That action is invalid: no sonar actions remaining.");
          continue;
        }
        doSonar(enemyBoard);
        sonarCount--;
        break;
      }
    }
  }

  /**
   * Do the move action, prompting the player for a coordinate to from and to
   * @return true if the move is successful, or false
   * @throws IOException if there is an error during input
   */
  protected boolean doMove() throws IOException {
    Coordinate from = readCoordinate("Which ship do you want to move?");
    Ship<Character> s = theBoard.getShipAt(from);
    if (s == null) {
      out.println("Error: There is no ship at " + from + "! Please choose an action again.");
      return false;
    }
    while (true){
      try{
        Placement to = readPlacement("Where would you like to move the ship to?");
        ShipMove<Character> mover = new ShipMove<>(theBoard, shipFactory);
        String result = mover.doMove(from, to);

        if (result != null) {
          out.println("Move failed: " + result + ". Please choose an action again.");
          return false;
        }

        moveCount--;
        out.println("Ship moved successfully!");
        return true;
      }
      catch (IllegalArgumentException e) {
        out.println("That placement is invalid: " + e.getMessage() + " Please try again.");
      }
    }
    
  }

  /**
   * Prompts the player for a coordinate to input the center of the sonar scan
   * Eexecute the sonar scan and display the result
   * @param enemyBoard is the board of the enemy player
   * @throws IOException if there is an error during input
   */
  protected void doSonar(Board<Character> enemyBoard) throws IOException {
    String prompt = "where do you want to center the sonar scan?";
    Coordinate center = readCoordinate(prompt);
    SonarScanner<Character> scanner = new SonarScanner<>(enemyBoard);
    Map<String, Integer> res = scanner.scan(center);
    out.println("---------------------------------------------------------------------------");
    out.println("Submarines occupy " + res.get("Submarine") + " squares");
    out.println("Destroyers occupy " + res.get("Destroyer") + " squares");
    out.println("Battleships occupy " + res.get("Battleship") + " squares");
    out.println("Carriers occupy " + res.get("Carrier") + " squares");
    out.println("---------------------------------------------------------------------------");
  }

}
