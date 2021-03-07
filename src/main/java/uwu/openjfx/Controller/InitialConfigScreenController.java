package uwu.openjfx.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import uwu.openjfx.Model.GameState;
import uwu.openjfx.Model.UserSetting;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InitialConfigScreenController implements Initializable {

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
    }

    @FXML
    public void handleKeyReleased() {
        String playerName = playerNameField.getText();
        letsGoButton.setDisable(playerName == null || playerName.isEmpty()
                || playerName.trim().isEmpty());
    }

    @FXML
    public void handleLetsGo() {
        // set current room to initial room
        GameState.getInstance().setCurrentRoom(GameState.getInstance().getGameMap().getInitialRoom());

        Stage stage = (Stage) letsGoButton.getScene().getWindow();
        try {
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/uwu/openjfx/fxml/roomView.fxml")));
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleDifficultyChange() {
        UserSetting.setDifficulty((String) difficultyComboBox.getValue());
    }
}
