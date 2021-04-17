package uwu.openjfx.input;

import com.almasb.fxgl.cutscene.Cutscene;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import javafx.util.Duration;
import uwu.openjfx.MapGeneration.GameMap;
import uwu.openjfx.components.PlayerComponent;

import java.util.ArrayList;
import java.util.Arrays;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;

public class TeleportToBossRoom extends UserAction {
    public TeleportToBossRoom(String name) {
        super(name);
    }

    @Override
    protected void onActionBegin() {
        GameMap gameMap = FXGL.geto("gameMap");
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
        FXGL.getSceneService().getTimer().runOnceAfter(() -> FXGL.getCutsceneService()
                .startCutscene(
                        new Cutscene(bossFight)), Duration.millis(1500));
    }
}
