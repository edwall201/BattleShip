package edu.duke.yh475.battleship;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Function;

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

  @Override
  public void doPlacementPhase() throws IOException {
    String[] placements = {
        "A0H", "B0H", "C0H", "D0H", "E0H", "F0U", "I0U", "L0U", "O0U", "R0U"
    };
    int i = 0;
    for (String shipName : shipsToPlace) {
      Placement p = new Placement(placements[i++]);
      Ship<Character> s = shipCreationFns.get(shipName).apply(p);
      theBoard.tryAddShip(s);
    }
  }

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
    if (hit != null) {
      out.println("Player " + name + " hit your " + hit.getName() + " at " + coord + "!");
    } else {
      out.println("Player " + name + " missed!");
    }
  }

  @Override
  public boolean isLost() {
    return theBoard.isLost();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Board<Character> getBoard() {
    return theBoard;
  }

  @Override
  public BoardTextView getView() {
    return view;
  }
}
