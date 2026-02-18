package edu.duke.yh475.battleship;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

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

    assertTrue(output.contains("Player Computer hit your Submarine at A0!"));
    assertTrue(output.contains("Player Computer missed!"));
    assertEquals("Computer", cp.getName());
    assertNotNull(cp.getBoard());
    assertEquals(20, cp.getBoard().getHeight());
    assertNotNull(cp.getView());
    assertFalse(cp.isLost());
  }
}
