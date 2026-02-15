package edu.duke.yh475.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class V2ShipFactoryTest {
  @Test
  public void test_makeCarrier_R() {
    V2ShipFactory factory = new V2ShipFactory();
    Ship<Character> s = factory.makeCarrier(new Placement("A0R"));
    assertFalse(s.occupiesCoordinates(new Coordinate(0, 0)));
    assertFalse(s.occupiesCoordinates(new Coordinate(0, 1)));
    assertTrue(s.occupiesCoordinates(new Coordinate(0, 2)));
    assertTrue(s.occupiesCoordinates(new Coordinate(1, 0)));
  }

  @Test
  public void test_makeCarrier_U() {
    V2ShipFactory f = new V2ShipFactory();
    Ship<Character> s = f.makeCarrier(new Placement("A0U"));
    assertTrue(s.occupiesCoordinates(new Coordinate(0, 0)));
    assertFalse(s.occupiesCoordinates(new Coordinate(0, 1)));
    assertTrue(s.occupiesCoordinates(new Coordinate(2, 1)));
  }

  @Test
  public void test_makeCarrier_D() {
    V2ShipFactory f = new V2ShipFactory();
    Ship<Character> s = f.makeCarrier(new Placement("A0D"));
    assertTrue(s.occupiesCoordinates(new Coordinate(0, 0)));
    assertFalse(s.occupiesCoordinates(new Coordinate(0, 1)));
    assertTrue(s.occupiesCoordinates(new Coordinate(2, 1)));
    assertTrue(s.occupiesCoordinates(new Coordinate(3, 2)));
  }

  @Test
  public void test_makeCarrier_L() {
    V2ShipFactory f = new V2ShipFactory();
    Ship<Character> s = f.makeCarrier(new Placement("A0L"));
    assertFalse(s.occupiesCoordinates(new Coordinate(0, 0)));
    assertTrue(s.occupiesCoordinates(new Coordinate(0, 1)));
    assertTrue(s.occupiesCoordinates(new Coordinate(1, 0)));
    assertTrue(s.occupiesCoordinates(new Coordinate(1, 2)));
    assertFalse(s.occupiesCoordinates(new Coordinate(1, 3)));
  }

  @Test
  public void test_makeBattleship_U() {
    V2ShipFactory f = new V2ShipFactory();
    Ship<Character> s = f.makeBattleship(new Placement("B2U"));
    assertEquals("Battleship", s.getName());
    assertTrue(s.occupiesCoordinates(new Coordinate(1, 3)));
    assertTrue(s.occupiesCoordinates(new Coordinate(2, 2)));
    assertTrue(s.occupiesCoordinates(new Coordinate(2, 3)));
    assertTrue(s.occupiesCoordinates(new Coordinate(2, 4)));
    assertFalse(s.occupiesCoordinates(new Coordinate(1, 2)));
    assertFalse(s.occupiesCoordinates(new Coordinate(1, 4)));
  }

  @Test
  public void test_makeBattleship_R() {
    V2ShipFactory f = new V2ShipFactory();
    Ship<Character> s = f.makeBattleship(new Placement("B2R"));
    assertTrue(s.occupiesCoordinates(new Coordinate(1, 2)));
    assertTrue(s.occupiesCoordinates(new Coordinate(2, 2)));
    assertTrue(s.occupiesCoordinates(new Coordinate(2, 3)));
    assertTrue(s.occupiesCoordinates(new Coordinate(3, 2)));
    assertFalse(s.occupiesCoordinates(new Coordinate(1, 3)));
    assertFalse(s.occupiesCoordinates(new Coordinate(3, 3)));
  }

  @Test
  public void test_makeBattleship_D() {
    V2ShipFactory f = new V2ShipFactory();
    Ship<Character> s = f.makeBattleship(new Placement("B2D"));
    assertTrue(s.occupiesCoordinates(new Coordinate(1, 2)));
    assertTrue(s.occupiesCoordinates(new Coordinate(1, 3)));
    assertTrue(s.occupiesCoordinates(new Coordinate(1, 4)));
    assertTrue(s.occupiesCoordinates(new Coordinate(2, 3)));
    assertFalse(s.occupiesCoordinates(new Coordinate(2, 2)));
    assertFalse(s.occupiesCoordinates(new Coordinate(2, 4)));
  }

  @Test
  public void test_makeBattleship_L() {
    V2ShipFactory f = new V2ShipFactory();
    Ship<Character> s = f.makeBattleship(new Placement("B2L"));
    assertTrue(s.occupiesCoordinates(new Coordinate(1, 3)));
    assertTrue(s.occupiesCoordinates(new Coordinate(2, 2)));
    assertTrue(s.occupiesCoordinates(new Coordinate(2, 3)));
    assertTrue(s.occupiesCoordinates(new Coordinate(3, 3)));
    assertFalse(s.occupiesCoordinates(new Coordinate(1, 2)));
    assertFalse(s.occupiesCoordinates(new Coordinate(3, 2)));
  }

  @Test
  public void test_makeSubmarine_V() {
    V2ShipFactory f = new V2ShipFactory();
    Ship<Character> s = f.makeSubmarine(new Placement("A0V"));
    assertEquals("Submarine", s.getName());
    assertTrue(s.occupiesCoordinates(new Coordinate(0, 0)));
    assertTrue(s.occupiesCoordinates(new Coordinate(1, 0)));
    assertFalse(s.occupiesCoordinates(new Coordinate(0, 1)));
  }

  @Test
  public void test_makeSubmarine_H() {
    V2ShipFactory f = new V2ShipFactory();
    Ship<Character> s = f.makeSubmarine(new Placement("A0H"));
    assertTrue(s.occupiesCoordinates(new Coordinate(0, 0)));
    assertTrue(s.occupiesCoordinates(new Coordinate(0, 1)));
    assertFalse(s.occupiesCoordinates(new Coordinate(1, 0)));
  }

  @Test
  public void test_makeDestroyer_V() {
    V2ShipFactory f = new V2ShipFactory();
    Ship<Character> s = f.makeDestroyer(new Placement("B2V"));
    assertEquals("Destroyer", s.getName());
    assertTrue(s.occupiesCoordinates(new Coordinate(1, 2)));
    assertTrue(s.occupiesCoordinates(new Coordinate(2, 2)));
    assertTrue(s.occupiesCoordinates(new Coordinate(3, 2)));
    assertFalse(s.occupiesCoordinates(new Coordinate(1, 3)));
  }

  @Test
  public void test_makeDestroyer_H() {
    V2ShipFactory f = new V2ShipFactory();
    Ship<Character> s = f.makeDestroyer(new Placement("B2H"));
    assertTrue(s.occupiesCoordinates(new Coordinate(1, 2)));
    assertTrue(s.occupiesCoordinates(new Coordinate(1, 3)));
    assertTrue(s.occupiesCoordinates(new Coordinate(1, 4)));
    assertFalse(s.occupiesCoordinates(new Coordinate(2, 2)));
  }

  @Test
  public void test_makeStandardShips_with_V2_orientations() {
    V2ShipFactory f = new V2ShipFactory();
    assertThrows(IllegalArgumentException.class, () -> f.makeSubmarine(new Placement("A0U")));
    assertThrows(IllegalArgumentException.class, () -> f.makeDestroyer(new Placement("A0R")));
  }

  @Test
  public void test_makeBattleship_invalid_direction() {
    V2ShipFactory f = new V2ShipFactory();
    assertThrows(IllegalArgumentException.class, () -> f.makeBattleship(new Placement("A0V")));
  }

  @Test
  public void test_makeCarrier_invalid_direction() {
    V2ShipFactory f = new V2ShipFactory();
    assertThrows(IllegalArgumentException.class, () -> f.makeCarrier(new Placement("A0H")));
  }
}
