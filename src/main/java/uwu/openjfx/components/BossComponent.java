package uwu.openjfx.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import uwu.openjfx.MainApp;
import uwu.openjfx.RoyalType;
import uwu.openjfx.UI;
import uwu.openjfx.behaviors.DoNothing;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getAudioPlayer;
import static com.almasb.fxgl.dsl.FXGLForKtKt.loopBGM;

// Todo: add more boss behavior here
public class BossComponent extends EnemyComponent {
    private static IntegerProperty bossHealthProperty = new SimpleIntegerProperty();


    public BossComponent(int healthPoints, String assetName, int width, int height, int frames) {
        super(healthPoints, assetName, width, height, frames, "finalboss", "melee");
//        setDieBehavior(new WinWhenDie());
        setDieBehavior(new DoNothing());
        bossHealthProperty.set(healthPoints);
    }

    @Override
    public void die() {
        super.die();
        if (!MainApp.isIsTesting()) {
            Entity finalDoor = FXGL.getGameWorld().getEntitiesByType(RoyalType.FINALDOOR).get(0);
            finalDoor.getViewComponent().clearChildren();
            finalDoor.getViewComponent().addChild(FXGL.texture("woodenDoorOpened.png"));
            getAudioPlayer().stopMusic(FXGL.getAssetLoader().loadMusic("boss/boss_battle_ 2.mp3"));
            loopBGM("end/Training Is Over.mp3");
            FXGL.getGameScene().clearUINodes();
            UI.init(FXGL.geto("player"));
        }
    }

    public static IntegerProperty getBossHealthProperty() {
        return bossHealthProperty;
    }

    public static void setBossHealthProperty(int bossHealthProperty) {
        BossComponent.bossHealthProperty.set(bossHealthProperty);
    }
}
