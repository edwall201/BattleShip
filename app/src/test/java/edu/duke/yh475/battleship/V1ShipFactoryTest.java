package edu.duke.yh475.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class V1ShipFactoryTest {

  private void checkShip(Ship<Character>testShip, String expectedName, char expectedLetter, Coordinate ...expectedLocs){
    assertEquals(expectedName, testShip.getName());
    for(Coordinate c: expectedLocs){
      assertTrue(testShip.occupiesCoordinates(c));
      assertEquals(expectedLetter, testShip.getDisplayInfoAt(c, true));
    }
  }
  @Test
  public void test_makeSubmarine() {
    V1ShipFactory factory = new V1ShipFactory();
    Placement v1 = new Placement(new Coordinate(1,2), 'v');
    Ship<Character> sub = factory.makeSubmarine(v1);
    checkShip(sub, "Submarine", 's', new Coordinate(1,2), new Coordinate(2, 2));

    Ship<Character> sub2 = factory.makeSubmarine(new Placement(new Coordinate(1, 2), 'H'));
    checkShip(sub2, "Submarine", 's', new Coordinate(1,2), new Coordinate(1,3));
  }

  @Test
  public void test_makeBattleShip() {
    V1ShipFactory factory = new V1ShipFactory();
    Placement v1 = new Placement(new Coordinate(1,2), 'v');
    Ship<Character> sub = factory.makeBattleship(v1);
    checkShip(sub, "BattleShip", 'b', new Coordinate(1,2), new Coordinate(2, 2));

    Ship<Character> sub2 = factory.makeBattleship(new Placement(new Coordinate(1, 2), 'H'));
    checkShip(sub2, "BattleShip", 'b', new Coordinate(1,2), new Coordinate(1,3));
  }

  @Test
  public void test_makeDestroyer() {
    V1ShipFactory factory = new V1ShipFactory();
    Placement v1 = new Placement(new Coordinate(1,2), 'v');
    Ship<Character> sub = factory.makeDestroyer(v1);
    checkShip(sub, "Destroy", 'd', new Coordinate(1,2), new Coordinate(2, 2));

    Ship<Character> sub2 = factory.makeDestroyer(new Placement(new Coordinate(1, 2), 'H'));
    checkShip(sub2, "Destroy", 'd', new Coordinate(1,2), new Coordinate(1,3));
  }

  @Test
  public void test_makeCarrier() {
    V1ShipFactory factory = new V1ShipFactory();
    Placement v1 = new Placement(new Coordinate(1,2), 'v');
    Ship<Character> sub = factory.makeCarrier(v1);
    checkShip(sub, "Carrier", 'c', new Coordinate(1,2), new Coordinate(2, 2));

    Ship<Character> sub2 = factory.makeCarrier(new Placement(new Coordinate(1, 2), 'H'));
    checkShip(sub2, "Carrier", 'c', new Coordinate(1,2), new Coordinate(1,3));
  }

  
  
}
