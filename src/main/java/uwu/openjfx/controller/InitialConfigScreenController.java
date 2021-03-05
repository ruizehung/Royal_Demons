package uwu.openjfx.controller;

import animatefx.animation.Bounce;
import animatefx.animation.FadeIn;
import animatefx.animation.SlideInLeft;
import animatefx.animation.Tada;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import uwu.openjfx.model.UserSetting;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InitialConfigScreenController implements Initializable {

    @FXML
    private Text title;
    @FXML
    private Label playerNameLabel;
    @FXML
    private Label difficultyLabel;
    @FXML
    private Label startingWeaponLabel;
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
        new Bounce(title).setDelay(Duration.valueOf("1000ms")).play();
        new Bounce(playerNameLabel).setDelay(Duration.valueOf("1000ms")).play();
        new Bounce(difficultyLabel).setDelay(Duration.valueOf("1000ms")).play();
        new Bounce(startingWeaponLabel).setDelay(Duration.valueOf("1000ms")).play();
        new Bounce(playerNameField).setDelay(Duration.valueOf("1000ms")).play();
        new Bounce(difficultyComboBox).setDelay(Duration.valueOf("1000ms")).play();
        new Bounce(startingWeaponComboBox).setDelay(Duration.valueOf("1000ms")).play();
        new Bounce(letsGoButton).setDelay(Duration.valueOf("1000ms")).play();
        new Bounce(goBackButton).setDelay(Duration.valueOf("1000ms")).play();

        letsGoButton.setOnMouseEntered(e -> new Tada(letsGoButton).play());
        goBackButton.setOnMouseEntered(e -> new Tada(goBackButton).play());
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
            String fxmlLocation = "/uwu/openjfx/view/initialGameScreenView.fxml";
            root = FXMLLoader.load(getClass().getResource(fxmlLocation));
            Scene scene = new Scene(root);
            String cssLocation = "/uwu/openjfx/css/styles.css";
            scene.getStylesheets().add(getClass().getResource(cssLocation)
                    .toExternalForm());
            SceneSwapController.addScene("initGame", scene);
            stage.setScene(scene);
            new FadeIn(root).play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleGoBack() {
        Stage stage = (Stage) letsGoButton.getScene().getWindow();
        Scene welcomeScene = SceneSwapController.getScene("welcome");
        stage.setScene(welcomeScene);
        new SlideInLeft(welcomeScene.getRoot()).play();
    }

    @FXML
    public void handleDifficultyChange() {
        UserSetting.setDifficulty((String) difficultyComboBox.getValue());
    }

}
