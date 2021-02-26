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
import org.testfx.matcher.control.ListViewMatchers;
import uwu.openjfx.MainApp;

@ExtendWith(ApplicationExtension.class)
public class TestMainApp {

    @Start
    private void start(Stage stage) throws Exception {
        MainApp game = new MainApp();
        game.start(stage);
    }

    //ray 1
    @Test
    void testWelcomeScreenContainsStartButton(FxRobot robot) {
        FxAssert.verifyThat(".button", LabeledMatchers.hasText("START"));
    }

    //ray 2
    @Test
    void testInitConfigScreenLetsGoDisabledWithEmptyName(FxRobot robot) {
        robot.clickOn(".button");
        FxAssert.verifyThat("#letsGoButton", Node::isDisabled);
    }

    //devan 1
    @Test
    void testChangeSceneToInitialConfig(FxRobot robot) {
        robot.clickOn(".button");

        FxAssert.verifyThat(".label", LabeledMatchers.hasText("Initial Configuration"));
    }

    //devan 2
    @Test
    void testHardDifficultySetsGold(FxRobot robot) {
        //go to initial config
        robot.clickOn(".button");
        //change name
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

    //james 1
    @Test
    void testMediumDifficultySetsGold(FxRobot robot) {
        //go to initial config
        robot.clickOn(".button");
        //change name
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

    //james 2
    @Test
    void testWeaponComboBox(FxRobot robot) {
        //go to initial config
        robot.clickOn(".button");
        //check that the weapons combo box has three options
        FxAssert.verifyThat("#startingWeaponComboBox", ComboBoxMatchers.hasItems(3));
    }

    //jason 1
    @Test
    void testGameContainsCorrectWeaponChoices(FxRobot robot) {
        // Go to initial config screen
        robot.clickOn(".button");
        // Check if the starting weapon combo box contains all the correct weapons
        FxAssert.verifyThat("#startingWeaponComboBox",
                ComboBoxMatchers.containsItems("Sword", "Wand", "Bow"));
    }
    //jason 2
    @Test
    void testGameDefaultDifficultyIsEasy(FxRobot robot) {
        // Go to initial config screen
        robot.clickOn(".button");
        // Check if difficulty is set to easy by default
        FxAssert.verifyThat("#difficultyComboBox",
                ComboBoxMatchers.hasSelectedItem("Easy"));
    }

    //alice 1
    @Test
    void testEasyDifficultySetsGold(FxRobot robot) {
        //go to initial config screen
        robot.clickOn(".button");
        //change name
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
