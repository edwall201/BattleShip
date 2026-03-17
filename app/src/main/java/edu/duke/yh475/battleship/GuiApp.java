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

    //layout
    private HBox boardsLayout;
    private VBox leftSide;
    private VBox rightSide;
    private VBox controlPanel;

    /**
     * starts the javafx app
     */
    @Override
    public void start(Stage primaryStage) {
        initializeGameData();

        buildBoardsLayout();
        buildControlPanel();
        buildInstructionLabel();

        VBox root = new VBox(30, instructionLabel, boardsLayout, controlPanel);
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
        
        boardsLayout = new HBox(50, leftSide); 
        boardsLayout.setAlignment(Pos.CENTER);
    }

    private void buildInstructionLabel() {
        instructionLabel = new Label("Select a ship and orientation, then click your board to place it.");
        instructionLabel.setStyle("-fx-font-size: 24px; " + 
        "-fx-font-weight: bold; " +
        "-fx-text-fill: white; " +
        "-fx-background-color: #959ba3ff;  " +
        "-fx-padding: 12px 24px; "+
        "-fx-background-radius: 30px; "+
        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);");
    }

    private void buildControlPanel() {
        shipSelector = new ComboBox<>();
        shipSelector.getItems().addAll("Submarine", "Destroyer", "Battleship", "Carrier");
        shipSelector.setValue("Submarine");
        shipSelector.setStyle("-fx-font-size: 18px; -fx-pref-width: 190px;");

        orientationSelector = new ComboBox<>();
        orientationSelector.getItems().addAll("V", "H"); 
        orientationSelector.setValue("V");
        orientationSelector.setStyle("-fx-font-size: 18px; -fx-pref-width: 190px;");

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

        startGameBtn = new Button("Start Game");
        startGameBtn.setDisable(false); 
        startGameBtn.setStyle("-fx-background-color: #1b683bff; -fx-text-fill: white; -fx-font-weight: bold;" + "-fx-font-size: 20px;");

        startGameBtn.setOnAction(e -> transitionToCombatPhase());

        Label shipLabel = new Label("Ship:");
        shipLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        Label orientationLabel = new Label("Orientation:");
        orientationLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        HBox selectionRow = new HBox(20, shipLabel, shipSelector, orientationLabel, orientationSelector);
        selectionRow.setAlignment(Pos.CENTER);

        controlPanel = new VBox(20, selectionRow, startGameBtn);
        controlPanel.setAlignment(Pos.CENTER);
        

        controlPanel.setPadding(new Insets(30, 0, 0, 0));
    }

    private void transitionToCombatPhase() {
        boardsLayout.getChildren().add(rightSide);
        
        controlPanel.setVisible(false);
        controlPanel.setManaged(false);

        // 3. Update the Instruction Banner
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