package edu.duke.yh475.battleship;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

public class RectangleShipTest {
  @Test
  public void test_basic() {
    Coordinate coord = new Coordinate(1,2);
    HashSet<Coordinate> expected = new HashSet<>();
    expected.add(new Coordinate(2,2));
    expected.add(new Coordinate(1,2));
    expected.add(new Coordinate(3, 2));
    HashSet<Coordinate> real = RectangleShip.makeCoords(coord, 1, 3);
    assertEquals(expected, real);
  }

  @Test
  public void test_ship_occupy(){
    Coordinate coord = new Coordinate(1, 2);
    RectangleShip<Character> ship = new RectangleShip<>(coord, 2, 3, 's', '*');
    assertTrue(ship.occupiesCoordinates(new Coordinate(1,2)));
    assertTrue(ship.occupiesCoordinates(new Coordinate(2,3)));
    assertTrue(ship.occupiesCoordinates(new Coordinate(3,3)));

    assertFalse(ship.occupiesCoordinates(new Coordinate(4,3)));
    
  }

}
