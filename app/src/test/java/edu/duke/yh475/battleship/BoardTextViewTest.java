package edu.duke.yh475.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BoardTextViewTest {

  private void emptyBoardHelper(int w, int h, String expectedHeader, String expectedBody) {
    Board<Character> b1 = new BattleShipBoard<Character>(w, h);
    BoardTextView view = new BoardTextView(b1);
    assertEquals(expectedHeader, view.makeHeader());
    String expectedFullBoard = expectedHeader + expectedBody + expectedHeader;
    assertEquals(expectedFullBoard, view.displayMyOwnBoard());
  }

  @Test
  public void test_display_empty_2by2() {
    String expectedHeader = "  0|1\n";
    String expected =
        "A  |  A\n" +
        "B  |  B\n";
    emptyBoardHelper(2, 2, expectedHeader, expected);
  }

  @Test
  public void test_invalid_board_size() {
    Board<Character> wideBoard = new BattleShipBoard<Character>(11, 20);
    Board<Character> tallBoard = new BattleShipBoard<Character>(10, 27);
    assertThrows(IllegalArgumentException.class, () -> new BoardTextView(wideBoard));
    assertThrows(IllegalArgumentException.class, () -> new BoardTextView(tallBoard));
  }

  @Test
  public void test_getToDisplay() {
    Board<Character> b1 = new BattleShipBoard<Character>(10, 20);
    BoardTextView view = new BoardTextView(b1);
    assertEquals(b1, view.getToDisplay());
  }

  @Test
  public void test_display_empty_3by2() {
    String header = "  0|1|2\n";
    String body = "A  | |  A\n" +
        "B  | |  B\n";
    emptyBoardHelper(3, 2, header, body);
  }

  @Test
  public void test_display_empty_3by5() {
    String header = "  0|1|2\n";
    String body = "A  | |  A\n" +
        "B  | |  B\n" +
        "C  | |  C\n" +
        "D  | |  D\n" +
        "E  | |  E\n";
    emptyBoardHelper(3, 5, header, body);
  }


  @Test
  public void test_display_empty_3by5_with_ship() {
    BattleShipBoard<Character> b = new BattleShipBoard<>(3, 5);
    BoardTextView view = new BoardTextView(b);
    RectangleShip<Character> sh = new RectangleShip<Character>(new Coordinate(1, 0), 's', 'X');
    b.tryAddShip(sh);
    String body =
      "  0|1|2\n" +
      "A  | |  A\n" +
      "B s| |  B\n" +
      "C  | |  C\n" +
      "D  | |  D\n" +
      "E  | |  E\n" +
      "  0|1|2\n";
   assertEquals(view.displayMyOwnBoard(), body);
  }

  @Test
  public void test_displayEnemyBoard() {
    BattleShipBoard<Character> b = new BattleShipBoard<>(4, 3);
    V1ShipFactory factory = new V1ShipFactory();
    BoardTextView view = new BoardTextView(b);

    Ship<Character> sub = factory.makeSubmarine(new Placement("B0H"));
    b.tryAddShip(sub);
    String myView = 
        "  0|1|2|3\n" +
        "A  | | |  A\n" +
        "B s|s| |  B\n" +
        "C  | | |  C\n" +
        "  0|1|2|3\n";
    assertEquals(myView, view.displayMyOwnBoard());

    String enemyViewStart = 
        "  0|1|2|3\n" +
        "A  | | |  A\n" +
        "B  | | |  B\n" +
        "C  | | |  C\n" +
        "  0|1|2|3\n";
    assertEquals(enemyViewStart, view.displayEnemyBoard());
    b.fireAt(new Coordinate(1, 0)); 
    b.fireAt(new Coordinate(0, 0));

    String enemyViewAfter = 
        "  0|1|2|3\n" +
        "A X| | |  A\n" + 
        "B s| | |  B\n" +
        "C  | | |  C\n" +
        "  0|1|2|3\n";
    assertEquals(enemyViewAfter, view.displayEnemyBoard());
  }
}
