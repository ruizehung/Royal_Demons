package uwu.openjfx.components;

import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import uwu.openjfx.DieScreenMenu;

public class BossComponent extends EnemyComponent {

    public BossComponent(int healthPoints, String type, int width, int height) {
        super(healthPoints, type, width, height);
    }

    @Override
    public void die() {
        super.die();
        FXGL.getSceneService().pushSubScene(new DieScreenMenu(MenuType.MAIN_MENU));
    }
}
