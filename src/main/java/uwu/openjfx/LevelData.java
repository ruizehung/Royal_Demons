package uwu.openjfx;

import com.almasb.fxgl.core.serialization.Bundle;

import java.util.HashMap;
import java.util.Map;

public class LevelData {
    private static LevelData levelData = new LevelData();
    private Map<String, Bundle> rooms;

    private LevelData() {
        rooms = new HashMap<>();
    }

}
