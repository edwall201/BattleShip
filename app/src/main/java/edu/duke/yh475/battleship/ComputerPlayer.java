package edu.duke.yh475.battleship;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Function;

/**
 * Computer player implements the player interface
 * Handles the ship placement and firing logic for a computer player
 * It will fire every coordinate on the board, starting from A0 to the end
 */
public class ComputerPlayer implements Player {
  protected final Board<Character> theBoard;
  protected final BoardTextView view;
  protected final PrintStream out;
  protected final AbstractShipFactory<Character> shipFactory;
  protected final String name;
  protected final ArrayList<String> shipsToPlace;
  protected final HashMap<String, Function<Placement, Ship<Character>>> shipCreationFns;

  private int FireRow = 0;
  private int FireCol = 0;

  /**
   * Constructor for the computer player
   * @param name the name of the player
   * @param theBoard the board for the player to place ships on and fire from
   * @param out the PrintStream to output messages to
   * @param shipFactory the factory to create ships for placement
   */
  public ComputerPlayer(String name, Board<Character> theBoard, PrintStream out,
      AbstractShipFactory<Character> shipFactory) {
    this.name = name;
    this.theBoard = theBoard;
    this.view = new BoardTextView(theBoard);
    this.out = out;
    this.shipFactory = shipFactory;
    this.shipsToPlace = new ArrayList<>();
    this.shipCreationFns = new HashMap<>();
    setupShipCreationMap();
    setupShipCreationList();
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
  protected void setupShipCreationList() {
    shipsToPlace.addAll(Collections.nCopies(2, "Submarine"));
    shipsToPlace.addAll(Collections.nCopies(3, "Destroyer"));
    shipsToPlace.addAll(Collections.nCopies(3, "Battleship"));
    shipsToPlace.addAll(Collections.nCopies(2, "Carrier"));
  }

  /**
   * Handles the ship placement for the computer player
   */
  @Override
  public void doPlacementPhase() throws IOException {
    String[] placements = {
        "A0H", "B0H", "C0H", "D0H", "E0H", "F0U", "I0U", "L0U", "O0U", "O2U"
    };
    int i = 0;
    for (String shipName : shipsToPlace) {
      Placement p = new Placement(placements[i++]);
      Ship<Character> s = shipCreationFns.get(shipName).apply(p);
      theBoard.tryAddShip(s);
    }
  }

  /**
   * Handles the firing logic for the computer player
   * @param enemyBoard the board of the enemy player to fire at
   * @param enemyView the view of the enemy board (not used by computer player)
   * @param myHeader the header to display for the computer player's board
   * @param enemyHeader the header to display for the enemy player's board
   */
  @Override
  public void playOneTurn(Board<Character> enemyBoard, BoardTextView enemyView, String myHeader, String enemyHeader)
      throws IOException {
    Coordinate coord = new Coordinate(FireRow, FireCol);
    FireCol++;
    if (FireCol >= enemyBoard.getWidth()) {
      FireCol = 0;
      FireRow++;
    }
    Ship<Character> hit = enemyBoard.fireAt(coord);
    char rowChar = (char) ('A' + coord.getRow());
    String coordStr = "" + rowChar + coord.getColumn();
    if (hit != null) {
      out.println("Player " + name + " hit your " + hit.getName() + " at " + coordStr + "!");
    } else {
      out.println("Player " + name + " missed!");
    }
  }

  /**
   * Desrcibe in Player interface
   */
  @Override
  public boolean isLost() {
    return theBoard.isLost();
  }

  /**
   * Desrcibe in Player interface
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * Desrcibe in Player interface
   */
  @Override
  public Board<Character> getBoard() {
    return theBoard;
  }

  /**
   * Desrcibe in Player interface
   */
  @Override
  public BoardTextView getView() {
    return view;
  }
}
