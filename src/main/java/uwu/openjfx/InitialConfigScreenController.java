package uwu.openjfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InitialConfigScreenController implements Initializable {

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
    }

    @FXML
    public void handleKeyReleased() {
        String playerName = playerNameField.getText();
        letsGoButton.setDisable(playerName.isEmpty() || playerName.trim().isEmpty());
    }

    @FXML
    public void handleLetsGo(ActionEvent event) {
        Stage stage = (Stage) letsGoButton.getScene().getWindow();
        // transition to initial game screen ???
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("initialGameScreen.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("CSS/initialGameScreenStyles.css").toExternalForm());
        stage.setScene(scene);
    }

    //    @FXML
//    public void handleGoBack(ActionEvent event) {
//        Stage stage = (Stage) letsGoButton.getScene().getWindow();
//        Parent root = null;
//        try {
//            root = FXMLLoader.load(getClass().getResource("initialGameScreen.fxml"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Scene scene = new Scene(root);
//        scene.getStylesheets().add(getClass().getResource("CSS/initialGameScreenStyles.css").toExternalForm());
//        stage.setScene(scene);
//
//    }
    @FXML
    public void handleDifficultyChange() {
        UserSetting.difficulty = (String) difficultyComboBox.getValue();
        System.out.println((String) difficultyComboBox.getValue());
    }

}
