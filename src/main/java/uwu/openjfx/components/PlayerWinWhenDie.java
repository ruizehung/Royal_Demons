package uwu.openjfx.components;

import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import uwu.openjfx.DieScreenMenu;
import uwu.openjfx.MainApp;

public class PlayerWinWhenDie implements DieBehavior {
    @Override
    public void die(double x, double y) {
        if (!MainApp.isIsTesting()) {
            FXGL.getSceneService().pushSubScene(new DieScreenMenu(MenuType.MAIN_MENU));
        }
    }
}
