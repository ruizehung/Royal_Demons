package uwu.openjfx.Controller;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import uwu.openjfx.MainApp;
import uwu.openjfx.Model.Coordinate;
import uwu.openjfx.Model.GameState;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WelcomeScreenController {
    private final Scene welcomeScene;
    private final Stage stage;

    public WelcomeScreenController(Stage stage) {
        welcomeScene = getWelcomeScene();
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
        title.setId("welcomeTitle");


        Button startBtn = new Button("START");
        startBtn.setId("startBtn");
        startBtn.setPrefHeight(50);
        startBtn.setPrefWidth(150);
        startBtn.setOnAction(event -> {
            // transition to initial configuration screen
            Stage stage = (Stage) startBtn.getScene().getWindow();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/uwu/openjfx/fxml/initialConfigScreen.fxml"));
                Scene scene = new Scene(loader.load());

                // pass initial room to RoomController
                InitialConfigScreenController initialConfigScreenController = loader.getController();

                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }




            Parent root = null;
            try {
                String fxmlLocation = "/uwu/openjfx/fxml/initialConfigScreen.fxml";
                root = FXMLLoader.load(getClass().getResource(fxmlLocation));
                Scene initialConfigScene = new Scene(root);
                stage.setScene(initialConfigScene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        BorderPane.setMargin(title, new Insets(50, 0, 0, 0));
        BorderPane.setMargin(startBtn, new Insets(0, 0, 92, 0));

        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setAlignment(startBtn, Pos.CENTER);
        borderPane.setTop(title);
        borderPane.setBottom(startBtn);
        //endregion

        head.getChildren().addAll(imgView, borderPane);

        Scene scene = new Scene(head, 960, 640);
        scene.getStylesheets().add(getClass().getResource(
                "/uwu/openjfx/css/styles.css").toExternalForm());

        return scene;
    }
}
