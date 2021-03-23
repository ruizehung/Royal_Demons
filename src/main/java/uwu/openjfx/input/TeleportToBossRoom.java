package uwu.openjfx.input;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import uwu.openjfx.MapGeneration.GameMap;

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
    }
}
