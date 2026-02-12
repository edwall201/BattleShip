package edu.duke.yh475.battleship;

import static org.junit.jupiter.api.Assertions.*;
import java.io.EOFException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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
          sb.append((char)('A' + i)).append("0H\n");
      }
      String inputData = sb.toString(); 
      TextPlayer player = createTextPlayer(10, 20, inputData, bytes);
      player.doPlacementPhase();
      String output = bytes.toString();
      assertTrue(output.contains("Player A"));
      assertTrue(output.contains("where do you want to place a Carrier?"));
  }

}

