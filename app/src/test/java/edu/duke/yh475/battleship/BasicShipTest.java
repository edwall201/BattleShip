package edu.duke.yh475.battleship;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

public class BasicShipTest {
  @Test
  public void test_iterable() {
    Coordinate c1 = new Coordinate(1, 2);
    RectangleShip<Character> ship = new RectangleShip<Character>("submarine", c1, 1, 3, 's', '*');

    assertTrue(ship.occupiesCoordinates(new Coordinate(1, 2)));
    assertTrue(ship.occupiesCoordinates(new Coordinate(2, 2)));
    assertTrue(ship.occupiesCoordinates(new Coordinate(3, 2)));

  }

  @Test
  void test_hit_and_sink() {
    Coordinate c1 = new Coordinate(1, 2);
    RectangleShip<Character> ship = new RectangleShip<Character>("submarine", c1, 1, 2, 's', '*');
    assertFalse(ship.wasHitAt(c1));
    assertFalse(ship.isSunk());
    ship.recordHitAt(c1);
    assertTrue(ship.wasHitAt(c1));
    Coordinate c2 = new Coordinate(2, 2);
    ship.recordHitAt(c2);
    assertTrue(ship.wasHitAt(c2));
    assertTrue(ship.isSunk());

  }

  @Test
  void test_getDisplay() {
    Coordinate c1 = new Coordinate(1, 2);
    RectangleShip<Character> ship = new RectangleShip<Character>("submarine", c1, 1, 2, 's', '*');
    assertEquals('s', ship.getDisplayInfoAt(c1, true));
    ship.recordHitAt(c1);
    assertEquals('*', ship.getDisplayInfoAt(c1, true));
  }

  @Test
  void test_checkCoordinateInthisShip() {
    Coordinate c1 = new Coordinate(1, 1);
    RectangleShip<Character> ship = new RectangleShip<Character>("submarine", c1, 1, 1, 's', '*');
    assertThrows(IllegalArgumentException.class, () -> {
      ship.wasHitAt(new Coordinate(2, 2));
    });
  }

  @Test
  public void test_getCoordinates() {
    Coordinate base = new Coordinate(1, 2);
    RectangleShip<Character> ship = new RectangleShip<Character>("TestShip", base, 1, 3, 's', '*');
    Iterable<Coordinate> coords = ship.getCoordinates();

    HashSet<Coordinate> actualCoords = new HashSet<>();
    for (Coordinate c : coords) {
      actualCoords.add(c);
    }

    assertTrue(actualCoords.contains(new Coordinate(1, 2)));
    assertTrue(actualCoords.contains(new Coordinate(2, 2)));
    assertTrue(actualCoords.contains(new Coordinate(3, 2)));
    assertFalse(actualCoords.contains(new Coordinate(0, 0)));

  }

  @Test
  public void test_ship_index_methods() {
    AbstractShipFactory<Character> factory = new V2ShipFactory();
    Ship<Character> ship = factory.makeSubmarine(new Placement("A0H"));
    assertEquals(2, ship.getNumPieces());

    assertFalse(ship.wasHitAt(0));
    assertFalse(ship.wasHitAt(1));

    ship.recordHitAt(1);

    assertTrue(ship.wasHitAt(1));
    assertFalse(ship.wasHitAt(0));
    assertTrue(ship.wasHitAt(new Coordinate("A1")));
  }

  @Test
  public void test_index_out_of_bounds() {
    AbstractShipFactory<Character> factory = new V2ShipFactory();
    Ship<Character> ship = factory.makeSubmarine(new Placement("A0H"));

    assertThrows(IllegalArgumentException.class, () -> ship.wasHitAt(-1));
    assertThrows(IllegalArgumentException.class, () -> ship.wasHitAt(2));
  }

  @Test
  public void test_getShipAt() {
    BattleShipBoard<Character> board = new BattleShipBoard<>(10, 20);
    V2ShipFactory factory = new V2ShipFactory();
    Ship<Character> sub = factory.makeBattleship(new Placement("A0U"));
    board.tryAddShip(sub);

    assertSame(sub, board.getShipAt(new Coordinate(1, 0)));
    assertSame(sub, board.getShipAt(new Coordinate(0, 1)));
    assertSame(sub, board.getShipAt(new Coordinate(1, 1)));
    assertSame(sub, board.getShipAt(new Coordinate(1, 2)));


    Ship<Character> dest = factory.makeDestroyer(new Placement("D5H"));
    board.tryAddShip(dest);
    assertSame(dest, board.getShipAt(new Coordinate(3, 5))); 
  }

  @Test
  public void test_getShipAt_null() {
    BattleShipBoard<Character> board = new BattleShipBoard<>(10, 10);
    V2ShipFactory factory = new V2ShipFactory();
    board.tryAddShip(factory.makeSubmarine(new Placement("A0H")));
    assertNotNull(board.getShipAt(new Coordinate(0, 0)));
    assertNull(board.getShipAt(new Coordinate(1, 5))); 
  }

  @Test
  public void test_ship_index_system() {
    V2ShipFactory factory = new V2ShipFactory();
    Ship<Character> dest = factory.makeCarrier(new Placement("A0L"));
    assertEquals(7, dest.getNumPieces());
    dest.recordHitAt(5);
    
    assertTrue(dest.wasHitAt(5));
    assertTrue(dest.wasHitAt(new Coordinate(0,3)));
    assertFalse(dest.wasHitAt(0));               
    
    assertThrows(IllegalArgumentException.class, () -> dest.recordHitAt(-1));
    assertThrows(IllegalArgumentException.class, () -> dest.wasHitAt(7));
  }

}
