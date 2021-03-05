package uwu.openjfx.Model;


import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Resources {
    public static Image[] floors;
    public static Image doors_leaf_closed_32x32;
    public Resources() {
        floors = new Image[8];
        try {
            InputStream is;
            for (int i = 1; i <= 8; ++i) {
                is = Files.newInputStream(
                        Paths.get("src/main/resources/uwu/openjfx/images/frames/floor_" + i + "_32x32.png"));
                floors[i - 1] = new Image(is);
            }

            is = Files.newInputStream(
                    Paths.get("src/main/resources/uwu/openjfx/images/frames/doors_leaf_closed_32x32.png"));
            doors_leaf_closed_32x32 = new Image(is);


            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
