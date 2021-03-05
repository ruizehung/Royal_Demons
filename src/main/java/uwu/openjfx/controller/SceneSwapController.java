package uwu.openjfx.controller;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.HashMap;

public class SceneSwapController {
    private static HashMap<String, Scene> scenes = new HashMap<>();

    public static void addScene(String name, Scene scene) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                Stage stage = (Stage) scene.getWindow();
                stage.close();
            }
            event.consume();
        });
        scenes.put(name, scene);
    }

    public static Scene getScene(String name) {
        return scenes.get(name);
    }
}
