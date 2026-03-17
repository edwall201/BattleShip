package edu.duke.yh475.battleship;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GuiApp extends Application {

    /**
     * starts the javafx app
     */
    @Override
    public void start(Stage primaryStage) {
        Board<Character> enemyBoard = new BattleShipBoard<>(10, 20);
        
        V2ShipFactory factory = new V2ShipFactory();
        Ship<Character> testShip = factory.makeSubmarine(new Placement("A0H")); 
        enemyBoard.tryAddShip(testShip);

        BoardView boardView = new BoardView(enemyBoard);
        Scene scene = new Scene(boardView.getGrid(), 360, 710);
        primaryStage.setTitle("ECE 651 Battleship - yh475");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}