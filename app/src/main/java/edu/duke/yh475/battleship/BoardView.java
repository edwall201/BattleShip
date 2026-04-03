package edu.duke.yh475.battleship;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.geometry.Pos;

/**
 * A javafx view for the battleship board.
 */
public class BoardView {
  private final GridPane grid;
  private final Board<Character> board;
  private final Button[][] cells;
  private boolean isEnemy;
  private ClickHandler placementHandler;

  /**
   * Constructor for the boardview
   * 
   * @param board   the board to create the view
   * @param isEnemy whether this is the enemy's view
   */
  public BoardView(Board<Character> board, boolean isEnemy) {
    this.board = board;
    this.isEnemy = isEnemy;
    this.grid = new GridPane();
    this.cells = new Button[board.getHeight()][board.getWidth()];
    setupGrid();
  }

  /**
   * Sets up the grid of buttons for the board view
   * initializes each button and adds it to the grid
   * also sets up the event handler for each button to handle firing at
   * coordinates when clicked
   */
  private void setupGrid() {

    // add column labels
    for (int col = 0; col < board.getWidth(); col++) {
      Label colLabel = new Label(String.valueOf(col));
      colLabel.setPrefSize(35, 35);
      colLabel.setAlignment(Pos.CENTER); // Center the text
      colLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #34495e; -fx-font-size: 14px;");
      grid.add(colLabel, col + 1, 0); // Shift right by 1
    }

    for (int row = 0; row < board.getHeight(); row++) {
      char rowChar = (char) ('A' + row);
      Label rowLabel = new Label(String.valueOf(rowChar));
      rowLabel.setPrefSize(35, 35);
      rowLabel.setAlignment(Pos.CENTER);
      rowLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #34495e; -fx-font-size: 14px;");
      grid.add(rowLabel, 0, row + 1); 
    }

    // add buttons for each cell
    for (int row = 0; row < board.getHeight(); row++) {
      char rowChar = (char) ('A' + row);
      for (int col = 0; col < board.getWidth(); col++) {
        
        Button cell = new Button();
        cell.setPrefSize(35, 35);
        String prefix = isEnemy ? "enemy" : "player";
        cell.setId(prefix + "-cell-" + rowChar + "-" + col);
        cell.setStyle("-fx-background-color: #2c3e50; -fx-border-color: #34495e;");

        cell.setPrefSize(35, 35);
        cell.setMinSize(35, 35); 
        cell.setMaxSize(35, 35);

        final int r = row;
        final int c = col;

        cell.setOnAction(e -> {
            if (placementHandler != null) {
                placementHandler.handle(r, c);
            }
        });

        grid.add(cell, col + 1, row + 1);
        cells[row][col] = cell;
      }
    }
  }

  /**
   * Functional interface for handling clicks on the board
   */
  public interface ClickHandler {
    void handle(int row, int col);
  }

  /**
   * sets the click handler for the board view
   * it will be called when a cell is clicked
   */
  public void setPlacement(ClickHandler handler) {
    this.placementHandler = handler;
  }

  /**
   * colors a specific cell on the board
   */
  public void colorCell(int row, int col, String hexColor) {
    cells[row][col].setStyle("-fx-background-color: " + hexColor + "; -fx-border-color: #34495e; -fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
  }

  /**
   * return the grid of buttons for the board view
   * @return the grid of buttons for the board view
   */
  public GridPane getGrid() {
    return grid;
  }

  /**
   * refreshes the board view to reflect the current state of the board
   */
  public void refresh() {
      for (int row = 0; row < board.getHeight(); row++) {
          for (int col = 0; col < board.getWidth(); col++) {
              Coordinate coord = new Coordinate(row, col);
              Ship<Character> ship = board.getShipAt(coord);
              Character enemyViewChar = board.whatIsAtForEnemy(coord);
              Character myViewChar = board.whatIsAtForSelf(coord);
              if (ship != null) {
                  if (ship.wasHitAt(coord)) {
                    cells[row][col].setStyle("-fx-background-color: #e74c3c; -fx-border-color: #34495e; -fx-padding: 0;");
                    cells[row][col].setStyle(cells[row][col].getStyle() + "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 24px;");
                  } else {
                    cells[row][col].setStyle("-fx-background-color: #2ecc71; -fx-border-color: #34495e; -fx-padding: 0;");
                  }
              } else {
                  if (enemyViewChar != null && enemyViewChar == 'X') {
                  cells[row][col].setStyle("-fx-background-color: #3498db; -fx-border-color: #34495e; -fx-padding: 0;");
                  cells[row][col].setStyle(cells[row][col].getStyle() + "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 24px;");
                } else {
                    cells[row][col].setStyle("-fx-background-color: #2c3e50; -fx-border-color: #34495e; -fx-padding: 0;");
                  }
              }
          }
      }
  }
  public void setCellText(int row, int col, String text) {
      cells[row][col].setText(text);
  }
}
