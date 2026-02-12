package edu.duke.yh475.battleship;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;

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
    assertEquals('s', ship.getDisplayInfoAt(c1, true));
    ship.recordHitAt(c1);
    assertEquals('*', ship.getDisplayInfoAt(c1, true));
  }

  @Test
  void test_checkCoordinateInthisShip() {
    Coordinate c1 = new Coordinate(1, 1);
    RectangleShip<Character> ship = new RectangleShip<Character>("submarine",c1, 1, 1, 's', '*');
    assertThrows(IllegalArgumentException.class, () -> {
      ship.wasHitAt(new Coordinate(2, 2));
    });
  }

  @Test
  public void test_getCoordinates(){
    Coordinate base = new Coordinate(1,2);
    RectangleShip<Character> ship = new RectangleShip<Character>("TestShip", base, 1, 3, 's', '*');
    Iterable<Coordinate> coords = ship.getCoordinates();

    HashSet<Coordinate> actualCoords = new HashSet<>();
    for(Coordinate c: coords){
      actualCoords.add(c);
    }
    
    assertTrue(actualCoords.contains(new Coordinate(1, 2)));
    assertTrue(actualCoords.contains(new Coordinate(2, 2)));
    assertTrue(actualCoords.contains(new Coordinate(3, 2)));
    assertFalse(actualCoords.contains(new Coordinate(0, 0)));
      
  }

}
