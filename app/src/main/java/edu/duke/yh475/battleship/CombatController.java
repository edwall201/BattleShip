package edu.duke.yh475.battleship;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class CombatController {
    private final GuiApp app;
    private ComboBox<String> actionSelector;
    private ComboBox<String> orientationSelector;
    private Label messageLabel;
    private Label orientationLabel;

    // Tracking actions
    private int sonarCount = 3;
    private int moveCount = 3;

    // Tracking labels
    private Label moveLabel;
    private Label sonarLabel;

    private Coordinate moveSourceCoord = null; 
    private ShipMove<Character> shipMoveLogic;

    public CombatController(GuiApp app) {
        this.app = app;
    }

    public VBox buildSidebar() {
        shipMoveLogic = new ShipMove<>(app.getPlayerBoard(), new V2ShipFactory());

        Label title = new Label("Action Options");
        title.setStyle("-fx-font-size: 24px; -fx-text-fill: #e74c3c;");

        Label trackerTitle = new Label("Actions Remaining:");
        trackerTitle.setStyle("-fx-font-size: 18px; -fx-text-fill: #2c3e50;");

        moveLabel = new Label("Move Ship: " + moveCount + " / 3");
        sonarLabel = new Label("Sonar Scan: " + sonarCount + " / 3");
        
        VBox trackerBox = new VBox(5, trackerTitle, moveLabel, sonarLabel);
        trackerBox.setStyle("-fx-background-color: #ecf0f1; -fx-padding: 10px; -fx-background-radius: 5px;");

        Label prompt = new Label("Select your action:");
        prompt.setStyle("-fx-font-size: 18px; -fx-text-fill: #2c3e50;");

        actionSelector = new ComboBox<>();
        actionSelector.setStyle("-fx-font-size: 18px; -fx-pref-width: 250px;");
        
        orientationSelector = new ComboBox<>();
        orientationSelector.setStyle("-fx-font-size: 18px; -fx-pref-width: 100px;");
        
        updateActionMenu(); 

        messageLabel = new Label("Awaiting orders, Captain.");
        messageLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #34495e; -fx-font-weight: bold;");
        messageLabel.setWrapText(true);
        messageLabel.setPrefHeight(100); 

        VBox sidebar = new VBox(15, title, trackerBox, prompt, actionSelector, new Label("Move Orientation:"), orientationSelector, messageLabel);
        sidebar.setAlignment(Pos.TOP_LEFT);
        sidebar.setPadding(new Insets(40, 20, 20, 40));
        sidebar.setPrefWidth(350);
        
        actionSelector.valueProperty().addListener((obs, oldVal, newVal) -> {
            moveSourceCoord = null; 
            if ("Move a ship".equals(newVal)) {
                messageLabel.setText("Move selected! Click a ship on YOUR board to select it.");
            }
        });
        
        return sidebar;
    }

    /**
     * Update the action menu based on remaining actions
     */
    public void updateActionMenu() {
        String currentSelection = actionSelector.getValue();
        actionSelector.getItems().clear();
        
        actionSelector.getItems().add("Fire at a square");
        
        if (moveCount > 0) {
            actionSelector.getItems().add("Move a ship");
        }
        if (sonarCount > 0) {
            actionSelector.getItems().add("Sonar scan");
        }
        
        // Default back to Fire if it ran out of other actions
        if (currentSelection != null && actionSelector.getItems().contains(currentSelection)) {
            actionSelector.setValue(currentSelection);
        } else {
            actionSelector.setValue("Fire at a square");
        }

        String activeStyle = "-fx-font-size: 18px; -fx-text-fill: #2ecc71;";
        String emptyStyle = "-fx-font-size: 18px; -fx-text-fill: #95a5a6;"; 
        
        moveLabel.setText("Move Ship: " + moveCount + " / 3");
        moveLabel.setStyle(moveCount > 0 ? activeStyle : emptyStyle);
        
        sonarLabel.setText("Sonar Scan: " + sonarCount + " / 3");
        sonarLabel.setStyle(sonarCount > 0 ? activeStyle : emptyStyle);
    }

    /**
     * Handles clicks on my board
     */
    public void handlePlayerBoardClick(int row, int col) {
        if (!"Move a ship".equals(actionSelector.getValue())) {
            messageLabel.setText("You can only click your own board to Move a ship!");
            return;
        }

        Coordinate clickedCoord = new Coordinate(row, col);

        // 1.Select the ship
        if (moveSourceCoord == null) {
            Ship<Character> shipToMove = app.getPlayerBoard().getShipAt(clickedCoord);
            if (shipToMove == null) {
                messageLabel.setText("No ship at " + clickedCoord + ". Click a valid ship.");
                messageLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 16px; -fx-font-weight: bold;");
            } else {
                moveSourceCoord = clickedCoord;
                String shipName = shipToMove.getName();

                for (Coordinate c : shipToMove.getCoordinates()) {
                    app.getPlayerView().colorCell(c.getRow(), c.getColumn(), "#e67e22"); 
                }

                orientationSelector.getItems().clear();
                if (shipName.equals("Submarine") || shipName.equals("Destroyer")) {
                    orientationSelector.getItems().addAll("V", "H");
                    orientationSelector.setValue("V");
                } else {
                    orientationSelector.getItems().addAll("U", "D", "L", "R");
                    orientationSelector.setValue("U");
                }

                messageLabel.setText(shipName + " selected at " + clickedCoord + "! Now select an orientation and click its NEW location on your board.");
                messageLabel.setStyle("-fx-text-fill: #2980b9; -fx-font-size: 16px; -fx-font-weight: bold;");
            }
        } 
        //2. Choose the destination
        else {
            String orientation = orientationSelector.getValue();
            Placement destination = new Placement(clickedCoord, orientation.charAt(0));
            
            String errorMsg = shipMoveLogic.doMove(moveSourceCoord, destination);
            
            if (errorMsg != null) {
                messageLabel.setText("Move Failed: " + errorMsg + " Try another spot.");
                messageLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 16px; -fx-font-weight: bold;");
            } else {
                moveCount--;
                updateActionMenu();
                moveSourceCoord = null;
                
                messageLabel.setText("Ship moved successfully!");
                messageLabel.setStyle("-fx-text-fill: #2ecc71; -fx-font-size: 16px; -fx-font-weight: bold;");
                
                // Redraw the player board to reflect the move
                app.getPlayerView().refresh();
            }
        }
    }

}