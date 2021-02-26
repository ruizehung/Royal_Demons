package test.java;

import javafx.scene.Node;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;
import uwu.openjfx.MainApp;

import java.awt.*;

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
}
