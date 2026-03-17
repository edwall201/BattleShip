package edu.duke.yh475.battleship;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

/**
 * A javafx view for the battleship board.
 */
public class BoardView {
    private final GridPane grid;
    private final Board<Character> board;
    private final Button[][] cells;
    private boolean isEnemyView;

    /**
     * Constructor for the boardview
     * @param board the board to create the view
     * @param isEnemyView whether this is the enemy's view
     */
    public BoardView(Board<Character> board, boolean isEnemyView) {
        this.board = board;
        this.isEnemyView = isEnemyView; 
        this.grid = new GridPane();
        this.cells = new Button[board.getHeight()][board.getWidth()];
        setupGrid();
    }

    /**
     * Sets up the grid of buttons for the board view
     * initializes each button and adds it to the grid
     * also sets up the event handler for each button to handle firing at coordinates when clicked
     */
    private void setupGrid() {
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                Button cell = new Button();
                cell.setPrefSize(35, 35);
                
                cell.setStyle("-fx-background-color: #2c3e50; -fx-border-color: #34495e;");
                
                final int r = row;
                final int c = col;
                if(isEnemyView){
                    cell.setOnAction(e -> handleCellClick(r, c));
                }
                else{
                    cell.setOnAction(null); 
                }
                                
                grid.add(cell, col, row);
                cells[row][col] = cell;
            }
        }
    }

    /**
     * Fires at given coords and updates the button color
     * if hit, change to red, otherwise change to blue
     * @param row the row of the coord to fire at
     * @param col the column of the coord to fire at
     */
    private void handleCellClick(int row, int col) {
        Coordinate coord = new Coordinate(row, col);
        Ship<Character> hit = board.fireAt(coord);
        
        if (hit != null) {
            cells[row][col].setStyle("-fx-background-color: #e74c3c;"); 
            System.out.println("Hit at " + coord);
        } else {
            cells[row][col].setStyle("-fx-background-color: #3498db;"); 
            System.out.println("Miss at " + coord);
        }
        cells[row][col].setDisable(true);
    }

    /** 
     * return the grid of buttons for the board view
     * @return the grid of buttons for the board view
     */
    public GridPane getGrid() {
        return grid;
    }
}