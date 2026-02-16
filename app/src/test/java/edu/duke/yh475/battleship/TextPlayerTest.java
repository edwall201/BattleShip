package edu.duke.yh475.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    sb.append("S\n").append("S\n").append("S\n"); 
    sb.append("S\n").append("F\n").append("A0\n");
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

}
