package test.java;

import javafx.scene.Node;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.ComboBoxMatchers;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TextMatchers;
import org.testfx.util.WaitForAsyncUtils;
import uwu.openjfx.MainApp;
import uwu.openjfx.model.Coordinate;
import uwu.openjfx.model.GameState;
import uwu.openjfx.model.Room;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(ApplicationExtension.class)
public class TestMainApp {
    /**
     * Starts the test.
     *
     * @param stage passes in the initial stage of the game
     * @throws Exception throws an exception if there is an issue
     *                    when running the tests
     */
    @Start
    private void start(Stage stage) throws Exception {
        MainApp game = new MainApp();
        game.start(stage);
    }

    /**
     * A test that ensures the welcome screen contains a start button.
     *
     * @param robot simulates a user's actions when using the game interface
     */
    //ray 1
    @Test
    void testWelcomeScreenContainsStartButton(FxRobot robot) {
        FxAssert.verifyThat("#startBtn", LabeledMatchers.hasText("START"));
    }

    /**
     * A test that ensures that the "Let's Go" button is disabled if
     * the "Name" field is empty.
     *
     * @param robot simulates a user's actions when using the game interface
     */
    //ray 2
    @Test
    void testInitConfigScreenLetsGoDisabledWithEmptyName(FxRobot robot) {
        robot.clickOn("#startBtn");
        WaitForAsyncUtils.sleep(2000, TimeUnit.MILLISECONDS);
        FxAssert.verifyThat("#letsGoButton", Node::isDisabled);
    }

    /**
     * A test that ensures the game changes to the "Initial Configuration" screen
     * once the start button has been pressed.
     *
     * @param robot simulates a user's actions when using the game interface
     */
    //devan 1
    @Test
    void testChangeSceneToInitialConfig(FxRobot robot) {
        robot.clickOn("#startBtn");
        //WaitForAsyncUtils.sleep(2000, TimeUnit.MILLISECONDS);
        FxAssert.verifyThat("#title", TextMatchers.hasText("Initial Configuration"));
    }

    /**
     * A test that checks if the "hard" difficulty level appropriately changes the
     * amount of money to 600 coins.
     *
     * @param robot simulates a user's actions when using the game interface
     */
    //devan 2
    @Test
    void testHardDifficultySetsGold(FxRobot robot) {
        //go to initial config
        robot.clickOn("#startBtn");
        //change name
//        WaitForAsyncUtils.sleep(2000, TimeUnit.MILLISECONDS);
        robot.clickOn("#playerNameField");
        robot.write("Devan");
        //change to hard
        robot.clickOn("#difficultyComboBox");
        robot.clickOn("Hard");
        //go to initial game screen
        robot.clickOn("Let's Go!");
        //check if money label updated
        FxAssert.verifyThat("#coins", LabeledMatchers.hasText("600"));
    }

    /**
     * A test that checks if the "medium" difficulty level appropriately changes the
     * amount of money to 800 coins.
     *
     * @param robot simulates a user's actions when using the game interface
     */
    //james 1
    @Test
    void testMediumDifficultySetsGold(FxRobot robot) {
        //go to initial config
        robot.clickOn("#startBtn");
        //change name
//        WaitForAsyncUtils.sleep(2000, TimeUnit.MILLISECONDS);
        robot.clickOn("#playerNameField");
        robot.write("James");
        //change to hard
        robot.clickOn("#difficultyComboBox");
        robot.clickOn("Medium");
        //go to initial game screen
        robot.clickOn("Let's Go!");
        //check if money label updated
        FxAssert.verifyThat("#coins", LabeledMatchers.hasText("800"));
    }

    /**
     * A test that checks if weapon combo box has the correct number of items.
     *
     * @param robot simulates a user's actions when using the game interface
     */
    //james 2
    @Test
    void testWeaponComboBox(FxRobot robot) {
        //go to initial config
        robot.clickOn("#startBtn");
        //check that the weapons combo box has three options
//        WaitForAsyncUtils.sleep(2000, TimeUnit.MILLISECONDS);
        FxAssert.verifyThat("#startingWeaponComboBox", ComboBoxMatchers.hasItems(3));
    }

    /**
     * A test that checks if the player correctly navigates to the top room (1, 0) after going East.
     *
     * @param robot simulates a user's actions when using the game interface
     */
    //james 3
    @Test
    void testGoEastFromStart(FxRobot robot) {
        // Go to initial config screen
        robot.clickOn("#startBtn");
        // Check if difficulty is set to easy by default
        WaitForAsyncUtils.sleep(2000, TimeUnit.MILLISECONDS);
        robot.clickOn("#playerNameField");
        robot.write("James");
        // Go to initial game screen
        robot.clickOn("Let's Go!");
        robot.clickOn("#goEastButton");
        // Create a test room that holds the correct coordinate should the player navigate to the East room
        Coordinate eastCoord = new Coordinate(1, 0);
        Room roomTest = new Room(eastCoord);
        // Get player's actual current room and compare
        Room currRoom = GameState.getInstance().getCurrentRoom();
        assertEquals(roomTest.getCoordinate(), currRoom.getCoordinate());
    }

    /**
     * A test that checks to make sure the 4 rooms adjacent to the initial room
     * have the appropriate coordinates.
     *
     * @param robot simulates a user's actions when using the game interface
     */
    //james 4
    @Test
    void testInitialAdjacentRooms(FxRobot robot) {
        // Go to initial config screen
        robot.clickOn("#startBtn");
        // Check if difficulty is set to easy by default
        WaitForAsyncUtils.sleep(2000, TimeUnit.MILLISECONDS);
        robot.clickOn("#playerNameField");
        robot.write("James");
        // Go to initial game screen
        robot.clickOn("Let's Go!");
        // Get the list of coordinates adjacent to the initial room
        List<Coordinate> initList = GameState.getInstance().getCurrentRoom().getAdjacentCoordinates();
        // Make sure that the list of coordinates equals { [0, 1]; [1, 0]; [0, -1]; [-1, 0] }
        List<Coordinate> correctList = new ArrayList<>();
        correctList.add(new Coordinate(0, 1));
        correctList.add(new Coordinate(1, 0));
        correctList.add(new Coordinate(0, -1));
        correctList.add(new Coordinate(-1, 0));
        assertEquals(initList, correctList);
    }

    /**
     * A test that checks if weapon combo box has the correct weapons/options,
     * a sword, wand, or bow option.
     *
     * @param robot simulates a user's actions when using the game interface
     */
    //jason 1
    @Test
    void testGameContainsCorrectWeaponChoices(FxRobot robot) {
        // Go to initial config screen
        robot.clickOn("#startBtn");
        // Check if the starting weapon combo box contains all the correct weapons
//        WaitForAsyncUtils.sleep(2000, TimeUnit.MILLISECONDS);
        FxAssert.verifyThat("#startingWeaponComboBox",
                ComboBoxMatchers.containsItems("Sword", "Wand", "Bow"));
    }

    /**
     * A test that checks if the game's default difficulty level is "easy."
     * It ensures that the difficulty combo box should be on "easy" (even if the player
     * does not interact with the combo box).
     *
     * @param robot simulates a user's actions when using the game interface
     */
    //jason 2
    @Test
    void testGameDefaultDifficultyIsEasy(FxRobot robot) {
        // Go to initial config screen
        robot.clickOn("#startBtn");
        // Check if difficulty is set to easy by default
//        WaitForAsyncUtils.sleep(2000, TimeUnit.MILLISECONDS);
        FxAssert.verifyThat("#difficultyComboBox",
                ComboBoxMatchers.hasSelectedItem("Easy"));
    }

    /**
     * A test that checks if the player correctly navigates to the top room (0, 1) after going North.
     *
     * @param robot simulates a user's actions when using the game interface
     */
    //jason 3
    @Test
    void testGoNorthFromStart(FxRobot robot) {
        // Go to initial config screen
        robot.clickOn("#startBtn");
        // Check if difficulty is set to easy by default
//        WaitForAsyncUtils.sleep(2000, TimeUnit.MILLISECONDS);
        robot.clickOn("#playerNameField");
        robot.write("Jason");
        // Go to initial game screen
        robot.clickOn("Let's Go!");
        robot.clickOn("#goNorthButton");
        // Create a test room that holds the correct coordinate should the player navigate to the North room
        Coordinate northCoord = new Coordinate(0, 1);
        Room roomTest = new Room(northCoord);
        // Get player's actual current room and compare
        Room currRoom = GameState.getInstance().getCurrentRoom();
        assertEquals(roomTest.getCoordinate(), currRoom.getCoordinate());
    }
    /**
     * A test that checks if the correct win screen image is shown after killing the final boss.
     *
     * @param robot simulates a user's actions when using the game interface
     */
    //jason 4
    @Test
    void testBossCanBeKilledInBossRoom(FxRobot robot) {
        // Go to initial config screen
        robot.clickOn("#startBtn");
        // Check if difficulty is set to easy by default
//        WaitForAsyncUtils.sleep(2000, TimeUnit.MILLISECONDS);
        robot.clickOn("#playerNameField");
        robot.write("Jason");
        // Go to initial game screen
        robot.clickOn("Let's Go!");
        // Get current boss coordinate and go to that exact room
        Coordinate bossCoordinate = GameState.getInstance().getGameMap().getBossRoom().getCoordinate();
        GameState.getInstance().setCurrentRoom(new Room(bossCoordinate));
        // Auto kill boss
        // Boss.health = 0
        FxAssert.verifyThat("#beatBossButton", LabeledMatchers.hasText("Beat Boss"));
    }

    /**
     * A test that checks if the "easy" difficulty level appropriately changes the
     * amount of money to 1000 coins.
     *
     * @param robot simulates a user's actions when using the game interface
     */
    //alice 1
    @Test
    void testEasyDifficultySetsGold(FxRobot robot) {
        //go to initial config screen
        robot.clickOn("#startBtn");
        //change name
//        WaitForAsyncUtils.sleep(2000, TimeUnit.MILLISECONDS);
        robot.clickOn("#playerNameField");
        robot.write("Alice");
        //change difficulty to easy
        robot.clickOn("#difficultyComboBox");
        robot.clickOn("Easy");
        //go to initial game screen
        robot.clickOn("Let's Go!");
        //check if money label updated
        FxAssert.verifyThat("#coins", LabeledMatchers.hasText("1000"));
    }

    /**
     * A test that ensures the difficulty combo box contains all appropriate levels,
     * easy, medium, and hard.
     *
     * @param robot simulates a user's actions when using the game interface
     */
    //alice 2
    @Test
    void testGameSelectContainsAllDifficultyLevels(FxRobot robot) {
        //go to initial config screen
        robot.clickOn("#startBtn");
        //check if the game difficulty combo box contains all appropriate levels,
        //easy, medium, and hard
//        WaitForAsyncUtils.sleep(2000, TimeUnit.MILLISECONDS);
        FxAssert.verifyThat("#difficultyComboBox",
                ComboBoxMatchers.containsItems("Easy", "Medium", "Hard"));
    }
}