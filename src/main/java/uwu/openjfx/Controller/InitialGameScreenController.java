package uwu.openjfx.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import uwu.openjfx.Model.UserSetting;

import java.net.URL;
import java.util.ResourceBundle;

public class InitialGameScreenController implements Initializable {

    @FXML
    private Label coins;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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