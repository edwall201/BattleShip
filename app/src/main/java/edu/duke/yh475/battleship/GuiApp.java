package edu.duke.yh475.battleship;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;



public class GuiApp extends Application {
    // state board view
    private Board<Character> playerBoard;
    private Board<Character> enemyBoard;
    private BoardView playerView;
    private BoardView enemyView;

    // control
    private ComboBox<String> shipSelector;
    private ComboBox<String> orientationSelector;
    private Button startGameBtn;
    private Label instructionLabel;
    private Label shipRemainingLabel;

    //layout
    private HBox boardsLayout;
    private VBox leftSide;
    private VBox rightSide;
    private VBox placementSidebar;

    //ships tracking
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
     * starts the javafx app
     */
    @Override
    public void start(Stage primaryStage) {
        initializeGameData();
        buildPlacementSidebar();
        buildBoardsLayout();
        buildInstructionLabel();

        VBox root = new VBox(30, instructionLabel, boardsLayout);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        Scene scene = new Scene(root, 900, 850);
        primaryStage.setTitle("ECE 651 Battleship - yh475");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void initializeGameData() {
        playerBoard = new BattleShipBoard<>(10, 20);
        enemyBoard = new BattleShipBoard<>(10, 20);

        V2ShipFactory factory = new V2ShipFactory();
        enemyBoard.tryAddShip(factory.makeSubmarine(new Placement("A0H")));

        playerView = new BoardView(playerBoard, false);
        enemyView = new BoardView(enemyBoard, true);
    }

    private void buildBoardsLayout() {
        Label playerLabel = new Label("Your Board");
        playerLabel.setStyle("-fx-font-size: 23px; -fx-font-weight: bold;");
        
        Label enemyLabel = new Label("Enemy Board");
        enemyLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        leftSide = new VBox(10, playerLabel, playerView.getGrid());
        leftSide.setAlignment(Pos.CENTER);

        rightSide = new VBox(10, enemyLabel, enemyView.getGrid());
        rightSide.setAlignment(Pos.CENTER);
        
        boardsLayout = new HBox(50, leftSide, placementSidebar); // Start with just the left side and sidebar
        boardsLayout.setAlignment(Pos.TOP_CENTER);
    }

    private void buildInstructionLabel() {
        instructionLabel = new Label("Select a ship and orientation, then click your board to place it");
        instructionLabel.setStyle("-fx-font-size: 24px; " + "-fx-font-weight: bold; " +"-fx-text-fill: white; " +
        "-fx-background-color: #959ba3ff;  " + "-fx-padding: 12px 24px; "+"-fx-background-radius: 30px; "+
        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);");
    }

   private void buildPlacementSidebar() {
        Label shapesInfo = new Label( "Ship Shapes:\n" + "• Submarine (1x2) - V, H\n" + "• Destroyer (1x3) - V, H\n" +"• Battleship (T-Shape) - U, D, L, R\n" +
            "• Carrier (Z-Shape) - U, D, L, R");

        shapesInfo.setStyle("-fx-font-size: 18px; -fx-text-fill: #34495e; -fx-padding: 10px 0;");

        Label trackerTitle = new Label("Remaining ships to Place:");
        trackerTitle.setStyle("-fx-font-size: 18px; -fx-text-fill: #2c3e50;");

        String trackerStyle = "-fx-font-size: 18px; -fx-text-fill: #e67e22;";
        subLabel = new Label("Submarines: " + subCount + " / " + MAX_SUB);
        destLabel = new Label("Destroyers: " + destCount + " / " + MAX_DEST);
        battleLabel = new Label("Battleships: " + battleCount + " / " + MAX_BATTLE);
        carrierLabel = new Label("Carriers: " + carrierCount + " / " + MAX_CARRIER);
        
        subLabel.setStyle(trackerStyle);
        destLabel.setStyle(trackerStyle);
        battleLabel.setStyle(trackerStyle);
        carrierLabel.setStyle(trackerStyle);

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

        HBox dropdowns = new HBox(10, shipSelector, orientationSelector);
        dropdowns.setAlignment(Pos.CENTER_LEFT);

        messageLabel = new Label("Awaiting placement...");
        messageLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #7f8c8d;");
        messageLabel.setWrapText(true);
        messageLabel.setPrefHeight(30);

        startGameBtn = new Button("Start Game");
        startGameBtn.setDisable(true);
        startGameBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 20px; -fx-padding: 10px 25px;");
        startGameBtn.setOnAction(e -> transitionToCombatPhase());

        placementSidebar = new VBox(20, shapesInfo, trackerBox, new Label("Select Ship & Orientation:"), dropdowns, messageLabel, startGameBtn);
        placementSidebar.setAlignment(Pos.TOP_LEFT);
        placementSidebar.setPadding(new Insets(40, 20, 20, 40));
        placementSidebar.setPrefWidth(350);
    }

    private void transitionToCombatPhase() {
        boardsLayout.getChildren().remove(placementSidebar);
        boardsLayout.getChildren().add(rightSide);
    
        instructionLabel.setText("Phase 2: Combat! Click the Enemy Board to fire.");
        instructionLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white; " +
            "-fx-background-color: #e74c3c; " +
            "-fx-padding: 12px 24px; -fx-background-radius: 8px; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);");
    }

    public static void main(String[] args) {
        launch(args);
    }
}