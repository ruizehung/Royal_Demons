package uwu.openjfx.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import uwu.openjfx.Model.Coordinate;
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
    @FXML
    private Button mapButton;

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
        Stage stage = (Stage) letsGoButton.getScene().getWindow();
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/uwu/openjfx/fxml/roomView.fxml"));
            Scene scene = new Scene(loader.load());

            // pass initial room to RoomController
            RoomController roomController = loader.getController();
            System.out.println(GameState.getGameMap().getRoom(new Coordinate(0, 0)));
            roomController.setRoom(GameState.getGameMap().getRoom(new Coordinate(0, 0)));

            scene.getStylesheets().add(getClass().getResource("/uwu/openjfx/css/styles.css")
                    .toExternalForm());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleDifficultyChange() {
        UserSetting.setDifficulty((String) difficultyComboBox.getValue());
    }

    @FXML
    public void handleViewMap() {
        Stage stage = (Stage) mapButton.getScene().getWindow();

        GameMapScene gameMapScene = new GameMapScene(GameState.getGameMap());
        stage.setScene(gameMapScene.getScene());
    }

}
