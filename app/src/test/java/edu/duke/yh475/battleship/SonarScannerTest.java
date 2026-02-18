package edu.duke.yh475.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;

public class SonarScannerTest {
  @Test
  public void test_SonarScanner_success() {
    BattleShipBoard<Character> board = new BattleShipBoard<>(10, 20);
    V2ShipFactory factory = new V2ShipFactory();
    board.tryAddShip(factory.makeSubmarine(new Placement("K4H")));
    board.tryAddShip(factory.makeCarrier(new Placement("I4L")));
    SonarScanner<Character> scanner = new SonarScanner<>(board);
    Map<String, Integer> result = scanner.scan(new Coordinate("J5"));
    assertEquals(2, result.get("Submarine"));
    assertEquals(6, result.get("Carrier"));
  }

  public void test_SonarScanner_invalid() {
    BattleShipBoard<Character> board = new BattleShipBoard<>(10, 20);
    V2ShipFactory factory = new V2ShipFactory();
    board.tryAddShip(factory.makeSubmarine(new Placement("K4H")));
    board.tryAddShip(factory.makeCarrier(new Placement("I4L")));
    SonarScanner<Character> scanner = new SonarScanner<>(board);
    Map<String, Integer> result = scanner.scan(new Coordinate("E0"));
    assertEquals(0, result.get("Submarine"));
    assertEquals(0, result.get("Destroyer"));
    assertEquals(0, result.get("Battleship"));
    assertEquals(0, result.get("Carrier"));
  }

  @Test
  public void test_sonar_scanner_outbound() {
    BattleShipBoard<Character> board = new BattleShipBoard<>(10, 10);
    SonarScanner<Character> scanner = new SonarScanner<>(board);
    Map<String, Integer> result1 = scanner.scan(new Coordinate(-1, 5));
    Map<String, Integer> result2 = scanner.scan(new Coordinate(10, 5));
    Map<String, Integer> result4 = scanner.scan(new Coordinate(5, 10));
    Map<String, Integer> result3 = scanner.scan(new Coordinate(5, -1));
    assertEquals(0, result1.get("Submarine"));
    assertEquals(0, result2.get("Destroyer"));
    assertEquals(0, result3.get("Battleship"));
    assertEquals(0, result4.get("Carrier"));
  }

  @Test
  public void test_sonar_scanning_T_shape() {
    BattleShipBoard<Character> board = new BattleShipBoard<>(10, 20);
    V2ShipFactory factory = new V2ShipFactory();
    board.tryAddShip(factory.makeBattleship(new Placement("B2U")));
    SonarScanner<Character> scanner = new SonarScanner<>(board);
    Map<String, Integer> res = scanner.scan(new Coordinate("B2"));

    assertEquals(4, res.get("Battleship"));
  }

}
