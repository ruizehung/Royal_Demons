package uwu.openjfx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class InitialGameScreenController implements Initializable {

    @FXML
    private Label coins;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        switch (UserSetting.difficulty) {
            case "Easy":
                coins.setText("1000");
                break;
            case "Medium":
                coins.setText("800");
                break;
            case "Hard":
                coins.setText("600");
                break;
        }
    }

}