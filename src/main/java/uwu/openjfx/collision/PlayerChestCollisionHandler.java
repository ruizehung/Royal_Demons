package uwu.openjfx.collision;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import uwu.openjfx.MainApp;
import uwu.openjfx.RoyalType;
import uwu.openjfx.behaviors.CanOnlyInteractOnce;

public class PlayerChestCollisionHandler extends CollisionHandler {


    public PlayerChestCollisionHandler() {
        super(RoyalType.PLAYER, RoyalType.CHEST);
    }

    @Override
    public void onCollision(Entity player, Entity chest) {
        CanOnlyInteractOnce chestComponent = chest.getObject("chestComponent");
        if ((MainApp.isIsTesting() || FXGL.getb("Fpressed"))
                && !chestComponent.hasInteractedBefore()) {
            chestComponent.interact();
            if (!MainApp.isIsTesting()) {
                FXGL.play("ui/chest.wav");
            }
        }
    }
}
