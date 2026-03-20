package edu.duke.yh475.battleship;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Stack;
import java.util.function.Function;

/**
 * Computer player implements the player interface
 * Handles the ship placement and firing logic for a computer player
 * It uses a random strategy for firing at the enemy board
 * and a predefined strategy for ship placement.
 */
public class ComputerPlayer implements Player {
  protected final Board<Character> theBoard;
  protected final BoardTextView view;
  protected final PrintStream out;
  protected final AbstractShipFactory<Character> shipFactory;
  protected final String name;
  protected final ArrayList<String> shipsToPlace;
  protected final HashMap<String, Function<Placement, Ship<Character>>> shipCreationFns;

  protected final Random random = new Random();
  // A set to keep track of coordinates that have already fired at
  protected final HashSet<Coordinate> firedCoordinates = new HashSet<>();
  // A stack to keep track of target coordinates for the computer player
  protected final Stack<Coordinate> targetStack = new Stack<>();

  protected Coordinate firstHit = null;
  protected boolean isVertical = false;
  protected boolean isHorizontal = false;

  /**
   * Constructor for the computer player
   * 
   * @param name        the name of the player
   * @param theBoard    the board for the player to place ships on and fire from
   * @param out         the PrintStream to output messages to
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
   * 
   * @param enemyBoard  the board of the enemy player to fire at
   * @param enemyView   the view of the enemy board (not used by computer player)
   * @param myHeader    the header to display for the computer player's board
   * @param enemyHeader the header to display for the enemy player's board
   */
  @Override
  public void playOneTurn(Board<Character> enemyBoard, BoardTextView enemyView, String myHeader, String enemyHeader)
      throws IOException {
    Coordinate coord = null;
    while (coord == null) {
      if (!targetStack.isEmpty()) {
        Coordinate potential = targetStack.pop();
        if (!firedCoordinates.contains(potential)) {
          coord = potential;
        }
      } else {
        // Stack is empty, generate a random coordinate
        coord = generateRandomCoordinate(enemyBoard);
      }
    }
    String coordStr = "" + (char) ('A' + coord.getRow()) + coord.getColumn();
    Ship<Character> hit = enemyBoard.fireAt(coord);
    firedCoordinates.add(coord);

    if (hit != null) {
      addNeighborsToStack(coord, enemyBoard);
      out.println("Player " + name + " hit your " + hit.getName() + " at " + coordStr + "!");
      if (enemyBoard.getShipAt(coord).isSunk()) {
        firstHit = null;
        targetStack.clear();
      }
    } else {
      out.println("Player " + name + " missed!");
    }
  }

  /**
   * Generates a random coordinate for the computer player to fire at
   * 
   * @param enemyBoard the board of the enemy player
   * @return a random coordinate that has not been fired before
   */
  protected Coordinate generateRandomCoordinate(Board<Character> enemyBoard) {
    int row, col;
    Coordinate coord;
    do {
      row = random.nextInt(enemyBoard.getHeight());
      col = random.nextInt(enemyBoard.getWidth());
      coord = new Coordinate(row, col);
    } while (firedCoordinates.contains(coord)); // ensure the coordinate has not been fired before
    return coord;
  }

  /**
   * Adds the neighboring coordinates of a hit coordinate to the target stack for
   * the computer player
   * 
   * @param coord      the coordinate that was hit
   * @param enemyBoard the board of the enemy player to check for valid neighbors
   */
  protected void addNeighborsToStack(Coordinate coord, Board<Character> enemyBoard) {
    int r = coord.getRow();
    int c = coord.getColumn();

    // Add neighbors in all four directions
    if (firstHit == null) {
        setFirstHitAndAddNeighbors(coord, enemyBoard);
        return;
    } 

    // if we have a first hit we want to prioritize firing along the same row or column to try to sink the ship
    boolean sameRow = (r == firstHit.getRow());
    boolean sameCol = (c == firstHit.getColumn());

    if (sameRow) {
        // determine if the ship is horizontal
        targetStack.removeIf(p -> p.getRow() != r);
        addfireCandiate(new Coordinate(r, c + 1), enemyBoard);
        addfireCandiate(new Coordinate(r, c - 1), enemyBoard);
    } else if (sameCol) {
        // determine if the ship is vertical
        targetStack.removeIf(p -> p.getColumn() != c);
        addfireCandiate(new Coordinate(r + 1, c), enemyBoard);
        addfireCandiate(new Coordinate(r - 1, c), enemyBoard);
    } else {
        // if we have hit a new ship, reset the first hit and add all neighbors
        setFirstHitAndAddNeighbors(coord, enemyBoard);
    }
  }

  /**
   * helper method to add a coordinate to the target stack if is is a valid
   * coordinate
   */
  private void addfireCandiate(Coordinate neighbor, Board<Character> enemyBoard) {
    int r = neighbor.getRow();
    int c = neighbor.getColumn();
    if (r >= 0 && r < enemyBoard.getHeight() && c >= 0 && c < enemyBoard.getWidth()) {
      if (!firedCoordinates.contains(neighbor)) {
        targetStack.push(neighbor);
      }
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

  /**
   * A GUI version of the turn logic
   * It fires at the board, updates its computer stack, and returns the coordinate it fired at
   * @param enemyBoard the board of the enemy player to fire at
   * @return the coordinate that the computer player fired at
   */
  public Coordinate doGuiTurn(Board<Character> enemyBoard) {
    Coordinate coord = null;
    while (coord == null) {
      if (!targetStack.isEmpty()) {
        Coordinate potential = targetStack.pop();
        if (!firedCoordinates.contains(potential)) {
          coord = potential;
        }
      } else {
        coord = generateRandomCoordinate(enemyBoard);
      }
    }

    Ship<Character> hit = enemyBoard.fireAt(coord);
    firedCoordinates.add(coord);

    if (hit != null) {
      addNeighborsToStack(coord, enemyBoard);
      if (hit.isSunk()) {
        firstHit = null;
        targetStack.clear();
      }
    }
    return coord;
  }

  private void setFirstHitAndAddNeighbors(Coordinate coord, Board<Character> enemyBoard) {
    int r = coord.getRow();
    int c = coord.getColumn();
    firstHit = coord;
    addfireCandiate(new Coordinate(r - 1, c), enemyBoard); // up
    addfireCandiate(new Coordinate(r + 1, c), enemyBoard); // down
    addfireCandiate(new Coordinate(r, c - 1), enemyBoard); // left
    addfireCandiate(new Coordinate(r, c + 1), enemyBoard); // right
  }
}
