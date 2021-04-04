package uwu.openjfx.components;

import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import uwu.openjfx.DieScreenMenu;
import uwu.openjfx.MainApp;

public class WinWhenDie implements Behavior {
    @Override
    public void act(Entity entity) {
        if (!MainApp.isIsTesting()) {
            FXGL.getSceneService().pushSubScene(new DieScreenMenu(MenuType.MAIN_MENU));
        }
    }
}
