package uwu.openjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import uwu.openjfx.controller.WelcomeScreenController;
import uwu.openjfx.model.UserSetting;


public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        WelcomeScreenController welcomeScreen = new WelcomeScreenController(stage);
        Scene welcomeScene = welcomeScreen.getWelcomeScene();
        //stage.setTitle("Royal Demons");
        stage.setScene(welcomeScene);
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
        stage.setResizable(false);
        stage.show();

        UserSetting.reset();
    }

    public static void main(String[] args) {
        launch(args);
    }

}