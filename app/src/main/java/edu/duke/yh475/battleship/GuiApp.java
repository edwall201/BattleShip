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

    /**
     * starts the javafx app
     */
    @Override
    public void start(Stage primaryStage) {
        Board<Character> playerBoard = new BattleShipBoard<>(10, 20);
        Board<Character> enemyBoard = new BattleShipBoard<>(10, 20);

        V2ShipFactory factory = new V2ShipFactory();
        Ship<Character> testShip = factory.makeSubmarine(new Placement("A0H"));
        enemyBoard.tryAddShip(testShip);

        BoardView playerView = new BoardView(enemyBoard, false);
        BoardView enemyView = new BoardView(playerBoard, true);

        Label playerLabel = new Label("Your Board");
        playerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        Label enemyLabel = new Label("Enemy Board");
        enemyLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        VBox leftSide = new VBox(10, playerLabel, playerView.getGrid());
        leftSide.setAlignment(Pos.CENTER);

        VBox rightSide = new VBox(10, enemyLabel, enemyView.getGrid());
        rightSide.setAlignment(Pos.CENTER);
        HBox mainLayout = new HBox(50, leftSide, rightSide); 
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(30));

        Scene scene = new Scene(mainLayout, 850, 800);
        primaryStage.setTitle("ECE 651 Battleship - yh475");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}