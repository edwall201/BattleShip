package edu.duke.yh475.battleship;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class BasicShipTest {
  @Test
  public void test_iterable() {
    Coordinate c1 = new Coordinate(1, 2);
    RectangleShip<Character> ship = new RectangleShip<Character>("submarine",c1, 1, 3, 's', '*');

    assertTrue(ship.occupiesCoordinates(new Coordinate(1, 2)));
    assertTrue(ship.occupiesCoordinates(new Coordinate(2, 2)));
    assertTrue(ship.occupiesCoordinates(new Coordinate(3, 2)));

  }

  @Test
  void test_hit_and_sink() {
    Coordinate c1 = new Coordinate(1, 2);
    RectangleShip<Character> ship = new RectangleShip<Character>("submarine",c1, 1, 2, 's', '*');
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
    RectangleShip<Character> ship = new RectangleShip<Character>("submarine",c1, 1, 2, 's', '*');
    assertEquals('s', ship.getDisplayInfoAt(c1));
    ship.recordHitAt(c1);
    assertEquals('*', ship.getDisplayInfoAt(c1));
  }

  @Test
  void test_checkCoordinateInthisShip() {
    Coordinate c1 = new Coordinate(1, 1);
    RectangleShip<Character> ship = new RectangleShip<Character>("submarine",c1, 1, 1, 's', '*');
    assertThrows(IllegalArgumentException.class, () -> {
      ship.wasHitAt(new Coordinate(2, 2));
    });
  }

}
