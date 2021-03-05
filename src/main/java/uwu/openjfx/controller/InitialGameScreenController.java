package uwu.openjfx.controller;

import animatefx.animation.Bounce;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import uwu.openjfx.model.UserSetting;

import java.net.URL;
import java.util.ResourceBundle;

public class InitialGameScreenController implements Initializable {

    @FXML
    private Label coins;
    @FXML
    private Pane coinPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new Bounce(coinPane).setDelay(Duration.valueOf("1000ms")).play();
        switch (UserSetting.getDifficulty()) {
        case "Easy":
            coins.setText("1000");
            break;
        case "Medium":
            coins.setText("800");
            break;
        case "Hard":
            coins.setText("600");
            break;
        default:
        }
    }

}