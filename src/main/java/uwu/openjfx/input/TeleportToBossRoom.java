package uwu.openjfx.input;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.ui.ProgressBar;
import javafx.scene.paint.Color;
import uwu.openjfx.MapGeneration.GameMap;
import uwu.openjfx.MapGeneration.Room;
import uwu.openjfx.components.BossComponent;
import uwu.openjfx.components.PlayerComponent;

import java.util.ArrayList;
import java.util.Arrays;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

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
        });
        ArrayList<String> bossFight = new ArrayList<>(Arrays.asList(
                "1.name = " + PlayerComponent.getPlayerName(),
                "2.name = boss guy",
                "1.image = ui_yoshi.png",
                "2.image = ui_peach.png",
                "1: where mah boo?",
                "2: theyre NOT behind the only door in the room,"
                        + "to which i ate the key and have been constipated"
                        + " for some time now MUAHAHAHA",
                "1: thats gross man..."
        ));
//        FXGL.getSceneService().getTimer().runOnceAfter(() -> FXGL.getCutsceneService()
//                .startCutscene(
//                        new Cutscene(bossFight)), Duration.millis(1500));
        getAudioPlayer().stopMusic(FXGL.getAssetLoader().loadMusic("evil4.mp3"));
        loopBGM("boss/boss_battle_ 2.mp3");

        ProgressBar bossHealth = new ProgressBar();
        bossHealth.setBackgroundFill(Color.RED);
        bossHealth.setHeight(25);
        bossHealth.setMinValue(0);
        bossHealth.setMaxValue(100);
        bossHealth.setTranslateX(FXGL.getAppWidth() / 2.0
            - bossHealth.getBackgroundBar().getWidth() / 2);
        bossHealth.setTranslateY(12.5);
        bossHealth.currentValueProperty().bind(BossComponent.getBossHealthProperty());
        FXGL.getGameScene().addUINodes(bossHealth);
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
