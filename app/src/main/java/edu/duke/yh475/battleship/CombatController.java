package edu.duke.yh475.battleship;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class CombatController {
    private final GuiApp app;
    private ComboBox<String> actionSelector;
    private Label messageLabel;
    
    // Tracking actions
    private int sonarCount = 3;
    private int moveCount = 3;
    // Tracking labels
    private Label moveLabel;
    private Label sonarLabel;

    public CombatController(GuiApp app) {
        this.app = app;
    }

    public VBox buildSidebar() {
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
        
        updateActionMenu(); 

        messageLabel = new Label("Awaiting actions....");
        messageLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #34495e;");
        messageLabel.setWrapText(true);
        messageLabel.setPrefHeight(100); 

        VBox sidebar = new VBox(20, title, trackerBox, prompt, actionSelector, messageLabel);
        sidebar.setAlignment(Pos.TOP_LEFT);
        sidebar.setPadding(new Insets(40, 20, 20, 40));
        sidebar.setPrefWidth(350);
        
        return sidebar;
    }

    /**
     * Call this method every time an action is performed to update the UI
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

    // Getter 
    public void decrementMove() { moveCount--; updateActionMenu(); }
    public void decrementSonar() { sonarCount--; updateActionMenu(); }
}