package uwu.openjfx.model;


import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Resources {
    private static Resources instance = new Resources();

    private static String framesPath = "src/main/resources/uwu/openjfx/images/frames";

    private Map<String, Image> frames;

    private Resources() {
        frames = new HashMap<>();
    }

    public static Resources getInstance() {
        return instance;
    }

    public Image get(String fileName) {
        Image image = frames.get(fileName);
        return image;
    }

    public void loadResources() {
        File dir = new File(framesPath);
        InputStream is;
        for (File file : dir.listFiles()) {
            try {
                is = Files.newInputStream(Paths.get(file.getPath()));
                frames.put(file.getName(), new Image(is));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
