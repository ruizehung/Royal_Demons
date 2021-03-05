package uwu.openjfx.controller;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.HashMap;

public class SceneSwapController {
    private static HashMap<String, Scene> scenes = new HashMap<>();
    private static Stage mainStage;
    private static Pane bgPane;
    private static Pane uiPane;


    public static void init() {
        Pane bgPane = new Pane();
        bgPane.setStyle("-fx-background-color: black");
        SceneSwapController.bgPane = bgPane;

        Font.loadFont(SceneSwapController.class.getResourceAsStream(
                "/uwu/openjfx/fonts/AkayaTelivigala-Regular.ttf"), 90);
    }

    public static void addScene(String name, Scene scene) {
        StackPane mainPane = new StackPane(bgPane, scene.getRoot());
        //mainPane = new StackPane(bgPane, scene.getRoot(), uiPane);
        Scene mainScene = new Scene(mainPane);
        mainScene.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                Stage stage = (Stage) mainScene.getWindow();
                stage.close();
            }
        });
        scenes.put(name, mainScene);
    }

    public static Scene getScene(String name) {
        return scenes.get(name);
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static void setMainStage(Stage mainStage) {
        SceneSwapController.mainStage = mainStage;
    }
}
