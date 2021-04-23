package uwu.openjfx.input;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import uwu.openjfx.MapGeneration.GameMap;
import uwu.openjfx.MapGeneration.Room;
import uwu.openjfx.UI;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;

public class TeleportToBossRoom extends UserAction {
    public TeleportToBossRoom(String name) {
        super(name);
    }

    @Override
    public void onActionBegin() {
        GameMap gameMap = FXGL.geto("gameMap");
        if (!canProceed(gameMap)) {
            return;
        }
        getGameScene().getViewport().fade(() -> {
            gameMap.loadRoom(gameMap.getBossRoom(), "west");
            UI.initBossCutsceneAndUI();
        });
    }

    public boolean canProceed(GameMap gameMap) {
        int challengeRoomsVisited = 0;
        for (Room room : gameMap.getRooms().values()) {
            if (room.getRoomType().equals("challengeRoom") && room.visited()) {
                ++challengeRoomsVisited;
            }
        }
        return challengeRoomsVisited >= 2;
    }
}
