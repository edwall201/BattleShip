package edu.duke.yh475.battleship;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class BasicShipTest {
  @Test
  public void test_iterable() {
    Coordinate c1 = new Coordinate(1, 2);
    RectangleShip<Character> ship = new RectangleShip<Character>(c1, 1, 3, 's', '*');
    
    assertTrue(ship.occupiesCoordinates(new Coordinate(1, 2)));
    assertTrue(ship.occupiesCoordinates(new Coordinate(2, 2)));
    assertTrue(ship.occupiesCoordinates(new Coordinate(3, 2)));   

  }

}
