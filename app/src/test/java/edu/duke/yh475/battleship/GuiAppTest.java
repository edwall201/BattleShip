package edu.duke.yh475.battleship;

import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.WaitForAsyncUtils;

@ExtendWith(ApplicationExtension.class)
    public class GuiAppTest {
    private GuiApp app;

    @Start
    public void start(Stage stage) {
        app = new GuiApp();
        app.start(stage);
    }

    @Test
    public void test_initial_layout(FxRobot robot) {
        FxAssert.verifyThat("Select a ship and orientation, then click your board to place it", NodeMatchers.isVisible());
    }

    @Test
    public void test_select_ship_and_orientation(FxRobot robot) {
        robot.clickOn("Submarine");
        robot.clickOn("Destroyer");
        robot.clickOn("V");
        robot.clickOn("H");
    }

    @Test
    public void test_full_placement_flow(FxRobot robot) {
        robot.clickOn("Submarine");
        robot.clickOn("V");
        robot.sleep(2000);
        robot.clickOn("#player-cell-A-0"); 
    }

    @Test
    public void test_place_all_ships_and_reset(FxRobot robot) {
        placeShip(robot, "Submarine", "V", "#player-cell-A-0");
        placeShip(robot, "Submarine", "V", "#player-cell-A-2");
        placeShip(robot, "Submarine", "Destroyer", "#player-cell-C-0"); 
        placeShip(robot, "Destroyer", "V", "#player-cell-C-2");
        placeShip(robot, "Destroyer", "V", "#player-cell-C-4");
        placeShip(robot, "Destroyer", "Battleship", "#player-cell-G-0");
        placeShip(robot, "Battleship", "U", "#player-cell-G-3");
        placeShip(robot, "Battleship", "U", "#player-cell-G-6");
        placeShip(robot, "Battleship", "Carrier", "#player-cell-L-0");
        placeShip(robot, "Carrier", "U", "#player-cell-L-5");
    
        WaitForAsyncUtils.waitForFxEvents();
        robot.clickOn("Reset");
        
    }

    @Test
    public void test_start_and_action(FxRobot robot) {
        placeShip(robot, "Submarine", "V", "#player-cell-A-0");
        placeShip(robot, "Submarine", "V", "#player-cell-A-2");
        placeShip(robot, "Submarine", "Destroyer", "#player-cell-C-0"); 
        placeShip(robot, "Destroyer", "V", "#player-cell-C-2");
        placeShip(robot, "Destroyer", "V", "#player-cell-C-4");
        placeShip(robot, "Destroyer", "Battleship", "#player-cell-G-0");
        placeShip(robot, "Battleship", "U", "#player-cell-G-3");
        placeShip(robot, "Battleship", "U", "#player-cell-G-6");
        placeShip(robot, "Battleship", "Carrier", "#player-cell-L-0");
        placeShip(robot, "Carrier", "U", "#player-cell-L-5");
    
        WaitForAsyncUtils.waitForFxEvents();
        robot.clickOn("Start");
        FxAssert.verifyThat("Start!", NodeMatchers.isVisible());
        robot.clickOn("#enemy-cell-A-0");
        robot.sleep(2000);

        robot.clickOn("Fire at a square");
        robot.clickOn("Sonar scan");
        robot.clickOn("#enemy-cell-B-5");
        FxAssert.verifyThat("Sonar Scan: 2 / 3", NodeMatchers.isVisible());
        robot.sleep(2000);

        robot.clickOn("Sonar scan");
        robot.clickOn("Move a ship");
        robot.clickOn("#player-cell-A-0");
        robot.clickOn("V");
        robot.clickOn("#player-cell-D-9");
        FxAssert.verifyThat("Move Ship: 2 / 3", NodeMatchers.isVisible());

        
    }


    private void placeShip(FxRobot robot, String currentShip, String targetShip, String cellId) {
        if (!currentShip.equals(targetShip)) {
            robot.clickOn(currentShip);
            robot.clickOn(targetShip);
        }
        robot.clickOn(cellId);
    }

}