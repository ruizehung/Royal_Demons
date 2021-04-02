package uwu.openjfx.collision;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import uwu.openjfx.RoyalType;
import uwu.openjfx.components.ChestComponent;

public class PlayerChestCollisionHandler extends CollisionHandler {


    public PlayerChestCollisionHandler() {
        super(RoyalType.PLAYER, RoyalType.CHEST);
    }

    @Override
    protected void onCollision(Entity player, Entity chest) {
        ChestComponent chestComponent = chest.getComponent(ChestComponent.class);
        if (FXGL.getb("Fpressed") && !chestComponent.isOpened()) {
            chestComponent.open();
        }
    }
}
