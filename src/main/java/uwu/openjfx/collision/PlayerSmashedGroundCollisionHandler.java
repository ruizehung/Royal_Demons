package uwu.openjfx.collision;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import uwu.openjfx.RoyalType;
import uwu.openjfx.behaviors.HasLife;
import uwu.openjfx.components.PlayerComponent;

/*
    This class is responsible for the collision interaction between player and a smashed ground
 */
public class PlayerSmashedGroundCollisionHandler extends CollisionHandler {
    private boolean hasAlreadyBeenHit = false;

    public PlayerSmashedGroundCollisionHandler() {
        super(RoyalType.SMASHEDGROUND, RoyalType.PLAYER);
    }

    @Override
    public void onCollisionBegin(Entity ground, Entity player) {
        if (!hasAlreadyBeenHit) {
            HasLife playerComponent = player.getComponent(PlayerComponent.class);
            if (!playerComponent.isInvulnerable()) {
                playerComponent.deductHealth(1, 1, 0, 1, 0);
            }
            hasAlreadyBeenHit = true;
            Runnable runnable = () -> { // attackBreaktime amount of time before cooldown is done
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                hasAlreadyBeenHit = false;
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }
    }

    @Override
    public void onCollision(Entity ground, Entity player) {
        player.getComponent(PlayerComponent.class).setSpeed(60);
    }

    @Override
    public void onCollisionEnd(Entity ground, Entity player) {
        player.getComponent(PlayerComponent.class).setSpeed(170);
    }
}
