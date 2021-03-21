package uwu.openjfx.collision;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import uwu.openjfx.RoyalType;
import uwu.openjfx.components.HealthComponent;
import uwu.openjfx.components.PlayerComponent;

public class PlayerEnemyCollisionHandler extends CollisionHandler {

    public PlayerEnemyCollisionHandler() {
        super(RoyalType.PLAYER, RoyalType.ENEMY);
    }

    protected void onCollision(Entity player, Entity enemy) {
        PlayerComponent playerComponent = player.getComponent(PlayerComponent.class);
        if (!playerComponent.getIsInvulnerable()) {
            playerComponent.deductHealth();
        }
    }
}
