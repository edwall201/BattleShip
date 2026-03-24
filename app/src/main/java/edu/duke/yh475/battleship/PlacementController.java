package edu.duke.yh475.battleship;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PlacementController{
    private final GuiApp app;
    private ComboBox<String> shipSelector;
    private ComboBox<String> orientationSelector;
    private Button startGameBtn;

    // ships tracking
    private Label subLabel;
    private Label destLabel;
    private Label battleLabel;
    private Label carrierLabel;
    private Label messageLabel;
    private int subCount = 0;
    private int destCount = 0;
    private int battleCount = 0;
    private int carrierCount = 0;
    private final int MAX_SUB = 2;
    private final int MAX_DEST = 3;
    private final int MAX_BATTLE = 3;
    private final int MAX_CARRIER = 2;

    /**
     * constructo for the placement controller
     */
    public PlacementController(GuiApp app) {
        this.app = app;
    }

    /**
     * Builds the sidebar for the placement
     */
    public VBox buildSidebar() {
        Label shapesInfo = new Label("Ship Shapes:\n" + "• Submarine (1x2) - V, H\n" + "• Destroyer (1x3) - V, H\n"
            + "• Battleship (T-Shape) - U, D, L, R\n" + "• Carrier (Z-Shape) - U, D, L, R");
        shapesInfo.setStyle("-fx-font-size: 18px; -fx-text-fill: #34495e; -fx-padding: 10px 0;");

        Label trackerTitle = new Label("Remaining ships to Place:");
        trackerTitle.setStyle("-fx-font-size: 18px; -fx-text-fill: #2c3e50;");
        String trackerStyle = "-fx-font-size: 18px; -fx-text-fill: #e67e22;";

        //inisital the number of ships to place
        subLabel = new Label("Submarines: " + subCount + " / " + MAX_SUB);
        destLabel = new Label("Destroyers: " + destCount + " / " + MAX_DEST);
        battleLabel = new Label("Battleships: " + battleCount + " / " + MAX_BATTLE);
        carrierLabel = new Label("Carriers: " + carrierCount + " / " + MAX_CARRIER);

        subLabel.setStyle(trackerStyle);
        destLabel.setStyle(trackerStyle);
        battleLabel.setStyle(trackerStyle);
        carrierLabel.setStyle(trackerStyle);

        // group the tracker labels together
        VBox trackerBox = new VBox(5, trackerTitle, subLabel, destLabel, battleLabel, carrierLabel);
        trackerBox.setStyle("-fx-background-color: #ecf0f1; -fx-padding: 10px; -fx-background-radius: 5px;");

        shipSelector = new ComboBox<>();
        shipSelector.getItems().addAll("Submarine", "Destroyer", "Battleship", "Carrier");
        shipSelector.setValue("Submarine");
        shipSelector.setStyle("-fx-font-size: 18px; -fx-pref-width: 180px;");

        orientationSelector = new ComboBox<>();
        orientationSelector.getItems().addAll("V", "H");
        orientationSelector.setValue("V");
        orientationSelector.setStyle("-fx-font-size: 18px; -fx-pref-width: 100px;");

        //update the orientation options base on the selected ship
        shipSelector.valueProperty().addListener((observable, oldValue, newValue) -> {
            orientationSelector.getItems().clear();
            if (newValue.equals("Submarine") || newValue.equals("Destroyer")) {
                orientationSelector.getItems().addAll("V", "H");
                orientationSelector.setValue("V");
            } else {
                orientationSelector.getItems().addAll("U", "D", "L", "R");
                orientationSelector.setValue("U");
            }
        });

        //group the dropdowns together
        HBox dropdowns = new HBox(10, shipSelector, orientationSelector);
        dropdowns.setAlignment(Pos.CENTER_LEFT);

        messageLabel = new Label("Awaiting placement...");
        messageLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #7f8c8d;");
        messageLabel.setWrapText(true);
        messageLabel.setPrefHeight(80);

        //set up the reset btm
        Button resetBtn = new Button("Reset");
        resetBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18px; -fx-padding: 10px 20px;");
        resetBtn.setOnAction(e -> resetPlacement());

        // set up the start btn
        startGameBtn = new Button("Start");
        startGameBtn.setDisable(true);
        startGameBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18px; -fx-padding: 10px 20px;");
        
        // When clicked, tells the main app to transition to combat phase
        startGameBtn.setOnAction(e -> app.transitionToCombatPhase());
        HBox actionButtons = new HBox(15, resetBtn, startGameBtn);
        VBox sidebar = new VBox(20, shapesInfo, trackerBox, new Label("Select Ship & Orientation:"), dropdowns, messageLabel, actionButtons);
        sidebar.setAlignment(Pos.TOP_LEFT);
        sidebar.setPadding(new Insets(40, 20, 20, 40));
        sidebar.setPrefWidth(350);
        return sidebar;
    }

    /**
     * handles clicks on the board during placement phase
     */
    public void handlePlacementClick(int row, int col) {
        String shipType = shipSelector.getValue();
        String orientationStr = orientationSelector.getValue();
        char rowChar = (char) ('A' + row);
        String coordStr = "" + rowChar + col;

        if (!canPlaceShip(shipType)) {
            messageLabel.setText("You have already placed all " + shipType + "s!");
            messageLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #e74c3c;");
            return;
        }

        try {
            Placement placement = new Placement(coordStr + orientationStr);
            V2ShipFactory factory = new V2ShipFactory();
            Ship<Character> newShip = createShip(factory, shipType, placement);

            String errorMsg = app.getPlayerBoard().tryAddShip(newShip);

            if (errorMsg != null) {
                messageLabel.setText("Error: " + errorMsg);
                messageLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #e74c3c;");
            } else {
                updateShipCount(shipType);
                messageLabel.setText(shipType + " placed at " + coordStr + "!");
                messageLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #2ecc71;"); 

                for (Coordinate c : newShip.getCoordinates()) {
                    app.getPlayerView().colorCell(c.getRow(), c.getColumn(), "#2ecc71");
                }
                checkIfAllPlaced();
            }
        } catch (IllegalArgumentException e) {
            messageLabel.setText("Invalid placement formatting.");
            messageLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #e74c3c;");
        }
    }

    /**
     *  helper methods for ship creation and tracking  
     */
    private Ship<Character> createShip(V2ShipFactory factory, String type, Placement p) {
        switch (type) {
            case "Submarine":
                return factory.makeSubmarine(p);
            case "Destroyer":
                return factory.makeDestroyer(p);
            case "Battleship":
                return factory.makeBattleship(p);
            case "Carrier":
                return factory.makeCarrier(p);
            default:
                throw new IllegalArgumentException("Unknown ship: " + type);
        }
    }

    /**
     * helper methods for ship tracking and placement validation
     */
    private boolean canPlaceShip(String type) {
        switch (type) {
            case "Submarine":
                return subCount < MAX_SUB;
            case "Destroyer":
                return destCount < MAX_DEST;
            case "Battleship":
                return battleCount < MAX_BATTLE;
            case "Carrier":
                return carrierCount < MAX_CARRIER;
            default:
                return false;
        }
    }

    /**
     * helper method to update the ship count and check if all ships of a type have been placed to update the UI
     */
    private void updateShipCount(String type) {
        String finishedStyle = "-fx-font-size: 18px; -fx-text-fill: #27ae60;";
        switch (type) {
            case "Submarine" -> { subCount++; subLabel.setText("Submarines: " + subCount + " / " + MAX_SUB); if (subCount == MAX_SUB) subLabel.setStyle(finishedStyle); }
            case "Destroyer" -> { destCount++; destLabel.setText("Destroyers: " + destCount + " / " + MAX_DEST); if (destCount == MAX_DEST) destLabel.setStyle(finishedStyle); }
            case "Battleship" -> { battleCount++; battleLabel.setText("Battleships: " + battleCount + " / " + MAX_BATTLE); if (battleCount == MAX_BATTLE) battleLabel.setStyle(finishedStyle); }
            case "Carrier" -> { carrierCount++; carrierLabel.setText("Carriers: " + carrierCount + " / " + MAX_CARRIER); if (carrierCount == MAX_CARRIER) carrierLabel.setStyle(finishedStyle); }
        }
    }

    /**
     * helper method to check if all ships have been placed and enable the start button
     */
    private void checkIfAllPlaced() {
        if (subCount == MAX_SUB && destCount == MAX_DEST && battleCount == MAX_BATTLE && carrierCount == MAX_CARRIER) {
            startGameBtn.setDisable(false);
            startGameBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18px; -fx-padding: 10px 20px;");
            messageLabel.setText("All ships placed! Ready to start.");
        }
    }

    /**
     * resets the placement state to allow the player to start over
     */
    private void resetPlacement() {
        app.setPlayerBoard(new BattleShipBoard<>(10, 20));
        app.setPlayerView(new BoardView(app.getPlayerBoard(), false));
        
        app.getPlayerView().setPlacement((row, col) -> handlePlacementClick(row, col));
        app.getLeftSide().getChildren().set(1, app.getPlayerView().getGrid());

        subCount = destCount = battleCount = carrierCount = 0;

        String defaultStyle = "-fx-font-size: 18px; -fx-text-fill: #e67e22;";
        subLabel.setText("Submarines: 0 / " + MAX_SUB); subLabel.setStyle(defaultStyle);
        destLabel.setText("Destroyers: 0 / " + MAX_DEST); destLabel.setStyle(defaultStyle);
        battleLabel.setText("Battleships: 0 / " + MAX_BATTLE); battleLabel.setStyle(defaultStyle);
        carrierLabel.setText("Carriers: 0 / " + MAX_CARRIER); carrierLabel.setStyle(defaultStyle);

        startGameBtn.setDisable(true);
        startGameBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18px; -fx-padding: 10px 20px;");
        messageLabel.setText("Board reset! Awaiting placement...");
        messageLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #7f8c8d;");
    }


}