package uwu.openjfx.collision;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import uwu.openjfx.RoyalType;
import uwu.openjfx.components.PlayerComponent;
import uwu.openjfx.components.SpikeComponent;

public class PlayerSpikeCollisionHandler extends CollisionHandler {
    public PlayerSpikeCollisionHandler() {
        super(RoyalType.PLAYER, RoyalType.SPIKE);
    }

    @Override
    protected void onCollision(Entity player, Entity spike) {
        SpikeComponent spikeComponent = spike.getComponent(SpikeComponent.class);
        if (spikeComponent.isActive()) {
            PlayerComponent playerComponent = player.getComponent(PlayerComponent.class);
            if (!playerComponent.isInvulnerable()) {
                playerComponent.deductHealth(1, 1, 0, 1, 0);
            }
        }
    }
}
