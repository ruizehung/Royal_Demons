package uwu.openjfx.collision;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import uwu.openjfx.RoyalType;
import uwu.openjfx.input.TeleportToWinRoom;

public class PlayerFinalDoorCollisionHandler extends CollisionHandler {

    public PlayerFinalDoorCollisionHandler() {
        super(RoyalType.PLAYER, RoyalType.FINALDOOR);
    }

    @Override
    protected void onCollisionBegin(Entity player, Entity finalDoor) {
        TeleportToWinRoom teleportToWinRoom = new TeleportToWinRoom("");
        teleportToWinRoom.onActionBegin();
    }
}