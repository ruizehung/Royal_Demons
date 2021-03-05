package test.java;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.ComboBoxMatchers;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TextMatchers;
import org.testfx.util.WaitForAsyncUtils;
import uwu.openjfx.MainApp;

import java.util.concurrent.TimeUnit;

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
        FxAssert.verifyThat(".button", LabeledMatchers.hasText("START"));
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
        robot.clickOn(".button");
        WaitForAsyncUtils.sleep(1000, TimeUnit.MILLISECONDS);
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
        robot.clickOn(".button");

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
        robot.clickOn(".button");
        //change name
        WaitForAsyncUtils.sleep(1000, TimeUnit.MILLISECONDS);
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
        robot.clickOn(".button");
        //change name
        WaitForAsyncUtils.sleep(1000, TimeUnit.MILLISECONDS);
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
        robot.clickOn(".button");
        //check that the weapons combo box has three options
        FxAssert.verifyThat("#startingWeaponComboBox", ComboBoxMatchers.hasItems(3));
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
        robot.clickOn(".button");
        // Check if the starting weapon combo box contains all the correct weapons
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
        robot.clickOn(".button");
        // Check if difficulty is set to easy by default
        FxAssert.verifyThat("#difficultyComboBox",
                ComboBoxMatchers.hasSelectedItem("Easy"));
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
        robot.clickOn(".button");
        //change name
        WaitForAsyncUtils.sleep(1000, TimeUnit.MILLISECONDS);
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
        robot.clickOn(".button");
        //check if the game difficulty combo box contains all appropriate levels,
        //easy, medium, and hard
        FxAssert.verifyThat("#difficultyComboBox",
                ComboBoxMatchers.containsItems("Easy", "Medium", "Hard"));
    }
}
