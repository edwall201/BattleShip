package edu.duke.yh475.battleship;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.util.Map;
import java.util.HashMap;
import javafx.animation.PauseTransition;
import javafx.util.Duration;


public class CombatController {
    private final GuiApp app;
    private ComboBox<String> actionSelector;
    private ComboBox<String> orientationSelector;
    private Label messageLabel;
    private Label computerMessage;
    private Label orientationLabel;

    // Tracking actions
    private int sonarCount = 3;
    private int moveCount = 3;

    // Tracking labels
    private Label moveLabel;
    private Label sonarLabel;

    private Coordinate moveSourceCoord = null; 
    private ShipMove<Character> shipMoveLogic;
    private SonarScanner<Character> sonarScanner;
    private ComputerController computerController;

    public CombatController(GuiApp app) {
        this.app = app;
    }

    public VBox buildSidebar() {
        shipMoveLogic = new ShipMove<>(app.getPlayerBoard(), new V2ShipFactory());
        sonarScanner = new SonarScanner<>(app.getEnemyBoard());
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

        orientationLabel = new Label("Move Orientation:");
        orientationLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #2c3e50;");
        orientationSelector = new ComboBox<>();
        orientationSelector.setStyle("-fx-font-size: 18px; -fx-pref-width: 100px;");

        // Start hidden!
        hideOrientationSelector();
        updateActionMenu(); 

        messageLabel = new Label("Awaiting choice...");
        messageLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #34495e; -fx-font-weight: bold;");
        messageLabel.setWrapText(true);
        messageLabel.setPrefHeight(100); 

        computerMessage = new Label("");
        computerMessage.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        computerMessage.setWrapText(true);
        computerMessage.setMinHeight(30);

        computerController = new ComputerController(app, computerMessage);
        VBox sidebar = new VBox(20, title, trackerBox, prompt, actionSelector, orientationLabel, orientationSelector, messageLabel, computerMessage);
        sidebar.setAlignment(Pos.TOP_LEFT);
        sidebar.setPadding(new Insets(40, 20, 20, 40));
        sidebar.setPrefWidth(350);
        
        actionSelector.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (moveSourceCoord != null) {
                app.getPlayerView().refresh(); 
            }
            moveSourceCoord = null; 
            // hide if player changes to fire or sonar 
            hideOrientationSelector(); 
            
            if ("Move a ship".equals(newVal)) {
                messageLabel.setText("Move selected!\n" + "Click a ship on your board to select it.");
                messageLabel.setStyle("-fx-text-fill: #34495e; -fx-font-size: 18px;");
            }
            else if ("Sonar scan".equals(newVal)) {
                messageLabel.setText("Sonar Scan selected!\n" + "Click a square on the enemy board to scan.");
                messageLabel.setStyle("-fx-text-fill: #34495e; -fx-font-size: 18px;");
            } else {
                messageLabel.setText("Fire selected!\n" + "Click a square on the enemy board to fire.");
                messageLabel.setStyle("-fx-text-fill: #34495e; -fx-font-size: 18px;");
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
     * Handles movement on my board
     */
    public void handleMoveClick(int row, int col) {
        if (!"Move a ship".equals(actionSelector.getValue())) {
            messageLabel.setText("You can only click your own board to move a ship!");
            messageLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 18px;");
            return;
        }
        if ("Fire at a square".equals(actionSelector.getValue())){
            messageLabel.setText("You cannot fire at your own board!\nClick the Enemy Board.");
            messageLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 18px; ");
            return;
        } else if ("Sonar scan".equals(actionSelector.getValue())) {
            messageLabel.setText("You cannot scan your own board!\nClick the Enemy Board.");
            messageLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 16px;");
            return;
        }

        Coordinate clickedCoord = new Coordinate(row, col);

        // 1.Select the ship
        if (moveSourceCoord == null) {
            Ship<Character> shipToMove = app.getPlayerBoard().getShipAt(clickedCoord);
            if (shipToMove == null) {
                messageLabel.setText("No ship at " + clickedCoord + "\n" + ". Click a valid ship.");
                messageLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 18px; -fx-font-weight");
            } else {
                moveSourceCoord = clickedCoord;
                String shipName = shipToMove.getName();

                for (Coordinate c : shipToMove.getCoordinates()) {
                    app.getPlayerView().colorCell(c.getRow(), c.getColumn(), "#e67e22"); 
                    app.getPlayerView().setCellText(c.getRow(), c.getColumn(), "");
                }

                orientationSelector.getItems().clear();
                if (shipName.equals("Submarine") || shipName.equals("Destroyer")) {
                    orientationSelector.getItems().addAll("V", "H");
                    orientationSelector.setValue("V");
                } else {
                    orientationSelector.getItems().addAll("U", "D", "L", "R");
                    orientationSelector.setValue("U");
                }
                showOrientationSelector();
                messageLabel.setText(shipName + " selected at " + clickedCoord + "! \n" + "Now select an orientation and click its new location on your board.");
                messageLabel.setStyle("-fx-text-fill: #2980b9; -fx-font-size: 18px;");
            }
        } 
        //2. Choose the destination
        else {
            String orientation = orientationSelector.getValue();
            Placement destination = new Placement(clickedCoord, orientation.charAt(0));
            
            String errorMsg = shipMoveLogic.doMove(moveSourceCoord, destination);
            
            if (errorMsg != null) {
                messageLabel.setText("Move Failed: \n" + errorMsg +"\n"+ "Try another spot.");
                messageLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 18px;");
            } else {
                moveCount--;
                updateActionMenu();
                moveSourceCoord = null;
                
                messageLabel.setText("Ship moved successfully!");
                messageLabel.setStyle("-fx-text-fill: #2ecc71; -fx-font-size: 18px;");
                hideOrientationSelector();

                // Redraw the player board to reflect the move
                app.getPlayerView().refresh();
                ComputerAttackwithDelay();
            }
        }
    }

    /**
     * Helper methods to show the orientation selector
     */
    private void showOrientationSelector() {
        orientationLabel.setVisible(true);
        orientationLabel.setManaged(true);
        orientationSelector.setVisible(true);
        orientationSelector.setManaged(true);
    }

    /**
     * Helper method to hide the orientation selector
     */
    private void hideOrientationSelector() {
        orientationLabel.setVisible(false);
        orientationLabel.setManaged(false);
        orientationSelector.setVisible(false);
        orientationSelector.setManaged(false);
    }

    /**
     * handles clicks on the enemy board during combat phase for both firing and sonar actions
     */
    public void handleEnemyBoardClick(int row, int col) {
        String action = actionSelector.getValue();
        Coordinate clickedCoord = new Coordinate(row, col);

        if ("Fire at a square".equals(action)) {
            executeFireAction(row, col, clickedCoord);
        } 
        else if ("Sonar scan".equals(action)) {
            executeSonarAction(clickedCoord);
        } 
    }

     /**
     * handles the logic for firing at the enemy board
     * updating the view 
     * showing messages based on hit or miss
     */
    private void executeFireAction(int row, int col, Coordinate clickedCoord){
        Ship<Character> hitShip = app.getEnemyBoard().fireAt(clickedCoord);
        String coordStr = "" + (char)('A' + row) + col;

        if (hitShip != null) {
            String shipName = hitShip.getName(); 
            app.getEnemyView().colorCell(row, col, "#e74c3c"); 
            messageLabel.setText("You hit a " + shipName + " at " + coordStr + "!");
            messageLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 18px;");

            if (hitShip.isSunk()) {
                String letter = shipName.substring(0, 1).toUpperCase();
                for (Coordinate c : hitShip.getCoordinates()) {
                    app.getEnemyView().getGrid().getChildren().forEach(node -> {
                        if (javafx.scene.layout.GridPane.getRowIndex(node) == c.getRow() + 1 && 
                            javafx.scene.layout.GridPane.getColumnIndex(node) == c.getColumn() + 1) {
                            javafx.scene.control.Button btn = (javafx.scene.control.Button) node;                
                            btn.setText(letter); 
                            btn.setStyle(btn.getStyle() + "-fx-text-fill: white; -fx-font-size: 18px; -fx-opacity: 1.0;");
                            btn.setDisable(true);
                        }
                    });
                }
            } else {
                app.getEnemyView().getGrid().getChildren().forEach(node -> {
                    if (javafx.scene.layout.GridPane.getRowIndex(node) == row + 1 && 
                        javafx.scene.layout.GridPane.getColumnIndex(node) == col + 1) {
                        javafx.scene.control.Button btn = (javafx.scene.control.Button) node;                
                        btn.setText("");
                        btn.setStyle(btn.getStyle() + "-fx-text-fill: white; -fx-font-size: 18px; -fx-opacity: 1.0;");
                        btn.setDisable(true);
                    }
                });
            }
        } else {
            app.getEnemyView().colorCell(row, col, "#3498db"); 
            messageLabel.setText("You missed at " + coordStr + "!");
            messageLabel.setStyle("-fx-text-fill: #2980b9; -fx-font-size: 18px;");
            
            app.getEnemyView().getGrid().getChildren().forEach(node -> {
                if (javafx.scene.layout.GridPane.getRowIndex(node) == row + 1 && 
                    javafx.scene.layout.GridPane.getColumnIndex(node) == col + 1) {
                    javafx.scene.control.Button btn = (javafx.scene.control.Button) node;                
                    btn.setStyle(btn.getStyle() + "-fx-text-fill: white;  -fx-font-size: 18px; -fx-opacity: 1.0;");
                    btn.setDisable(true);
                }
            });
        }
        
        if (app.getEnemyBoard().isLost()) {
            endGame("YOU WIN! All enemy ships are destroyed!", "#2ecc71");
        } else {
            ComputerAttackwithDelay();
        }
    }

    /**
     * handles the logic for performing a sonar scan on the enemy board
     * updates the view to show the scanned area and results
     * decreases sonar count and updates the menu
     */
    private void executeSonarAction(Coordinate clickedCoord) {
        if (sonarCount <= 0) return; 
        Map<String, Integer> results = sonarScanner.scan(clickedCoord);
        sonarCount--;
        updateActionMenu();
        
        int centerRow = clickedCoord.getRow();
        int centerCol = clickedCoord.getColumn();

        for (int r = -3; r <= 3; r++) {
            for (int c = -3; c <= 3; c++) {
                if (Math.abs(r) + Math.abs(c) <= 3) { 
                    int currR = centerRow + r;
                    int currC = centerCol + c;
                    
                    if (currR >= 0 && currR < app.getEnemyBoard().getHeight() &&
                        currC >= 0 && currC < app.getEnemyBoard().getWidth()) {
                        app.getEnemyView().colorCell(currR, currC, "#e67e22"); 
                    }
                }
            }
        }

        String report = String.format("Submarines: %d, Destroyers: %d\nBattleships: %d, Carriers: %d",
            results.get("Submarine"), results.get("Destroyer"), results.get("Battleship"), results.get("Carrier"));
        messageLabel.setText("Sonar scanned at " + clickedCoord + "!\n" + report);
        messageLabel.setStyle("-fx-text-fill: #e67e22; -fx-font-size: 18px;");
        ComputerAttackwithDelay();
    }
   
    private void ComputerAttackwithDelay(){
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e ->{computerController.doComputerTurn();
            if (app.getPlayerBoard().isLost()) {
                endGame("YOU LOSE! All your ships have been destroyed!", "#e74c3c");
            }   
        });
        pause.play();
    }

    private void endGame(String message, String color) {
    messageLabel.setText(message);
    messageLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 26px; -fx-font-weight: bold;");
    computerMessage.setText(""); 

    actionSelector.setDisable(true);
    if (orientationSelector != null) {
        orientationSelector.setDisable(true);
    }

    app.getPlayerView().setPlacement(null);
    app.getEnemyView().setPlacement(null);
  }

}