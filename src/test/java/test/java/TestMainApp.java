package test.java;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;
import uwu.openjfx.MainApp;

@ExtendWith(ApplicationExtension.class)
public class TestMainApp {

    /**
     * Will be called with {@code @Before} semantics, i. e. before each test method.
     *
     * @param stage - Will be injected by the test runner.
     */
    @Start
    private void start(Stage stage) throws Exception {
        MainApp game = new MainApp();
        game.start(stage);
    }

    /**
     * @param robot - Will be injected by the test runner.
     */
    @Test
    void should_contain_start_button_with_text(FxRobot robot) {
        FxAssert.verifyThat(".button", LabeledMatchers.hasText("START"));
    }

    /**
     * @param robot - Will be injected by the test runner.
     */
//    @Test
//    void when_button_is_clicked_scene_changes(FxRobot robot) {
//        // when:
//        robot.clickOn(".button");
//
//        // then:
//        FxAssert.verifyThat(button, LabeledMatchers.hasText("clicked!"));
//        // or (lookup by css id):
//        FxAssert.verifyThat("#myButton", LabeledMatchers.hasText("clicked!"));
//        // or (lookup by css class):
//        FxAssert.verifyThat(".button", LabeledMatchers.hasText("clicked!"));
//    }
}
