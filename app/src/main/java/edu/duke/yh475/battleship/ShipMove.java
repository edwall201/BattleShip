package edu.duke.yh475.battleship;

/**
 * A class to handle the moving action
 */
public class ShipMove<T> {
  private final Board<T> board;
  private final AbstractShipFactory<T> factory;

  /**
   * Constructs a ShipMove with the given board and factory
   * @param board the board to move ships on
   * @param factory the factory to create new ships for moving
   */
  public ShipMove(Board<T> board, AbstractShipFactory<T> factory) {
    this.board = board;
    this.factory = factory;
  }

  /**
   * Moves a ship from one coordinate to a new placement
   * @param from the coordinate of the ship to move
   * @param to the new placement for the ship
   * @return null if the move is successful, or an error message if the move is invalid
   */
  public String doMove(Coordinate from, Placement to) {
    Ship<T> shipToMove = board.getShipAt(from);
    if (shipToMove == null) {
      return "No ship at this place";
    }
    Ship<T> newShip = createNewShip(shipToMove.getName(), to);
    board.removeShip(shipToMove);
    String result = board.tryAddShip(newShip);
    if (result != null) {
      // If the move is invalid, put the original ship back and return the error message
      board.tryAddShip(shipToMove);
      return result;
    }

    //transfer hit status from old ship to new ship
    for (int i = 0; i < shipToMove.getNumPieces(); i++) {
        if (shipToMove.wasHitAt(i)) {
            newShip.recordHitAt(i);
        }
    }
    return null; // Move successful
    
  }

  /**
   * Creates a new ship based on the name and placement
   * @param name the name of the ship to create
   * @param to the placement for the new ship
   * @return the newly created ship
   * @throws IllegalArgumentException if the ship name is unknown
   */
  private Ship<T> createNewShip(String name, Placement to) {
    if (name.equals("Submarine")) {
        return factory.makeSubmarine(to);
    } else if (name.equals("Destroyer")) {
        return factory.makeDestroyer(to);
    } else if (name.equals("Battleship")) {
        return factory.makeBattleship(to);
    } else if (name.equals("Carrier")) {
        return factory.makeCarrier(to);
    } else {
        throw new IllegalArgumentException("Unknown ship type: " + name);
    }
  }

}
