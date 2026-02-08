package edu.duke.yh475.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class NoCollisionRuleCheckerTest {
  @Test
  public void test_collision_basic() {
    Board<Character> board = new BattleShipBoard<>(10, 10);
    PlacementRuleChecker<Character> checker = new NoCollisionRuleChecker<>(new InBoundsRuleChecker<>(null));
    V1ShipFactory factory = new V1ShipFactory();
    Ship<Character> s1 = factory.makeCarrier(new Placement("A0V"));
    board.tryAddShip(s1);

    Ship<Character> s2 = factory.makeDestroyer(new Placement("A0V"));
    board.tryAddShip(s2);
    assertFalse(checker.checkPlacement(s2, board));

    Ship<Character> s3 = factory.makeDestroyer(new Placement("A0H"));
    Ship<Character> s4 = factory.makeDestroyer(new Placement("B5V"));
    assertTrue(checker.checkPlacement(s4, board));
      
  }

}
