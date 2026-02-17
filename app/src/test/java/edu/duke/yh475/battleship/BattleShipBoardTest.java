package edu.duke.yh475.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class BattleShipBoardTest {
  private <T> void checkwhatIsAtForSelfBoard(Board<T> b, Character[][] expected) {
    assertEquals(expected.length, b.getHeight());
    assertEquals(expected[0].length, b.getWidth());
    for (int i = 0; i < b.getHeight(); i++) {
      for (int j = 0; j < b.getWidth(); j++) {
        Coordinate coord = new Coordinate(i, j);
        assertEquals(expected[i][j], b.whatIsAtForSelf(coord));
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
  public void test_whatIsAtForSelf() {
    BattleShipBoard<Character> b = new BattleShipBoard<>(3, 5);
    Character[][] expected = new Character[5][3];
    checkwhatIsAtForSelfBoard(b, expected);

    Coordinate c1 = new Coordinate(1, 0);
    RectangleShip<Character> s1 = new RectangleShip<Character>(c1, 's', 'X');
    assertEquals(null, b.tryAddShip(s1));
    Coordinate c2 = new Coordinate(1, 0);
    expected[1][0] = 's';

    checkwhatIsAtForSelfBoard(b, expected);
  }

  @Test
  public void test_whatIsAtForSelf_invalid_outbound() {
    BattleShipBoard<Character> b = new BattleShipBoard<>(3, 5);
    V1ShipFactory factory = new V1ShipFactory();
    Ship<Character> s1 = factory.makeSubmarine(new Placement("A8H"));
    assertEquals("That placement is invalid: the ship goes off the right of the board.", b.tryAddShip(s1));
  }

  @Test
  public void test_whatIsAtForSelf_invalid_collosion() {
    BattleShipBoard<Character> b = new BattleShipBoard<>(10, 10);
    V1ShipFactory factory = new V1ShipFactory();
    Ship<Character> s1 = factory.makeSubmarine(new Placement("A2H"));
    b.tryAddShip(s1);
    Ship<Character> s2 = factory.makeSubmarine(new Placement("A2H"));
    assertEquals("That placement is invalid: the ship overlaps another ship.", b.tryAddShip(s2));
  }

  @Test
  public void test_whatIsAtForEnemy(){
    BattleShipBoard<Character> b = new BattleShipBoard<>(10, 20);
    V1ShipFactory factory = new V1ShipFactory();
    Ship<Character> s1 = factory.makeSubmarine(new Placement("A0H"));
    b.tryAddShip(s1);
    assertNull(b.whatIsAtForEnemy(new Coordinate(0, 0)));
    assertNull(b.whatIsAtForEnemy(new Coordinate(6, 8)));

    b.fireAt(new Coordinate(0, 0));
    assertEquals('s', b.whatIsAtForEnemy(new Coordinate(0, 0)));
    assertNull(b.whatIsAtForEnemy(new Coordinate(0, 1)));
    b.fireAt(new Coordinate(2, 2));
    assertEquals('X', b.whatIsAtForEnemy(new Coordinate(2, 2)));
  }

  @Test
  public void test_isLost(){
    BattleShipBoard<Character> b = new BattleShipBoard<>(10, 20);
    V1ShipFactory factory = new V1ShipFactory();
    Ship<Character> s1 = factory.makeSubmarine(new Placement("A0H"));
    b.tryAddShip(s1);
    assertFalse(b.isLost());
    b.fireAt(new Coordinate(0, 0));
    assertFalse(b.isLost());
    b.fireAt(new Coordinate(0, 1));
    assertTrue(b.isLost());
    
  }
  
  @Test
  public void test_V2Ships_outOfBounds_onBoard() {
    Board<Character> board = new BattleShipBoard<>(10, 20);
    V2ShipFactory f = new V2ShipFactory();

    Ship<Character> b = f.makeBattleship(new Placement("T0U"));
    assertEquals("That placement is invalid: the ship goes off the bottom of the board.", board.tryAddShip(b));

    Ship<Character> cR = f.makeCarrier(new Placement("A6R"));
    assertEquals("That placement is invalid: the ship goes off the right of the board.", board.tryAddShip(cR));

    Ship<Character> cU = f.makeCarrier(new Placement("Q0U"));
    assertEquals("That placement is invalid: the ship goes off the bottom of the board.", board.tryAddShip(cU));
    
  }
  
  @Test
  public void test_removeShip() {
    BattleShipBoard<Character> board = new BattleShipBoard<>(10, 10);
    V2ShipFactory factory = new V2ShipFactory();
    Ship<Character> sub = factory.makeSubmarine(new Placement("A0V"));
    board.tryAddShip(sub);
    assertEquals(sub, board.getShipAt(new Coordinate(0, 0)));
    board.removeShip(sub);
    assertNull(board.getShipAt(new Coordinate(0, 0)));
  }
}
