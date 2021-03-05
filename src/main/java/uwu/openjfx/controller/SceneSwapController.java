package uwu.openjfx.controller;

import javafx.scene.Scene;

import java.util.HashMap;

public class SceneSwapController {
    private static HashMap<String, Scene> scenes = new HashMap<>();

    public static void addScene(String name, Scene scene) {
        scenes.put(name, scene);
    }

    public static Scene getScene(String name) {
        return scenes.get(name);
    }
}
