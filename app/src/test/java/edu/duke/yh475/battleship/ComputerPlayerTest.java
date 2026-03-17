package edu.duke.yh475.battleship;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class ComputerPlayerTest {
  @Test
  public void test_computer_play_one_turn() throws IOException {
    Board<Character> b1 = new BattleShipBoard<>(10, 20);
    V2ShipFactory factory = new V2ShipFactory();
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes);

    ComputerPlayer cp = new ComputerPlayer("Computer", b1, out, factory);
    Board<Character> enemyBoard = new BattleShipBoard<>(10, 20);
    cp.playOneTurn(enemyBoard, new BoardTextView(enemyBoard), "My", "Enemy");
    String output = bytes.toString();
    assertTrue(output.contains("Player Computer missed!"));
  }

  @Test
  public void test_computer_placement_and_turn() throws IOException {
    Board<Character> b = new BattleShipBoard<>(10, 20);
    V2ShipFactory f = new V2ShipFactory();
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes);

    ComputerPlayer cp = new ComputerPlayer("Robot", b, out, f);

    cp.doPlacementPhase();
    Board<Character> enemyBoard = new BattleShipBoard<>(10, 20);
    cp.playOneTurn(enemyBoard, null, null, null);

    String output = bytes.toString();
    assertTrue(output.contains("Player Robot missed!"));
  }

  @Test
  public void test_computer_fire() throws IOException {
    Board<Character> b = new BattleShipBoard<>(10, 20);
    V2ShipFactory f = new V2ShipFactory();
    Board<Character> enemyBoard = new BattleShipBoard<>(10, 20);

    Ship<Character> s = f.makeSubmarine(new Placement("A0H"));
    enemyBoard.tryAddShip(s);

    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes);
    ComputerPlayer cp = new ComputerPlayer("Computer", b, out, f);
    cp.doPlacementPhase();
    cp.playOneTurn(enemyBoard, null, null, null);

    for (int i = 0; i < 9; i++) {
      cp.playOneTurn(enemyBoard, null, null, null);
    }

    cp.playOneTurn(enemyBoard, null, null, null);
    String output = bytes.toString();

    // assertTrue(output.contains("Player Computer hit your Submarine at A0!"));
    assertTrue(output.contains("Player Computer missed!"));
    assertEquals("Computer", cp.getName());
    assertNotNull(cp.getBoard());
    assertEquals(20, cp.getBoard().getHeight());
    assertNotNull(cp.getView());
    assertFalse(cp.isLost());
  }

  @Test
  void test_playOneTurn_skips_fired_targets() throws IOException {
    Board<Character> board = new BattleShipBoard<>(10, 20);
    AbstractShipFactory<Character> factory = new V2ShipFactory();
    PrintStream out = new PrintStream(new ByteArrayOutputStream());
    ComputerPlayer player = new ComputerPlayer("Computer", board, out, factory);

    Coordinate a0 = new Coordinate(0, 0);
    Coordinate a1 = new Coordinate(0, 1);
    player.firedCoordinates.add(a0);
    player.targetStack.push(a1);
    player.targetStack.push(a0);
    player.playOneTurn(board, null, "", "");

    assertTrue(player.firedCoordinates.contains(a1));
    assertTrue(player.targetStack.isEmpty());
  }

  @Test
  void test_generateRandomCoordinate_retry() {
    Board<Character> board = new BattleShipBoard<>(2, 2);
    ComputerPlayer player = new ComputerPlayer("AI", board, null, null);

    player.firedCoordinates.add(new Coordinate(0, 0));
    player.firedCoordinates.add(new Coordinate(0, 1));
    player.firedCoordinates.add(new Coordinate(1, 0));
    Coordinate coord = player.generateRandomCoordinate(board);

    assertEquals(new Coordinate(1, 1), coord);
  }

  @Test
  void test_addNeighborsToStack() {
    Board<Character> board = new BattleShipBoard<Character>(10, 20);
    V2ShipFactory factory = new V2ShipFactory();
    ComputerPlayer player = new ComputerPlayer("Computer", board, System.out, factory);
    Coordinate center = new Coordinate(5, 5);
    player.addNeighborsToStack(center, board);
    List<Coordinate> expectedNeighbors = Arrays.asList(
        new Coordinate(4, 5), new Coordinate(6, 5),
        new Coordinate(5, 4), new Coordinate(5, 6));

    assertEquals(4, player.targetStack.size());
    for (Coordinate neighbor : expectedNeighbors) {
      assertTrue(player.targetStack.contains(neighbor));
    }

    player.targetStack.clear();
    Coordinate corner = new Coordinate(0, 0);
    player.addNeighborsToStack(corner, board);

    assertEquals(0, player.targetStack.size());
  }

  @Test
  void test_generateRandomCoordinate_hits() {
    Board<Character> board = new BattleShipBoard<Character>(2, 1);
    ComputerPlayer player = new ComputerPlayer("Computer", board, System.out, null);

    Coordinate c0 = new Coordinate(0, 0);
    player.firedCoordinates.add(c0);

    for (int i = 0; i < 10; i++) {
      Coordinate coord = player.generateRandomCoordinate(board);
      assertEquals(new Coordinate(0, 1), coord);
    }
  }

  @Test
  void test_addNeighborsToStack_skips_fired() {
    Board<Character> board = new BattleShipBoard<>(10, 20);
    ComputerPlayer player = new ComputerPlayer("Computer", board, System.out, null);
    Coordinate center = new Coordinate(5, 5);
    Coordinate neighbor = new Coordinate(4, 5);

    player.firedCoordinates.add(neighbor);

    player.addNeighborsToStack(center, board);
    assertEquals(3, player.targetStack.size());
    assertFalse(player.targetStack.contains(neighbor));
  }

  @Test
  void test_playOneTurn_hits_target() throws IOException {
    Board<Character> enemyBoard = new BattleShipBoard<>(10, 20);
    V2ShipFactory f = new V2ShipFactory();
    Ship<Character> s = f.makeSubmarine(new Placement("A0H"));
    enemyBoard.tryAddShip(s);

    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    ComputerPlayer cp = new ComputerPlayer("Computer", new BattleShipBoard<>(10, 20), new PrintStream(bytes), f);
    cp.targetStack.push(new Coordinate(0, 0));

    cp.playOneTurn(enemyBoard, null, null, null);

    String output = bytes.toString();
    assertTrue(output.contains("hit your Submarine at A0!"));
  }

  @Test
  void test_first_hit_adds_four_neighbors() {
    Board<Character> board = new BattleShipBoard<>(10, 20);
    ComputerPlayer cp = new ComputerPlayer("Computer", board, System.out, new V2ShipFactory());
    Coordinate hit = new Coordinate(5, 5);
    cp.addNeighborsToStack(hit, board);
    assertEquals(hit, cp.firstHit);
    assertEquals(4, cp.targetStack.size());
    List<Coordinate> expected = Arrays.asList(new Coordinate(4, 5), new Coordinate(6, 5),
        new Coordinate(5, 4), new Coordinate(5, 6));
    for (Coordinate c : expected) {
      assertTrue(cp.targetStack.contains(c));
    }
  }

  @Test
  void test_second_hit_horizontal_lock() {
    Board<Character> board = new BattleShipBoard<>(10, 20);
    ComputerPlayer cp = new ComputerPlayer("Computer", board, System.out, new V2ShipFactory());
    cp.addNeighborsToStack(new Coordinate(5, 5), board);

    cp.addNeighborsToStack(new Coordinate(5, 6), board);

    for (Coordinate c : cp.targetStack) {
      assertEquals(5, c.getRow());
    }
    assertTrue(cp.targetStack.contains(new Coordinate(5, 7)));
  }

  @Test
  void test_second_hit_vertical_lock() {
    Board<Character> board = new BattleShipBoard<>(10, 20);
    ComputerPlayer cp = new ComputerPlayer("Computer", board, System.out, new V2ShipFactory());

    cp.addNeighborsToStack(new Coordinate(5, 5), board);

    cp.addNeighborsToStack(new Coordinate(6, 5), board);

    for (Coordinate c : cp.targetStack) {
      assertEquals(5, c.getColumn());
    }
    assertTrue(cp.targetStack.contains(new Coordinate(7, 5)));
  }

  @Test
  void test_playOneTurn_reset_after_sinking() throws IOException {
    Board<Character> enemyBoard = new BattleShipBoard<>(10, 20);
    V2ShipFactory factory = new V2ShipFactory();
    Ship<Character> s = factory.makeSubmarine(new Placement("A0H"));
    enemyBoard.tryAddShip(s);

    ComputerPlayer cp = new ComputerPlayer("Computer", new BattleShipBoard<>(10, 20), System.out, factory);

    cp.targetStack.push(new Coordinate(0, 0));
    cp.playOneTurn(enemyBoard, null, null, null);
    cp.targetStack.push(new Coordinate(0, 1));
    cp.playOneTurn(enemyBoard, null, null, null);

    assertNull(cp.firstHit);
  }

  @Test
  void test_boundary_hit_at_0_0() {
    Board<Character> board = new BattleShipBoard<>(10, 20);
    ComputerPlayer cp = new ComputerPlayer("Computer", board, System.out, new V2ShipFactory());
    cp.addNeighborsToStack(new Coordinate(0, 0), board);
    assertEquals(2, cp.targetStack.size());
    assertTrue(cp.targetStack.contains(new Coordinate(1, 0)));
    assertTrue(cp.targetStack.contains(new Coordinate(0, 1)));
  }

  @Test
  void test_vertical_invalid() {
    Board<Character> board = new BattleShipBoard<>(10, 20);
    ComputerPlayer cp = new ComputerPlayer("Computer", board, System.out, new V2ShipFactory());
    cp.addNeighborsToStack(new Coordinate(5, 5), board);

    cp.addNeighborsToStack(new Coordinate(4, 5), board);

    assertTrue(cp.targetStack.contains(new Coordinate(3, 5)));
  }
  @Test
  void test_horizontal_invalid() {
    Board<Character> board = new BattleShipBoard<>(10, 20);
    ComputerPlayer cp = new ComputerPlayer("Computer", board, System.out, new V2ShipFactory());
    cp.addNeighborsToStack(new Coordinate(5, 5), board);

    cp.addNeighborsToStack(new Coordinate(5, 4), board);

    assertTrue(cp.targetStack.contains(new Coordinate(5, 3)));
  }
}
