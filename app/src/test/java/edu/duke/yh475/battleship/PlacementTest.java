package edu.duke.yh475.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class PlacementTest {
  @Test
  public void test_string_valid() {
    Coordinate c1 = new Coordinate(0, 0);
    Placement p1 = new Placement("A0V");
    assertEquals(c1, p1.getWhere());
    assertEquals('V', p1.getOrientation());

    Placement p2 = new Placement("A0H");
    assertEquals(c1, p2.getWhere());
    assertEquals('H', p2.getOrientation());

    Coordinate c2 = new Coordinate(2, 9);
    Placement p3 = new Placement("C9h");
    assertEquals(c2, p3.getWhere());
    assertEquals('H', p3.getOrientation());

    Placement p4 = new Placement("C9v");
    assertEquals(c2, p4.getWhere());
    assertEquals('V', p4.getOrientation());

  }

  @Test
  public void test_string_error_cases() {
    assertThrows(IllegalArgumentException.class, () -> new Placement("A0"));
    assertThrows(IllegalArgumentException.class, () -> new Placement("A0VV"));
    assertThrows(IllegalArgumentException.class, () -> new Placement("A0l"));
    assertThrows(IllegalArgumentException.class, () -> new Placement("A01"));
    assertThrows(IllegalArgumentException.class, () -> new Placement(" "));
    assertThrows(IllegalArgumentException.class, () -> new Placement("!0V"));
  }

  @Test
  public void test_toString() {
    Coordinate c1 = new Coordinate(2, 3);
    Placement p1 = new Placement(c1, 'V');
    assertEquals("(2, 3)V", p1.toString());
  }

  @Test
  public void test_equals_and_hashCode() {
    Coordinate c1 = new Coordinate(1, 2);
    Coordinate c2 = new Coordinate(1, 2);
    Placement p1 = new Placement(c1, 'V');
    Placement p2 = new Placement(c2, 'v');
    Placement p3 = new Placement(c1, 'H');
    assertEquals(p1, p1);
    assertEquals(p1, p2);
    assertNotEquals(p1, p3);
    assertNotEquals(p1, "A1V");
    assertEquals(p1.hashCode(), p2.hashCode());
    assertNotEquals(p1.hashCode(), p3.hashCode());
  }

}
