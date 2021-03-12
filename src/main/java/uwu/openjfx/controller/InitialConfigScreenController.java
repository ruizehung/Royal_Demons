package uwu.openjfx.controller;

import animatefx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.util.Duration;
import uwu.openjfx.model.GameState;
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

        playerNameField.setOnMouseEntered(e -> new Shake(playerNameField).play());
        difficultyComboBox.setOnMouseEntered(e -> new Shake(difficultyComboBox).play());
        startingWeaponComboBox.setOnMouseEntered(e -> new Shake(startingWeaponComboBox).play());

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
        // set current room to initial room
        GameState.getInstance().setCurrentRoom(GameState.getInstance()
                .getGameMap().getInitialRoom());
        Parent root;
        try {
            String fxmlLocation = "/uwu/openjfx/view/roomView.fxml";
            root = FXMLLoader.load(getClass().getResource(fxmlLocation));
            Scene scene = new Scene(root);
            SceneSwapController.addScene("initGame", scene);
            SceneSwapController.getMainStage().setScene(SceneSwapController.getScene("initGame"));
            new FadeIn(root).play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleGoBack() {
        Scene welcomeScene = SceneSwapController.getScene("welcome");
        SceneSwapController.getMainStage().setScene(welcomeScene);
        new FadeIn(welcomeScene.getRoot()).play();
    }

    @FXML
    public void handleDifficultyChange() {
        UserSetting.setDifficulty((String) difficultyComboBox.getValue());
    }

}
