package uwu.openjfx.collision;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import uwu.openjfx.RoyalType;
import uwu.openjfx.events.InteractEvent;

public class PlayerNPCCollisionHandler extends CollisionHandler {
    public PlayerNPCCollisionHandler() {
        super(RoyalType.PLAYER, RoyalType.NPC);
    }

    @Override
    public void onCollision(Entity player, Entity npc) {
        if (FXGL.getb("Fpressed")) {
            FXGL.getEventBus().fireEvent(new InteractEvent(InteractEvent.NPC, npc));
        }
    }
}
