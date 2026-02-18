package edu.duke.yh475.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

public class TextPlayerTest {

  private TextPlayer createTextPlayer(int w, int h, String inputData, OutputStream bytes) {
    BufferedReader input = new BufferedReader(new StringReader(inputData));
    PrintStream output = new PrintStream(bytes, true);
    Board<Character> board = new BattleShipBoard<Character>(w, h);
    V1ShipFactory shipFactory = new V1ShipFactory();
    return new TextPlayer("A", board, input, output, shipFactory);
  }

  @Test
  public void test_read_placement() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer(10, 20, "B2V\nC8H\na4v\n", bytes);
    String prompt = "Please enter a location for a ship:";
    Placement p = player.readPlacement(prompt);
    assertEquals(new Coordinate(1, 2), p.getWhere());
    assertEquals('V', p.getOrientation());
    assertEquals(prompt + "\n", bytes.toString());
  }

  @Test
  public void test_do_one_placement() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer(10, 20, "A0V\n", bytes);
    V1ShipFactory shipFactory = new V1ShipFactory();
    player.doOnePlacement("Destroyer", shipFactory::makeDestroyer);
    String response = bytes.toString();
    assertTrue(response.contains("Player A where do you want to place a Destroyer?"));
    assertTrue(response.contains("D"));
  }

  @Test
  public void test_read_placement_EOF() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer(10, 20, "", bytes);
    EOFException thrown = assertThrows(EOFException.class, () -> {
      player.readPlacement("Prompt");
    });

    String expectedMsg = "Player A failed to enter a placement";
    assertEquals(expectedMsg, thrown.getMessage());
  }

  @Test
  public void test_doOnePlacement_reaches_all_lines() throws IOException {

    String inputData = "A1\nZ0V\nA0V\n";

    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer(10, 20, inputData, bytes);
    V1ShipFactory factory = new V1ShipFactory();

    player.doOnePlacement("Destroyer", (p) -> factory.makeDestroyer(p));

    String output = bytes.toString();

    assertTrue(output.contains("it does not have the correct format."));
    assertTrue(output.contains("That placement is invalid: the ship goes off the bottom"));
  }

  @Test
  public void test_doPlacementPhase() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 10; i++) {
      sb.append((char) ('A' + i)).append("0H\n");
    }
    String inputData = sb.toString();
    TextPlayer player = createTextPlayer(10, 20, inputData, bytes);
    player.doPlacementPhase();
    String output = bytes.toString();
    assertTrue(output.contains("Player A"));
    assertTrue(output.contains("where do you want to place a Carrier?"));
    assertTrue(output.contains("\"Battleships\" that are now shaped as shown below"));
  }

  @Test
  public void test_playoneturn_invalid() throws IOException {
    Board<Character> b1 = new BattleShipBoard<>(10, 20);
    Board<Character> b2 = new BattleShipBoard<>(10, 20);
    AbstractShipFactory<Character> f = new V2ShipFactory();
    Ship<Character> ship = f.makeSubmarine(new Placement("A0H"));
    b2.tryAddShip(ship);

    BufferedReader input = new BufferedReader(new StringReader("ZZ\nF\nB0\n"));
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes);

    TextPlayer p1 = new TextPlayer("A", b1, input, out, f);
    BoardTextView enemyView = new BoardTextView(b2);
    p1.playOneTurn(b2, enemyView, "My Ocean", "Enemy Ocean");

    String output = bytes.toString();
    assertTrue(output.contains("That action is invalid"));
    assertTrue(output.contains("You missed!"));

  }

  @Test
  public void test_playoneturn_EOF() throws IOException {
    Board<Character> b1 = new BattleShipBoard<>(10, 20);
    Board<Character> b2 = new BattleShipBoard<>(10, 20);
    AbstractShipFactory<Character> f = new V1ShipFactory();

    BufferedReader input = new BufferedReader(new StringReader(""));
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes);

    TextPlayer p1 = new TextPlayer("A", b1, input, out, f);
    BoardTextView enemyView = new BoardTextView(b2);
    EOFException thrown = assertThrows(EOFException.class, () -> {
      p1.playOneTurn(b2, enemyView, "My Ocean", "Enemy Ocean");
    });
    assertEquals("Input ended unexpectedly", thrown.getMessage());
  }

  @Test
  public void test_playOneTurn_valid_fire() throws IOException {
    Board<Character> b1 = new BattleShipBoard<>(10, 20);
    Board<Character> b2 = new BattleShipBoard<>(10, 20);
    AbstractShipFactory<Character> f = new V1ShipFactory();
    Ship<Character> ship = f.makeSubmarine(new Placement("A0H"));
    b2.tryAddShip(ship);

    BufferedReader input = new BufferedReader(new StringReader("F\nA0\n"));
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes);

    TextPlayer p1 = new TextPlayer("A", b1, input, out, f);
    BoardTextView enemyView = new BoardTextView(b2);
    p1.playOneTurn(b2, enemyView, "My Ocean", "Enemy Ocean");

    String output = bytes.toString();

    assertTrue(output.contains("Possible actions for Player A:"));
    assertTrue(output.contains("You hit a Submarine!"));
  }

  @Test
  public void test_moveCount_invalid() throws IOException {
    Board<Character> b1 = new BattleShipBoard<>(10, 20);
    Board<Character> b2 = new BattleShipBoard<>(10, 20);
    V2ShipFactory f = new V2ShipFactory();
    Ship<Character> sub = f.makeSubmarine(new Placement("A0H"));
    b1.tryAddShip(sub);
    StringBuilder sb = new StringBuilder();
    sb.append("M\nA0\nB0H\n");
    sb.append("M\nB0\nC0H\n");
    sb.append("M\nC0\nD0H\n");
    sb.append("M\nF\nE0\n");

    BufferedReader input = new BufferedReader(new StringReader(sb.toString()));
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes);

    TextPlayer player = new TextPlayer("A", b1, input, out, f);
    BoardTextView enemyView = new BoardTextView(b2);

    for (int i = 0; i < 4; i++) {
      player.playOneTurn(b2, enemyView, "My", "Enemy");
    }

    String output = bytes.toString();

    assertTrue(output.contains("That action is invalid: no move actions remaining."));
    assertTrue(output.contains("You missed!"));
  }

  @Test
  public void test_playOneTurn_sonar_coverage_and_exhaustion() throws IOException {
    Board<Character> b1 = new BattleShipBoard<>(10, 20);
    Board<Character> b2 = new BattleShipBoard<>(10, 20);
    V2ShipFactory f = new V2ShipFactory();
    StringBuilder sb = new StringBuilder();
    sb.append("S\nE5\n");
    sb.append("S\nE5\n").append("S\nE5\n");
    sb.append("S\nE5\n").append("F\n").append("A0\n");
    BufferedReader input = new BufferedReader(new StringReader(sb.toString()));
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes);
    TextPlayer player = new TextPlayer("A", b1, input, out, f);
    BoardTextView enemyView = new BoardTextView(b2);

    for (int i = 0; i < 4; i++) {
      player.playOneTurn(b2, enemyView, "My", "Enemy");
    }

    String output = bytes.toString();

    assertTrue(output.contains("That action is invalid: no sonar actions remaining."));
    assertTrue(output.contains("You missed!"));
  }

  @Test
  public void test_playOneTurn_auto_fire() throws IOException {
    Board<Character> b1 = new BattleShipBoard<>(10, 20);
    Board<Character> b2 = new BattleShipBoard<>(10, 20);
    V2ShipFactory f = new V2ShipFactory();

    b1.tryAddShip(f.makeSubmarine(new Placement("A0V")));
    b1.tryAddShip(f.makeSubmarine(new Placement("A1V")));
    b1.tryAddShip(f.makeSubmarine(new Placement("A2V")));

    String inputData = "M\nA0\nB0V\n" +
        "M\nA1\nB1V\n" +
        "M\nA2\nB2V\n" +
        "S\nE5\n" +
        "S\nE5\n" +
        "S\nE5\n" +
        "A0";
    BufferedReader input = new BufferedReader(new StringReader(inputData));
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes);
    TextPlayer player = new TextPlayer("A", b1, input, out, f);

    BoardTextView enemyView = new BoardTextView(b2);
    for (int i = 0; i < 7; i++) {
      player.playOneTurn(b2, enemyView, "My Ocean", "Enemy Ocean");
    }
    String output = bytes.toString();

    assertTrue(output.contains("input a coordinate to fire at"));
    assertTrue(output.contains("You missed!"));
  }

  @Test
  public void test_readCoordinate_all_paths() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    String inputData = "ZZ\nA0\n";
    TextPlayer player = createTextPlayer(10, 20, inputData, bytes);
    Coordinate coord = player.readCoordinate("Enter coord:");

    assertEquals(new Coordinate(0, 0), coord);
    String output = bytes.toString();
    assertTrue(output.contains("Enter coord:"));
    assertTrue(output.contains("Please try again."));
  }

  @Test
  public void test_readCoordinate_EOF() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer(10, 20, "", bytes);

    EOFException thrown = assertThrows(EOFException.class, () -> {
      player.readCoordinate("Prompt");
    });

    assertEquals("Input ended unexpectedly", thrown.getMessage());
  }

  @Test
  public void test_doMove_noShip() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes);
    Board<Character> board = new BattleShipBoard<>(10, 10);
    V2ShipFactory factory = new V2ShipFactory();

    StringReader sr = new StringReader("A0\n");
    BufferedReader br = new BufferedReader(sr);
    TextPlayer player = new TextPlayer("A", board, br, out, factory);

    boolean result = player.doMove();

    assertFalse(result);
    assertTrue(bytes.toString().contains("There is no ship at (0, 0)"));
  }

  @Test
  public void test_doMove_invalidDest() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    Board<Character> board = new BattleShipBoard<>(10, 10);
    V2ShipFactory factory = new V2ShipFactory();
    Ship<Character> s1 = factory.makeSubmarine(new Placement("A0V"));
    Ship<Character> obstacle = factory.makeSubmarine(new Placement("D0V"));
    board.tryAddShip(s1);
    board.tryAddShip(obstacle);

    StringReader sr = new StringReader("A0\nD0V\n");
    BufferedReader br = new BufferedReader(sr);
    TextPlayer player = new TextPlayer("A", board, br, new PrintStream(bytes), factory);

    boolean result = player.doMove();

    assertFalse(result);
    assertSame(s1, board.getShipAt(new Coordinate("A0")));
    assertTrue(bytes.toString().contains("Move failed"));
  }

  @Test
  public void test_doMove_success() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    Board<Character> board = new BattleShipBoard<>(10, 10);
    V2ShipFactory factory = new V2ShipFactory();

    Ship<Character> s = factory.makeSubmarine(new Placement("A0V"));
    board.tryAddShip(s);
    s.recordHitAt(0);

    StringReader sr = new StringReader("A0\nB5H\n");
    BufferedReader br = new BufferedReader(sr);
    TextPlayer player = new TextPlayer("A", board, br, new PrintStream(bytes), factory);

    boolean result = player.doMove();

    assertTrue(result);
    Ship<Character> moved = board.getShipAt(new Coordinate("B5"));
    assertNotNull(moved);
    assertTrue(moved.wasHitAt(0));
    assertNull(board.getShipAt(new Coordinate("A0")));
  }

  @Test
  public void test_doMove_failure_coverage() throws IOException {
    Board<Character> b1 = new BattleShipBoard<>(10, 20);
    Board<Character> b2 = new BattleShipBoard<>(10, 20);
    V2ShipFactory f = new V2ShipFactory();
    String inputData = "M\nA0\nF\nD5\nS\nE5\n";
    BufferedReader input = new BufferedReader(new StringReader(inputData));

    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = new TextPlayer("A", b1, input, new PrintStream(bytes), f);
    BoardTextView enemyView = new BoardTextView(b2);

    player.playOneTurn(b2, enemyView, "My", "Enemy");
    player.playOneTurn(b2, enemyView, "My", "Enemy");
    String output = bytes.toString();
    assertTrue(output.contains("Error: There is no ship at (0, 0)! Please choose an action again."));
    assertTrue(output.contains("You missed!"));
  }

  @Test
  public void test_doSonar_output_format() throws IOException {
    BattleShipBoard<Character> enemyBoard = new BattleShipBoard<>(10, 20);
    V2ShipFactory factory = new V2ShipFactory();
    enemyBoard.tryAddShip(factory.makeSubmarine(new Placement("B2H")));

    String inputData = "C2\n";
    BufferedReader input = new BufferedReader(new StringReader(inputData));

    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes);

    TextPlayer player = new TextPlayer("A", new BattleShipBoard<>(10, 20), input, out, factory);
    player.doSonar(enemyBoard);

    String output = bytes.toString();
    String expectedHeader = "---------------------------------------------------------------------------";

    assertTrue(output.contains("where do you want to center the sonar scan?"));
    assertTrue(output.contains(expectedHeader));
    assertTrue(output.contains("Submarines occupy 2 squares"));
    assertTrue(output.contains("Destroyers occupy 0 squares"));
    assertTrue(output.contains("Battleships occupy 0 squares"));
    assertTrue(output.contains("Carriers occupy 0 squares"));

    String[] lines = output.split("\n");
    assertEquals(expectedHeader, lines[lines.length - 1].trim());
  }

  @Test
  public void test_doMove_invalid_to() throws IOException {
    Board<Character> board = new BattleShipBoard<>(10, 20);
    V2ShipFactory f = new V2ShipFactory();
    board.tryAddShip(f.makeSubmarine(new Placement("A0V")));
    String inputData = "M\nA0\nf\nC0V\n";
    BufferedReader input = new BufferedReader(new StringReader(inputData));
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes);
    
    TextPlayer player = new TextPlayer("A", board, input, out, f);
    BoardTextView enemyView = new BoardTextView(new BattleShipBoard<>(10, 20));

    player.playOneTurn(new BattleShipBoard<>(10, 20), enemyView, "My", "Enemy");

    String output = bytes.toString();

    assertTrue(output.contains("That placement is invalid"));
    assertTrue(output.contains("Please try again"));
    
    assertTrue(output.contains("Ship moved successfully!"));
    
    assertNull(board.getShipAt(new Coordinate("A0")));
    assertNotNull(board.getShipAt(new Coordinate("C0")));
  }

  @Test
  public void test_doFire_outOfBounds() throws IOException {
    String inputData = "z4\na0\n";
    BufferedReader input = new BufferedReader(new StringReader(inputData));
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes);
    
    Board<Character> board = new BattleShipBoard<>(10, 20);
    V2ShipFactory factory = new V2ShipFactory();
    TextPlayer player = new TextPlayer("A", board, input, out, factory);
    
    Board<Character> enemyBoard = new BattleShipBoard<>(10, 20);
    player.doFire(enemyBoard);

    String output = bytes.toString();
    assertTrue(output.contains("is out of bounds"));
    assertTrue(output.contains("You missed!"));
  }
}
