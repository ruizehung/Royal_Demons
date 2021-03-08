package uwu.openjfx.controller;

import animatefx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeScreenController implements Initializable {
    @FXML
    private Text title;
    @FXML
    private Button startBtn;
    @FXML
    private Button exitBtn;

    public void initialize(URL url, ResourceBundle rb) {
        new Bounce(title).setDelay(Duration.valueOf("1000ms")).play();
        new Bounce(startBtn).setDelay(Duration.valueOf("1000ms")).play();
        startBtn.setOnMouseEntered(e -> new Tada(startBtn).play());
        new Bounce(exitBtn).setDelay(Duration.valueOf("1000ms")).play();
        exitBtn.setOnMouseEntered(e -> new Tada(exitBtn).play());
    }

    @FXML
    public void handleStart() {
        if (SceneSwapController.getScene("initConfig") == null) {
            Parent root = null;
            try {
                String fxmlLocation = "/uwu/openjfx/view/initialConfigScreenView.fxml";
                root = FXMLLoader.load(getClass().getResource(fxmlLocation));
                Scene initialConfigScene = new Scene(root);
                SceneSwapController.addScene("initialConfig", initialConfigScene);
                SceneSwapController.getMainStage().setScene(SceneSwapController.getScene("initialConfig"));
                new SlideInRight(root).play();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Scene initConfigScene = SceneSwapController.getScene("initConfig");
            SceneSwapController.getMainStage().setScene(initConfigScene);
            new SlideInRight(initConfigScene.getRoot()).play();
        }
    }

    @FXML
    public void handleExit() {
        Stage stage = (Stage) exitBtn.getScene().getWindow();
        stage.close();
    }

}
