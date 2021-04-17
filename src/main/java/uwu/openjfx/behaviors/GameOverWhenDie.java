package uwu.openjfx.behaviors;

import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import uwu.openjfx.DieScreenMenu;
import uwu.openjfx.MainApp;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getAudioPlayer;

public class GameOverWhenDie implements Behavior {
    @Override
    public void act(Entity entity) {
        if (!MainApp.isIsTesting()) {
            FXGL.getSceneService().pushSubScene(new DieScreenMenu(MenuType.GAME_MENU));
            getAudioPlayer().stopMusic(FXGL.getAssetLoader().loadMusic("evil4.mp3"));
            FXGL.play("die.wav");
        }
    }
}
