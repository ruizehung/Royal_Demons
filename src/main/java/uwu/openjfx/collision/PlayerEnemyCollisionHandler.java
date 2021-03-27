package uwu.openjfx.collision;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import uwu.openjfx.RoyalType;
import uwu.openjfx.components.HealthComponent;
import uwu.openjfx.components.PlayerComponent;

/*
    This class is responsible for handling player TOUCHING enemy collision.
 */
public class PlayerEnemyCollisionHandler extends CollisionHandler {

    public PlayerEnemyCollisionHandler() {
        super(RoyalType.PLAYER, RoyalType.ENEMY);
    }

    public void onCollision(Entity player, Entity enemy) {
        HealthComponent playerHealth = player.getComponent(PlayerComponent.class);
        if (!playerHealth.getIsInvulnerable()) {
            playerHealth.deductHealth();
        }
    }
}
