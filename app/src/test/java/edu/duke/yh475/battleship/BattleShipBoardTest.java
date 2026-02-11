package edu.duke.yh475.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BattleShipBoardTest {
  private <T> void checkWhatIsAtBoard(Board<T> b, Character[][] expected) {
    assertEquals(expected.length, b.getHeight());
    assertEquals(expected[0].length, b.getWidth());
    for (int i = 0; i < b.getHeight(); i++) {
      for (int j = 0; j < b.getWidth(); j++) {
        Coordinate coord = new Coordinate(i, j);
        assertEquals(expected[i][j], b.whatIsAt(coord));
      }
    }
  }

  @Test
  public void test_width_and_height() {
    Board<Character> b1 = new BattleShipBoard<Character>(10, 20);
    assertEquals(10, b1.getWidth());
    assertEquals(20, b1.getHeight());
  }

  @Test
  public void test_invalid_dimensions() {
    assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard<Character>(10, 0));
    assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard<Character>(0, 20));
    assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard<Character>(10, -5));
    assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard<Character>(-8, 20));
  }

  @Test
  public void test_whatIsAt() {
    BattleShipBoard<Character> b = new BattleShipBoard<>(3, 5);
    Character[][] expected = new Character[5][3];
    checkWhatIsAtBoard(b, expected);

    Coordinate c1 = new Coordinate(1, 0);
    RectangleShip<Character> s1 = new RectangleShip<Character>(c1, 's', 'X');
    assertEquals(null, b.tryAddShip(s1));
    Coordinate c2 = new Coordinate(1, 0);
    expected[1][0] = 's';

    checkWhatIsAtBoard(b, expected);
  }

  @Test
  public void test_whatIsAt_invalid_outbound() {
    BattleShipBoard<Character> b = new BattleShipBoard<>(3, 5);
    V1ShipFactory factory = new V1ShipFactory();
    Ship<Character> s1 = factory.makeSubmarine(new Placement("A8H"));
    assertEquals("That placement is invalid: the ship goes off the right of the board.", b.tryAddShip(s1));
  }

 @Test
 public void test_whatIsAt_invalid_collosion() {
    BattleShipBoard<Character> b = new BattleShipBoard<>(10, 10);
    V1ShipFactory factory = new V1ShipFactory();
    Ship<Character> s1 = factory.makeSubmarine(new Placement("A2H"));
    b.tryAddShip(s1);
    Ship<Character> s2 = factory.makeSubmarine(new Placement("A2H"));
    assertEquals("That placement is invalid: the ship overlaps another ship.", b.tryAddShip(s2));

 }
}
