package uwu.openjfx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

public class InitialConfigScreenController  implements Initializable {

    @FXML
    private Label title;
    @FXML
    private TextField playerNameField;
    @FXML
    private ComboBox difficultyComboBox;
    @FXML
    private ComboBox startingWeaponComboBox;
    @FXML
    private Button letsGoButton;
    @FXML
    private Button goBackButton;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        letsGoButton.setDisable(true);
        System.out.println(javafx.scene.text.Font.getFamilies());
    }

    @FXML
    public void handleKeyReleased() {
        String playerName = playerNameField.getText();
        letsGoButton.setDisable(playerName.isEmpty() || playerName.trim().isEmpty());
    }


}
