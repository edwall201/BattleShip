package edu.duke.yh475.battleship;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GuiApp extends Application {
  // state board view
  private Board<Character> playerBoard;
  private Board<Character> enemyBoard;
  private BoardView playerView;
  private BoardView enemyView;

  private Label instructionLabel;

  // layout
  private HBox boardsLayout;
  private VBox leftSide;
  private VBox rightSide;
  private VBox placementSidebar;

  // controllers
  private PlacementController placementController;
  private CombatController combatController;
  private VBox combatSidebar;

  /**
   * starts the javafx app
   */
  @Override
  public void start(Stage primaryStage) {
    // Initialize the controller first
    placementController = new PlacementController(this);
    combatController = new CombatController(this);

    initializeGameData();
    buildBoardsLayout();
    buildInstructionLabel();

    VBox root = new VBox(30, instructionLabel, boardsLayout);
    root.setAlignment(Pos.CENTER);
    root.setPadding(new Insets(30));

    Scene scene = new Scene(root, 900, 900);
    primaryStage.setTitle("Battleship");
    primaryStage.setScene(scene);
    primaryStage.setResizable(false);
    primaryStage.show();
  }

  /**
   * initializes the game data
   */
  private void initializeGameData() {
    playerBoard = new BattleShipBoard<>(10, 20);
    enemyBoard = new BattleShipBoard<>(10, 20);

    playerView = new BoardView(playerBoard, false);
    enemyView = new BoardView(enemyBoard, true);
    
    // Pass the clicks to the controller
    playerView.setPlacement((row, col) -> placementController.handlePlacementClick(row, col));
  }

  /**
   * Builds the layout for the boards and sidebars
   */
  private void buildBoardsLayout() {
    Label playerLabel = new Label("Your Board");
    playerLabel.setStyle("-fx-font-size: 23px; -fx-font-weight: bold;");

    Label enemyLabel = new Label("Enemy Board");
    enemyLabel.setStyle("-fx-font-size: 23px; -fx-font-weight: bold;");

    leftSide = new VBox(10, playerLabel, playerView.getGrid());
    leftSide.setAlignment(Pos.CENTER);

    rightSide = new VBox(10, enemyLabel, enemyView.getGrid());
    rightSide.setAlignment(Pos.CENTER);

    placementSidebar = placementController.buildSidebar();

    boardsLayout = new HBox(50, leftSide, placementSidebar);
    boardsLayout.setAlignment(Pos.TOP_CENTER);
  }

  /**
   * put the instruction label at the top of the screen
   */
  private void buildInstructionLabel() {
    instructionLabel = new Label("Select a ship and orientation, then click your board to place it");
    instructionLabel.setStyle("-fx-font-size: 24px; " + "-fx-font-weight: bold; " + "-fx-text-fill: white; " +
        "-fx-background-color: #959ba3ff;  " + "-fx-padding: 12px 24px; " + "-fx-background-radius: 30px; " +
        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);");
  }

  /**
   * when the placementis complete, transition to the combat mode
   */
  public void transitionToCombatPhase() {
    boardsLayout.getChildren().remove(placementSidebar);
    boardsLayout.getChildren().add(rightSide);

    combatSidebar = combatController.buildSidebar();
    boardsLayout.getChildren().add(combatSidebar);
    
    playerView.setPlacement((row, col) -> combatController.handleMoveClick(row, col));
    enemyView.setPlacement((row, col) -> combatController.handleEnemyBoardClick(row, col));
    
    instructionLabel.setText("Start!");
    instructionLabel.setStyle("-fx-font-size: 24px; " + "-fx-font-weight: bold; " + "-fx-text-fill: white; " +
        "-fx-background-color: #959ba3ff;  " + "-fx-padding: 12px 24px; " + "-fx-background-radius: 30px; " +
        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);");

    //resize the window to fit the new layouy
    boardsLayout.getScene().getWindow().setWidth(1350);
  }

  //Getters and Setters 
  public Board<Character> getPlayerBoard() { return playerBoard; }
  public Board<Character> getEnemyBoard() { return enemyBoard; }
  public void setPlayerBoard(Board<Character> board) { this.playerBoard = board; }
  public BoardView getPlayerView() { return playerView; }
  public BoardView getEnemyView() { return enemyView; }
  public void setPlayerView(BoardView view) { this.playerView = view; }
  public VBox getLeftSide() { return leftSide; }

  public static void main(String[] args) {
    launch(args);
  }
}