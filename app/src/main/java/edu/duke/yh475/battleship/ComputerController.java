package edu.duke.yh475.battleship;

import javafx.scene.control.Label;

public class ComputerController{
    private final GuiApp app;
    private final Label messageLabel;
    private final ComputerPlayer computerPlayer;

    public ComputerController(GuiApp app, Label messageLabel) {
        this.app = app;
        this.messageLabel = messageLabel;
        this.computerPlayer = new ComputerPlayer("Computer", app.getEnemyBoard(), System.out, new V2ShipFactory());
        try {
            this.computerPlayer.doPlacementPhase();
        } catch (Exception e) {
            System.out.println("Error placing computer ships: " + e.getMessage());
        }
    }

    public void doComputerTurn() {
        Coordinate target = computerPlayer.doGuiTurn(app.getPlayerBoard());
        
        int row = target.getRow();
        int col = target.getColumn();

        String coordStr = "" + (char)('A' + row) + col;
        Ship<Character> hitShip = app.getPlayerBoard().getShipAt(target);
             
        if (hitShip != null) {
            app.getPlayerView().colorCell(row, col, "#e74c3c");
            messageLabel.setText("Computer hit your " + hitShip.getName() + " at " + coordStr + "!");
            messageLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 18px;");
        } else {
            app.getPlayerView().colorCell(row, col, "#3498db");
            messageLabel.setText("Computer missed at " + coordStr + ".");
            messageLabel.setStyle("-fx-text-fill: #3498db; -fx-font-size: 18px;");
        }
        
        app.getPlayerView().getGrid().getChildren().forEach(node -> {
            if (javafx.scene.layout.GridPane.getRowIndex(node) == row + 1 && 
                javafx.scene.layout.GridPane.getColumnIndex(node) == col + 1) {
                javafx.scene.control.Button btn = (javafx.scene.control.Button) node;                
                btn.setStyle(btn.getStyle() + "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 24px; -fx-opacity: 1.0;");
            }
        });
    }

}