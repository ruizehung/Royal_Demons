package uwu.openjfx.collision;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import uwu.openjfx.RoyalType;

public class PlayerFinalDoorCollisionHandler extends CollisionHandler {

    public PlayerFinalDoorCollisionHandler() {
        super(RoyalType.PLAYER, RoyalType.FINALDOOR);
    }

    @Override
    protected void onCollisionBegin(Entity player, Entity finalDoor) {
        if (FXGL.getGameWorld().getEntitiesByType(RoyalType.ENEMY).size() > 0) {
            System.out.println("Teleport Player to final room");
        } else {
            System.out.println("Player should kill the boss first");
        }

    }
}
