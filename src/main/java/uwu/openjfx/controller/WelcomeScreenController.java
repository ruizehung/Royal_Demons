package uwu.openjfx.controller;

import animatefx.animation.*;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WelcomeScreenController {
    private final Scene welcomeScene;
    private final Stage stage;

    public WelcomeScreenController(Stage stage) {
        welcomeScene = getWelcomeScene();
        SceneSwapController.addScene("welcome", welcomeScene);
        this.stage = stage;
    }
    public Scene getWelcomeScene() {
        //region BACKGROUND
        StackPane head = new StackPane();

        ImageView imgView = new ImageView();
        try {
            InputStream is = Files.newInputStream(
                    Paths.get("src/main/resources/uwu/openjfx/images/dungeon_bg.png"));
            Image img = new Image(is);
            is.close();
            imgView = new ImageView(img);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //endregion

        Font.loadFont(getClass().getResourceAsStream(
                        "/uwu/openjfx/fonts/AkayaTelivigala-Regular.ttf"), 90);

        //region GAMETITLE + START BTN
        BorderPane borderPane = new BorderPane();
        Text title = new Text("Royal Demons");
        title.setId("title");
        new Bounce(title).setDelay(Duration.valueOf("1000ms")).play();

        Button startBtn = new Button("START");
        startBtn.setId("btn");
        startBtn.setPrefHeight(50);
        startBtn.setPrefWidth(150);
        startBtn.setAlignment(Pos.BASELINE_RIGHT);
        new Bounce(startBtn).setDelay(Duration.valueOf("1000ms")).play();
        startBtn.setOnAction(event -> {
            // transition to initial configuration screen
            if (SceneSwapController.getScene("initConfig") == null) {
                Parent root = null;
                try {
                    String fxmlLocation = "/uwu/openjfx/view/initialConfigScreenView.fxml";
                    root = FXMLLoader.load(getClass().getResource(fxmlLocation));
                    Scene initialConfigScene = new Scene(root);
                    SceneSwapController.addScene("initialConfig", initialConfigScene);
                    stage.setScene(initialConfigScene);
                    new SlideInRight(root).play();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Scene initConfigScene = SceneSwapController.getScene("initConfig");
                stage.setScene(initConfigScene);
                new SlideInRight(initConfigScene.getRoot()).play();
            }
        });
        startBtn.setOnMouseEntered(e -> new Tada(startBtn).play());

        Button exitBtn = new Button("EXIT");
        exitBtn.setId("btn");
        exitBtn.setPrefHeight(50);
        exitBtn.setPrefWidth(150);
        exitBtn.setAlignment(Pos.BASELINE_LEFT);
        new Bounce(exitBtn).setDelay(Duration.valueOf("1000ms")).play();
        exitBtn.setOnAction(event -> {
            Stage stage = (Stage) exitBtn.getScene().getWindow();
            stage.close();
        });
        exitBtn.setOnMouseEntered(e -> new Tada(exitBtn).play());

        HBox buttons = new HBox();
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(startBtn, exitBtn);
        buttons.setSpacing(15);

        BorderPane.setMargin(title, new Insets(50, 0, 0, 0));
        BorderPane.setMargin(buttons, new Insets(0, 0, 92, 0));

        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setAlignment(buttons, Pos.CENTER_RIGHT);
        borderPane.setTop(title);
        borderPane.setBottom(buttons);
        //endregion

        head.getChildren().addAll(imgView, borderPane);

        Scene scene = new Scene(head, 960, 640);
        scene.getStylesheets().add(getClass().getResource(
                "/uwu/openjfx/css/styles.css").toExternalForm());
        new BounceIn(head).play();
        return scene;
    }
}
