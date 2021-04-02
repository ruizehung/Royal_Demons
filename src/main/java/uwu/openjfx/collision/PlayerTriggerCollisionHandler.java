package uwu.openjfx.collision;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import uwu.openjfx.RoyalType;
import uwu.openjfx.components.TrapComponent;

import java.util.List;

public class PlayerTriggerCollisionHandler extends CollisionHandler {

    public PlayerTriggerCollisionHandler() {
        super(RoyalType.PLAYER, RoyalType.TRAP_TRIGGER);
    }

    @Override
    protected void onCollisionBegin(Entity player, Entity trapTrigger) {
        // for trap that you only need to collide with it
        if (!trapTrigger.getComponent(TrapComponent.class).needToPressUse()) {
            int trapGroupId = trapTrigger.getProperties().getInt("groupId");
            List<Entity> traps = FXGL.getGameWorld().getEntitiesByType(RoyalType.TRAP);
            for (Entity trap : traps) {
                if (trap.getProperties().getInt("groupId") == trapGroupId) {
                    trap.getComponent(TrapComponent.class).trigger();
                }
            }
            trapTrigger.getComponent(TrapComponent.class).trigger();
        }
    }

    @Override
    protected void onCollision(Entity player, Entity trapTrigger) {
        if (trapTrigger.getComponent(TrapComponent.class).needToPressUse()
                && FXGL.getb("Fpressed")) {
            int trapGroupId = trapTrigger.getProperties().getInt("groupId");
            List<Entity> traps = FXGL.getGameWorld().getEntitiesByType(RoyalType.TRAP);
            for (Entity trap : traps) {
                if (trap.getProperties().getInt("groupId") == trapGroupId) {
                    trap.getComponent(TrapComponent.class).trigger();
                }
            }
            trapTrigger.getComponent(TrapComponent.class).trigger();
        }
    }
}
