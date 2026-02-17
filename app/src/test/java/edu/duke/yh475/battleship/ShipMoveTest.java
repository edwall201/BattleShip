package edu.duke.yh475.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ShipMoveTest {
  @Test
  public void test_doMove_successfule() {
    BattleShipBoard<Character> board = new BattleShipBoard<>(10, 10);
    V2ShipFactory factory = new V2ShipFactory();
    ShipMove<Character> mover = new ShipMove<>(board, factory);
    Placement p1 = new Placement("A0U");
    Ship<Character> b1 = factory.makeBattleship(p1);

    board.tryAddShip(b1);
    b1.recordHitAt(0);
    assertTrue(b1.wasHitAt(0));

    Placement p2 = new Placement("B5R");
    String result = mover.doMove(new Coordinate(0, 1), p2);
    assertNull(result);
    assertNull(board.getShipAt(new Coordinate(0, 1)));
    Ship<Character> movedShip = board.getShipAt(new Coordinate(3, 5));

    assertNotNull(movedShip);
    assertEquals("Battleship", movedShip.getName());
    assertTrue(movedShip.wasHitAt(0));
  }

  @Test
  public void test_doMove_no_ship_at_source() {
    BattleShipBoard<Character> board = new BattleShipBoard<>(10, 10);
    V2ShipFactory factory = new V2ShipFactory();
    ShipMove<Character> mover = new ShipMove<>(board, factory);
    Coordinate from = new Coordinate("A0");
    Placement to = new Placement("B5U");
    String result = mover.doMove(from, to);
    assertEquals("No ship at this place", result);
  }

  @Test
  public void test_doMove_failed_and_addback() {
    BattleShipBoard<Character> board = new BattleShipBoard<>(10, 10);
    V2ShipFactory factory = new V2ShipFactory();
    ShipMove<Character> mover = new ShipMove<>(board, factory);

    Ship<Character> shipToMove = factory.makeSubmarine(new Placement("A0V"));
    board.tryAddShip(shipToMove);

    Ship<Character> obstacle = factory.makeSubmarine(new Placement("B5V"));
    board.tryAddShip(obstacle);

    Coordinate from = new Coordinate("A0");
    Placement to = new Placement("B5V");

    String result = mover.doMove(from, to);

    assertNotNull(result);
    assertTrue(result.contains("overlap"));

    assertSame(shipToMove, board.getShipAt(new Coordinate("A0")));

    assertSame(obstacle, board.getShipAt(new Coordinate("B5")));
  }

   @Test
  public void test_move_Submarine() {
    BattleShipBoard<Character> board = new BattleShipBoard<>(10, 10);
    V2ShipFactory factory = new V2ShipFactory();
    ShipMove<Character> mover = new ShipMove<>(board, factory);

    Ship<Character> s = factory.makeSubmarine(new Placement("A0V"));
    board.tryAddShip(s);
    
    String result = mover.doMove(new Coordinate("A0"), new Placement("C5V"));
    
    assertNull(result);
    assertEquals("Submarine", board.getShipAt(new Coordinate("C5")).getName());
  }

  @Test
  public void test_move_Destroyer() {
    BattleShipBoard<Character> board = new BattleShipBoard<>(10, 10);
    V2ShipFactory factory = new V2ShipFactory();
    ShipMove<Character> mover = new ShipMove<>(board, factory);

    Ship<Character> d = factory.makeDestroyer(new Placement("A1V"));
    board.tryAddShip(d);
    
    String result = mover.doMove(new Coordinate("A1"), new Placement("D1V"));
    
    assertNull(result);
    assertEquals("Destroyer", board.getShipAt(new Coordinate("D1")).getName());
  }

  @Test
  public void test_move_Battleship() {
    BattleShipBoard<Character> board = new BattleShipBoard<>(10, 10);
    V2ShipFactory factory = new V2ShipFactory();
    ShipMove<Character> mover = new ShipMove<>(board, factory);

    Ship<Character> b = factory.makeBattleship(new Placement("A0U"));
    board.tryAddShip(b);
    b.recordHitAt(0);

    String result = mover.doMove(new Coordinate(0, 1), new Placement("B5R"));
    
    assertNull(result);
    Ship<Character> newShip = board.getShipAt(new Coordinate("C6"));
    assertEquals("Battleship", newShip.getName());
    assertTrue(newShip.wasHitAt(0));
  }

  @Test
  public void test_move_Carrier() {
    BattleShipBoard<Character> board = new BattleShipBoard<>(10, 10);
    V2ShipFactory factory = new V2ShipFactory();
    ShipMove<Character> mover = new ShipMove<>(board, factory);

    Ship<Character> c = factory.makeCarrier(new Placement("A2U"));
    board.tryAddShip(c);
    
    String result = mover.doMove(new Coordinate("A2"), new Placement("D2U"));
    
    assertNull(result);
    assertEquals("Carrier", board.getShipAt(new Coordinate("D2")).getName());
    assertEquals(7, board.getShipAt(new Coordinate("D2")).getNumPieces());
  }

  @Test
  public void test_createNewShip_unknown_type_for_coverage() {
    BattleShipBoard<Character> board = new BattleShipBoard<>(10, 10);
    V2ShipFactory factory = new V2ShipFactory();
    ShipMove<Character> mover = new ShipMove<>(board, factory);
    Ship<Character> alienShip = new RectangleShip<Character>( "Alien", new Coordinate(0, 0), 1, 1, 'A', '*');
    board.tryAddShip(alienShip);
    assertThrows(IllegalArgumentException.class, () -> {mover.doMove(new Coordinate(0, 0), new Placement("B5V"));});
  }


}
