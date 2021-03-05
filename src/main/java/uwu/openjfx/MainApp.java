package uwu.openjfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import uwu.openjfx.controller.WelcomeScreenController;
import uwu.openjfx.model.UserSetting;


public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        WelcomeScreenController welcomeScreen = new WelcomeScreenController(stage);
        Scene welcomeScene = welcomeScreen.getWelcomeScene();
        stage.setTitle("Royal Demons");
        stage.setScene(welcomeScene);
        stage.show();

        UserSetting.reset();
    }

    public static void main(String[] args) {
        launch(args);
    }

}