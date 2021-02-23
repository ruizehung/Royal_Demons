package uwu.openjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;


public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        //region BACKGROUND
        StackPane head = new StackPane();

        InputStream is = Files.newInputStream(Paths.get("src/main/resources/images/WelcomeScreen.png"));
        Image img = new Image(is);
        is.close();

        ImageView imgView = new ImageView(img);
        //endregion

        //region GAMETITLE + START BTN
        BorderPane borderPane = new BorderPane();
        Text title = new Text("Royal Demons");
        title.setFill(Color.GOLD);
        title.setFont(Font.font("Old English Text MT", FontWeight.BLACK, FontPosture.REGULAR, 90));
        title.setStyle("-fx-fill: ffb900; -fx-stroke: black; -fx-stroke-width: 2px");

        Button startBtn = new Button("START");
        startBtn.setPrefHeight(50);
        startBtn.setPrefWidth(150);
        startBtn.setStyle("-fx-font-weight: bold; -fx-background-color: #634801; " +
                "-fx-background-radius: 22; -fx-font-size: 30; -fx-text-fill: ffb900");

        borderPane.setMargin(title, new Insets(80, 0, 0, 0));
        borderPane.setMargin(startBtn, new Insets(0, 0, 92, 0));

        borderPane.setAlignment(title, Pos.CENTER);
        borderPane.setAlignment(startBtn, Pos.CENTER);
        borderPane.setTop(title);
        borderPane.setBottom(startBtn);
        //endregion

        head.getChildren().addAll(imgView, borderPane);

//        Parent root = FXMLLoader.load(getClass().getResource("scene.fxml"));
        
        Scene scene = new Scene(head);
//        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        
        stage.setTitle("JavaFX and Gradle");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
