package uwu.openjfx;

import animatefx.animation.BounceIn;
import animatefx.animation.SlideInRight;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import uwu.openjfx.controller.SceneSwapController;
import uwu.openjfx.controller.WelcomeScreenController;
import uwu.openjfx.model.UserSetting;

import java.io.IOException;


public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        SceneSwapController.setMainStage(stage);
        SceneSwapController.init();

        //stage.setTitle("Royal Demons");

        stage.initStyle(StageStyle.TRANSPARENT);
        Parent root = null;
        try {
            String fxmlLocation = "/uwu/openjfx/view/welcomeScreenView.fxml";
            root = FXMLLoader.load(getClass().getResource(fxmlLocation));
            Scene welcomeScene = new Scene(root);
            SceneSwapController.addScene("welcome", welcomeScene);
            stage.setScene(SceneSwapController.getScene("welcome"));
            new BounceIn(root).play();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setResizable(false);
        stage.show();

        UserSetting.reset();
    }

    public static void main(String[] args) {
        launch(args);
    }

}