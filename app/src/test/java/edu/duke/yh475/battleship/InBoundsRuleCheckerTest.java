package edu.duke.yh475.battleship;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class InBoundsRuleCheckerTest {
  @Test
  public void test_checkmyrules() {
    Board<Character> board = new BattleShipBoard<>(10, 20);
    PlacementRuleChecker<Character> checker = new InBoundsRuleChecker<>(null);
    Ship<Character> s1 = new RectangleShip<>("submarine", new Coordinate(0, 0), 1, 3, 's', '*');
    assertNull(checker.checkPlacement(s1, board));

    Ship<Character> s2 = new RectangleShip<>("submarine", new Coordinate(20, 0), 1, 3, 's', '*');
    assertEquals("That placement is invalid: the ship goes off the bottom of the board.", checker.checkPlacement(s2, board));

    Ship<Character> s4 = new RectangleShip<>("submarine", new Coordinate(-1, 0), 2, 1, 's', '*');
    assertEquals("That placement is invalid: the ship goes off the top of the board.", checker.checkPlacement(s4, board));

    Ship<Character> s5 = new RectangleShip<>("submarine", new Coordinate(19, 9), 1, 1, 's', '*');
    assertNull(checker.checkPlacement(s5, board));

    Ship<Character> s6 = new RectangleShip<>("submarine", new Coordinate(19, 29), 1, 1, 's', '*');
    assertEquals("That placement is invalid: the ship goes off the right of the board.", checker.checkPlacement(s6, board));

    Ship<Character> s7 = new RectangleShip<>("submarine", new Coordinate(19, -1), 1, 1, 's', '*');
    assertEquals("That placement is invalid: the ship goes off the left of the board.", checker.checkPlacement(s7, board));


  }

  @Test
  public void test_in_bound() { 
   InBoundsRuleChecker<Character> checker = new InBoundsRuleChecker<>(null);
   Board<Character> board = new BattleShipBoard<>(10, 20, checker, 'X');
    V1ShipFactory factory = new V1ShipFactory();
    Ship<Character> s1 = factory.makeSubmarine(new Placement("A0V"));

    assertNull(checker.checkPlacement(s1, board));

    Ship<Character> s2 = factory.makeSubmarine(new Placement("Z0V"));
    assertEquals("That placement is invalid: the ship goes off the bottom of the board.", checker.checkPlacement(s2, board));

  }
}
