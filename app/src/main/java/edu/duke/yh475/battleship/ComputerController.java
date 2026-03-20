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
    }

    public void doComputerTurn() {
        Coordinate target = computerPlayer.doGuiTurn(app.getPlayerBoard());
        
        int row = target.getRow();
        int col = target.getColumn();

        Ship<Character> hitShip = app.getPlayerBoard().getShipAt(target);
        Character displayChar = app.getPlayerBoard().whatIsAtForSelf(target);
        String buttonText = (displayChar != null) ? displayChar.toString() : "";

        if (hitShip != null) {
            app.getPlayerView().colorCell(row, col, "#e74c3c");
            messageLabel.setText(messageLabel.getText() + "\nComputer hit your " + hitShip.getName() + " at " + target + "!");
        } else {
            app.getPlayerView().colorCell(row, col, "#3498db");
            messageLabel.setText(messageLabel.getText() + "\nComputer missed at " + target + ".");
        }
        
        app.getPlayerView().getGrid().getChildren().forEach(node -> {
            if (javafx.scene.layout.GridPane.getRowIndex(node) == row + 1 && 
                javafx.scene.layout.GridPane.getColumnIndex(node) == col + 1) {
                javafx.scene.control.Button btn = (javafx.scene.control.Button) node;                
                btn.setText(buttonText); 
                btn.setStyle(btn.getStyle() + "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 24px; -fx-opacity: 1.0;");
            }
        });
    }

}