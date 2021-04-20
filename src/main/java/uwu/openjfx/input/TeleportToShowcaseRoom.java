package uwu.openjfx.input;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.ui.FXGLScrollPane;
import com.almasb.fxgl.ui.ProgressBar;
import javafx.scene.paint.Color;
import uwu.openjfx.MapGeneration.Coordinate;
import uwu.openjfx.MapGeneration.GameMap;
import uwu.openjfx.MapGeneration.Room;
import uwu.openjfx.components.BossComponent;
import uwu.openjfx.components.DummyBossComponent;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;

public class TeleportToShowcaseRoom extends UserAction {
    public TeleportToShowcaseRoom(String name) {
        super(name);
    }

    @Override
    protected void onActionBegin() {
        GameMap gameMap = FXGL.geto("gameMap");
        Room showcaseRoom = new Room(new Coordinate(-999, -999));
        showcaseRoom.setRoomType("weaponsShowcaseRoom");
        getGameScene().getViewport().fade(() -> {
            gameMap.loadRoom(showcaseRoom, "west");
        });

        ProgressBar bossHealth = new ProgressBar();
        FXGL.set("dummyBossHealth", bossHealth);
        bossHealth.setBackgroundFill(Color.RED);
        bossHealth.setHeight(25);
        bossHealth.setMinValue(0);
        bossHealth.setMaxValue(100);
        bossHealth.setTranslateX(FXGL.getAppWidth() / 2.0
                - bossHealth.getBackgroundBar().getWidth() / 2);
        bossHealth.setTranslateY(12.5);
        bossHealth.currentValueProperty().bind(DummyBossComponent.getBossHealthProperty());
        FXGL.getGameScene().addUINodes(bossHealth);
    }
}
